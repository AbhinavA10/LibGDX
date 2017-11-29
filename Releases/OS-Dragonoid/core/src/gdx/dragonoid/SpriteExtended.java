package gdx.dragonoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static gdx.dragonoid.utils.Constants.fPPM;

public class SpriteExtended extends Sprite {

    public int nNumJumps = 2;
    private float fX, fY; // just for original spawn points, or last alive point when we reset for deaths
    String sFile;
    Texture txImg;
    private Sprite sprImg;
    Animation araniPlayer[];
    Sprite sprPlayer, spr;
    float fSx, fSy, fW, fH;
    Texture txSheet;
    TextureRegion trTemp;
    public int nPos, nFrame;
    public boolean isMoving;
    public Body body;

    //Constuctor
    public SpriteExtended(String _sFile, float _fX, float _fY, World world) {
        createBoxBody(world, _fX, _fY);
        nPos = 0;
        nFrame = 0;
        sFile = _sFile;
        fX = body.getPosition().x * fPPM;
        fY = body.getPosition().y * fPPM;
        txImg = new Texture(sFile);
        //txSheet=txImg;
        txSheet = new Texture(sFile);
        sprImg = new Sprite(txImg);
        araniPlayer = new Animation[8];
        fW = txSheet.getWidth() / 8;
        fH = txSheet.getHeight() / 8;
        for (int i = 0; i < 8; i++) {
            Sprite[] arSprPlayer = new Sprite[8];
            for (int j = 0; j < 8; j++) {
                fSx = j * fW;
                fSy = i * fH;
                sprPlayer = new Sprite(txSheet, Math.round(fSx), Math.round(fSy), Math.round(fW), Math.round(fH));
                arSprPlayer[j] = new Sprite(sprPlayer);
            }
            araniPlayer[i] = new Animation(10f, arSprPlayer);
        }
    }

    public void death(World world) {
        reset(world);
    }

    public void reset(World world) {
        world.destroyBody(body);
        createBoxBody(world, fX, fY);
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
        trTemp = araniPlayer[nPos].getKeyFrame(nFrame, true);
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
}
