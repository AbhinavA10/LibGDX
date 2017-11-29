/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdx.gravitydetection;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Sprite2 extends Sprite {

    String sName;
    double dX, dY;
    double dAccel;
    double dVel, dVelLimit;
    Texture txSheet;
    float fSx, fSy, fW, fH;
    Rectangle rect;
    double dLastX, dLastY;
    int nNumJumps;

    //Constuctor
    Sprite2(Texture txSheet_, float fSx_, float fSy_, float fW_, float fH_, double dX_, double dY_, double dAccel_, double dVel_, double dVelLimit_) {
        fSx = fSx_;
        fSy = fSy_;
        fW = fW_;
        fH = fH_;
        dX = dX_;
        dY = dY_;
        dAccel = dAccel_;
        dVel = dVel_;
        dVelLimit = dVelLimit_;
    }

    public void gravity() {
        dVel += dAccel;
        if (dVel >= dVelLimit) {
            dVel = dVelLimit;
        }
        dY -= dVel;
    }
    
    public void jump() {
        dVel = -12;
        nNumJumps --;
    }
    
    public Rectangle retRect() { //replaces .getBoundingRectangle() for hit detection 
        rect = new Rectangle(Math.round((float)dX), Math.round((float)dY), fW, fH);
        return rect;
    }
    
    
    public void reset() {
        dY = 600;
        dX = 300;
    }
}
