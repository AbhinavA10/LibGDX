package gdx.scrollingCam;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author G
 */
public class SprBad extends Sprite {

    private float fX, fY, fVx, fVy; // coordinates and velocities
    String sFile;
    Texture txImg;
    private Sprite sprImg;

    public SprBad(String _sFile, float _fX, float _fY) {
        sFile = _sFile;
        fX = _fX;
        fY = _fY;
        txImg = new Texture(sFile);
        sprImg = new Sprite(txImg); // I should not have to create a new Sprite, since this class extends the Sprite class. 
        //this = new Sprite(txImg);
        //sprImg.setSize(2, 2); // wut
    }

    // mutators and accessors
    void update(float _fVx, float _fVy) {
        fVx = _fVx;
        fVy = _fVy;
        fX += fVx;
        fY += fVy;
        setXY(this, fX, fY);

    }

    public static void setXY(Sprite spr, float fX, float fY) {
        spr.setX(fX);
        spr.setY(fY);
    }

    public Sprite getSprite() {
        return sprImg;
    }

}
