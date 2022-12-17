package com.politecnicomalaga.snakepowerup.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.politecnicomalaga.snakepowerup.manager.AssetsManager;
import com.politecnicomalaga.snakepowerup.manager.SettingsManager;

import java.util.ArrayList;

public class Snake {

    // ATRIBUTOS
    protected Texture imgHead = new Texture(AssetsManager.IMG_HEAD);
    protected Texture imgBody = new Texture(AssetsManager.IMG_BODY);
    protected ArrayList<Square> myBody;
    protected Square.Direction myDirection;

    // CONSTRUCTOR
    public Snake() {
        float posX = (float) (((int) (Math.random() * SettingsManager.SITES_WIDTH)) * SettingsManager.SIDE_SIZE) + SettingsManager.LMT_MINX;
        float posY = (float) (((int) (Math.random() * SettingsManager.SITES_HEIGHT)) * SettingsManager.SIDE_SIZE) + SettingsManager.LMT_MINY;
        myBody = new ArrayList<>();
        Square aux = new Square(posX, posY, SettingsManager.SIDE_SIZE, imgHead);
        myBody.add(aux);
        int iRandom = (int) (Math.random() * 3);
        switch (iRandom) {
            case (0):
                myDirection = Square.Direction.UP;
                break;
            case (1):
                myDirection = Square.Direction.DOWN;
                break;
            case (2):
                myDirection = Square.Direction.LEFT;
                break;
            case (3):
                myDirection = Square.Direction.RIGHT;
                break;
        }
//        miDireccion = Square.ARRIBA;
    }

    // Pintame
    public void render(SpriteBatch miSB) {
        for (Square aux : myBody) {
            aux.render(miSB);
        }
    }

    // Comportamiento
    public void Move() {
        // Y si crezco y elimino el ultimo??
        this.GrowUp();
        myBody.remove(myBody.size() - 1);
    }

    public void GrowUp() {
        // Para crecer, creo un nuevo cuadrado como en el constructor,
        // lo muevo, y lo añado como nueva cabezaa
        Square newHead = new Square(myBody.get(0));
        newHead.selfMove(myDirection);
        myBody.add(0, newHead);

        Square newBody = new Square(myBody.get(1).getPosX(), myBody.get(1).getPosY(), myBody.get(1).getSide(), imgBody);
        myBody.remove(1);
        myBody.add(1, newBody);
    }

    public void changeDirection(Square.Direction iNewDir) {
        boolean bNoChange;
        bNoChange = (myDirection == Square.Direction.UP && iNewDir == Square.Direction.DOWN);
        bNoChange = bNoChange || (myDirection == Square.Direction.DOWN && iNewDir == Square.Direction.UP);
        bNoChange = bNoChange || (myDirection == Square.Direction.LEFT && iNewDir == Square.Direction.RIGHT);
        bNoChange = bNoChange || (myDirection == Square.Direction.RIGHT && iNewDir == Square.Direction.LEFT);
        if (!bNoChange) {
            myDirection = iNewDir;
        }
    }

    public boolean isInside(float lmtMinX, float lmtMaxX, float lmtMinY, float lmtMaxY) {
        return (this.getHeadX() >= lmtMinX &&
                this.getHeadX() <= lmtMaxX - myBody.get(0).getSide() &&
                this.getHeadY() >= lmtMinY &&
                this.getHeadY() <= lmtMaxY - myBody.get(0).getSide());
    }

    public boolean Colision() {
        Square aux = myBody.get(0);
        for (int i = 4; i < myBody.size(); i++) {
            if (myBody.get(i).Colision(aux)) {
                return true;
            }
        }
        return false;
    }

    public void dispose() {
        if (imgHead != null) imgHead.dispose();
        if (imgBody != null) imgBody.dispose();
    }

    // Getters & Setters
    public float getHeadX() {
        return myBody.get(0).getPosX();
    }

    public float getHeadY() {
        return myBody.get(0).getPosY();
    }

}
