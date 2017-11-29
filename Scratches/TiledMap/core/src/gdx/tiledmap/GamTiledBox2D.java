//NOTE: circles do not seem to load
package gdx.tiledmap;
//http://www.gamefromscratch.com/post/2014/05/01/LibGDX-Tutorial-11-Tiled-Maps-Part-2-Adding-a-character-sprite.aspx
//http://stackoverflow.com/questions/20063281/libgdx-collision-detection-with-tiledmap
//Spawn points: https://www.youtube.com/watch?v=iZFdLJdiJe8&feature=youtu.be&t=298
// Create a basic world: https://www.youtube.com/watch?v=_y1RvNWoRFU
//Polyline stuff, with box2d: https://www.youtube.com/watch?v=BcbjBEnIWKU
// converting tiled object to box2d shapes http://gamedev.stackexchange.com/questions/66924/how-can-i-convert-a-tilemap-to-a-box2d-world
// camera boundries https://www.youtube.com/watch?v=Lb2vZ5lBgCY 

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import java.util.Iterator;

public class GamTiledBox2D extends ApplicationAdapter {

    private boolean DEBUG = false;
    private final float SCALE = 2.0f;
    private OrthographicCamera camera;
    OrthogonalTiledMapRenderer tmr;
    TiledMap tiledMap;
    MapProperties mapProp;
    Box2DDebugRenderer b2dr;
    private World world;
    private Body bodyPlayer;
    public static final float PPM = 32; // pixels per meter
    // when giving Box2D units, divide by PPM
    MapLayer spawnObjectLayer;
    MapObjects objectsSpawnPoints;
    Iterator<MapObject> objectIterator;
    int nLevelWidth = 0;
    int nLevelHeight = 0;

    @Override
    public void create() {
        int x = 0, y = 0;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w / SCALE, h / SCALE);

        world = new World(new Vector2(0, -9.8f), false);
        b2dr = new Box2DDebugRenderer();

        tiledMap = new TmxMapLoader().load("MyMap.tmx");
        tmr = new OrthogonalTiledMapRenderer(tiledMap);
        mapProp = tiledMap.getProperties();
        nLevelWidth = mapProp.get("width", Integer.class);
        nLevelHeight = mapProp.get("height", Integer.class);

        spawnObjectLayer = tiledMap.getLayers().get("SpawnPoint");
        objectsSpawnPoints = spawnObjectLayer.getObjects();
        objectIterator = objectsSpawnPoints.iterator();
        while (objectIterator.hasNext()) {
            MapObject object = objectIterator.next();
            System.out.println(object.getName());
            if (object.getProperties().get("toSpawn").equals("Hero")) {
                System.out.println("FOUND THE HERO");
                x = object.getProperties().get("x", float.class).intValue();
                y = object.getProperties().get("y", float.class).intValue();
            }
            if (object.getProperties().get("toSpawn").equals("Enemy")) {
                System.out.println("FOUND THE BADGUY");
                System.out.println("locX: " + object.getProperties().get("x", float.class));
                System.out.println("locX: " + object.getProperties().get("y", float.class));
            }
        }
        bodyPlayer = createBox(x, y, 32, 32, false);  //  bodyPlayer = createBox(120, 180, 32, 32, false);
        //   createBox(120, 170, 64, 32, true);

        TiledObjectUtil.parseTiledObjectLayer(world, tiledMap.getLayers().get("collision").getObjects());
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime()); // to compensate for lag

        // Render
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tmr.render();
        b2dr.render(world, camera.combined.scl(PPM));

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / SCALE, height / SCALE);
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
        tmr.dispose();
        tiledMap.dispose();
    }

    public void update(float delta) {
        world.step(1 / 60f, 6, 2);

        inputUpdate(delta);
        cameraUpdate(delta);
        tmr.setView(camera);
    }

    public void inputUpdate(float delta) {
        int horizontalForce = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            horizontalForce -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            horizontalForce += 1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            bodyPlayer.applyForceToCenter(0, 300, false);
        }

        bodyPlayer.setLinearVelocity(horizontalForce * 5, bodyPlayer.getLinearVelocity().y);
    }

    public void cameraUpdate(float delta) {
        Vector3 position = camera.position;
        // if we comment the next two lines and camera.set() out while keeping lerpToTarget, the camera gets a cool "lag"
        // position.x = bodyPlayer.getPosition().x * PPM; 
        // position.y = bodyPlayer.getPosition().y * PPM;
        lerpToTarget(camera, bodyPlayer.getPosition().scl(PPM));

        float StartX = camera.viewportWidth / 2;
        float StartY = camera.viewportHeight / 2;
        int tileSize = 32;
        boundry(camera, StartX, StartY, nLevelWidth * tileSize - StartX * 2, nLevelHeight * tileSize - StartY * 2);
        camera.position.set(position);

        camera.update();
    }
// Polyline Source

    public Body createBox(int x, int y, int width, int height, boolean isStatic) {
        Body body;
        BodyDef def = new BodyDef();

        if (isStatic) {
            def.type = BodyDef.BodyType.StaticBody;
        } else {
            def.type = BodyDef.BodyType.DynamicBody;
        }

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        body.createFixture(shape, 1.0f);
        shape.dispose();
        return body;
    }

    public static void lerpToTarget(Camera camera, Vector2 target) {
        // a + (b-a) * lerpFactor
        Vector3 position = camera.position;
        position.x = camera.position.x + (target.x - camera.position.x) * .1f;
        position.y = camera.position.y + (target.y - camera.position.y) * .1f;
        camera.position.set(position);
        camera.update();

    }

    public static void boundry(Camera camera, float fStartX, float fStartY, float fWidth, float fHeight) {
        Vector3 vec3Position = camera.position;

        vec3Position.x = MathUtils.clamp(vec3Position.x, fStartX, fStartX + fWidth);
        vec3Position.y = MathUtils.clamp(vec3Position.y, fStartY, fStartY + fHeight);
        camera.position.set(vec3Position);
        camera.update();
    }
}
