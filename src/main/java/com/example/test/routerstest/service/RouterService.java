package com.example.test.routerstest.service;

import java.util.List;

import com.example.test.routerstest.dtos.RouterCacheDTO;
import com.example.test.routerstest.entities.RouterEntity;

public interface RouterService {
    List<RouterEntity> getAllRouters();
    RouterEntity getRuterByIP(String ip);
    RouterEntity getRuterByHostname(String hostname);
    RouterEntity saveRouter(RouterEntity router);
    void pingTask();
    List<RouterCacheDTO> getRouterFromCache(String routerIp);
    List<List<RouterCacheDTO>> getAllRoutersFromCache();
}
