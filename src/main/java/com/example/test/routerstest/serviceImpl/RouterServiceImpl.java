package com.example.test.routerstest.serviceImpl;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.test.routerstest.dtos.RouterCacheDTO;
import com.example.test.routerstest.entities.RouterEntity;
import com.example.test.routerstest.reporitories.RouterRepository;
import com.example.test.routerstest.service.RouterService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RouterServiceImpl implements RouterService{

    private final ConcurrentMap<String, List<RouterCacheDTO>> cache = new ConcurrentHashMap<>();

    DecimalFormat decimalFormat = new DecimalFormat("#.#");
    DateTimeFormatter formatoPersonalizado = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private RouterRepository routerRepository;

    @Override
    public List<RouterEntity> getAllRouters() {
        return (List<RouterEntity>) routerRepository.findAll();
    }

    @Override
    public RouterEntity getRuterByIP(String ip) {
        return routerRepository.findByIp(ip).orElseThrow(() -> new NoSuchElementException("Not found Router with ip: "+ip));
    }

    @Override
    public RouterEntity getRuterByHostname(String hostname) {
        return routerRepository.findByHostname(hostname).orElseThrow(() -> new NoSuchElementException("Not found Router with hostname: "+hostname));
    }

    @Override
    public RouterEntity saveRouter(RouterEntity router) {
        return routerRepository.save(router);
    }

    @Override
    @Scheduled(fixedRate = 30*60*1000)
    public void pingTask() {
        int NUM_PACKAGES = 4;
        List<RouterEntity> listRouter = getAllRouters();
        if (listRouter.isEmpty()) {
            saveDataFromJson();
            listRouter = getAllRouters();
        }
        if (!listRouter.isEmpty()) {
            for (RouterEntity routerEntity : listRouter) {
                if (routerEntity.isActive()) {
                    double min = 0.0;
                    double max = 0.0;
                    double avg = 0.0;
                    for (int i = 0; i < NUM_PACKAGES; i++) {
                        /* 
                        * !Determina el rango de latencia para cada transferencia de paquete
                        */
                        Random random = new Random();
                        double cpl = 30 + random.nextDouble() * (50 - 30); //Current Package Latency
                        min = min > cpl ? cpl : min;
                        max = max < cpl ? cpl : max;
                    }
                    avg = (min + max)/NUM_PACKAGES;//Latencia estimada para el router de esta iteración
                    
                    String latencyFormat = decimalFormat.format(avg);
                    routerEntity.setLastLatency(latencyFormat);
                    routerEntity.setLostPackages("0%");
                    routerEntity.setCurrentTime(LocalDateTime.now());
                }
                else {
                    routerEntity.setLastLatency("undefined");
                    routerEntity.setLostPackages("100%");
                }
                new RouterCacheDTO();
                
                RouterCacheDTO routerDto = RouterCacheDTO
                                                    .builder()
                                                    .ip(routerEntity.getIp())
                                                    .hostname(routerEntity.getHostname())
                                                    .name(routerEntity.getName())
                                                    .identifier(routerEntity.getIdentifier())
                                                    .latency(routerEntity.getLastLatency()+"ms")
                                                    .lostPackages(routerEntity.getLostPackages())
                                                    .currentTime(routerEntity.getCurrentTime().format(formatoPersonalizado))
                                                    .build();
                updateCache(routerEntity.getIp(), routerDto);
            }
        }
    }

    private void updateCache(String routerIP, RouterCacheDTO router){
        List<RouterCacheDTO> data = cache.getOrDefault(routerIP, new ArrayList<>());

        data.add(router);

        while (data.size() > 1440) {
            data.remove(0);
        }

        cache.put(routerIP, data);
    }

    @Override
    public List<RouterCacheDTO> getRouterFromCache(String routerIp) {
        List<RouterCacheDTO> data = cache.get(routerIp);
        if (data != null && !data.isEmpty()) {
            return data;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<List<RouterCacheDTO>> getAllRoutersFromCache() {
        List<List<RouterCacheDTO>> historial = new ArrayList<>();
        for (String ip : cache.keySet()) {
            historial.add(getRouterFromCache(ip));
        }
        return historial;
    }

    private void saveDataFromJson(){
        try {

            ClassPathResource resource = new ClassPathResource("data/" + "routers.json");
            /* File file = resource.getFile(); */
            InputStream file = resource.getInputStream();
            ObjectMapper data = new ObjectMapper();
            List<RouterEntity> routersList = data.readValue(file, data.getTypeFactory().constructCollectionType(List.class, RouterEntity.class));
            if (!routersList.isEmpty()) {
                routerRepository.saveAll(routersList);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
