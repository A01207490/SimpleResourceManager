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
                */
             

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
