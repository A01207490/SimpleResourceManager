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
import java.util.ArrayList;
import java.util.Collections;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import parallel.Analysis;
import prolog.PrologQuery;

public class LandmarksController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	@FXML
	private ListView<String> landmarksListView;
	@FXML
	private Button addButton;
	@FXML
	private TextField textFieldLandmarkFilter;
        @FXML
	private TextField landmarkTextField;

	private String file = "pl\\landmarks.pl";
	private ArrayList<String> landmarkCatalog;

	public void initialize() {
		addButton.setDisable(true);
		initializeListView();
		// landmarksListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	public void initializeListView() {
		landmarksListView.getItems().clear();
		landmarkCatalog = PrologQuery.getLandmarks();
		Collections.sort(landmarkCatalog);
		if (landmarkCatalog != null) {
			landmarksListView.getItems().addAll(landmarkCatalog);
                        TextFields.bindAutoCompletion(textFieldLandmarkFilter, landmarkCatalog);
		}
	}

	public void keyReleasedTextField() {
		try {
			String string = landmarkTextField.getText();
			char firstCharacter = string.charAt(0);
			boolean isBlank = (string.isEmpty());
			boolean doesNotStartsWithLetter = !(Character.isLetter(firstCharacter));
			addButton.setDisable(doesNotStartsWithLetter || isBlank);
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(landmarkTextField.getText());
	}

	public void delete(ActionEvent event) {
		try {
			String entry = landmarksListView.getSelectionModel().getSelectedItems().get(0);
			boolean landmarkUsedRoute = false, landmarkUsedLocation = false;
			landmarkUsedRoute = PrologQuery.isLandmarkUsedInRoutes(entry);
			landmarkUsedLocation = PrologQuery.isLandmarkUsedInLocations(entry);
			if (landmarkUsedLocation) {
				alertError("Constraint violation.", "Cannot delete a landmark used in a location.");
			} else if (landmarkUsedRoute) {
				alertError("Constraint violation.", "Cannot delete a landmark used in a route.");
			} else {
				entry = "'" + entry + "'";
				Analysis.deleteEntry(file, "landmark", entry);
				initializeListView();
                                alertSuccess("Success","The entry was deleted.");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception");

		}

	}

	public void insert(ActionEvent event) {
		String entry = Analysis.formatEntry4(landmarkTextField.getText());
		if (!PrologQuery.isEntryALandmark(entry)) {
			entry = Analysis.addSimpleQuotationMarks(entry);
			String landmark = String.format("landmark(%s).\n", entry);
			Analysis.insertFile(file, landmark);
			initializeListView();
                        alertSuccess("Success","The entry was added.");
		} else {
			alertError("Duplicate entry.", "Please enter a different name for the landmark.");
		}

	}
        
        public void onActionFilter(){
                String selectedResource = textFieldLandmarkFilter.getText();
                selectedResource = selectedResource.trim();
                boolean noMatch = true;
                landmarksListView.getItems().clear();
                for (int i = 0; i < landmarkCatalog.size(); i++) {
                        if(landmarkCatalog.get(i).toLowerCase().contains(selectedResource.toLowerCase())){
                                landmarksListView.getItems().addAll(landmarkCatalog.get(i));
                                noMatch = false;
                        }
                }
                if(noMatch){
                        landmarksListView.getItems().addAll(landmarkCatalog);
                }
        }
        
        public void onActionClear(){
                landmarksListView.getItems().clear();
                landmarksListView.getItems().addAll(landmarkCatalog);
                textFieldLandmarkFilter.setText("");
        }

	public void alertError(String header, String text) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(text);
		alert.showAndWait();
	}
        
        public void alertSuccess(String header, String text) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(header);
		alert.setContentText(text);
		alert.showAndWait();
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

}
