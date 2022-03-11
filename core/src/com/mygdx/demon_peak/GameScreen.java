package com.mygdx.demon_peak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameScreen implements Screen {
    final Demon game;

    Texture backgroundImage;
    Texture pause;
    Rectangle pauseRectangle;
    OrthographicCamera camera;
    Animaciones playerAnim;
    Rectangle player;
    Texture pipeUpImage;
    Texture pipeDownImage;
    Array<Rectangle> obstacles;

    int lastObstacleTime;
    int tiempoAparicion;
    int spawn;

    float speedy;
    float gravity;

    float score;
    boolean dead;

    Sound flapSound;
    Sound failSound;

    boolean musicaParada = false;
    boolean juegoPausado = false;

    public GameScreen(final Demon gam) {
        this.game = gam;
        // load the images
        backgroundImage = new Texture(Gdx.files.internal("background.jpg"));
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        playerAnim = new Animaciones(8f, true, "player/player6.png", "player/player5.png", "player/player4.png", "player/player3.png", "player/player2.png", "player/player1.png");

        // create a Rectangle to logically represent the player
        player = new Rectangle();
        player.x = 200;
        player.y = 480 / 2 - 64 / 2;
        player.width = 35;
        player.height = 30;

        speedy = 0;
        gravity = 850f;

        pause = new Texture(Gdx.files.internal("pause.png"));
        pauseRectangle = new Rectangle();
        pauseRectangle.x = 725;
        pauseRectangle.y = 430;
        pauseRectangle.width = 35;
        pauseRectangle.height = 35;

        pipeUpImage = new Texture(Gdx.files.internal("pipe_up.png"));
        pipeDownImage = new Texture(Gdx.files.internal("pipe_down.png"));

        tiempoAparicion = 120;

        // create the obstacles array and spawn the first obstacle
        obstacles = new Array<Rectangle>();
        spawnObstacle();
        score = 0;

        // load the sound effects
        flapSound = Gdx.audio.newSound(Gdx.files.internal("flap.wav"));
        failSound = Gdx.audio.newSound(Gdx.files.internal("gameover.wav"));
    }

    @Override
    public void render(float delta) {
        //Render
        // clear the screen with a color
        ScreenUtils.clear(0.3f, 0.8f, 0.8f, 1);
        // tell the camera to update its matrices.
        camera.update();
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch
        game.batch.begin();

        game.batch.draw(backgroundImage, 0, 0);
        game.batch.draw(playerAnim.getFrame(Temporizador.tiempoJuego), player.x, player.y);

        // Dibuixa els obstacles: Els parells son tuberia inferior,
        //els imparells tuberia superior
        for (int i = 0; i < obstacles.size; i++) {
            game.batch.draw(
                    i % 2 == 0 ? pipeUpImage : pipeDownImage,
                    obstacles.get(i).x, obstacles.get(i).y);
        }
        game.batch.draw(pause, 725, 430);

        game.font.getData().setScale(1);
        game.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        game.font.draw(game.batch, "Score: " + (int) score, 10, 470);
        game.batch.end();

        if (!juegoPausado) {
            spawn++;
            // Comprova que el jugador no es surt de la pantalla.
            // Si surt per la part inferior, game over
            if (player.y > 480 - 45) {
                player.y = 480 - 45;
            }
            if (player.y < 0 - 45) {
                dead = true;
            }
            Temporizador.tiempoJuego += 1;
            //Actualitza la posició del jugador amb la velocitat vertical
            player.y += speedy * Gdx.graphics.getDeltaTime();
            //Actualitza la velocitat vertical amb la gravetat
            speedy -= gravity * Gdx.graphics.getDeltaTime();
            //La puntuació augmenta amb el temps de joc
            score += Gdx.graphics.getDeltaTime();

            // Comprova que el jugador no es surt de la pantalla.
            // Si surt per la part inferior, game over
            if (player.y > 480 - 45) {
                player.y = 480 - 45;
            } else if (player.y < 0 - 45) {
                dead = true;
            }

            // Comprova si cal generar un obstacle nou y genera dificultat cada 10 oleadas
            if (spawn - lastObstacleTime > tiempoAparicion) {
                if ((int) score % 10 == 0) {
                    if (tiempoAparicion > 60) {
                        tiempoAparicion -= 20;
                    }
                }
                spawnObstacle();
            }
            // Mou els obstacles. Elimina els que estan fora de la pantalla
            // Comprova si el jugador colisiona amb un obstacle,
            // llavors game over
            Iterator<Rectangle> iter = obstacles.iterator();
            while (iter.hasNext()) {
                Rectangle tuberia = iter.next();
                tuberia.x -= 200 * Gdx.graphics.getDeltaTime();
                if (tuberia.x < -64)
                    iter.remove();
                if (tuberia.overlaps(player)) {
                    dead = true;
                }
            }

            if (dead) {
                Demon.scoreList.add(new Score((int)score));

                game.lastScore = (int) score;
                MainMenuScreen.musica_menu.stop();
                musicaParada = true;
                failSound.play();
                if (game.lastScore > game.topScore)
                    game.topScore = game.lastScore;
                game.setScreen(new GameOverScreen(game));
                dispose();
            }
            if (!dead && musicaParada) {
                MainMenuScreen.musica_menu.play();
                musicaParada = false;
            }
        }
        //Logica
        // process user input
        if (Gdx.input.justTouched()) {
            speedy = 400f;
            flapSound.play();

            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            // Boton pausa
            if (pauseRectangle.contains(touchPos.x, touchPos.y) && !juegoPausado) {
                juegoPausado = true;
                MainMenuScreen.musica_menu.pause();
            } else if(pauseRectangle.contains(touchPos.x, touchPos.y) && juegoPausado){
                juegoPausado = false;
                MainMenuScreen.musica_menu.play();
            }
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
        pipeUpImage.dispose();
        pipeDownImage.dispose();

        failSound.dispose();
        flapSound.dispose();
    }

    private void spawnObstacle() {
        // Calcula la alçada de l'obstacle aleatòriament
        float holey = MathUtils.random(50, 230);
        // Crea dos obstacles: Una tubería superior i una inferior
        Rectangle pipe1 = new Rectangle();
        pipe1.x = 800;
        pipe1.y = holey - 230;
        pipe1.width = 64;
        pipe1.height = 230;
        obstacles.add(pipe1);
        Rectangle pipe2 = new Rectangle();
        pipe2.x = 800;
        pipe2.y = holey + 200;
        pipe2.width = 64;
        pipe2.height = 230;
        obstacles.add(pipe2);
        lastObstacleTime = spawn;
    }
}