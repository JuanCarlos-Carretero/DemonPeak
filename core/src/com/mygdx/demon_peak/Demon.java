package com.mygdx.demon_peak;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Demon extends Game {
	SpriteBatch batch;
	BitmapFont font;
	int topScore;
	int lastScore;


	public void create() {
		batch = new SpriteBatch();
		// Use LibGDX's default Arial font.
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
		topScore = 0;
		lastScore = 0;
	}
	public void render() {
		super.render(); // important!
	}
	public void dispose() {
	}
}

