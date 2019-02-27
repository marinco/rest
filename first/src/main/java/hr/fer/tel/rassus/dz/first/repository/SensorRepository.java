package hr.fer.tel.rassus.dz.first.repository;

import hr.fer.tel.rassus.dz.first.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface SensorRepository extends JpaRepository<Sensor, Long>{
    Sensor findByUsername(String username);
}
