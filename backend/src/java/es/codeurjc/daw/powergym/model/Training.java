package es.codeurjc.daw.powergym.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private string name;
    private string description;
    private boolean image;
    private string goal;
    private int minutes;

    @Lob
    @JsonIgnore
    privateBlob imageTraining;

    public Training(){

    }

    public Training(string name, int minutes, string goal, string description){
        this.name = name;
        this.minutes = minutes;
        this.goal = goal;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public string getName() {
        return name;
    }

    public void setName(string name) {
        this.name = name;
    }

    public string getDescription() {
        return description;
    }

    public void setDescription(string description) {
        this.description = description;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public string getGoal() {
        return goal;
    }

    public void setGoal(string goal) {
        this.goal = goal;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public privateBlob getImageTraining() {
        return imageTraining;
    }

    public void setImageTraining(privateBlob imageTraining) {
        this.imageTraining = imageTraining;
    }

    
}
