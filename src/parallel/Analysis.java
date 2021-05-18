/*
This file is part of Simple Videogame Resource Manager.

Simple Videogame Resource Manager is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Simple Videogame Resource Manager is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Simple Videogame Resource Manager.  If not, see <https://www.gnu.org/licenses/>.
*/
package parallel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Analysis {
	private String origin;
	private String landmarks[];
	private ArrayList<String> destinations;
	private ArrayList<String[]> paths;
	private int pair_len, pair_pos;
	private int odd_len, odd_pos;

	public Analysis() {
		paths = new ArrayList<String[]>();
		destinations = new ArrayList<String>();
	}

	public void initialize_paths() {
		for (int i = 0; i < landmarks.length; i++) {
			paths.add(new String[0]);
		}
	}

	public void initialize_destinations() {
		for (int i = 0; i < paths.size() / 2; i++) {
			destinations.add("");
		}
	}

	public static void deleteEntry(String file, String type, String entry) {
		BufferedReader reader;
		BufferedWriter writer;
		String parts[];
		String contents = "", currentItem;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				switch (type) {
				case "resource":
					currentItem = line.substring(9, line.length() - 2);
					if (!(entry.equals(currentItem))) {
						contents += String.format("resource(%s).\n", currentItem);
					}
					break;
				case "landmark":
					currentItem = line.substring(9, line.length() - 2);
					if (!(entry.equals(currentItem))) {
						contents += String.format("landmark(%s).\n", currentItem);

					}
					break;
				case "location":
					parts = line.substring(9, line.length() - 2).split(",");
					currentItem = parts[0];
					if (!(entry.equals(currentItem))) {
						contents += line + "\n";
					}
					break;
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			new FileOutputStream(file).close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(contents);
			writer.close();
		} catch (Exception e) {
			e.getStackTrace();
		}

	}

	public static void updateLocation(String file, String resource, String updatedLine) {
		BufferedReader reader;
		BufferedWriter writer;
		String parts[];
		String contents = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				parts = line.substring(9, line.length() - 2).split(",");
				if (resource.equals(parts[0])) {
					contents += updatedLine;
				} else {
					contents += String.format("%s\n", line);
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			new FileOutputStream(file).close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(contents);
			writer.close();
		} catch (Exception e) {
			e.getStackTrace();
		}

	}

	public static void deletePath(String file, String origin, String destination) {
		BufferedReader reader;
		BufferedWriter writer;
		String contents = "";
		String route1 = "route(" + origin + "," + destination + ").";
		String route2 = "route(" + destination + "," + origin + ").";
		System.out.println("Going to delete path.");
		System.out.println(route1);
		System.out.println(route2);
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				if (!(line.equals(route1) || line.equals(route2))) {
					contents += line + "\n";
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			new FileOutputStream(file).close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(contents);
			writer.close();
		} catch (Exception e) {
			e.getStackTrace();
		}

	}

	public static void insertFile(String file, String item) {
		BufferedReader reader;
		BufferedWriter writer;
		String contents = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				contents += String.format("%s\n", line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		contents += item;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(contents);
			writer.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	/*-
	public static String formatEntry(String string) {
		String newString = string.toLowerCase();
		newString = newString.replace(" ", "_");
		return newString;
	}
	*/

	/*-
	public static String formatEntry2(String string) {
		String newString = "'";
		String firstLetterUpperCase;
		String[] parts = string.toLowerCase().split(" ");
		for (String word : parts) {
			firstLetterUpperCase = Character.toUpperCase(word.charAt(0)) + word.substring(1, word.length());
			newString += firstLetterUpperCase + " ";
		}
		newString = newString.substring(0, newString.length() - 1) + "'";
		return newString;
	}
	*/

	public static String addSimpleQuotationMarks(String string) {
		return "'" + string + "'";
	}

	public static String formatEntry4(String string) {
		String newString = "";
		String firstLetterUpperCase;
		String[] parts = string.toLowerCase().split(" ");
		for (String word : parts) {
			firstLetterUpperCase = Character.toUpperCase(word.charAt(0)) + word.substring(1, word.length());
			newString += firstLetterUpperCase + " ";
		}
		newString = newString.substring(0, newString.length() - 1);
		return newString;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public ArrayList<String[]> getPaths() {
		return paths;
	}

	public void setPaths(ArrayList<String[]> paths) {
		this.paths = paths;
	}

	public String[] getLandmarks() {
		return landmarks;
	}

	public void setLandmarks(String[] landmarks) {
		this.landmarks = landmarks;
	}

	public int getPair_len() {
		return pair_len;
	}

	public void setPair_len(int par_len) {
		this.pair_len = par_len;
	}

	public int getPair_pos() {
		return pair_pos;
	}

	public void setPair_pos(int par_pos) {
		this.pair_pos = par_pos;
	}

	public int getOdd_len() {
		return odd_len;
	}

	public void setOdd_len(int odd_len) {
		this.odd_len = odd_len;
	}

	public int getOdd_pos() {
		return odd_pos;
	}

	public void setOdd_pos(int odd_pos) {
		this.odd_pos = odd_pos;
	}

	public ArrayList<String> getDestinations() {
		return destinations;
	}

	public void setDestinations(ArrayList<String> destinations) {
		this.destinations = destinations;
	}
}
