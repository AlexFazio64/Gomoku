package game.controller;

import game.Main;
import game.model.AI;
import game.model.Human;
import game.model.Player;
import game.model.RuleChecker;
import game.settings.GS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BorderPane;

public class MenuController {
	@FXML
	private ChoiceBox<String> p1option;
	@FXML
	private ChoiceBox<String> p2option;
	@FXML
	private BorderPane pane;
	@FXML
	private CheckBox three_and_three;
	@FXML
	private CheckBox four_and_four;
	@FXML
	private CheckBox handicap;
	@FXML
	private CheckBox swap;
	@FXML
	private CheckBox g_pro;
	@FXML
	private CheckBox freestyle;
	@FXML
	private CheckBox renju;
	@FXML
	private CheckBox omok;
	@FXML
	private CheckBox g_plus;
	
	@FXML
	private void initialize() {
		pane.setBackground(new Background(new BackgroundImage(new Image(Main.getRes("bg_blur.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, null)));
		
		p1option.getItems().add("Human");
		p1option.getItems().add("AI (Normal)");
		p1option.getItems().add("AI (Hard)");
		p1option.setValue("Human");
		
		p2option.getItems().add("Human");
		p2option.getItems().add("AI (Normal)");
		p2option.getItems().add("AI (Hard)");
		p2option.setValue("Human");
		
		RuleChecker checker = RuleChecker.getInstance();
		checker.submit(three_and_three, "size-omok");
		checker.submit(four_and_four, "size");
		checker.submit(handicap, "size");
		checker.submit(swap, "advantage");
		checker.submit(g_pro, "advantage");
		checker.submit(freestyle, "style");
		checker.submit(renju, "style");
		checker.submit(omok, "style-omok");
		checker.submit(g_plus, "style");
		
		checker.registerIncompatibility("size", three_and_three, four_and_four, handicap);
		checker.registerIncompatibility("omok", three_and_three, omok);
		checker.registerIncompatibility("advantage", swap, g_pro);
		checker.registerIncompatibility("style", freestyle, renju, omok, g_plus);
	}
	
	@FXML
	private void ruleChange(ActionEvent actionEvent) {
		CheckBox rule = (CheckBox) actionEvent.getSource();
		RuleChecker.getInstance().update(rule);
	}
	
	@FXML
	private void play(ActionEvent actionEvent) {
		Player p1, p2;
		p1 = p2 = null;
		
		//Game customization
		GS.RULES.THREE = three_and_three.isSelected();
		GS.RULES.FOUR = four_and_four.isSelected();
		GS.RULES.HANDICAP = handicap.isSelected();
		GS.RULES.SWAP = swap.isSelected();
		GS.RULES.PRO = g_pro.isSelected();
		GS.RULES.FREESTYLE = freestyle.isSelected();
		GS.RULES.RENJU = renju.isSelected();
		GS.RULES.OMOK = omok.isSelected();
		GS.RULES.PLUS = g_plus.isSelected();
		
		if ( GS.RULES.RENJU ) {
			GS.GRIDSIZE = 16;
			GS.DIM = GS.GRIDSIZE * GS.CELLSIZE;
		}
		
		switch (p1option.getValue()) {
			case "Human":
				p1 = new Human(1);
				break;
			case "AI (Normal)":
				p1 = new AI(1, false);
				break;
			case "AI (Hard)":
				p1 = new AI(1, true);
				break;
		}
		
		switch (p2option.getValue()) {
			case "Human":
				p2 = new Human(2);
				break;
			case "AI (Normal)":
				p2 = new AI(2, false);
				break;
			case "AI (Hard)":
				p2 = new AI(2, true);
				break;
		}
		
		Main.play(p1, p2);
	}
}
