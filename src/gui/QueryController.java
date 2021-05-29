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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.controlsfx.control.textfield.TextFields;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import parallel.Analysis;
import parallel.Graph;
import parallel.ShortestRoute;
import parallel.Worker;
import prolog.PrologQuery;

public class QueryController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private String resourceString, landmarkString;
	@FXML
        private Label closestDestination;
        @FXML
	private TextField resource, landmark;
	@FXML
	private Button submit;
	@FXML
	private ListView<String> landmarksListView, pathListView, resourcesListView;
	@FXML
	private ComboBox<String> resourceComboBox, landmarkComboBox;
	ObservableList<String> resourceObservableList, landmarkObservableList;

	public ObservableList<String> getLandmarkObservableList() {
		return landmarkObservableList;
	}

	public void setLandmarkObservableList(ObservableList<String> landmarkObservableList) {
		this.landmarkObservableList = landmarkObservableList;
	}

	public ObservableList<String> getResourceObservableList() {
		return resourceObservableList;
	}

	public void setResourceObservableList(ObservableList<String> resourceObservableList) {
		this.resourceObservableList = resourceObservableList;
	}

	private void initializeComboBox() {
		resourceComboBox.setEditable(true);
		landmarkComboBox.setEditable(true);
		resourceObservableList = FXCollections.observableArrayList(resourcesList);
		landmarkObservableList = FXCollections.observableArrayList(landmarksList);
		resourceComboBox.setItems(resourceObservableList);
		landmarkComboBox.setItems(landmarkObservableList);
		TextFields.bindAutoCompletion(resourceComboBox.getEditor(), resourceComboBox.getItems());
		TextFields.bindAutoCompletion(landmarkComboBox.getEditor(), landmarkComboBox.getItems());
	}

	private ArrayList<String> resourcesList;
	private ArrayList<String> landmarksList;

	public void initialize() {
		submit.setDisable(true);
		resourcesList = PrologQuery.getResourcesThatHaveLocations();
		//landmarksList = PrologQuery.getLandmarks();
		landmarksList = PrologQuery.getLandmarksUsedInRoutes();
                Collections.sort(resourcesList);
                Collections.sort(landmarksList);
                initializeComboBox();
		// TextFields.bindAutoCompletion(resource, resourcesList);
		// TextFields.bindAutoCompletion(landmark, landmarksList);
	}

	private void load(ActionEvent event) {
		stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void switchToMenu(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		load(event);
	}

	public void keyReleasedTextFields() {
		resourceString = resource.getText();
		landmarkString = landmark.getText();
		boolean isDisabled = (resourceString.isEmpty()) || (landmarkString.isEmpty());
		// submit.setDisable(isDisabled);
	}

	public void onActionComboBoxes() {
		try {
			resourceString = resourceComboBox.getValue();
			landmarkString = landmarkComboBox.getValue();
			boolean isDisabled = (resourceString.isEmpty()) || (landmarkString.isEmpty());
			submit.setDisable(isDisabled);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
        
        private long singleThreadedJava = 0, multiThreadedJava = 0, singleThreadedProlog = 0;
        private String closestDestinationString, origin;
        private  Analysis kb;
        // Shortest path and list of detinations with lengths.
        private String[] path, destinations;
        private Graph map = PrologQuery.getMap();
        
	public void sumbit(ActionEvent event) {
                pathListView.getItems().clear();
                landmarksListView.getItems().clear();
                resourcesListView.getItems().clear();
                closestDestination.setText("");
		resourceString = resourceComboBox.getValue();
		landmarkString = landmarkComboBox.getValue();
		origin = landmarkString;
		String resource = resourceString;
		if (!PrologQuery.isEntryAResource(resourceString)) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("Invalid entry.");
			alert.setContentText("Please enter a valid resource.");
			alert.showAndWait();
			return;
		}
		if (!PrologQuery.isLandmarkUsedInRoutes(landmarkString)) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("Invalid entry.");
			alert.setContentText("Please enter a valid landmark.");
			alert.showAndWait();
			return;
		}
                long start, end;
		String destination;
		//*****Get all the landmarks where you can find the resource
                kb = new Analysis(PrologQuery.getLandmarksWhereResourceIsFound(resource));
		kb.setOrigin(origin);
                initializePaths(kb);
                int n = 1;
                long sum1TP = 0;
                long sum1TJ = 0;
                long sum2TJ = 0;
                long sum3TJ = 0;
                long sum4TJ = 0;
                long sumQTJ = 0;
                long interruption;
                for (int i = 0; i < n; i++) {
                        //*****Single threaded shortest route prolog
                        //sum1TP += singleThreadedSetShortestRouteProlog();
                        //*****Single threaded shortest route java
                        //interruption = singleThreadedSetShortestRouteJava();
                        /*
                        if(interruption == -10){
                                //System.out.println(interruption);
                                return;
                        }else{
                                //System.out.println(interruption);
                                sum1TJ += interruption;
                        }
                        */
                        //*****Two threaded shortest route java
                        sum2TJ += multiThreadedSetShortestRouteJava(2);
                        if(kb.isCannotReachDestinations()){
                                alertError("Incomplete map.", "Cannot reach destinations from origin.");
                                return;
                        }
                        //*****Three threaded shortest route java
                        //sum3TJ += multiThreadedSetShortestRouteJava(3);
                        //*****Four threaded shortest route java
                        //sum4TJ += multiThreadedSetShortestRouteJava(4);
                        //*****Amount of destinations threaded shortest route java
                        //sumQTJ +=  multiThreadedSetShortestRouteJava(kb.getLandmarks().length);
                }
                float avg1TProlog = sum1TP / n;
                float avg1TJava = sum1TJ / n;
                float avg2TJava = sum2TJ / n;
                float avg3TJava = sum3TJ / n;
                float avg4TJava = sum4TJ / n;
                float avgQTJava = sumQTJ / n;
                System.out.printf("\nQuerying %d paths\n1TP = %fms\n1TJ = %fms\n2TJ = %fms\n3TJ = %fms\n4TJ = %fms\n%dTJ = %fms\n", kb.getLandmarks().length, avg1TProlog, avg1TJava, avg2TJava, avg3TJava, avg4TJava, kb.getLandmarks().length, avgQTJava);
                //*****Sort
                n = 100;
                sum1TJ = 0;
                sum2TJ = 0;
                sum3TJ = 0;
                sum4TJ = 0;
                for (int i = 0; i < n; i++) {
                        //*****Single threaded shortest route java
                        kb.getSortedRoutes().clear();
                        sum1TJ += sortPaths(1);
                        //*****Two threaded shortest route java
                        kb.getSortedRoutes().clear();
                        sum2TJ += sortPaths(2);
                        //*****Three threaded shortest route java
                        kb.getSortedRoutes().clear();
                        sum3TJ += sortPaths(3);
                        //*****Four threaded shortest route java
                        kb.getSortedRoutes().clear();
                        sum4TJ += sortPaths(4);
                }
                buildDestinationsWithLength();
                avg1TJava = sum1TJ / n;
                avg2TJava = sum2TJ / n;
                avg3TJava = sum3TJ / n;
                avg4TJava = sum4TJ / n;
                System.out.printf("\nSorting %d paths\n1TJ = %fms\n2TJ = %fms\n3TJ = %fms\n4TJ = %fms\n", kb.getLandmarks().length, avg1TJava, avg2TJava, avg3TJava, avg4TJava);
                /*
		Thread thread3 = new Thread(new Worker(kb), "Pair");
		Thread thread4 = new Thread(new Worker(kb), "Odd");
                start = System.currentTimeMillis();
		thread3.start();
		thread4.start();
		try {
			thread3.join();
			thread4.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
                end = System.currentTimeMillis();
                */
               
		//System.out.println("Finished sorting after: " + (end - start) + " miliseconds\n");
                //----------
                /*
                Map<Integer, ArrayList<String[]>> sortedRoutes = kb.getSortedRoutes();
                String[] destinations = new String[kb.getLandmarks().length];
                int length, i = 0;
                String closestDestinationString = null;
                for (Map.Entry<Integer, ArrayList<String[]>> entry : sortedRoutes.entrySet()){
                        for (String[] currentPath : entry.getValue()) { 
                                length = currentPath.length;
                                if(length == 0){
                                        destination = origin;
                                }else{
                                        destination = currentPath[length - 1];
                                }
                                destinations[i] = destination + " (" + entry.getKey().toString() + ")";
                                //System.out.println(destinations[i]);
                                if(i == 0){
                                     closestDestinationString = destination;  
                                     path = currentPath;
                                }
                                i++;
                        }
                        
                }
                */
                pathListView.getItems().clear();
                pathListView.getItems().addAll(path);
                landmarksListView.getItems().clear();
                landmarksListView.getItems().addAll(destinations);
                closestDestination.setText(""); 
		closestDestination.setText(closestDestinationString);
		resourcesListView.getItems().clear();
		resourcesListView.getItems().addAll(PrologQuery.getResourcesFoundInLandmark(closestDestinationString));
	}
        
        public void initializePaths(Analysis kb) {
                String path[] = null;
		for (int i = 0; i < kb.getLandmarks().length; i++) {
                        kb.getPaths().add(i,path);
		}
        }
        
        public void buildDestinationsWithLength(){
                Map<Integer, ArrayList<String[]>> sortedRoutes = kb.getSortedRoutes();
                String destination;
                destinations = new String[kb.getLandmarks().length];
                int length, i = 0;
                for (Map.Entry<Integer, ArrayList<String[]>> entry : sortedRoutes.entrySet()){
                        for (String[] currentPath : entry.getValue()) { 
                                length = currentPath.length;
                                if(length == 0){
                                        destination = origin;
                                }else{
                                        destination = currentPath[length - 1];
                                }
                                destinations[i] = destination + " (" + entry.getKey().toString() + ")";
                                //System.out.println(destinations[i]);
                                if(i == 0){
                                     closestDestinationString = destination;  
                                     path = currentPath;
                                }
                                i++;
                        }       
                }
        }
        
        public long sortPaths(int total){
                int numberOfDestinations = kb.getLandmarks().length;
                int start, end;
                Thread[] T = new Thread[total];
                // Create Threads
                for(int i=0;i<total;i++){
                        start = numberOfDestinations * i / total;
                        end = numberOfDestinations * (i + 1) / total;
                        T[i]= new Thread(new Worker(kb, start, end), Integer.toString(i));
                }
                long startTime = System.currentTimeMillis();
                // Start threads
                for(int i=0;i<total;i++){
                        T[i].start();
                }
                try {
                        // Wait Threads to join
                        for(int i=0;i<total;i++){
                                T[i].join();
                        }
                } catch (Exception e) {
                }
                long endTime = System.currentTimeMillis();
                return (endTime - startTime);
                /*
                Thread thread3 = new Thread(new Worker(kb, start, end), "Pair");
		Thread thread4 = new Thread(new Worker(kb, start, end), "Odd");
                long start = System.currentTimeMillis();
		thread3.start();
		thread4.start();
		try {
			thread3.join();
			thread4.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
                long end = System.currentTimeMillis();
                return (end - start);
                */
        }
        
        public long singleThreadedSetShortestRouteProlog() {
                String path[] = null;
		String destination;
                long start = System.currentTimeMillis();
		for (int i = 0; i < kb.getLandmarks().length; i++) {
			destination = kb.getLandmarks()[i];
                        path = PrologQuery.shortestRoute(origin, destination);
                        if(path != null){
                                kb.getPaths().set(i,path);
                        }else{
                                alertError("Incomplete map.", "Cannot reach destinations from origin.");
                                return 0;
                        }
		}
                long end = System.currentTimeMillis();
                return (end - start);              
        }
        
        public long singleThreadedSetShortestRouteJava() {
                String path[] = null;
		String destination;
                long start = System.currentTimeMillis();
		for (int i = 0; i < kb.getLandmarks().length; i++) {
			destination = kb.getLandmarks()[i];
                        try {
                                if(origin.equals(destination)){
                                        String[] aux = {origin};
                                        path = aux;                                         
                                }else{
                                        path = map.shortestPath(origin,destination).toArray(new String[0]); 
                                }
                                kb.getPaths().set(i,path);
                        } catch (Exception e) {
                                alertError("Incomplete map.", "Cannot reach destinations from origin.");
                                kb.setCannotReachDestinations(true);
                                return 0;
                        }
		}
                long end = System.currentTimeMillis();
                return (end - start);
        }
        
       
        
        public long multiThreadedSetShortestRouteJava(int total){
                int numberOfDestinations = kb.getLandmarks().length;
                int start, end;
                Thread[] T = new Thread[total];
                // Create Threads
                for(int i=0;i<total;i++){
                        start = numberOfDestinations * i / total;
                        end = numberOfDestinations * (i + 1) / total;
                        T[i]= new Thread(new ShortestRoute(kb,map,origin,start,end), Integer.toString(i));
                }
                long startTime = System.currentTimeMillis();
                // Start threads
                for(int i=0;i<total;i++){
                        T[i].start();
                }
                try {
                        // Wait Threads to join
                        for(int i=0;i<total;i++){
                                T[i].join();
                        }
                } catch (Exception e) {
                }
                long endTime = System.currentTimeMillis();
                return (endTime - startTime);
        }
        
        public long multiThreadedSortShortestRoute(int total){
                int numberOfDestinations = kb.getLandmarks().length;
                int start, end;
                Thread[] T = new Thread[total];
                // Create Threads
                for(int i=0;i<total;i++){
                        start = numberOfDestinations * i / total;
                        end = numberOfDestinations * (i + 1) / total;
                        T[i]= new Thread(new ShortestRoute(kb,map,origin,start,end), Integer.toString(i));
                }
                long startTime = System.currentTimeMillis();
                // Start threads
                for(int i=0;i<total;i++){
                        T[i].start();
                }
                try {
                        // Wait Threads to join
                        for(int i=0;i<total;i++){
                                T[i].join();
                        }
                } catch (Exception e) {
                }
                long endTime = System.currentTimeMillis();
                return (endTime - startTime);
        }
        
        public void alertError(String header, String text) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(text);
		alert.showAndWait();
	}
        
	public class Console extends OutputStream {
		private TextArea console;

		public Console(TextArea console) {
			this.console = console;
		}

		public void appendText(String valueOf) {
			Platform.runLater(() -> console.appendText(valueOf));
		}

		public void write(int b) throws IOException {
			appendText(String.valueOf((char) b));
		}
	}
}
