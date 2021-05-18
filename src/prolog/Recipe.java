package prolog;

public class Recipe {
	private String name;
	private String stat;
	private int potency;
	private int duration;
	private String resources;

	public Recipe(String name, String stat, int potency, int duration, String resources) {
		super();
		this.name = name;
		this.stat = stat;
		this.potency = potency;
		this.duration = duration;
		this.resources = resources;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public int getPotency() {
		return potency;
	}

	public void setPotency(int potency) {
		this.potency = potency;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}
}
