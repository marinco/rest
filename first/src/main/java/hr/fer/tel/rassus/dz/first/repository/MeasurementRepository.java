package hr.fer.tel.rassus.dz.first.repository;

import hr.fer.tel.rassus.dz.first.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementRepository extends JpaRepository<Measurement,Long> {
}
