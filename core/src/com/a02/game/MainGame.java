/*
 * Esta clase es llamada desde el método main. Gestiona la creación de
 * distintas Screens, y posee el SpriteBatch que se encarga del dibujado.
 */

package com.a02.game;

import com.a02.screens.GameScreen;
import com.a02.screens.MenuScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;

public class MainGame extends Game {
	public SpriteBatch entityBatch;
	public static GameScreen mainGameScreen;

	public MainGame(String gamemode, double diff, boolean musicCheck, boolean soundCheck, boolean tutorialCheck) {
		Settings.s.setGamemode(gamemode);
		Settings.s.setDiff(diff);
		Settings.s.setMusicCheck(musicCheck);
		Settings.s.setSoundCheck(soundCheck);
		Settings.s.setTutorialCheck(tutorialCheck);
	}

	@Override
	public void create () {
		entityBatch = new SpriteBatch();
		mainGameScreen = new GameScreen(this, 0);
		this.setScreen(new MenuScreen(this));
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		entityBatch.dispose();
	}
}
