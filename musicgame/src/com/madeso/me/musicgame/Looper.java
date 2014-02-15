package com.madeso.me.musicgame;

import com.badlogic.gdx.utils.Disposable;

public interface Looper extends Disposable {
void play();
void setVolume(float v);
void stop();
}
