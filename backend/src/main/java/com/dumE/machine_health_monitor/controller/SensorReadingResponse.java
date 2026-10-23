package com.dumE.machine_health_monitor.controller;

import com.dumE.machine_health_monitor.model.SensorReading;
import com.dumE.machine_health_monitor.model.SensorType;

import java.time.Instant;

public record SensorReadingResponse(
        Long id,
        String machineId,
        SensorType sensorType,
        Double value,
        Instant timestamp,
        boolean anomaly) {

    public static SensorReadingResponse from(SensorReading r) {
        return new SensorReadingResponse(
                r.getId(),
                r.getMachineId(),
                r.getSensorType(),
                r.getValue(),
                r.getTimestamp(),
                r.isAnomaly());
    }
}