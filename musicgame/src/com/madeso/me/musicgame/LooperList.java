package com.madeso.me.musicgame;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Disposable;

public class LooperList implements Disposable {
	ArrayList<Looper> loops = new ArrayList<Looper>();

	public LooperList add(Looper l) {
		loops.add(l);
		return this;
	}

	public Looper get(int level) {
		return loops.get(level);
	}

	@Override
	public void dispose() {
		for(Looper l : loops) {
			l.dispose();
		}
	}

	public int count() {
		// TODO Auto-generated method stub
		return loops.size();
	}
}
