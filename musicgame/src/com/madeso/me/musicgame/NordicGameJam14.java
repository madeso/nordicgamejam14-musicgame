package com.madeso.me.musicgame;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class NordicGameJam14 implements ApplicationListener {
	public PerspectiveCamera cam;
	public CameraInputController inputController;
	public ModelBatch modelBatch;
	public Model model;
	public Model bankmodel;
	public ModelInstance instance;
	public Environment environment;
	
	ArrayList<Bank> banks = new ArrayList<Bank>();
	
	Looper background;
	
	@Override
	public void create() {		
		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0, 0, 10f);
		cam.lookAt(0,0,0);
		cam.near = 0.1f;
		cam.far = 300f;
		cam.update();

		// ModelBuilder modelBuilder = new ModelBuilder();
		// model = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position | Usage.Normal);
		
		ObjLoader loader = new ObjLoader();
        model = loader.loadModel(Gdx.files.internal("player/ship.obj"));
        bankmodel = loader.loadModel(Gdx.files.internal("player/ship.obj"));
        
        banks.add(new Bank(bankmodel, new LooperList().add(new LooperMusic("Bank1/birds_twitter.mp3"))));
        banks.add(new Bank(bankmodel, new LooperList().add(new LooperMusic("Bank2/walla.mp3"))));
        banks.add(new Bank(bankmodel, new LooperList().add(new LooperMusic("Bank3/highway_sound.mp3"))));
        // banks.add(new Bank(bankmodel));
        background = new LooperMusic("Bank0/pad_drone.mp3");
        background.play();
		
		instance = new ModelInstance(model);
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		model.dispose();
		bankmodel.dispose();
		background.dispose();
		
		for(Bank b: banks) {
			b.dispose();
		}
	}
	
	private static float KeepWithin(float mi, float va, float ma) {
		if( va > ma ) return ma;
		if( va < mi ) return mi;
		return va;
	}
	
	private Vector3 GetTouchPos() {
		Vector3 touchPos = new Vector3();
		touchPos.set(Gdx.input.getX(0), Gdx.input.getY(0), 0);
		//cam.unproject(touchPos);
		touchPos.x = KeepWithin(-1, 2*(touchPos.x / Gdx.graphics.getWidth()) - 1, 1);
		touchPos.y = KeepWithin(-1, -2*(touchPos.y / Gdx.graphics.getHeight()) +1, 1);
		
		// System.out.println("x " + Float.toString( touchPos.x) + " z " + Float.toString(touchPos.y));
		touchPos.z = 0;
		return touchPos;
	}
	
	
	
	float playerrot = 0.0f;
	float playersize = 1.0f;
	float bouncetimer = 0;
	Vector3 playerpos = new Vector3(0,0,0);
	Vector3 lastseenplayer = new Vector3(0,0,0);
	boolean hasplayer = false;
	boolean locktouch = false;
	
	@Override
	public void render() {		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		float dt = Gdx.graphics.getDeltaTime();
		
		for(Bank bank : banks) {
			if( bank.update(dt, playerpos) ) {
				hasplayer = false;
				locktouch = true;
				// System.out.println("Loosing player");
			}
		}
		
		playerrot += dt * Constants.PLAYERROTSPEED;
		while(playerrot > 360) playerrot -= 360;
		playersize += dt * Constants.PLAYERSIZESPEED;
		while(playersize > 1) playersize -= 1;

		modelBatch.begin(cam);
		modelBatch.render(instance, environment);
		for(Bank bank : banks) {
			bank.render(modelBatch, environment);
		}
		modelBatch.end();
		
		instance.transform.setToRotation(0, 0, 1, playerrot);
		float scale = 1.5f + (float) Math.sin(playersize * Math.PI * 2);
		instance.transform.scale(scale, scale, scale);
		
		Vector3 target = GetTouchPos();
		target.x *= Constants.WORLDWIDTH;
		target.y *= Constants.WORLDHEIGHT;
		
		if( locktouch == false ) {
			if( Gdx.input.isTouched(0) && hasplayer==false ) {
				float size = Constants.PLAYERSIZE;
				if( new Vector3(target).sub(playerpos).len2() < size ) {
					hasplayer = true;
				}
			}
		}
		
		float bgvol = 1.0f;
		for(Bank b:banks) {
			if( b.level == 0 && b.pausetimer > 0 ) {
				
			}
			else {
				bgvol = 0.0f;
			}
		}
		background.setVolume(bgvol);
		
		if( Gdx.input.isTouched(0) && hasplayer) {
			playerpos = target;
			bouncetimer = 0.0f;
			lastseenplayer = new Vector3(playerpos);
		}
		else {
			bouncetimer += dt * Constants.BOUNCESPEED;
			if(bouncetimer > 1) bouncetimer = 1;
			hasplayer = false;
			if( Gdx.input.isTouched(0) == false ) {
				locktouch = false;
			}
			playerpos = new Vector3(lastseenplayer);
			playerpos.scl( 1-Elastic.easeOut(bouncetimer, 0, 1, 1) );
 		}
		
		instance.transform.setTranslation(playerpos);
	}

	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = width;
		cam.viewportHeight = height;
		cam.update();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	public boolean needsGL20 () {
		return true;
	}
}
