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

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuController {
	private Stage stage;
	private Scene scene;
	private Parent root;

	private void load(ActionEvent event) {
		stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void switchToQuery(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Query.fxml"));
		load(event);
	}

	public void switchToResources(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Resources.fxml"));
		load(event);
	}

	public void switchToLandmarks(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Landmarks.fxml"));
		load(event);
	}

	public void switchToLocations(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Locations.fxml"));
		load(event);
	}

	public void switchToPaths(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Paths.fxml"));
		load(event);
	}

	public void switchToQueryRecipes(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("QueryRecipes.fxml"));
		load(event);
	}

}
