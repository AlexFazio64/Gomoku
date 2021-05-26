package game.controller;

import game.Main;
import game.model.AI;
import game.model.Human;
import game.model.Player;
import game.model.RuleChecker;
import game.settings.GS;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BorderPane;

public class MenuController {
	public ChoiceBox<String> p1option;
	public ChoiceBox<String> p2option;
	public BorderPane pane;
	public CheckBox three_and_three;
	public CheckBox four_and_four;
	public CheckBox handicap;
	public CheckBox swap;
	public CheckBox g_pro;
	public CheckBox freestyle;
	public CheckBox renju;
	public CheckBox omok;
	public CheckBox g_plus;
	public Button play;
	
	public void initialize() {
		pane.setBackground(new Background(new BackgroundImage(new Image(Main.getRes("bg_blur.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, null)));
		
		p1option.getItems().add("Human");
		p1option.getItems().add("AI (Normal)");
		p1option.getItems().add("AI (Hard)");
		
		p2option.getItems().add("Human");
		p2option.getItems().add("AI (Normal)");
		p2option.getItems().add("AI (Hard)");
		
		RuleChecker.getInstance().submit(three_and_three, "size-omok");
		RuleChecker.getInstance().submit(four_and_four, "size");
		RuleChecker.getInstance().submit(swap, "advantage");
		RuleChecker.getInstance().submit(g_pro, "advantage");
		RuleChecker.getInstance().submit(freestyle, "style");
		RuleChecker.getInstance().submit(renju, "style");
		RuleChecker.getInstance().submit(omok, "style-omok");
		RuleChecker.getInstance().submit(g_plus, "style");
		
		RuleChecker.getInstance().registerIncompatibility("size", three_and_three, four_and_four);
		RuleChecker.getInstance().registerIncompatibility("omok", three_and_three, omok);
		RuleChecker.getInstance().registerIncompatibility("advantage", swap, g_pro);
		RuleChecker.getInstance().registerIncompatibility("style", freestyle, renju, omok, g_plus);
	}
	
	public void ruleChange(ActionEvent actionEvent) {
		CheckBox rule = (CheckBox) actionEvent.getSource();
		RuleChecker.getInstance().update(rule);
	}
	
	public void play(ActionEvent actionEvent) {
		//Game customization
		if ( p1option.getValue() == null || p2option.getValue() == null ) {
			return;
		}
		
		Player p1, p2;
		p1 = p2 = null;
		
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
		GS.RULES.THREE = three_and_three.isSelected();
		GS.RULES.FOUR = four_and_four.isSelected();
		GS.RULES.HANDICAP = handicap.isSelected();
		GS.RULES.SWAP = swap.isSelected();
		GS.RULES.PRO = g_pro.isSelected();
		GS.RULES.FREESTYLE = freestyle.isSelected();
		GS.RULES.RENJU = renju.isSelected();
		GS.RULES.OMOK = omok.isSelected();
		GS.RULES.PLUS = g_plus.isSelected();
		
		if ( GS.RULES.FREESTYLE ) {
			GS.GRIDSIZE = 20;
		}
		
		Main.play(p1, p2);
	}
}
