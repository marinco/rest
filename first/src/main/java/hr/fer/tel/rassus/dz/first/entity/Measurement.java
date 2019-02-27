package hr.fer.tel.rassus.dz.first.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Measurement {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JsonBackReference
    private Sensor sensor;

    private String username;
    private String parameter;
    private float averageValue;

    public Measurement(){}

    public Measurement(String username, String parameter, float averageValue, Sensor sensor) {
        this.username = username;
        this.parameter = parameter;
        this.averageValue = averageValue;
        this.sensor=sensor;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public float getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(float averageValue) {
        this.averageValue = averageValue;
    }

    @Override
    public String toString() {
        return "Measurement{" + "id=" + id + ", username=" + username + ", parameter=" + parameter + ", averageValue=" + averageValue + '}';
    }

}
