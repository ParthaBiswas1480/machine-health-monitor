package com.dumE.machine_health_monitor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Entity
@Table(name = "sensor_readings")
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "machine_id", nullable = false)
    private String machineId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sensor_type", nullable = false)
    private SensorType sensorType;

    @NotNull
    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    private Instant timestamp = Instant.now();

    @Column(name = "is_anomaly", nullable = false)
    private boolean anomaly = false;

    public SensorReading() {
    }

    public SensorReading(String machineId, SensorType sensorType, Double value, Instant timestamp) {
        this.machineId = machineId;
        this.sensorType = sensorType;
        this.value = value;
        this.timestamp = timestamp == null ? Instant.now() : timestamp;
    }

    public Long getId() {
        return id;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isAnomaly() {
        return anomaly;
    }

    public void setAnomaly(boolean anomaly) {
        this.anomaly = anomaly;
    }
}