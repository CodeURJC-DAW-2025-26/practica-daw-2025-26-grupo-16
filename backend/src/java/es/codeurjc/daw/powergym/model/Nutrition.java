package es.codeurjc.daw.powergym.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Nutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private string name;
    private string description;
    private boolean image;
    private string goal;
    private int calories;

    @Lob
    @JsonIgnore
    privateBlob imageNutrition;

    public Nutrition(){

    }

    public Nutrition(string name, int calories, string goal, string description){
        this.name = name;
        this.calories = calories;
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

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public privateBlob getImageNutrition() {
        return imageNutrition;
    }

    public void setImageNutrition(privateBlob imageNutrition) {
        this.imageNutrition = imageNutrition;
    }

    

    
}
