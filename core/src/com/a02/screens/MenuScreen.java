package com.a02.screens;

import com.a02.entity.UIButton;
import com.a02.game.MainGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuScreen implements Screen {

    final MainGame game;
    private UIButton round1Button;
    private UIButton round2Button;

    private OrthographicCamera camera;
    private static Logger logger = Logger.getLogger(MenuScreen.class.getName());

    public MenuScreen(MainGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 320, 180);

        round1Button = new UIButton(120, 110, 90, 27, "Boton1.png", -1);
        round2Button = new UIButton(120, 50, 90, 27, "Boton2.png", -1);

        Logger.getLogger("").setLevel(Level.INFO);
        Logger.getLogger("").getHandlers()[0].setLevel(Level.INFO);
        logger.info("Inicio del MenuScreen");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.entityBatch.setProjectionMatrix(camera.combined);

        game.entityBatch.begin();
        game.entityBatch.draw(new Texture(Gdx.files.internal("wallpaperTest.png")),0,0);
        game.entityBatch.draw(new Texture(Gdx.files.internal("Boton1.png")),120,110);
        game.entityBatch.draw(new Texture(Gdx.files.internal("Boton2.png")),120,50);
        game.entityBatch.end();

        if (round1Button.isJustClicked()) {
            game.setScreen(new GameScreen(game, 1));
        }
        else if (round2Button.isJustClicked()) {
            game.setScreen(new GameScreen(game, 2));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
