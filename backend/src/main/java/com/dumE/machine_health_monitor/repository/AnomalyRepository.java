package com.dumE.machine_health_monitor.repository;

import com.dumE.machine_health_monitor.model.Anomaly;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnomalyRepository extends JpaRepository<Anomaly, Long> {

    List<Anomaly> findTop20ByOrderByDetectedAtDesc();
}