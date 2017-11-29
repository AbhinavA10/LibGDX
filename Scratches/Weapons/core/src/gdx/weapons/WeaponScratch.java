package gdx.weapons;
// add rate of bullet spawn

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

public class WeaponScratch extends ApplicationAdapter implements InputProcessor {

    private final float fSCALE = 1.0f;
    private OrthographicCamera camera;
    Box2DDebugRenderer b2dr;
    private World world;
    public static final float fPPM = 32;
    private StaticBox obj1, obj2;
    private ArrayList<Bullet> alBullets;
    private ArrayList<Bullet> alDeadBullets;
    private ArrayList<StaticBox> alBoxes;
    float w, h;
    int nForce = 3;
    Vector2 vec2SizeBullet;
    int nWeaponSelected = 1; //1-3
    boolean arbUnlocked[] = new boolean[3];

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


        alBoxes.add(new StaticBox(world, 75, 75));
        alBoxes.add(new StaticBox(world, -75, 75));
        alBoxes.add(new StaticBox(world, 75, 25));
        alBoxes.add(new StaticBox(world, -75, 25));
        alBoxes.add(new StaticBox(world, 75, -25));
        alBoxes.add(new StaticBox(world, -75, -25));
        vec2SizeBullet = new Vector2(24, 24);
        arbUnlocked[0] = true; // basic bullet

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
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            nWeaponSelected = 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            nWeaponSelected = 2;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            nWeaponSelected = 3;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2)) {
            arbUnlocked[1] = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_3)) {
            arbUnlocked[2] = true;
        }
        System.out.println(arbUnlocked[0] + ", " + arbUnlocked[1] + ", " + arbUnlocked[2]);
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
        weaponUpdate();
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

    public void weaponUpdate() {
        if (arbUnlocked[nWeaponSelected - 1]) {
            switch (nWeaponSelected) {
                case 1:
                    nForce = 3;
                    vec2SizeBullet.set(24, 24);
                    break;
                case 2:
                    nForce = 5;
                    vec2SizeBullet.set(16, 16);
                    break;
                case 3:
                    nForce = 7;
                    vec2SizeBullet.set(8, 8);
                    break;
            }
        }
    }

    public void destroy(Bullet bullet) {
        if (!alDeadBullets.contains(bullet)) {
            alDeadBullets.add(bullet);
            alBullets.remove(bullet);
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
        Vector3 position = camera.unproject(clickCoordinates);
        alBullets.add(new Bullet(position.x, position.y, world, vec2SizeBullet, nForce));
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
