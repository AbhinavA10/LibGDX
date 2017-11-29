package gdx.alphahydranoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteExtended extends Sprite {

    String sName;
    // float fSx, fSy, fW, fH;
    int nNumJumps = 2;
    private float fX, fY;
    String sFile;
    Texture txImg;
    private Sprite sprImg;

    //Constuctor
    public SpriteExtended(String _sFile, float _fX, float _fY) {
        sFile = _sFile;
        fX = _fX;
        fY = _fY;
        txImg = new Texture(sFile);
        sprImg = new Sprite(txImg);

    }

    public void jump() {
        nNumJumps--;
    }

    public Sprite getSprite() {
        return sprImg;
    }
}
