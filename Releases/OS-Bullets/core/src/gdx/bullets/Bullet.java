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

public class Bullet extends Sprite {

    public int nNumJumps = 2;
    public float fX, fY; //public due to poor coding for batch.draw
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
    int nHorizontalForce;
    int nDir;

    //Constuctor
    public Bullet(String _sFile, float _fX, float _fY, World world, int _nDir) { //for nDir, 1 is left 2 is right
        createBoxBody(world, _fX, _fY);
        sFile = _sFile;
        fX = body.getPosition().x;
        fY = body.getPosition().y;
        txImg = new Texture(sFile);
        sprImg = new Sprite(txImg);
        nDir = _nDir;
        if (nDir == 1) { 
            nHorizontalForce = -5;
        } else if (nDir == 2) {
            nHorizontalForce = 5;
        }
    }

    public Sprite getSprite() {
        return sprImg;
    }
    
    public void move() {
        body.setLinearVelocity(nHorizontalForce, body.getLinearVelocity().y);
        body.setGravityScale(0);
        //System.out.println("BULLET BULLET");
    }

    private void createBoxBody(World world, float x, float y) {
        BodyDef bDef = new BodyDef();
        bDef.fixedRotation = true;
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(x / fPPM, y / fPPM);
        bDef.bullet=true;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(16 / 2 / fPPM, 16 / 2 / fPPM);

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
