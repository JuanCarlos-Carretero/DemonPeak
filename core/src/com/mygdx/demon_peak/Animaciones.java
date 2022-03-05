package com.mygdx.demon_peak;
import com.badlogic.gdx.graphics.Texture;

public class Animaciones {
    float duracionFrame;
    boolean repetir;
    Texture[] textures;

    Animaciones(float duracionFrame, boolean repetir, String... ficherosTexturas) {
        this.duracionFrame = duracionFrame;
        this.repetir = repetir;
        this.textures = new Texture[ficherosTexturas.length];
        for (int i = 0; i < ficherosTexturas.length; i++) {
            textures[i] = new Texture(ficherosTexturas[i]);
        }
    }

    Texture getFrame(float tiempo) {
        int frameNum = (int) (tiempo / duracionFrame);

        if (repetir) frameNum %= textures.length;
        else if (frameNum >= textures.length) frameNum = textures.length - 1;

        return textures[frameNum];
    }
}