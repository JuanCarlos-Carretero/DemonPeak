package com.mygdx.demon_peak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainMenuScreen implements Screen {
    final Demon game;
    static Random random = new Random();
    OrthographicCamera camera;
    Texture backgroundImage;
    Animaciones press_start;
    Demon_Menu dragonMenu1;
    static Music musica_menu;

    public MainMenuScreen(final Demon gam) {
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        press_start = new Animaciones(8f, true, "menu/pressenter.png", "menu/pressenter1.png", "menu/pressenter2.png", "menu/pressenter3.png");
        backgroundImage = new Texture(Gdx.files.internal("background.png"));

        dragonMenu1 = new Demon_Menu();

        musica_menu = Gdx.audio.newMusic(Gdx.files.internal("Sound/yellowcard.wav"));
        musica_menu.play();

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();


        Temporizador.tiempomenu += 1;

        game.batch.draw(backgroundImage, 0, 0);
        dragonMenu1.render(game.batch);
        dragonMenu1.update();
        game.batch.draw(press_start.getFrame(Temporizador.tiempomenu), 300, -70, 400, 500);

        game.batch.end();

        if (Gdx.input.justTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
