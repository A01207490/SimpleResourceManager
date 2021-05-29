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

import org.controlsfx.control.textfield.TextFields;

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
import javafx.stage.Stage;
import parallel.Analysis;
import prolog.PrologQuery;

public class LocationController {
        private String resourceString;
        private final String file = "pl\\locations.pl";
	private Stage stage;
	private Scene scene;
	private Parent root;
        @FXML
	private Button addButton, deleteButton;
        @FXML
	private ListView<String>    landmarkListView;
        private String[]            landmarkArrayStrings;
	
	
	
	// ArrayList<String> landmarkCatalogArrayStrings;
        
	@FXML
	private ComboBox<String>        resourceComboBox,       landmarkComboBox;
	private ObservableList<String>  resourceObservableList, landmarkObservableList;
        private ArrayList<String>       resourceCatalog,        landmarkCatalog;

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
                resourceCatalog = PrologQuery.getResources();
                landmarkCatalog = PrologQuery.getLandmarks();
                Collections.sort(resourceCatalog);
                Collections.sort(landmarkCatalog);
		resourceObservableList = FXCollections.observableArrayList(resourceCatalog);
		landmarkObservableList = FXCollections.observableArrayList(landmarkCatalog);
		resourceComboBox.setItems(resourceObservableList);
		landmarkComboBox.setItems(landmarkObservableList);
		TextFields.bindAutoCompletion(resourceComboBox.getEditor(), resourceComboBox.getItems());
		TextFields.bindAutoCompletion(landmarkComboBox.getEditor(), landmarkComboBox.getItems());
	}

	public void initialize() {
		initializeComboBox();
		setDisableButtons(true);
                landmarkListView.setPlaceholder(new Label("Please select a resource."));
	}

	public void initializeListView() {
                PrologQuery.isEntryAResource(resourceString);
		if (PrologQuery.isEntryAResource(resourceString)) {
                        landmarkArrayStrings = PrologQuery.getLandmarksWhereResourceIsFound(resourceString);
                        if(landmarkArrayStrings.length == 0){
                                landmarkListView.getItems().clear();
                                landmarkListView.setPlaceholder(new Label("No associated landmarks found."));
                        }else{
                                landmarkListView.getItems().clear();
                                landmarkListView.getItems().addAll(landmarkArrayStrings);
                        }
			setDisableButtons(false);
		} else {
			setDisableButtons(true);
			alertError("Invalid entry.", "Please enter a valid resource.");
		}
	}
        
        public void resetComponents(){
                landmarkListView.getItems().clear();
		setDisableButtons(true);
                landmarkListView.setPlaceholder(new Label("Please select a resource."));
        }
        
	public void setDisableButtons(Boolean val) {
		addButton.setDisable(val);
		deleteButton.setDisable(val);
                landmarkComboBox.setDisable(val);
	}

	public void onActionComboBoxResource() {
		try {
			resourceString = resourceComboBox.getValue();
			System.out.println(resourceString);
			initializeListView();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void delete(ActionEvent event) {
		try {
			ObservableList<String> selectedItems = landmarkListView.getSelectionModel().getSelectedItems();
			String entry = selectedItems.get(0);
                        if(entry != null){
                                String contents = "location(";
                                // resourceString = "'" + resourceString + "'";
                                contents += "'" + resourceString + "'" + ",[";
                                for (String string : landmarkArrayStrings) {
                                        if (!string.equals(entry)) {
                                                string = "'" + string + "'";
                                                contents += string + ",";
                                        }
                                }
                                if (contents.charAt(contents.length() - 1) == ',') {
                                        contents = contents.substring(0, contents.length() - 1);
                                } else {
                                        contents = contents.substring(0, contents.length());
                                }
                                contents += "]).\n";
                                System.out.println(contents);
                                Analysis.updateLocation(file, "'" + resourceString + "'", contents);
                                initializeListView();
                                alertSuccess("Success","The entry was deleted.");
                        }
		} catch (Exception e) {
			System.out.println("Exception");
		}
	}

	public void insert(ActionEvent event) {
		// String entry = Analysis.formatEntry4(landmarkComboBox.getValue());
                String entry = landmarkComboBox.getValue();
		System.out.println(entry);
		if (PrologQuery.isEntryALandmark(entry)) {
			entry = Analysis.addSimpleQuotationMarks(entry);
			String contents = "location(";
			// resourceString = "'" + resourceString + "'";
			contents += "'" + resourceString + "'" + ",[";
			for (String string : landmarkArrayStrings) {
				string = "'" + string + "'";
				contents += string + ",";
				if (string.equals(entry)) {
					alertError("Duplicate entry.", "Please enter a different landmark.");
					return;
				}
			}
			contents += entry + "]).\n";
			System.out.println(contents);
			Analysis.updateLocation(file, "'" + resourceString + "'", contents);
			initializeListView();
                        alertSuccess("Success","The entry was added.");
		} else {
			alertError("Invalid entry.", "Please enter a valid landmark.");
		}

	}

        public void alertSuccess(String header, String text) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(header);
		alert.setContentText(text);
		alert.showAndWait();
	}
        
	public void alertError(String header, String text) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
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
