package gdx.weapons;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static gdx.weapons.WeaponScratch.fPPM;

public class Bullet {

    public Body body;
    int nHorizontalForce;
    int nW, nH;
    //Constuctor

    public Bullet(float _fX, float _fY, World world, Vector2 vecSize, int _nHorizontalForce) { //for nDir, 1 is left 2 is right
        createBoxBody(world, _fX, _fY, vecSize);
        nHorizontalForce = _nHorizontalForce;
    }

    private void createBoxBody(World world, float x, float y, Vector2 vecSize) {
        BodyDef bDef = new BodyDef();
        bDef.fixedRotation = true;
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(x / fPPM, y / fPPM);
        bDef.bullet = true;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(vecSize.x / 2 / fPPM, vecSize.y / 2 / fPPM);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 1.0f;

        body = world.createBody(bDef);
        body.createFixture(fixDef).setUserData(this);
        shape.dispose();
    }

    public void move() {
        body.setLinearVelocity(nHorizontalForce, body.getLinearVelocity().y);
        body.setGravityScale(0);
    }

    public void stop() {
        body.setLinearVelocity(0, 0);
        body.setGravityScale(0);
    }
}
