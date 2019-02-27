package hr.fer.tel.rassus.dz.first.controller;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import hr.fer.tel.rassus.dz.first.UserAddress;
import hr.fer.tel.rassus.dz.first.entity.Measurement;
import hr.fer.tel.rassus.dz.first.entity.Sensor;
import hr.fer.tel.rassus.dz.first.repository.MeasurementRepository;
import hr.fer.tel.rassus.dz.first.repository.SensorRepository;
import org.springframework.web.bind.annotation.*;

import static java.lang.Math.*;

@RestController
public class SensorController {

	private final SensorRepository repository;

	private final MeasurementRepository mrepo;

	public SensorController(SensorRepository repository, MeasurementRepository mrepo) {
		this.repository = repository;
		this.mrepo=mrepo;
	}

	// Aggregate root

	@GetMapping("/sensors")
	List<Sensor> all() {
		List<Sensor> list= new LinkedList<>(repository.findAll());
		for(Sensor s: list){
			s.setMeasurements(null);
		}
		return list;
	}

	@PostMapping("/sensors")
	Sensor register(@RequestParam(value="username") String username,
					@RequestParam(value="latitude") String latitude,
					@RequestParam(value="longitude") String longitude,
					@RequestParam(value="IPaddress") String IPaddress,
					@RequestParam(value="port") String port) {
		Sensor s = new Sensor(username,Double.parseDouble(latitude),Double.parseDouble(longitude),IPaddress, Integer.parseInt(port));
		return repository.save(s);
	}

	@GetMapping("/sensors/{username}/search")
	UserAddress searchNeighbours(@PathVariable String username){
		Sensor sensor=repository.findByUsername(username);
		Sensor r=null;
		double min=6371;
		for(Sensor s : repository.findAll()){
			if(s.getId()==sensor.getId()){
				continue;
			}
			double dist=calcDistance(s.getLongitude(),s.getLatitude(),sensor.getLongitude(),sensor.getLatitude());
			if(dist<min){
				r=s;
				min=dist;
			}
		}
		//implement to find the neighbour
		UserAddress u=new UserAddress(r.getIPaddress(),r.getPort());
		return u;

		//return repository.findById(1L).get();
	}

	@PostMapping("/sensors/{username}/store")
	Measurement storeMeasurement(@RequestParam(value = "username")String username,
								 @RequestParam(value="parameter") String parameter,
								 @RequestParam(value = "value")String value){
		Sensor sensor=repository.findByUsername(username);
		Measurement measurement=new Measurement(username,parameter,Float.parseFloat(value),sensor);
		sensor.getMeasurements().add(measurement);
		mrepo.save(measurement);
		repository.save(sensor);
		return mrepo.save(measurement);
	}



	public double calcDistance(double long1, double lat1, double long2, double lat2) {
		int R = 6371;
		double dlon = long2 - long1;
		double dlat = lat2 - lat1;
		double a = pow(sin(dlat / 2), 2) + cos(lat1) * cos(lat2) * (pow(sin(dlon / 2), 2));
		double c = 2 * atan2(sqrt(a), sqrt(1 - a));
		double d = R * c;
		return d;
	}
	// // Single item
	//
	// @GetMapping("/employees/{id}")
	// Sensor one(@PathVariable Long id) {
	//
	// return repository.findById(id)
	// .orElseThrow(() -> new EmployeeNotFoundException(id));
	// }
	//
	// @PutMapping("/employees/{id}")
	// Sensor replaceEmployee(@RequestBody Sensor newEmployee, @PathVariable
	// Long id) {
	//
	// return repository.findById(id)
	// .map(employee -> {
	// employee.setName(newEmployee.getName());
	// employee.setRole(newEmployee.getRole());
	// return repository.save(employee);
	// })
	// .orElseGet(() -> {
	// newEmployee.setId(id);
	// return repository.save(newEmployee);
	// });
	// }
	//
	// @DeleteMapping("/employees/{id}")
	// void deleteEmployee(@PathVariable Long id) {
	// repository.deleteById(id);
	// }

}
