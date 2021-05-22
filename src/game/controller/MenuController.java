package game.controller;

import game.Main;
import game.model.RuleChecker;
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
	public CheckBox r1;
	public CheckBox r2;
	public CheckBox r3;
	public CheckBox r4;
	public CheckBox r5;
	public CheckBox r6;
	public CheckBox r7;
	public CheckBox r8;
	public CheckBox r9;
	public Button play;
	
	public void initialize() {
		pane.setBackground(new Background(new BackgroundImage(new Image(Main.getRes("bg_blur.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, null)));
		
		p1option.getItems().add("Human");
		p1option.getItems().add("AI (Normal)");
		p1option.getItems().add("AI (Hard)");
		
		p2option.getItems().add("Human");
		p2option.getItems().add("AI (Normal)");
		p2option.getItems().add("AI (Hard)");
		
		RuleChecker.getInstance().submit(r1, "size-omok");
		RuleChecker.getInstance().submit(r2, "size");
		RuleChecker.getInstance().submit(r4, "advantage");
		RuleChecker.getInstance().submit(r5, "advantage");
		RuleChecker.getInstance().submit(r6, "style");
		RuleChecker.getInstance().submit(r7, "style");
		RuleChecker.getInstance().submit(r8, "style-omok");
		RuleChecker.getInstance().submit(r9, "style");
		
		RuleChecker.getInstance().registerIncompatibility("size", r1, r2);
		RuleChecker.getInstance().registerIncompatibility("omok", r1, r8);
		RuleChecker.getInstance().registerIncompatibility("advantage", r4, r5);
		RuleChecker.getInstance().registerIncompatibility("style", r6, r7, r8, r9);
	}
	
	public void ruleChange(ActionEvent actionEvent) {
		CheckBox rule = (CheckBox) actionEvent.getSource();
		RuleChecker.getInstance().update(rule);
	}
	
	public void play(ActionEvent actionEvent) {
		//preparations for Stage change code
		Main.play();
	}
}
