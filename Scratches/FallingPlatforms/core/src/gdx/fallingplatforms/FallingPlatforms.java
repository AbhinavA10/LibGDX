package gdx.fallingplatforms;
// Kinematic Bodies, for falling platforms: http://www.emanueleferonato.com/2012/05/11/understanding-box2d-kinematic-bodies/

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;

public class FallingPlatforms extends ApplicationAdapter {

    private final float fSCALE = 2.0f;
    private OrthographicCamera camera;
    Box2DDebugRenderer b2dr;
    private World world;
    public static final float fPPM = 32;
    private StaticBox obj1, obj2, obj3;
    private KinematicBox kinematicBox, fallingBox, fallingBox2, fallingBox3;
    private PlayerBox playerBox;

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w / fSCALE, h / fSCALE);

        world = new World(new Vector2(0, -9.8f), false);
        world.setContactListener(new ContactListenerUtil());
        b2dr = new Box2DDebugRenderer();
        playerBox = new PlayerBox(world, "PLAYER", -25, 100);
        obj1 = new StaticBox(world, "OBJ1", 75, 75);
        obj2 = new StaticBox(world, "OBJ2", -75, 75);

        fallingBox = new KinematicBox(world, "down", 0, -75, false, 0);
        fallingBox2 = new KinematicBox(world, "down", -25, -75, false, 0);
        fallingBox3 = new KinematicBox(world, "down", 25, -75, false, 0);

        kinematicBox = new KinematicBox(world, "sideways", -100, 0, true, 21);

    }

    @Override
    public void render() {
        update();
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        b2dr.render(world, camera.combined.scl(fPPM));

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
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

    public void update() {
        world.step(1 / 60f, 6, 2);

        inputUpdate();
        cameraUpdate();
    }

    public void inputUpdate() {
        int horizontalForce = 0;
        fallingBox.activate(fallingBox.body.getLinearVelocity().x, -4);
        fallingBox2.activate(fallingBox2.body.getLinearVelocity().x, -4);
        fallingBox3.activate(fallingBox3.body.getLinearVelocity().x, -4);

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            horizontalForce -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            horizontalForce += 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            playerBox.body.applyForceToCenter(0, 75, false);
        }
        playerBox.body.setLinearVelocity(horizontalForce * 5, playerBox.body.getLinearVelocity().y);
    }

    public void cameraUpdate() {
        Vector3 position = camera.position;
        position.x = 0;
        position.y = 0;
        camera.position.set(position);
        camera.update();
    }
}
