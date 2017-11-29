package com.aiscratch;

import com.badlogic.gdx.physics.box2d.*;

import static com.aiscratch.AiScratch.fPPM;

public class PlayerBox {

    public Body body;
    public String sId;

    public PlayerBox(World world, String _sId, float x, float y) {
        this.sId = _sId;
        createBoxBody(world, x, y);
        body.setLinearDamping(20f);
        body.setAngularDamping(1f);
    }

    private void createBoxBody(World world, float x, float y) {
        BodyDef bDef = new BodyDef();
        bDef.fixedRotation = false;
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(x / fPPM, y / fPPM);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(32 / 2 / fPPM, 32 / 2 / fPPM);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 1.0f;
        //fixDef.restitution = 2;

        this.body = world.createBody(bDef);
        this.body.createFixture(fixDef).setUserData(this);
        shape.dispose();
    }

    public void hit() {
        System.out.println("Yo, the player is hit");
    }
}
