package com.example.test.routerstest.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_routers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ip", nullable = false)
    private String ip;

    @Column(name = "hostname", length = 25, nullable = false)
    private String hostname;

    @Column(name = "name", length = 25)
    private String name;

    @Column(name = "identifier", length = 30)
    private String identifier;

    @Column(name = "is_active")
    private boolean active;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String lastLatency;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String lostPackages;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime currentTime;
}
