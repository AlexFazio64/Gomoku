package game;

import game.model.Pawn;
import game.model.Placed;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	public static Handler handler;
	private static Stage stage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		stage.setTitle("五目並べ - Gomoku narabe");
		stage.setResizable(false);
		
		Parent menu = FXMLLoader.load(getClass().getResource("view/menu.fxml"));
		stage.setScene(new Scene(menu));
		stage.show();
	}
	
	public static void play() {
		stage.hide();
		
		try {
			Parent board = FXMLLoader.load(Main.class.getResource("view/board.fxml"));
			stage.setScene(new Scene(board));
		} catch (Exception ignored) {
		}
		
		stage.show();
	}
	
	public static String getRes(String s) {
		return Main.class.getResource("resources/" + s).toString();
	}
	
	public static void main(String[] args) throws Exception {
//		handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));
		handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2linux"));
		ASPMapper.getInstance().registerClass(Pawn.class);
		ASPMapper.getInstance().registerClass(Placed.class);
		launch(args);
	}
}
