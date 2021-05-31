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
package gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import prolog.PrologQuery;
import parallel.Analysis;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jpl7.Term;
import parallel.Graph;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		/*-
		Analysis kb = new Analysis();
		String origin = "torin_wetland";
		String resource = "armoranth";
		kb.setOrigin(origin);
		kb.filter_resource(resource);
		kb.initialize_paths();
		String path[];
		String destination;
		for (int i = 0; i < kb.getLandmarks().length; i++) {
			destination = kb.getLandmarks()[i];
			path = Analysis.shortest_path(origin, destination);
			kb.getPaths().add(i, path);
		}
		// Get the closest destination
		Thread thread1 = new Thread(new Worker(kb), "Pair");
		Thread thread2 = new Thread(new Worker(kb), "Odd");
		Thread thread3 = new Thread(new Destinations(kb), "Destinations");
		// Get all the destinations where you can find the resource
		kb.initialize_destinations();
		
		thread1.start();
		thread2.start();
		thread3.start();
		try {
			thread1.join();
			thread2.join();
			thread3.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int min_pos;
		if (kb.getOdd_len() < kb.getPair_len()) {
			min_pos = kb.getOdd_pos();
		} else {
			min_pos = kb.getPair_pos();
		}
		path = kb.getPaths().get(min_pos);
		for (int j = 0; j < path.length; j++) {
			// System.out.println(path[j]);
		}
		// System.out.println();
		destination = path[path.length - 1];
		System.out.println("Closest landmark where you can find " + resource + ": " + destination);
		System.out.println("List of landmarks where you can find " + resource);
		for (int i = 0; i < kb.getDestinations().size() / 2; i++) {
			System.out.println("- " + kb.getDestinations().get(i));
		}
		
		System.out.println();
		for (int i = 0; i < kb.getPaths().size(); i++) {
			path = kb.getPaths().get(i);
			for (int j = 0; j < path.length; j++) {
				System.out.println(path[j]);
			}
			System.out.println();
		}
		*/

		// Query query = new Query(querString);
		// q.setSolutions(query);
		// Map<String, Term>[] solutions = q.getSolutions();
		/*-
		// Here we need the solutions
		String stat, name, resources;
		int potency, duration;
		stat = "'Attack'";
		duration = 15;
		potency = 3;
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
		*/
                /*
		String entry = "Gerudo Highlands";
		System.out.println(entry);
		System.out.println(PrologQuery.isEntryALandmark(entry));
		// Don't add simple quotations.
		System.out.println(Analysis.addSimpleQuotationMarks(entry));
		System.out.println(PrologQuery.isEntryALandmark(Analysis.addSimpleQuotationMarks(entry)));
                */
                /*
                Map<String, Term>[] solutions = PrologQuery.getSumLocations();
                float sum = 0;
                float count = 0;
                float length = solutions.length;
                for (int i = 0; i < length; i++) {
			int amount = Integer.parseInt(solutions[i].get("X").toString());
                        if(amount > 30 && amount < 40){
                                count++;
                                System.out.printf("%d = %d\n", i, amount);
                                sum += amount;
                        }
		}
                System.out.printf("%.1f percent, avg = %f\n", (count/length*100), sum/count);
                */
                List<String> path;
                Graph graph = new Graph();
                graph.addEdge("A", "B");
                graph.addEdge("A", "C");
                graph.addEdge("B", "C");
                graph.addEdge("B", "D");
                graph.addEdge("C", "D");
                graph.addEdge("C", "E");
                graph.addEdge("D", "E");
                graph.addEdge("D", "F");
               
                path = graph.shortestPath("A","F");
                System.out.println("gui.Main.start()");
                //path = graph.shortestPath("A","F");
                path = graph.shortestPath("A","F");
                        
                
                
                for (int i = 0; i < path.size(); i++) {
                        String get = path.get(i);
                        System.out.printf("%s\n", get);
                }
                
             

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                        primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
