package com.dumE.machine_health_monitor.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "anomalies")
public class Anomaly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "machine_id", nullable = false)
    private String machineId;

    @Enumerated(EnumType.STRING)
    @Column(name = "sensor_type", nullable = false)
    private SensorType sensorType;

    @Column(nullable = false)
    private Double value;

    @Column(name = "rolling_mean")
    private Double rollingMean;

    @Column(name = "rolling_std_dev")
    private Double rollingStdDev;

    @Column(nullable = false)
    private String reason;

    @Column(name = "detected_at", nullable = false)
    private Instant detectedAt = Instant.now();

    @Column(name = "acknowledged", nullable = false)
    private boolean acknowledged = false;

    @Column(name = "acknowledged_at")
    private Instant acknowledgedAt;

    public Anomaly() {
    }

    public Anomaly(String machineId, SensorType sensorType, Double value,
                   Double rollingMean, Double rollingStdDev, String reason, Instant detectedAt) {
        this.machineId = machineId;
        this.sensorType = sensorType;
        this.value = value;
        this.rollingMean = rollingMean;
        this.rollingStdDev = rollingStdDev;
        this.reason = reason;
        this.detectedAt = detectedAt == null ? Instant.now() : detectedAt;
    }

    public Long getId() {
        return id;
    }

    public String getMachineId() {
        return machineId;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public Double getValue() {
        return value;
    }

    public Double getRollingMean() {
        return rollingMean;
    }

    public Double getRollingStdDev() {
        return rollingStdDev;
    }

    public String getReason() {
        return reason;
    }

    public Instant getDetectedAt() {
        return detectedAt;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public Instant getAcknowledgedAt() {
        return acknowledgedAt;
    }

    public void acknowledge() {
        this.acknowledged = true;
        this.acknowledgedAt = Instant.now();
    }
}
