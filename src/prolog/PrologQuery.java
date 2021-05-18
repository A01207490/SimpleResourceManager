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
package prolog;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jpl7.Atom;
import org.jpl7.Compound;
import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Variable;

public class PrologQuery {
	public Map<String, Term>[] solutions;

	private String queryString;
	private Query query;

	public PrologQuery(String queryString) {
		this.query = new Query(queryString);
		this.queryString = queryString;
	}

	public static Map<String, Term>[] getSolutions(Query query) {
		Query consult = new Query("consult", new Term[] { new Atom("pl\\queries.pl") });
		try {
			consult.hasSolution();
			return query.allSolutions();
		} catch (Exception e) {
			return null;
		}
	}

	public static String[] getSolution(Query query) {
		java.util.Map<String, Term> term;
		Query consult = new Query("consult", new Term[] { new Atom("pl\\queries.pl") });
		try {
			consult.hasSolution();
			term = query.oneSolution();
			return Term.atomListToStringArray(term.get("X"));
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean getSolutionBoolean(Query query) {
		Query consult = new Query("consult", new Term[] { new Atom("pl\\queries.pl") });
		try {
			consult.hasSolution();
			return query.hasSolution();
		} catch (Exception e) {
			return false;
		}
	}

	public static String[] getLandmarksWhereResourceIsFound(String resource) {
		Query query = new Query("getLandmarksWhereResourceIsFound", new Term[] { new Atom(resource), new Variable("X") });
		return getSolution(query);
	}

	public static String[] getListOfDestinationsOfLandmark(String landmarkString) {
		Query query = new Query("getListOfDestinationsOfLandmark", new Term[] { new Atom(landmarkString), new Variable("X") });
		return getSolution(query);
	}

	public static String[] shortestRoute(String origin, String destination) {
		Query query = new Query(new Compound("shortestRoute", new Term[] { new Atom(origin), new Atom(destination), new Variable("X") }));
		return getSolution(query);
	}

        public static boolean isLandmarkUsedInRoutes(String landmarkString) {
		Query query = new Query("isLandmarkUsedInRoutes", new Term[] { new Atom(landmarkString) });
		return getSolutionBoolean(query);
	}
        
	public static boolean isDestinationReachable(String landmarkOrigin, String landmarkDestination) {
		Query query = new Query("isDestinationReachable", new Term[] { new Atom(landmarkOrigin),  new Atom(landmarkDestination) });
		return getSolutionBoolean(query);
	}

	public static boolean isLandmarkUsedInLocations(String landmarkString) {
		Query query = new Query("isLandmarkUsedInLocations", new Term[] { new Atom(landmarkString) });
		return getSolutionBoolean(query);
	}

	public static boolean isResourceUsedInLocations(String resourceString) {
		Query query = new Query("isResourceUsedInLocations", new Term[] { new Atom(resourceString) });
		return getSolutionBoolean(query);
	}

	public static boolean isEntryAResource(String entryString) {
		Query query = new Query("isEntryAResource", new Term[] { new Atom(entryString) });
		return getSolutionBoolean(query);
	}

	public static boolean isEntryALandmark(String entryString) {
		Query query = new Query("isEntryALandmark", new Term[] { new Atom(entryString) });
		return getSolutionBoolean(query);
	}

	public static ArrayList<String> getResources() {
		Query query = new Query("getResources(X).");
		Map<String, Term>[] solutions = getSolutions(query);
		String string;
		ArrayList<String> arrayList = new ArrayList<String>();
		Pattern p = Pattern.compile("^(\\')(.*)(\\')");
		Matcher m;
		for (int i = 0; i < solutions.length; i++) {
			// System.out.println("X = " + solutions[i].get("X").toString());
			m = p.matcher(solutions[i].get("X").toString());
			if (m.find()) {
				string = m.group(2);
				arrayList.add(string);
			} else {
				System.out.println("NO MATCH");
			}
		}
		return arrayList;
	}

	public static ArrayList<String> getResourcesThatHaveLocations() {
		Query query = new Query("getResourcesThatHaveLocations(X).");
		Map<String, Term>[] solutions = getSolutions(query);
		String string;
		ArrayList<String> arrayList = new ArrayList<String>();
		Pattern p = Pattern.compile("^(\\')(.*)(\\')");
		Matcher m;
		for (int i = 0; i < solutions.length; i++) {
			// System.out.println("X = " + solutions[i].get("X").toString());
			m = p.matcher(solutions[i].get("X").toString());
			if (m.find()) {
				string = m.group(2);
				arrayList.add(string);
			} else {
				System.out.println("NO MATCH");
			}
		}
		return arrayList;
	}

	public static ArrayList<String> getLandmarks() {
		Query query = new Query("getLandmarks(X).");
		Map<String, Term>[] solutions = getSolutions(query);
		String string;
		ArrayList<String> arrayList = new ArrayList<String>();
		Pattern p = Pattern.compile("^(\\')(.*)(\\')");
		Matcher m;
		for (int i = 0; i < solutions.length; i++) {
			// System.out.println("X = " + solutions[i].get("X").toString());
			m = p.matcher(solutions[i].get("X").toString());
			if (m.find()) {
				string = m.group(2);
				arrayList.add(string);
			} else {
				System.out.println("NO MATCH");
			}
		}
		return arrayList;
	}
        
        public static ArrayList<String> getLandmarksUsedInRoutes() {
		Query query = new Query("getLandmarksUsedInRoutes(X).");
		Map<String, Term>[] solutions = getSolutions(query);
		String string;
		ArrayList<String> arrayList = new ArrayList<String>();
		Pattern p = Pattern.compile("^(\\')(.*)(\\')");
		Matcher m;
		for (int i = 0; i < solutions.length; i++) {
			// System.out.println("X = " + solutions[i].get("X").toString());
			m = p.matcher(solutions[i].get("X").toString());
			if (m.find()) {
				string = m.group(2);
				arrayList.add(string);
			} else {
				System.out.println("NO MATCH");
			}
		}
                LinkedHashSet<String> hashSet = new LinkedHashSet<>(arrayList);
                ArrayList<String> arrayListWithoutDuplicates = new ArrayList<>(hashSet);
                System.out.println(arrayListWithoutDuplicates);
                for (int i = 0; i < arrayListWithoutDuplicates.size(); i++) {
                
                    String get = arrayListWithoutDuplicates.get(i);
                    System.out.println(get);
                            }
		return arrayListWithoutDuplicates;
	}

	public static ArrayList<String> getAvailableLandmarks(String landmarkString) {
		Query query = new Query("getAvailableLandmarks", new Term[] { new Atom(landmarkString), new Variable("X") });
		Map<String, Term>[] solutions = getSolutions(query);
		String string;
		ArrayList<String> arrayList = new ArrayList<String>();
		Pattern p = Pattern.compile("^(\\')(.*)(\\')");
		Matcher m;
		for (int i = 0; i < solutions.length; i++) {
			// System.out.println("X = " + solutions[i].get("X").toString());
			m = p.matcher(solutions[i].get("X").toString());
			if (m.find()) {
				string = m.group(2);
				arrayList.add(string);
			} else {
				System.out.println("NO MATCH");
			}
		}
		return arrayList;
	}

	public static ArrayList<String> getResourcesFoundInLandmark(String landmarkString) {
		Query query = new Query("getResourcesFoundInLandmark", new Term[] { new Atom(landmarkString), new Variable("X") });
		Map<String, Term>[] solutions = getSolutions(query);
		String string;
		ArrayList<String> arrayList = new ArrayList<String>();
		Pattern p = Pattern.compile("^(\\')(.*)(\\')");
		Matcher m;
		for (int i = 0; i < solutions.length; i++) {
			// System.out.println("X = " + solutions[i].get("X").toString());
			m = p.matcher(solutions[i].get("X").toString());
			if (m.find()) {
				string = m.group(2);
				arrayList.add(string);
			} else {
				System.out.println("NO MATCH");
			}
		}
		return arrayList;
	}

	public static ArrayList<Recipe> getRecipes(String stringQuery) {
		Query query = new Query(stringQuery);
		Map<String, Term>[] solutions = getSolutions(query);
		String stat, name, resources;
		int potency, duration;
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		Pattern p = Pattern.compile("^(\\[\\')(.*)(\\'\\, \\')(.*)(\\'\\, )(.*)(\\, )(.*)(\\, \\[)(.*)(\\]\\])");
		Matcher m;
		for (int i = 0; i < solutions.length; i++) {
			// System.out.println("X = " + solutions[i].get("X").toString());
			m = p.matcher(solutions[i].get("X").toString());
			if (m.find()) {
				name = m.group(2);
				stat = m.group(4);
				potency = Integer.parseInt(m.group(6));
				duration = Integer.parseInt(m.group(8));
				resources = m.group(10).replace("'", "");
				recipes.add(new Recipe(name, stat, potency, duration, resources));
			} else {
				System.out.println("NO MATCH");
			}
		}
		return recipes;
	}

}
