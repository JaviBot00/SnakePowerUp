package com.politecnicomalaga.snakepowerup;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.politecnicomalaga.snakepowerup.manager.AssetsManager;
import com.politecnicomalaga.snakepowerup.manager.SettingsManager;
import com.politecnicomalaga.snakepowerup.model.PowerUp;
import com.politecnicomalaga.snakepowerup.model.Snake;
import com.politecnicomalaga.snakepowerup.model.Square;
import com.politecnicomalaga.snakepowerup.view.PanelNumeros;

public class GdxSnakePowerUp extends ApplicationAdapter {
    private SpriteBatch myBatch;
    private Texture imgStart, imgGame, imgDead;

    private enum Screens {START, GAME, DEAD}

    private Screens myScreens;

    float mouseX, mouseY; // Posicion del Raton al Clikar
    float headX, headY; // Posicion de la Cabeza de la Snake
    float difX, difY; // Diferencia entre ambas Posiciones

    private Snake myBody;
    private PowerUp myPowerUp;
    private int iCounter, pCounter;
    private boolean bPowerUp;

    private Music mMusic, mDead;
    private boolean bMusicOnOFF;
    private OrthographicCamera camera;
    private PanelNumeros scorePanel;


    @Override
    public void create() {
        myBatch = new SpriteBatch();
        imgStart = new Texture(AssetsManager.IMG_START);
        imgGame = new Texture(AssetsManager.IMG_GAME);
        imgDead = new Texture(AssetsManager.IMG_DEAD);
        myScreens = Screens.START;

        myBody = new Snake();
        myPowerUp = new PowerUp();
        iCounter = 0;
        pCounter = 0;

        mMusic = Gdx.audio.newMusic(Gdx.files.internal(AssetsManager.MSC_GAME));
        mMusic.setLooping(true);
        mDead = Gdx.audio.newMusic(Gdx.files.internal(AssetsManager.MSC_DEAD));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SettingsManager.SCREEN_WIDTH, SettingsManager.SCREEN_HEIGHT);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        myBatch.setProjectionMatrix(camera.combined);
        myBatch.begin();
        myBatch.end();
        switch (myScreens) {
            case START:
                startScreen();
                break;
            case GAME:
                gameScreen();
                break;
            case DEAD:
                deadScreen();
                break;
        }
    }

    //Métodos de trabajo de cada una de las pantallas
    private void startScreen() {
        // Pintar Pantalla de Inicio
        myBatch.begin();
        myBatch.draw(imgStart, 0, 0, SettingsManager.SCREEN_WIDTH, SettingsManager.SCREEN_HEIGHT);
        scorePanel = new PanelNumeros(450f, 470f, 90f);
        scorePanel.setData(0);
        myBatch.end();

        if (Gdx.input.justTouched()) {
            myScreens = Screens.GAME;
            myBody = new Snake();
        }
    }

    private void gameScreen() {
        // Pintar Pantalla de Juego
        myBatch.begin();
        myBatch.draw(imgGame, 0, 0, SettingsManager.SCREEN_WIDTH, SettingsManager.SCREEN_HEIGHT);
        scorePanel.pintarse(myBatch);
        myBatch.end();
        if (!bMusicOnOFF) {
            mMusic.play();
            bMusicOnOFF = true;
        }

        // Cuando nos tocan
        if (Gdx.input.justTouched()) {

            // Sabemos donde han tocado
            mouseX = Math.round(Gdx.input.getX() * (SettingsManager.SCREEN_WIDTH / SettingsManager.SYS_WIDTH)); // Ancho
            mouseY = Math.round((SettingsManager.SCREEN_HEIGHT - Gdx.input.getY()) * (SettingsManager.SCREEN_HEIGHT / SettingsManager.SYS_HEIGHT)); // Invertir coordenadas.... // Alto

            headX = myBody.getHeadX();
            headY = myBody.getHeadY();
            difX = Math.abs(mouseX - headX);
            difY = Math.abs(mouseY - headY);

            if (difX > difY) { // En el Eje X
                if (mouseX > headX) { // Derecha
                    myBody.changeDirection(Square.Direction.RIGHT);
                } else { // Izquierda
                    myBody.changeDirection(Square.Direction.LEFT);
                }
            } else { // En el eje Y
                if (mouseY > headY) { // Arriba
                    myBody.changeDirection(Square.Direction.UP);
                } else { // Abajo
                    myBody.changeDirection(Square.Direction.DOWN);
                }
            }
        }
        iCounter++;
        pCounter++;

        // Configurar Estados
        if (myBody.isInside(SettingsManager.LMT_MINX, SettingsManager.LMT_MAXX, SettingsManager.LMT_MINY, SettingsManager.LMT_MAXY)) {
            // Self Movement
            if (iCounter % 15 == 0) {
                myBody.Move();
            } else if (iCounter == 59) {
                myBody.GrowUp();
                iCounter = 0;
                scorePanel.incrementa(1);
            }
            // PowerUp Live
            if (myPowerUp != null) {
                myPowerUp.render(myBatch);
                if (pCounter == 1200) {
                    myPowerUp = null;
                }
                // Get PowerUp
                if (myBody.getHeadX() == myPowerUp.getPosX() && myBody.getHeadY() == myPowerUp.getPosY()) {
                    bPowerUp = true;
                    myPowerUp = null;
                    pCounter = 0;
                }
            } else {
                if (pCounter == 1500) {
                    myPowerUp = new PowerUp();
                    pCounter = 0;
                }
            }
            // Use PowerUp
            if (bPowerUp) {
                if (pCounter == 600) {
                    bPowerUp = false;
                    pCounter = 0;
                }
            } else {
                if (myBody.Colision()) {
                    // Aqui tiene que haber sangreeeeeee
                    isDead();
                }
            }
        } else {
            isDead();
        }
        // Pintamos la Boa
        myBody.render(myBatch);

    }

    private void deadScreen() {
        // Pintar Pantalla de Muerto
        myBatch.begin();
        myBatch.draw(imgDead, 0, 0, SettingsManager.SCREEN_WIDTH, SettingsManager.SCREEN_HEIGHT);
        myBatch.end();
        if (Gdx.input.justTouched()) {
            mDead.stop();
            myScreens = Screens.START;
        }
    }

    private void isDead() {
        mMusic.stop();
        bMusicOnOFF = false;
        myScreens = Screens.DEAD;
        mDead.play();
    }

    @Override
    public void dispose() {
        myBatch.dispose();
        imgStart.dispose();
        imgGame.dispose();
        imgDead.dispose();
        myBody.dispose();
        myPowerUp.dispose();
        mMusic.dispose();
        mDead.dispose();
        scorePanel.dispose();
    }
}
