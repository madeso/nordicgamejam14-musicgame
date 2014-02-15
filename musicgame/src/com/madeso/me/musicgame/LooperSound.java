package com.madeso.me.musicgame;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;

public class LooperSound implements Looper, OnCompletionListener {
	static Random random = new Random();
	ArrayList<Music> sound = new ArrayList<Music>();
	int count = 0;
	int currentlyplaying = -1;
	
	public LooperSound add(String path) {
		Music m = Gdx.audio.newMusic(Gdx.files.internal(path));
		sound.add(m);
		m.setOnCompletionListener(this);
		m.setLooping(false);
		++count;
		return this;
	}
	
	@Override
	public void play() {
		if( count > 1) {
			int index = -1;
			do {
				index = random.nextInt(count);
			} while(index == currentlyplaying);
			currentlyplaying = index;
			Music m = sound.get(index);
			m.play();
		}
	}
	
	@Override
	public void stop() {
		for(Music sound:this.sound) {
			sound.stop();
		}
		currentlyplaying = -1;
	}
	
	@Override
	public void setVolume(float f) {
		for(Music sound:this.sound) {
			sound.setVolume(f);
		}
	}
	
	@Override
	public void dispose() {
		for(Music sound:this.sound) {
			sound.dispose();
		}
	}

	@Override
	public void onCompletion(Music music) {
		play();
	}
}
