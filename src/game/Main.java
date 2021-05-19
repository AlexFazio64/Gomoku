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
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("view/board.fxml"));
		primaryStage.setTitle("五目並べ - Gomoku narabe");
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();
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
