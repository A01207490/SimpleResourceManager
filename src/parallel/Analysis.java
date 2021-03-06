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
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Analysis {
	private String origin;
	private String[] landmarks;
	private ArrayList<String[]> paths;
        private Map<Integer, ArrayList<String[]>> sortedRoutes;
        private boolean cannotReachDestinations; 

	public Analysis(String[] landmarks) {
                this.landmarks = landmarks;
		paths = new ArrayList<String[]>();
                sortedRoutes = new TreeMap<Integer, ArrayList<String[]>>(); 
                cannotReachDestinations = false;
	}  

        public boolean isCannotReachDestinations() {
                return cannotReachDestinations;
        }

        public void setCannotReachDestinations(boolean cannotReachDestinations) {
                this.cannotReachDestinations = cannotReachDestinations;
        }

        public void setSortedRoutes(TreeMap<Integer, ArrayList<String[]>> sortedRoutes) {
                this.sortedRoutes = sortedRoutes;
        }

        public Map<Integer, ArrayList<String[]>> getSortedRoutes() {
                return sortedRoutes;
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

	public static void deletePath(String file, String route, String origin, String destination) {
		BufferedReader reader;
		BufferedWriter writer;
		String contents = "";
		String route1 = route + "(" + origin + "," + destination + ").";
		String route2 = route + "(" + destination + "," + origin + ").";
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

}
