package com.madeso.me.musicgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class NordicGameJam14 implements ApplicationListener {
	public PerspectiveCamera cam;
	public CameraInputController inputController;
	public ModelBatch modelBatch;
	public Model model;
	public ModelInstance instance;
	public Environment environment;
	
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
		
		instance = new ModelInstance(model);
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		model.dispose();
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
	
	static final float WORLDHEIGHT = 6.0f;
	static final float WORLDWIDTH = 10.0f;
	static final float PLAYERROTSPEED = 2*360.0f;
	static final float PLAYERSIZESPEED = 2.0f;
	
	static final float PLAYERFORCELIM = 1.0f;
	static final float FORCERECHARGE = 2.0f;
	
	float playerrot = 0.0f;
	float playersize = 1.0f;
	Vector3 playerpos = new Vector3(0,0,0);
	float playerforce = 0;

	static float ForceScale(float f) {
		return f * f * f;
	}
	
	@Override
	public void render() {		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		float dt = Gdx.graphics.getDeltaTime();
		
		playerrot += dt * PLAYERROTSPEED;
		while(playerrot > 360) playerrot -= 360;
		playersize += dt * PLAYERSIZESPEED;
		while(playersize > 1) playersize -= 1;

		modelBatch.begin(cam);
		modelBatch.render(instance, environment);
		modelBatch.end();
		
		instance.transform.setToRotation(0, 0, 1, playerrot);
		float scale = 1.5f + (float) Math.sin(playersize * Math.PI * 2);
		instance.transform.scale(scale, scale, scale);
		
		if( Gdx.input.isTouched(0) ) {
			Vector3 target = GetTouchPos();
			target.x *= WORLDWIDTH;
			target.y *= WORLDHEIGHT;
			
			playerforce += dt;
			
			if( playerforce < PLAYERFORCELIM ) {
				
			}
			else {
				playerforce = PLAYERFORCELIM;
			}
			
			float fs = ForceScale(playerforce / PLAYERFORCELIM);
			target.scl(1-fs);
			playerpos = target;
		}
		else {
			playerforce -= dt * FORCERECHARGE;
			if( playerforce < 0 ) playerforce = 0;
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