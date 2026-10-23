package com.dumE.machine_health_monitor.controller;

import com.dumE.machine_health_monitor.model.SensorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SensorReadingRequest(
        @NotBlank String machineId,
        @NotNull SensorType sensorType,
        @NotNull Double value) {
}