package game;

import game.model.AI;
import game.model.GomokuBoard;
import game.model.Player;
import game.model.Referee;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	private static Stage stage;
	
	public static GomokuBoard board;
	public static Referee referee;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		stage.setTitle("Gomoku narabe");
		stage.setResizable(false);
		
		Parent menu = FXMLLoader.load(getClass().getResource("view/menu.fxml"));
		stage.setScene(new Scene(menu));
		stage.show();
	}
	
	public static void play(Player p1, Player p2) {
		stage.hide();
		
		board = new GomokuBoard();
		referee = new Referee(board, p1, p2);
		
		try {
			Parent board = FXMLLoader.load(Main.class.getResource("view/board.fxml"));
			stage.setScene(new Scene(board));
		} catch (Exception ignored) {
		}
		stage.show();
	}
	
	public static void restart() {
		stage.hide();
		stage.setTitle("Gomoku narabe");
		stage.setResizable(false);
		
		AI.Engine.stopAI();
		Parent menu;
		
		try {
			menu = FXMLLoader.load(Main.class.getResource("view/menu.fxml"));
		} catch (Exception ignored) {
			return;
		}
		
		stage.setScene(new Scene(menu));
		stage.show();
	}
	
	public static void close() {
		stage.close();
	}
	
	public static String getRes(String s) {
		return Main.class.getResource("resources/" + s).toString();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
