package game.model;

import javafx.scene.control.CheckBox;

import java.util.HashMap;

public final class RuleChecker {
	private final HashMap<CheckBox, String> rules;
	private final HashMap<String, CheckBox[]> incompatibilities;
	private static RuleChecker instance = null;
	
	private RuleChecker() {
		rules = new HashMap<>();
		incompatibilities = new HashMap<>();
	}
	
	public static RuleChecker getInstance() {
		if ( instance == null ) {
			instance = new RuleChecker();
		}
		return instance;
	}
	
	public void submit(CheckBox k, String v) {
		rules.put(k, v);
	}
	
	public void registerIncompatibility(String rule, CheckBox... list) {
		incompatibilities.put(rule, list);
	}
	
	public void update(CheckBox k) {
		if ( rules.get(k) == null ) {
			return;
		}
		
		String[] rule_set = rules.get(k).split("-");
		for (String s: rule_set) {
			for (CheckBox r: incompatibilities.get(s)) {
				if ( !r.equals(k) && k.isSelected() ) {
					r.setSelected(false);
				}
			}
		}
		
	}
}