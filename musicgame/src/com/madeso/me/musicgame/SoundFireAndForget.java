package com.madeso.me.musicgame;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

public class SoundFireAndForget implements Disposable {
	Random random = new Random();
	ArrayList<Sound> sound = new ArrayList<Sound>();
	int count = 0;
	
public void add(String path) {
	sound.add(Gdx.audio.newSound(Gdx.files.internal(path)));
	++count;
}

void play() {
	if( count > 0) {
		int index = random.nextInt(count);
		sound.get(index).play();
	}
}

void stopall() {
	for(Sound sound:this.sound) {
		sound.stop();
	}
}

@Override
public void dispose() {
	for(Sound sound:this.sound) {
		sound.dispose();
	}
}

}
