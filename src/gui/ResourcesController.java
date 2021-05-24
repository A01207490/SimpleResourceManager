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

public class ResourcesController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	@FXML
	private ListView<String> resourcesListView;
	@FXML
	private Button addButton;
        @FXML
	private TextField textFieldResourceFilter;
	@FXML
	private TextField resourceTextField;
	private ArrayList<String> resourceCatalog;
	//private String fileResources = System.getProperty("user.dir") + "\\src\\application\\resources.pl";
	//private String fileLocations = System.getProperty("user.dir") + "\\src\\application\\locations.pl";
        private String fileResources = "pl\\resources.pl";
        private String fileLocations = "pl\\locations.pl";
        

	public void initialize() {
		addButton.setDisable(true);
		initializeListView();
                
		// resourcesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	public void initializeListView() {
		resourcesListView.getItems().clear();
		resourceCatalog = PrologQuery.getResources();
                Collections.sort(resourceCatalog);
		if (resourceCatalog != null) {
			resourcesListView.getItems().addAll(resourceCatalog);
                        TextFields.bindAutoCompletion(textFieldResourceFilter, resourceCatalog);
		}
	}

	public void keyReleasedTextField() {
		try {
			String string = resourceTextField.getText();
			char firstCharacter = string.charAt(0);
			boolean isBlank = (string.isEmpty());
			boolean doesNotStartsWithLetter = !(Character.isLetter(firstCharacter));
			addButton.setDisable(doesNotStartsWithLetter || isBlank);
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(resourceTextField.getText());
	}

	public void delete(ActionEvent event) {
		try {
			boolean invalidEntry = false;
			String entry = resourcesListView.getSelectionModel().getSelectedItems().get(0);
			// invalidEntry = PrologQuery.isResourceUsedInLocations(entry);
			if (!PrologQuery.isResourceUsedInLocations(entry)) {
				entry = "'" + entry + "'";
				Analysis.deleteEntry(fileResources, "resource", entry);
				Analysis.deleteEntry(fileLocations, "location", entry);
				initializeListView();
                                alertSuccess("Success","The entry was deleted.");
			} else {
				alertError("Constraint violation.", "Cannot delete a resource used in location.");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception");
		}

	}

	public void insert(ActionEvent event) {
		/*-
		boolean duplicateEntry = false;
		String entry = Analysis.formatEntry2(resourceTextField.getText());
		for (String string : resourceCatalog) {
			if (entry.equals(Analysis.formatEntry2(string))) {
				duplicateEntry = true;
			}
		}
		*/
		String entry = Analysis.formatEntry4(resourceTextField.getText());
		if (!PrologQuery.isEntryAResource(entry)) {
			entry = Analysis.addSimpleQuotationMarks(entry);
			String location = String.format("location(%s,[]).\n", entry);
			String resource = String.format("resource(%s).\n", entry);
			Analysis.insertFile(fileResources, resource);
			Analysis.insertFile(fileLocations, location);
			initializeListView();
                        alertSuccess("Success","The entry was added.");
		} else {
			alertError("Duplicate entry.", "Please enter a different name for the resource.");
		}

	}
        
        public void onActionFilter(){
                String selectedResource = textFieldResourceFilter.getText();
                selectedResource = selectedResource.trim();
                boolean noMatch = true;
                resourcesListView.getItems().clear();
                for (int i = 0; i < resourceCatalog.size(); i++) {
                        if(resourceCatalog.get(i).toLowerCase().contains(selectedResource.toLowerCase())){
                                resourcesListView.getItems().addAll(resourceCatalog.get(i));
                                noMatch = false;
                        }
                }
                if(noMatch){
                        resourcesListView.getItems().addAll(resourceCatalog);
                }
        }
        
        public void onActionClear(){
                resourcesListView.getItems().clear();
                resourcesListView.getItems().addAll(resourceCatalog);
                textFieldResourceFilter.setText("");
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
