package hr.fer.tel.rassus.dz.first;

import hr.fer.tel.rassus.dz.first.entity.Measurement;
import hr.fer.tel.rassus.dz.first.entity.Sensor;
import hr.fer.tel.rassus.dz.first.repository.MeasurementRepository;
import hr.fer.tel.rassus.dz.first.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbInit implements CommandLineRunner {

	@Autowired
	public SensorRepository sensorRepo;

	@Autowired
	MeasurementRepository measurementRepository;
	
	@Override
	public void run(String... strings) throws Exception {

		Sensor senzor1 = new Sensor("Senzor1", 10, 10, "localhost", 10001);
		sensorRepo.save(senzor1);

		Sensor senzor2 = new Sensor("Senzor2", 11, 11, "localhost", 10002);
		sensorRepo.save(senzor2);

		Sensor senzor3 = new Sensor("Senzor3", 12, 12, "localhost", 10003);
		sensorRepo.save(senzor3);

		Measurement measurement1=new Measurement("sensor1","temperatura",7,senzor1);
		measurementRepository.save(measurement1);
		senzor1.getMeasurements().add(measurement1);
		sensorRepo.save(senzor1);

	}	

}
