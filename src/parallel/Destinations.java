package parallel;

public class Destinations implements Runnable {
	private Analysis kb;

	public Destinations(Analysis kb) {
		this.kb = kb;
	}

	@Override
	public void run() {
		String path[];
		String destination;
		int length;
		// System.out.println(kb.getPaths().size() / 2);
		for (int i = 0; i < kb.getPaths().size() / 2; i++) {
			path = kb.getPaths().get(i);
			length = path.length;
			destination = path[length - 1];
			kb.getDestinations().add(i, destination);
		}

	}

}
