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

import prolog.PrologQuery;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import prolog.Recipe;

public class RecipesController implements Initializable {
	private Stage stage;
	private Scene scene;
	private Parent root;
	@FXML
	private TableView<Recipe> RecipeTable;
	@FXML
	private TableColumn<Recipe, String> nameColumn;
	@FXML
	private TableColumn<Recipe, String> statColumn;
	@FXML
	private TableColumn<Recipe, String> potencyColumn;
	@FXML
	private TableColumn<Recipe, String> durationColumn;
	@FXML
	private TableColumn<Recipe, String> resourcesColumn;

	private void load(ActionEvent event) {
		stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void initialize(URL locat, ResourceBundle res) {
		nameColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("name"));
		statColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("stat"));
		potencyColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("potency"));
		durationColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("duration"));
		resourcesColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("resources"));
		RecipeTable.setItems(getRecipes());

	}

	public ObservableList<Recipe> getRecipes() {
		ObservableList<Recipe> observableList = FXCollections.observableArrayList();
		String queryString = "getRecipes(_," + "'Attack'" + "," + 3 + "," + 15 + ",_,X).";
		ArrayList<Recipe> data = PrologQuery.getRecipes(queryString);
		for (int i = 0; i < data.size(); i++) {
			observableList.add(data.get(i));
		}
		return observableList;
	}

	public void switchToMenu(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		load(event);
	}

	public void sumbit(ActionEvent event) {

	}
}
