package com.politecnicomalaga.snakepowerup.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.politecnicomalaga.snakepowerup.manager.AssetsManager;
import com.politecnicomalaga.snakepowerup.manager.SettingsManager;

public class PowerUp {

    private final float posX, posY;
    private final int side;
    private final Texture imgPowerUp;

    public PowerUp() {
        this.posX = (float) (((int) (Math.random() * SettingsManager.SITES_WIDTH)) * SettingsManager.SIDE_SIZE) + SettingsManager.LMT_MINX;
        this.posY = (float) (((int) (Math.random() * SettingsManager.SITES_HEIGHT)) * SettingsManager.SIDE_SIZE) + SettingsManager.LMT_MINY;
        this.side = SettingsManager.SIDE_SIZE;
        this.imgPowerUp = new Texture(AssetsManager.IMG_POWERUP);
    }

    // Pintame
    public void render(SpriteBatch myBatch) {
        myBatch.begin();
        myBatch.draw(imgPowerUp, posX, posY, side, side);
        myBatch.end();
    }

    public void dispose() {
        if (imgPowerUp != null) imgPowerUp.dispose();
    }

    // Getters & Setters
    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

}
