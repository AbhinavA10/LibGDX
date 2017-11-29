package gdx.bullets;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static gdx.bullets.BulletScratch.fPPM;

public class Bullet {

    public Body body;
    int nHorizontalForce;

    //Constuctor
    public Bullet(float _fX, float _fY, World world) { //for nDir, 1 is left 2 is right
        createBoxBody(world, _fX, _fY);
    }

    private void createBoxBody(World world, float x, float y) {
        BodyDef bDef = new BodyDef();
        bDef.fixedRotation = true;
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(x / fPPM, y / fPPM);
        bDef.bullet = true;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(16 / 2 / fPPM, 16 / 2 / fPPM);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 1.0f;

        body = world.createBody(bDef);
        body.createFixture(fixDef).setUserData(this);
        shape.dispose();
        nHorizontalForce = 5;
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
