package gdx.bullets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static gdx.bullets.screens.ScrPlay.fPPM;

public class SpriteExtended extends Sprite {

    public int nNumJumps = 2;
    public float fX, fY; //public only to be used to place bullets on top of it
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
    public Body body;
    public int nDir;

    //Constuctor
    public SpriteExtended(String _sFile, float _fX, float _fY, World world) {
        createBoxBody(world, _fX, _fY);
        nDir = 2;
        nPos = 0;
        nFrame = 0;
        sFile = _sFile;
        fX = body.getPosition().x;
        fY = body.getPosition().y;
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

    public void resetJump() {
        nNumJumps = 2;
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

    private void createBoxBody(World world, float x, float y) {
        BodyDef bDef = new BodyDef();
        bDef.fixedRotation = true;
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(x / fPPM, y / fPPM);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(32 / 2 / fPPM, 32 / 2 / fPPM);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 1.0f;

        body = world.createBody(bDef);
        body.createFixture(fixDef).setUserData(this);
        shape.dispose();
    }

    public void hit() {
        System.out.println("the player is hit");
    }
}
