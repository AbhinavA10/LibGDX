package gdx.bullets;
//Destroying a body upon collision: http://box2d.org/forum/viewtopic.php?t=9724
//  Try/catch: https://docs.oracle.com/javase/tutorial/essential/exceptions/catch.html
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

public class BulletScratch extends ApplicationAdapter implements InputProcessor {

    private final float fSCALE = 1.0f;
    private OrthographicCamera camera;
    Box2DDebugRenderer b2dr;
    private World world;
    public static final float fPPM = 32;
    private StaticBox obj1, obj2;
    private ArrayList<Bullet> alBullets;
    private ArrayList<Bullet> alDeadBullets;

    private ArrayList<StaticBox> alBoxes;
    private ArrayList<StaticBox> alDestroyBoxes;
    float w, h;

    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w / fSCALE, h / fSCALE);

        world = new World(new Vector2(0, 0), false);
        world.setContactListener(new ContactListenerUtil(this));
        b2dr = new Box2DDebugRenderer();

        alBullets = new ArrayList<Bullet>();
        alDeadBullets = new ArrayList<Bullet>();

        alBoxes = new ArrayList<StaticBox>();
        alDestroyBoxes = new ArrayList<StaticBox>();


        alBoxes.add(new StaticBox(world, "OBJ1", 75, 75));
        alBoxes.add(new StaticBox(world, "OBJ2", -75, 75));
        alBoxes.add(new StaticBox(world, "OBJ2", 75, 25));
        alBoxes.add(new StaticBox(world, "OBJ2", -75, 25));
        alBoxes.add(new StaticBox(world, "OBJ2", 75, -25));
        alBoxes.add(new StaticBox(world, "OBJ2", -75, -25));

    }

    @Override
    public void render() {
        update(); // to compensate for lag

        // Render
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
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
    }

    public void update() {
        world.step(1 / 60f, 6, 2);
        bulletUpdate();
        boxUpdate();
        cameraUpdate();
    }

    public void cameraUpdate() {
        Vector3 position = camera.position;
        position.x = 0;
        position.y = 0;
        camera.position.set(position);
        camera.update();
    }

    public void bulletUpdate() {
        for (int i = alDeadBullets.size() - 1; i >= 0; i--) {
            Bullet bullet = alDeadBullets.get(i);
            world.destroyBody(bullet.body);
            System.out.println("Bullet removed");
        }
        alDeadBullets.clear();
        for (Bullet bullet : alBullets) {
            if (bullet.body.getPosition().x * fPPM < w) {
                bullet.move();
            } else {
                bullet.stop();
            }
        }
    }

    public void boxUpdate() {
        for (int i = alDestroyBoxes.size() - 1; i >= 0; i--) {
            StaticBox staticBox = alDestroyBoxes.get(i);
            world.destroyBody(staticBox.body);
            System.out.println("Bullet removed");
        }
        alDestroyBoxes.clear();
    }

    public void destroy(Bullet bullet, StaticBox staticBox) {
        if (!alDeadBullets.contains(bullet)) {
            alDeadBullets.add(bullet);
            alBullets.remove(bullet);
        }
        if (!alDestroyBoxes.contains(staticBox)) {
            alDestroyBoxes.add(staticBox);
            alBoxes.remove(staticBox);
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("mouse down, creating bullet");
        Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
        Vector3 position = camera.unproject(clickCoordinates);
        alBullets.add(new Bullet(position.x, position.y, world));
        return true;
    }

    @Override
    public boolean keyDown(int i) {
        return true;
    }

    @Override
    public boolean keyUp(int i) {
        return true;
    }

    @Override
    public boolean keyTyped(char c) {
        return true;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return true;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return true;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return true;
    }

    @Override
    public boolean scrolled(int i) {
        return true;
    }

}
