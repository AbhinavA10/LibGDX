package gdx.fallingplatforms;

import com.badlogic.gdx.physics.box2d.*;

import static gdx.fallingplatforms.FallingPlatforms.fPPM;

public class KinematicBox {

    public Body body;
    public String sId;
    public boolean bHit;
    public int nCount = 0;

    public KinematicBox(World world, String _sId, float x, float y, boolean _bHit, int _nCount) {
        this.sId = _sId;
        createBoxBody(world, x, y);
        bHit = _bHit;
        nCount = _nCount;
        activate(5, body.getLinearVelocity().y);
    }

    private void createBoxBody(World world, float x, float y) {
        BodyDef bDef = new BodyDef();
        bDef.fixedRotation = true;
        bDef.type = BodyDef.BodyType.KinematicBody;
        bDef.position.set(x / fPPM, y / fPPM);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(32 / 2 / fPPM, 32 / 2 / fPPM);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 1.0f;

        this.body = world.createBody(bDef);
        this.body.createFixture(fixDef).setUserData(this);
        shape.dispose();
    }

    void activate(float x, float y) {
        if (bHit) {
            if (nCount > 20) {
                body.setLinearVelocity(x, y);
            } else {
                nCount++;
            }

        }
    }
}
