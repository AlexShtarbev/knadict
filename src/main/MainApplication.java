package main;

import java.io.IOException;
import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Controller.FailedToCreateDictionaryDirectoryExceptions;

public class MainApplication extends Application{
	private static final String ADD_WORD_LABEL = "Add word";
	private static final String SEARCH_LABEL = "Search";
	private static final String TRANSLATION_LABEL = "Translation:";
	private static final String WORD_LABEL = "Word:";
	private static final String APP_NAME = "KNA Dictionary";
	private static final String RGB_WHITE = "rgb(255,255,255)";
	private static final Font STANDART_FONT = new Font("Arial", 14);
	
	private TextField searchWordTextField = new TextField();
	private ScrollPane searchTranslationScrollPane = new ScrollPane();
	private Text searchTranslationText = new Text();
	
	private TextField addWordTextField = new TextField();
	private TextField addTranslationScrollPane = new TextField();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(APP_NAME);
		
		// Tab pane
		TabPane tabs = new TabPane();
		tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        
		// Seach tab
		Tab tabSearch = new Tab();
        tabSearch.setText(SEARCH_LABEL);		
		tabSearch.setContent(searchPane(primaryStage));
		
		// Ad dWord tab
		Tab addWordTab = new Tab();
        addWordTab.setText(ADD_WORD_LABEL);		
		addWordTab.setContent(addWordPane(primaryStage));
		
		// Add 
		tabs.getTabs().addAll(tabSearch, addWordTab);
		Scene scene = new Scene(tabs, 300, 400); // Manage scene size
        primaryStage.setScene(scene);
		
		primaryStage.show();
	}

	private Pane searchPane(Stage primaryStage) {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Label wordLabel = new Label(WORD_LABEL);
		grid.add(wordLabel, 0, 1);
		
		grid.add(searchWordTextField, 1, 1);
		searchWordTextField.setFont(STANDART_FONT);
		
		Label translationLabel = new Label(TRANSLATION_LABEL);
		grid.add(translationLabel, 0, 2);
		
		// Translation scroll pane
		searchTranslationScrollPane.setStyle("-fx-background:" + RGB_WHITE);
		searchTranslationText.setFont(STANDART_FONT);
		searchTranslationScrollPane.setContent(searchTranslationText);
		grid.add(searchTranslationScrollPane, 1, 2);
				
		// Create the search button
		Button btn = new Button(SEARCH_LABEL);
		btn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// Display the word that is searched for.
				String word = searchWordTextField.getText();
				try {
					String search = Controller.instance().search(word);
					searchTranslationText.setText(search == null ? DictionaryWord.NO_TRANSLATION_AVAILABLE : search);
					searchTranslationScrollPane.setContent(searchTranslationText);
				} catch (FailedToCreateDictionaryDirectoryExceptions e) {
					showAlertDialog(Controller.ERROR_SEARCHING_WORD);
				} catch (IOException e) {
					showAlertDialog(Controller.ERROR_SEARCHING_WORD);
				}
			}
		});
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 4);
		
		return grid;
	}
	
	private Pane addWordPane(Stage primaryStage) {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Label wordLabel = new Label(WORD_LABEL);
		grid.add(wordLabel, 0, 1);
		
		grid.add(addWordTextField, 1, 1);
		
		Label translationLabel = new Label(TRANSLATION_LABEL);
		grid.add(translationLabel, 0, 2);
		
		grid.add(addTranslationScrollPane, 1, 2);
		
		Button btn = new Button(ADD_WORD_LABEL);
		btn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				try {
					String result = Controller.instance().createDictionaryWord(addWordTextField.getText(), addTranslationScrollPane.getText());
					
					addWordTextField.setText(null);
					addTranslationScrollPane.setText(null);
					
					showInfoDialog(result);
				} catch (IOException e) {
					showAlertDialog(Controller.ERROR_CREATING_WORD);
				} catch (FailedToCreateDictionaryDirectoryExceptions e) {
					showAlertDialog(Controller.ERROR_CREATING_WORD);
				}				
			}
		});
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 4);
		
		return grid;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void showInfoDialog(String text) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(APP_NAME);
		alert.setHeaderText(null);
		alert.setContentText(text);

		alert.showAndWait();
	}
	
	private void showAlertDialog(String text) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(APP_NAME);
		alert.setHeaderText(null);
		alert.setContentText(text);

		alert.showAndWait();
	}

}
