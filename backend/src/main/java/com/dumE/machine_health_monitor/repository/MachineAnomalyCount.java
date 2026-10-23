package com.dumE.machine_health_monitor.repository;

public interface MachineAnomalyCount {
    String getMachineId();
    Long getOpenCount();
}