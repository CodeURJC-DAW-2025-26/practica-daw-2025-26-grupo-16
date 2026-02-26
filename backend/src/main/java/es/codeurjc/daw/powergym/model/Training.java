package es.codeurjc.daw.powergym.model;

import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Training {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id = null;

	private String name;
    private String description;
    private String goal;
    private int time;

	@OneToOne
	private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_training_subscriptions",
	           joinColumns = @JoinColumn(name = "training_id"),
	           inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> subscribers = new HashSet<>();

	public Training() {
	}

	public Training(String name, String description, String goal, int time) {
		super();
		this.name = name;
		this.description = description;
		this.goal = goal;
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

	public Set<User> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(Set<User> subscribers) {
		this.subscribers = subscribers;
	}

    public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Training [id=" + id + ", name=" + name + ", description=" + description + ", goal=" + goal + ", time=" + time + "]";
	}
}