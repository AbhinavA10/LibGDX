package com.aiscratch;
// Contact Listening:https://www.youtube.com/watch?v=ien40lFovG8

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;

public class AiScratch extends ApplicationAdapter {

    private final float fSCALE = 1.0f;
    private OrthographicCamera camera;
    Box2DDebugRenderer b2dr;
    private World world;
    public static final float fPPM = 32;
    private StaticBox obj1, obj2;
    private PlayerBox playerBox;
    AI entity, target;
    Body body;

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w / fSCALE, h / fSCALE);

        world = new World(new Vector2(0, 0), false);
        world.setContactListener(new ContactListenerUtil());
        b2dr = new Box2DDebugRenderer();
        playerBox = new PlayerBox(world, "PLAYER", 0, 0);
        obj1 = new StaticBox(world, "OBJ1", 75, 75);
        obj2 = new StaticBox(world, "OBJ2", -75, 75);
        
        body = createCircle(0, 0);
        entity = new AI(body, 30);
        
        body = createCircle(5,5);
        target = new AI(body, 30);
        
        Arrive<Vector2> arriveSB = new Arrive<Vector2>(entity, target)
                .setTimeToTarget(1f)
                .setArrivalTolerance(1f)
                .setDecelerationRadius(1);
        entity.setBehavior(arriveSB);
        
            
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime()); // to compensate for lag

        // Render
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        b2dr.render(world, camera.combined.scl(fPPM));

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }
    
    public Body createCircle(float fX, float fY) {
        Body body;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(fX,fY);
        body = world.createBody(def);
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(32 / 2 / fPPM ,32 / 2 / fPPM);
        
        body.createFixture(shape, 1.0f);
        shape.dispose();
        
        return body;
    }
    
    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / fSCALE, height / fSCALE);
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
    }

    public void update(float delta) {
        world.step(1 / 60f, 6, 2);

        inputUpdate(delta);
        cameraUpdate(delta);
    }

    public void inputUpdate(float delta) {
        int horizontalForce = 0;
        int verticalForce = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            horizontalForce -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            horizontalForce += 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            verticalForce += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            verticalForce -= 1;
        }
        target.body.setLinearVelocity(horizontalForce * 10, verticalForce * 10);
        
        entity.update(delta);
    }

    public void cameraUpdate(float delta) {
        Vector3 position = camera.position;
        position.x = 0;
        position.y = 0;
        camera.position.set(position);
        camera.update();
    }
}
