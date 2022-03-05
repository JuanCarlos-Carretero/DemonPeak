package com.mygdx.demon_peak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class Demon_Menu {
    Animaciones animacion;
    float x, y, w, h, v;

    Demon_Menu() {
        animacion = new Animaciones(8f, true, "player/player1.png", "player/player2.png", "player/player3.png", "player/player4.png", "player/player5.png", "player/player6.png");
        x = MainMenuScreen.random.nextInt(800);
        y = -50;
        w = 50;
        h = 50;
        v = 3;
    }

    void render(SpriteBatch batch) {
        batch.draw(animacion.getFrame(Temporizador.tiempomenu), x, y, w, h);
    }

    void update() {
        y += v;
        if (y > 700) {
            y = -50;
            x = MainMenuScreen.random.nextInt(800);
/*1580*/
        }
    }
}
