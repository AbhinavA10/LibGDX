package gdx.weapons;

import com.badlogic.gdx.physics.box2d.*;

import static gdx.weapons.WeaponScratch.fPPM;

public class StaticBox {

    public Body body;

    public StaticBox(World world, float x, float y) {
        createBoxBody(world, x, y);
    }

    private void createBoxBody(World world, float x, float y) {
        BodyDef bDef = new BodyDef();
        bDef.fixedRotation = true;
        bDef.type = BodyDef.BodyType.StaticBody;
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
}
