package com.mygdx.demon_peak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class GameOverScreen implements Screen {
    Preferences prefs = Gdx.app.getPreferences("game preferences");
    float x = 125;
    float y = 125;
    final Demon game;
    OrthographicCamera camera;
    Texture background = new Texture("gameover.png");
    List<Score> scoreCopia;
    List<Score> scoreOrdenado = new ArrayList<>();
    Score scoreGuard;
    int aborrar;
    int i;

    public GameOverScreen(final Demon gam) {
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f, 0, 0, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.font.getData().setScale(2);
        game.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        game.batch.begin();
        game.batch.draw(background, 0, 0, 800, 480);
        /*game.font.draw(game.batch, "Game Over! ", 300, 300);
        game.font.draw(game.batch, "Final Score: " + game.lastScore, 300, 180);
        game.font.draw(game.batch, "Top Score: " + game.topScore, 300, 120);*/

        game.font.draw(game.batch, "SCOREBOARD", x + 300, y + 210);

        scoreCopia = Demon.scoreList;
        for (int j = 0; j < Demon.scoreList.size(); j++) {
            if (!scoreCopia.isEmpty()) scoreGuard = scoreCopia.get(0);
            else break;
            for (int i = 1; i < scoreCopia.size(); i++) {
                if (scoreGuard.puntuacion <= scoreCopia.get(i).puntuacion) {
                    scoreGuard = scoreCopia.get(i);
                    aborrar = i;
                }
            }
            scoreOrdenado.add(scoreGuard);
            scoreCopia.remove(scoreGuard);
        }

        for (Score score : scoreOrdenado) {
            if (i<4){
            game.font.draw(game.batch, "" + score.puntuacion, x + 400, y + 150 - i * 40);
            i++;}
        }
        for (int i = 0; i < scoreOrdenado.size() && i < 4; i++) {
            game.font.draw(game.batch, "" + scoreOrdenado.get(i).puntuacion, x + 400, y + 150 - i * 40);
        }
        game.batch.end();

        if (Gdx.input.justTouched()) {
            game.setScreen(new MainMenuScreen(game));
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