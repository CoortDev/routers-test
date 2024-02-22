package com.example.test.routerstest.reporitories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.test.routerstest.entities.RouterEntity;

@Repository
public interface RouterRepository extends CrudRepository<RouterEntity, Long>{
    Optional<RouterEntity> findByIp(String ip);
    Optional<RouterEntity> findByHostname(String hostname);
}
