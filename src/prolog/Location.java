package prolog;

import java.util.ArrayList;

public class Location {
	private String landmark;
	private ArrayList<String> resources;

	public Location(String landmark, ArrayList<String> resources) {
		this.landmark = landmark;
		this.resources = resources;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public ArrayList<String> getResources() {
		return resources;
	}

	public void setResources(ArrayList<String> resources) {
		this.resources = resources;
	}

	public void printLocation() {
		System.out.printf("%s has: \n", landmark);
		for (String resource : resources) {
			System.out.printf("- %s\n", resource);
		}
	}
}
