package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("view/board.fxml"));
		primaryStage.setTitle("五目並べ - Go moku narabe");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
	
	public static String getRes(String s) {
		return Main.class.getResource("resources/" + s).toString();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
