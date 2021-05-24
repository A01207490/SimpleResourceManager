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
import java.util.Map;

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
import parallel.Destinations;
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

	public void sumbit(ActionEvent event) {
                pathListView.getItems().clear();
                landmarksListView.getItems().clear();
                resourcesListView.getItems().clear();
                closestDestination.setText("");
		// resourceString = resource.getText();
		// landmarkString = landmark.getText();
		resourceString = resourceComboBox.getValue();
		landmarkString = landmarkComboBox.getValue();
		String origin = landmarkString;
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
		// Get all the landmarks where you can find the resource
                Analysis kb = new Analysis(PrologQuery.getLandmarksWhereResourceIsFound(resource));
		kb.setOrigin(origin);
                // System.out.println("Landmarks where resource is found");
		// kb.setLandmarks(PrologQuery.getLandmarksWhereResourceIsFound(resource));
                for (int i = 0; i < kb.getLandmarks().length; i++) {
			//System.out.println("- " + kb.getLandmarks()[i]);
		}
                  
		String path[] = null;
		String destination;
                long start, end;
                start = System.currentTimeMillis();
		// Obtain all the shortest paths from the origin to the landmarks where you can find the resource
		for (int i = 0; i < kb.getLandmarks().length; i++) {
			destination = kb.getLandmarks()[i];
			// System.out.println("Destination: " + destination);
                        path = PrologQuery.shortestRoute(origin, destination);
                        if(path != null){
                            //path = PrologQuery.shortestRoute(origin, destination);
                            kb.getPaths().add(path);
                        }else{
                            alertError("Incomplete map.", "Cannot reach destinations from origin.");
                            return;
                        } 
                      
                        
		}
                end = System.currentTimeMillis();
                System.out.println("Finished after: " + (end-start) + " miliseconds\n");
		Thread thread1 = new Thread(new Worker(kb), "Pair");
		Thread thread2 = new Thread(new Worker(kb), "Odd");
		Thread thread3 = new Thread(new Destinations(kb), "Destinations");
                System.out.println("Origin: " + origin);
                
                long startTime, endTime, singleThreaded, multiThreaded;
                startTime = System.currentTimeMillis();
		thread1.start();
		thread2.start();
		try {
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
                endTime = System.currentTimeMillis();
                multiThreaded = endTime - startTime;
		System.out.println("Finished after: " + multiThreaded + " miliseconds\n");
                Map<Integer, ArrayList<String[]>> sortedRoutes = kb.getSortedRoutes();
                String[] destinations = new String[kb.getLandmarks().length];
                // using for-each loop for iteration over Map.entrySet()
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
                                System.out.println(destinations[i]);
                                if(i == 0){
                                     closestDestinationString = destination;  
                                     path = currentPath;
                                }
                                i++;
                        };
                        
                }
                
		landmarksListView.getItems().addAll(destinations);
                
		pathListView.getItems().addAll(path);
		
		resourcesListView.getItems().addAll(PrologQuery.getResourcesFoundInLandmark(closestDestinationString));
                closestDestination.setText(closestDestinationString);
                
                
                /*
		int min_pos;
		if (kb.getOdd_len() < kb.getPair_len()) {
			min_pos = kb.getOdd_pos();
		} else {
			min_pos = kb.getPair_pos();
		}
		path = kb.getPaths().get(min_pos);
                int length = path.length;
                System.out.println(length);
                if(length == 0){
                        destination = origin;
                }else{
                        destination = path[path.length - 1];
                }
                */
		
		// System.out.println("Closest landmark where you can find " + resource + ": " + destination);
		/*
                System.out.println("List of landmarks where you can find " + resource);
		for (int i = 0; i < kb.getDestinations().length; i++) {
			System.out.println("- " + kb.getDestinations()[i]);
		}
                */
                /*
                for (int i = 0; i < kb.getLandmarks().length; i++) {
			System.out.println("- " + kb.getLandmarks()[i]);
		}
                */
                /*
		landmarksListView.getItems().clear();
    

		landmarksListView.getItems().addAll(kb.getDestinations());
                // landmarksListView.getItems().addAll(kb.getLandmarks());
		pathListView.getItems().clear();

		pathListView.getItems().addAll(path);
		resourcesListView.getItems().clear();

		resourcesListView.getItems().addAll(PrologQuery.getResourcesFoundInLandmark(destination));

		
                closestDestination.setText(destination);
                System.out.println();
*/
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
