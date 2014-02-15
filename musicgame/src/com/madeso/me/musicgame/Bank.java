package com.madeso.me.musicgame;

import java.util.Random;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class Bank {
	float posrot = 0.0f;
	float speed = 1.0f;
	float size = 2.0f;
	Vector3 pos = new Vector3(0,0,0);
	ModelInstance instance;
	static Random random = new Random();
	
	public Bank(Model m) {
		instance = new ModelInstance(m);
		posrot = random.nextFloat();
		randomize();
	}
	
	void randomize() {
		speed = random.nextFloat() * 0.5f - 0.25f;
	}
	
	void update(float dt, Vector3 playerpos) {
		posrot += dt * speed;
		while(posrot > 1) posrot -= 1;
		while(posrot < 0) posrot += 1;
		double angle = posrot * Math.PI * 2;
		pos.x = (float) Math.cos(angle) * Constants.WORLDWIDTH;
		pos.y = (float) Math.sin(angle) * Constants.WORLDHEIGHT;
		instance.transform.setTranslation(pos);
		
		Vector3 diff = new Vector3(pos);
		if( diff.sub(playerpos).len2() < size*size ) {
			randomize();
		}
	}

	public void render(ModelBatch modelBatch, Environment environment) {
		modelBatch.render(instance, environment);
	}

}
