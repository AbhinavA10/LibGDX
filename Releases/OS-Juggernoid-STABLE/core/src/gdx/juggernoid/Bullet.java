package gdx.juggernoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

import static gdx.juggernoid.utils.Constants.fPPM;

public class Bullet extends Sprite {

    private String sFile;
    private Texture txImg;
    private Sprite sprImg;
    public Body body;
    private int nHorizontalForce;
    public boolean bHit;

    //------------------------------------ CONSTRUCTOR ----------------------------------------
    public Bullet(String _sFile, float _fX, float _fY, World world, int nDir) { //for nDir, 1 is left 2 is right
        createBoxBody(world, _fX, _fY);
        sFile = _sFile;
        txImg = new Texture(sFile);
        sprImg = new Sprite(txImg);
        bHit = false;
        if (nDir == 1) {
            nHorizontalForce = -9;
        } else if (nDir == 2) {
            nHorizontalForce = 9;
        }
        body.setLinearVelocity(0, 0);
    }

    //------------------------------------ GET SPRITE ----------------------------------------
    public Sprite getSprite() {
        return sprImg;
    }

    //------------------------------------ MOVE----------------------------------------
    public void move() {
        body.setLinearVelocity(nHorizontalForce, 0);
        body.setGravityScale(0);
    }

    //------------------------------------ CREATE BOX BODY ----------------------------------------
    private void createBoxBody(World world, float x, float y) {
        BodyDef bDef = new BodyDef();
        bDef.fixedRotation = true;
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(x / fPPM, y / fPPM);
        bDef.bullet = true;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8 / 2 / fPPM, 8 / 2 / fPPM);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 1.0f;

        body = world.createBody(bDef);
        body.createFixture(fixDef).setUserData(this);
        shape.dispose();
        nHorizontalForce = 5;
    }

}
