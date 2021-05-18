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
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import parallel.Analysis;
import prolog.PrologQuery;

public class PathController {
        private String originString;
        private final String file = "pl\\routes.pl";
	private Stage stage;
	private Scene scene;
	private Parent root;
        @FXML 
        private Button addButton, deleteButton;
        @FXML 
        private ListView<String>        destinationsListView;
        private String[]                destinationsArrayStrings;
	@FXML 
        private ComboBox<String>        availableDestinationsComboBox,          originsComboBox;
        private ObservableList<String>  availableDestinationsObservableList,    originsObservableList;
        private ArrayList<String>       availableDestinationsArrayStrings,      originsArrayStrings;
        
        public ComboBox<String> getAvailableDestinationsComboBox() {
                return availableDestinationsComboBox;
        }

         public ComboBox<String> getOriginsComboBox() {
                return originsComboBox;
        }
         
        public void setAvailableDestinationsComboBox(ComboBox<String> availableDestinationsComboBox) {
                this.availableDestinationsComboBox = availableDestinationsComboBox;
        }

        public void setOriginsComboBox(ComboBox<String> originsComboBox) {
                this.originsComboBox = originsComboBox;
        }
        
	public void initialize() {
		initializeComboBoxOrigins();
		setDisableComponents(true);
	}

	public void initializeListViewDestinations() {
		destinationsListView.getItems().clear();
		destinationsArrayStrings = PrologQuery.getListOfDestinationsOfLandmark(originString);
		destinationsListView.getItems().addAll(destinationsArrayStrings);
	}
        
        public void initializeComboBoxOrigins() {
		originsComboBox.setEditable(true);
		originsArrayStrings = PrologQuery.getLandmarks();
                Collections.sort(originsArrayStrings);
		originsObservableList = FXCollections.observableArrayList(originsArrayStrings);
		originsComboBox.setItems(originsObservableList);
		TextFields.bindAutoCompletion(originsComboBox.getEditor(), originsComboBox.getItems());
	}
        
	public void initializeComboBoxAvailableDestinations() {
		availableDestinationsComboBox.setEditable(true);
		availableDestinationsArrayStrings = PrologQuery.getAvailableLandmarks(originString);
                Collections.sort(availableDestinationsArrayStrings);
		availableDestinationsObservableList = FXCollections.observableArrayList(availableDestinationsArrayStrings);
		availableDestinationsComboBox.setItems(availableDestinationsObservableList);
		TextFields.bindAutoCompletion(availableDestinationsComboBox.getEditor(), availableDestinationsComboBox.getItems());
	}

	public void initiliazeComponents() {
		try {
			originString = originsComboBox.getValue();
			if (PrologQuery.isEntryALandmark(originString)) {
                                initializeListViewDestinations();
				initializeComboBoxAvailableDestinations();
				deleteButton.setDisable(false);
				availableDestinationsComboBox.setDisable(false);
			} else {
				destinationsListView.getItems().clear();
				setDisableComponents(true);
				alertError("Invalid entry.", "Please enter a valid landmark.");
			}
		} catch (Exception e) {
			System.out.println("Exception");
		}
	}

	public void setDisableComponents(boolean val) {
		deleteButton.setDisable(val);
		addButton.setDisable(val);
		availableDestinationsComboBox.setDisable(val);
	}

	public void onActionComboBoxOrigins() {
		initiliazeComponents();
	}

	public void onActionComboBoxAvailableDestinations() {
		addButton.setDisable(false);
	}

	public void delete(ActionEvent event) {
		try {
			ObservableList<String> selectedItems = destinationsListView.getSelectionModel().getSelectedItems();
			String entry = selectedItems.get(0);
                        if(entry != null){
                                originString = Analysis.addSimpleQuotationMarks(originString);
                                entry = Analysis.addSimpleQuotationMarks(entry);
                                System.out.println(originString + "-" + entry);
                                Analysis.deletePath(file, originString, entry);
                                initiliazeComponents();
                                alertSuccess("Success","The entry was deleted.");
                        }
		} catch (Exception e) {
			System.out.println("Exception");
		}
	}

	public void insert(ActionEvent event) {
		try {
			Boolean entryIsLandmark = false;
			Boolean newDestination = true;
			String entry = Analysis.formatEntry4(availableDestinationsComboBox.getValue());
			for (String string : destinationsArrayStrings) {
				if (entry.equals(string)) {
					newDestination = false;
				}
			}
			entryIsLandmark = PrologQuery.isEntryALandmark(entry);
			if (entryIsLandmark && newDestination) {
				entry = Analysis.addSimpleQuotationMarks(entry);
				originString = Analysis.addSimpleQuotationMarks(originString);
				String contents = "route(" + originString + "," + entry + ").\n" + "route(" + entry + "," + originString + ").\n";
				System.out.println(contents);
				Analysis.insertFile(file, contents);
				initiliazeComponents();
				addButton.setDisable(true);
                                alertSuccess("Success","The entry was added.");
			} else if (!entryIsLandmark) {
				alertError("Invalid entry.", "Please enter a valid landmark.");
			} else {
				alertError("Duplicate entry.", "Please enter a new landmark.");
			}
		} catch (Exception e) {
			alertError("Exception Invalid entry.", "Please enter a valid landmark.");
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
