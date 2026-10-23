package com.dumE.machine_health_monitor.repository;

import com.dumE.machine_health_monitor.model.SensorReading;
import com.dumE.machine_health_monitor.model.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

    List<SensorReading> findTop50ByMachineIdAndSensorTypeOrderByTimestampDesc(
            String machineId, SensorType sensorType);
}