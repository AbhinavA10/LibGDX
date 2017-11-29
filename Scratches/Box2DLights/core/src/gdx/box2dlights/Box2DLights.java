//add camera boundries: circles do not seem to work
package gdx.box2dlights;
//http://www.gamefromscratch.com/post/2014/05/01/LibGDX-Tutorial-11-Tiled-Maps-Part-2-Adding-a-character-sprite.aspx
//http://stackoverflow.com/questions/20063281/libgdx-collision-detection-with-tiledmap
//Spawn points: https://www.youtube.com/watch?v=iZFdLJdiJe8&feature=youtu.be&t=298
// Create a basic world: https://www.youtube.com/watch?v=_y1RvNWoRFU
//Polyline stuff, with box2d: https://www.youtube.com/watch?v=BcbjBEnIWKU
// converting tiled object to box2d shapes http://gamedev.stackexchange.com/questions/66924/how-can-i-convert-a-tilemap-to-a-box2d-world
//https://www.youtube.com/watch?v=p024Ix0I8W0 good tutorial on Box2DLights, basic rendering of lights and ambient lights

// NOTE circles and polygons do not seem to work (at least not yet)
import box2dLight.ChainLight;
import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import java.util.Iterator;

public class Box2DLights extends ApplicationAdapter {

    private boolean DEBUG = false;
    private final float SCALE = 2.0f;
    private OrthographicCamera camera;
    OrthogonalTiledMapRenderer tmr;
    TiledMap tiledMap;
    Box2DDebugRenderer b2dr;
    private World world;
    private Body bodyPlayer;
    private RayHandler rayHandler; //for lighting
    private PointLight pointLight;
    private ConeLight coneLight;
    private ChainLight chainLight;
    boolean bBlur;
    public static final float PPM = 32; // pixels per meter
    // when giving Box2D units, divide by PPM
    MapLayer spawnObjectLayer;
    MapObjects objectsSpawnPoints;
    Iterator<MapObject> objectIterator;

    @Override
    public void create() {
        int x = 0, y = 0;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        bBlur = true;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w / SCALE, h / SCALE);

        world = new World(new Vector2(0, -9.8f), false);
        b2dr = new Box2DDebugRenderer();

        tiledMap = new TmxMapLoader().load("MyMap.tmx");
        tmr = new OrthogonalTiledMapRenderer(tiledMap);

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

        //createBox(120, 170, 64, 32, true);   //createBox(120, 170, 64, 32, true);

        TiledObjectUtil.parseTiledObjectLayer(world, tiledMap.getLayers().get("collision").getObjects());

        rayHandler = new RayHandler(world); //making a ray handler
        rayHandler.setAmbientLight(0.1f); // this makes it so the default light isnt just black
        //rayHandler.setBlur(bBlur); //adds a blur effect over all the lights, kind of interesting
        //rayHandler.setBlurNum(50); //sets amount of blur

        chainLight = new ChainLight(rayHandler, 20, Color.GREEN, 5, 4, new float[]{1, 6, 12, 6, 12, 15}); // float array has sets of coordinates
        pointLight = new PointLight(rayHandler, 20, Color.RED, 128 / PPM, 17, 11); // 1st number is the amount of rays it shoots out in every direction (between 20 and 200 is usually good) 2nd number is length it is going, last two numbers are x and y
        pointLight.setSoftnessLength(0f); //so it doesnt bleed into character
        //pointLight.attachToBody(bodyPlayer); //works only if lights are declared below character, can add an offset in x and y
        //pointLight.setXray(true); //light goes through everything
        //color doesnt seem to work? so color actually works but over top of the blue background it looks blue, if you drop into the black area you can see it
        coneLight = new ConeLight(rayHandler, 50, Color.GOLDENROD, 5, 7, 7, 45, 30); //last two numbers are angle and cone size
        coneLight.attachToBody(bodyPlayer, 0, 0, 0); //body, offset x, offset y, degrees
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime()); // to compensate for lag

        // Render
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tmr.render();
        b2dr.render(world, camera.combined.scl(PPM));
        rayHandler.render();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        System.out.println(bodyPlayer.getPosition());
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
        rayHandler.dispose();
    }

    public void update(float delta) {
        world.step(1 / 60f, 6, 2);
        rayHandler.update();

        inputUpdate(delta);
        cameraUpdate(delta);
        tmr.setView(camera);
        rayHandler.setCombinedMatrix(camera.combined.cpy().scl(PPM)); // cpy method creates space for another vector 2 object every time it is called, inefficient but idk what else works
    }                                                                 //unsure why the name gets crossed out

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
        position.x = bodyPlayer.getPosition().x * PPM;
        position.y = bodyPlayer.getPosition().y * PPM;
        //  camera.position.x = MathUtils.clamp(camera.position.x, 0, tiledMap.getProperties().get("width", Integer.class) - camera.viewportWidth);
        //camera.position.y = MathUtils.clamp(camera.position.y, 0, tiledMap.getProperties().get("width", Integer.class) - camera.viewportHeight);

        camera.position.set(position);

        camera.update();
    }
// Polyline Source

    public Body createBox(int x, int y, int width, int height, boolean isStatic) {
        Body pBody;
        BodyDef def = new BodyDef();

        if (isStatic) {
            def.type = BodyDef.BodyType.StaticBody;
        } else {
            def.type = BodyDef.BodyType.DynamicBody;
        }

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();
        return pBody;
    }
}
