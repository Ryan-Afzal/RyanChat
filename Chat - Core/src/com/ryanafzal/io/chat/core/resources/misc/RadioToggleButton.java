package com.ryanafzal.io.chat.core.resources.misc;

import javafx.scene.control.ToggleButton;

public class RadioToggleButton extends ToggleButton {
	
	public RadioToggleButton() {
		super();
	}
	
	public RadioToggleButton(String name) {
		super(name);
	}

	@Override
	public void fire() {
		if (getToggleGroup() == null || !isSelected()) {
			super.fire();
		}
	}

}
