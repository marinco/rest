package hr.fer.tel.rassus.dz.first.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;


@Entity
public class Sensor {
	
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;
    private double latitude;
    private double longitude;
    private String IPaddress;
    private int port;

    @OneToMany(mappedBy = "sensor")
    @JsonManagedReference
    private List<Measurement> measurements;
    
    public Sensor(){
    	this.measurements=new LinkedList<>();
    }      

	public Sensor(String username, double latitude, double longitude,
			String iPaddress, int port) {
		//super();
		
		this.username = username;
		this.latitude = latitude;
		this.longitude = longitude;
		IPaddress = iPaddress;
		this.port = port;

		this.measurements=new LinkedList<>();


	}	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getIPaddress() {
		return IPaddress;
	}

	public void setIPaddress(String iPaddress) {
		IPaddress = iPaddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    @Override
	public String toString() {
		return "Sensor {id=" + id + ", username=" + username + ", latitude="
				+ latitude + ", longitude=" + longitude + ", IPaddress="
				+ IPaddress + ", port=" + port + "}  \u0020 or %20  <br> \n \\n" + " <br> \n";
	}
    
    

}
