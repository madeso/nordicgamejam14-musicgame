package com.madeso.me.musicgame;

import java.util.Random;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class Bank implements Disposable {
	float posrot = 0.0f;
	float speed = 1.0f;
	float size = 2.0f;
	Vector3 pos = new Vector3(0,0,0);
	ModelInstance instance;
	static Random random = new Random();
	float spin = 0;
	
	int level = 0;
	LooperList levels;
	float pausetimer;
	float lifetimer = 0;
	public Bank(Model m, LooperList levels) {
		instance = new ModelInstance(m);
		posrot = random.nextFloat();
		randomize();
		this.levels = levels;
		this.pausetimer = Constants.STARTTIME + random.nextFloat() * Constants.STARTRANGE;
	}

	private void playCurrentLevel() {
		Looper looper = this.levels.get(this.level);
		looper.play();
		looper.setVolume(1.0f);
	}
	
	void randomize() {
		speed = random.nextFloat() * Constants.BANKSPEED -Constants.BANKSPEED/2;
	}
	
	boolean update(float dt, Vector3 playerpos) {
		boolean touched = false;
		
		if( pausetimer > 0 ) {
			spin += dt * Constants.SPINTIMES;
		}
		else {
			spin += dt * Constants.SPINTIMES_ACTIVE;
		}
		while(spin > 1) spin -= 1;
		
		posrot += dt * speed;
		while(posrot > 1) posrot -= 1;
		while(posrot < 0) posrot += 1;
		double angle = posrot * Math.PI * 2;
		pos.x = (float) Math.cos(angle) * Constants.WORLDWIDTH * Constants.BANKSCALE;
		pos.y = (float) Math.sin(angle) * Constants.WORLDHEIGHT * Constants.BANKSCALE;
		instance.transform.setToRotation(0, 0, 1, (float) (spin * 360));
		instance.transform.setTranslation(pos);
		if( pausetimer < 0.0f ) {
			lifetimer += dt;
			if( lifetimer > Constants.BANKLIFE) {
				randomize();
				if( level < levels.count()-1 ) ++level;
				playCurrentLevel();
				lifetimer -= Constants.BANKLIFE;
			}
				Vector3 diff = new Vector3(pos);
				if( diff.sub(playerpos).len2() < size*size ) {
					randomize();
					touched = true;
					// game.onhit(this.index);
					levels.get(level).setVolume(0);
					if( level > 0 ) --level;
					pausetimer = Constants.PAUSETIMER;
				}
		}
		else {
			pausetimer -= dt;
			if( pausetimer < 0.0f ) {
				playCurrentLevel();
			}
		}
		
		return touched;
	}

	public void render(ModelBatch modelBatch, Environment environment) {
		modelBatch.render(instance, environment);
	}

	@Override
	public void dispose() {
		levels.dispose();
	}

}
