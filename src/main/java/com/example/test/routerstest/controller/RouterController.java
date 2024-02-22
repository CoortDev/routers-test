package com.example.test.routerstest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.routerstest.dtos.FindRuterDTO;
import com.example.test.routerstest.dtos.RouterCacheDTO;
import com.example.test.routerstest.entities.RouterEntity;
import com.example.test.routerstest.service.RouterService;

@RestController
@RequestMapping(value = "/test/")
public class RouterController {

    @Autowired
    private RouterService routerService;

    @GetMapping(value = "get-all-routers")
    public ResponseEntity<List<RouterEntity>> getAllRouters() {
        return ResponseEntity.ok(routerService.getAllRouters());
    }

    @PostMapping(value = "save-router")
    public ResponseEntity<RouterEntity> saveRouter(@RequestBody RouterEntity newRouter){
        return ResponseEntity.ok(routerService.saveRouter(newRouter));
    }

    @GetMapping(value = "get-router-from-cache")
    public ResponseEntity<List<RouterCacheDTO>> getRouterFromCache(@RequestBody FindRuterDTO router){

        return ResponseEntity.ok(routerService.getRouterFromCache(router.getIp()));
    }

    @GetMapping(value = "get-all-routers-from-cache")
    public ResponseEntity<List<List<RouterCacheDTO>>> getAllRoutersFromCache(){
        return ResponseEntity.ok(routerService.getAllRoutersFromCache());
    }
}
