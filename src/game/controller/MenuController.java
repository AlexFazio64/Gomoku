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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
	private CheckBox g_pro;
	@FXML
	private CheckBox freestyle;
	@FXML
	private CheckBox renju;
	@FXML
	private CheckBox omok;

	@FXML
	private void initialize() {
		pane.setBackground(new Background(new BackgroundImage(GS.getMenuBG(), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, null, null)));

		p1option.getItems().add(GS.HUMAN);
		p1option.getItems().add(GS.AI);
		p1option.setValue(GS.HUMAN);

		p2option.getItems().add(GS.HUMAN);
		p2option.getItems().add(GS.AI);
		p2option.setValue(GS.AI);

		RuleChecker checker = RuleChecker.getInstance();
		checker.submit(three_and_three, "size-omok");
		checker.submit(four_and_four, "size");
		checker.submit(handicap, "size");
		checker.submit(g_pro, "opening");
		checker.submit(freestyle, "style-omok");
		checker.submit(renju, "style");
		checker.submit(omok, "style-omok");

		checker.registerIncompatibility("size", three_and_three, four_and_four, handicap);
		checker.registerIncompatibility("omok", three_and_three, omok, freestyle);
		checker.registerIncompatibility("style", freestyle, renju, omok);
		checker.registerIncompatibility("opening", g_pro);
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

		// Game customization
		GS.RULES.THREE = three_and_three.isSelected() || handicap.isSelected() || omok.isSelected();
		GS.RULES.FOUR = four_and_four.isSelected() && !GS.RULES.THREE;
		GS.RULES.HANDICAP = handicap.isSelected() && !omok.isSelected();
		GS.RULES.PRO = g_pro.isSelected();
		GS.RULES.FREESTYLE = freestyle.isSelected() || omok.isSelected();
		GS.RULES.RENJU = renju.isSelected();
		GS.RULES.OMOK = omok.isSelected();

		if (GS.RULES.RENJU) {
			GS.GRIDSIZE = 16;
		} else {
			GS.GRIDSIZE = 20;
		}

		GS.DIM = GS.GRIDSIZE * GS.CELLSIZE;

		switch (p1option.getValue()) {
			case GS.HUMAN:
				p1 = new Human(1);
				break;
			case GS.AI:
				p1 = new AI(1);
				break;
		}

		switch (p2option.getValue()) {
			case GS.HUMAN:
				p2 = new Human(2);
				break;
			case GS.AI:
				p2 = new AI(2);
				break;
		}

		Main.play(p1, p2);
	}

	@FXML
	public void exit(KeyEvent keyEvent) {
		if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
			Main.close();
		}
	}
}
