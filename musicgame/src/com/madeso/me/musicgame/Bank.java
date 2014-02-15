package com.madeso.me.musicgame;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class Bank {
	float position;
	ModelInstance instance;
	
	public Bank(Model m) {
		instance = new ModelInstance(m);
	}
	
	void update(float dt) {
		
	}

}
