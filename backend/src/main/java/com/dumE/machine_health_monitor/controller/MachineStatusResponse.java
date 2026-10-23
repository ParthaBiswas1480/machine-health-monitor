package com.dumE.machine_health_monitor.controller;

public record MachineStatusResponse(String machineId, long openAnomalies) {
}
