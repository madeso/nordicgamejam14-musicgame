package com.madeso.me.musicgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class LooperMusic implements Looper {
	Music music;
	
	public LooperMusic(String path) {
		music = Gdx.audio.newMusic(Gdx.files.internal(path));
		music.setLooping(true);
	}

	@Override
	public void dispose() {
		music.dispose();
	}

	@Override
	public void play() {
		music.play();
	}

	@Override
	public void setVolume(float v) {
		music.setVolume(v);
	}

	@Override
	public void stop() {
		music.stop();
	}

}
