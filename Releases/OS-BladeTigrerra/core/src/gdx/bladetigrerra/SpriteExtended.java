package gdx.bladetigrerra;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteExtended extends Sprite {

    String sName;
    int nNumJumps = 2;// will use in next version
    private float fX, fY;
    String sFile;
    Texture txImg;
    private Sprite sprImg;
    Animation araniVlad[];
    Sprite sprVlad, spr;
    float fSx, fSy, fW, fH;
    Texture txSheet;
    TextureRegion trTemp;
    public int nPos, nFrame;
    public boolean isMoving;

    //Constuctor
    public SpriteExtended(String _sFile, float _fX, float _fY) {
        nPos = 0;
        nFrame = 0;
        sFile = _sFile;
        fX = _fX;
        fY = _fY;
        txImg = new Texture(sFile);
        //txSheet=txImg;
        txSheet = new Texture(sFile);
        sprImg = new Sprite(txImg);
        araniVlad = new Animation[8];
        fW = txSheet.getWidth() / 8;
        fH = txSheet.getHeight() / 8;
        for (int i = 0; i < 8; i++) {
            Sprite[] arSprVlad = new Sprite[8];
            for (int j = 0; j < 8; j++) {
                fSx = j * fW;
                fSy = i * fH;
                sprVlad = new Sprite(txSheet, Math.round(fSx), Math.round(fSy), Math.round(fW), Math.round(fH));
                arSprVlad[j] = new Sprite(sprVlad);
            }
            araniVlad[i] = new Animation(10f, arSprVlad);
        }
    }

    public void jump() {
        nNumJumps--;
    }

    public void animate() {

        if (isMoving) {
            nFrame++;
        } else {
            nFrame = 0;
        }
        trTemp = araniVlad[nPos].getKeyFrame(nFrame, true);
    }

    public Sprite getSprite() {
        animate();
        spr = new Sprite(trTemp);
        return spr;
    }
}
