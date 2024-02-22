package com.example.test.routerstest.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouterCacheDTO {
    private String ip;
    private String hostname;
    private String name;
    private String identifier;
    private String latency;
    private String lostPackages;
    private String currentTime;
}
