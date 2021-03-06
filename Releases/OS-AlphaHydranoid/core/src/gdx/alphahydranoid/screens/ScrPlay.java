//1.0 Alpha Hydranoid: Create and import a map/level (using Tiled) with solid tiles. Have hit detection with box2d on the hero
package gdx.alphahydranoid.screens;
//-http://www.gamefromscratch.com/post/2014/05/01/LibGDX-Tutorial-11-Tiled-Maps-Part-2-Adding-a-character-sprite.aspx
//-http://stackoverflow.com/questions/20063281/libgdx-collision-detection-with-tiledmap
//-Spawn points: https://www.youtube.com/watch?v=iZFdLJdiJe8&feature=youtu.be&t=298
//-Create a basic world: https://www.youtube.com/watch?v=_y1RvNWoRFU

import gdx.alphahydranoid.utils.TiledObjectUtil;
import gdx.alphahydranoid.utils.CameraStyleUtil;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import gdx.alphahydranoid.SpriteExtended;
import java.util.Iterator;

public class ScrPlay extends ApplicationAdapter implements Screen {

    private final float fSCALE = 2.0f;
    private OrthographicCamera ocCam;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    TiledMap tiledMap;
    MapProperties mapProp;
    Box2DDebugRenderer b2dr;
    private World world;
    private Body bodyPlayer;
    public static final float fPPM = 32; // pixels per meter
    // when giving Box2D units, divide by fPPM
    MapObjects objectsSpawnPoints;
    Iterator<MapObject> objectIterator;
    int nLevelWidth, nLevelHeight, nTileSize;

    Game game;
    SpriteBatch batch;
    SpriteExtended sprExtHero;
    double dZoom; // for camera, eventually

    //@Override
    public ScrPlay(Game _game) {
        int nX = 0, nY = 0;
        ocCam = new OrthographicCamera();
        ocCam.setToOrtho(false, Gdx.graphics.getWidth() / fSCALE, Gdx.graphics.getHeight() / fSCALE);

        world = new World(new Vector2(0, -9.8f), false);
        b2dr = new Box2DDebugRenderer();

        tiledMap = new TmxMapLoader().load("Map.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        mapProp = tiledMap.getProperties();
        nLevelWidth = mapProp.get("width", Integer.class);
        nLevelHeight = mapProp.get("height", Integer.class);
        nTileSize = mapProp.get("tilewidth", Integer.class);

        objectsSpawnPoints = tiledMap.getLayers().get("SpawnPoint").getObjects();
        objectIterator = objectsSpawnPoints.iterator();
        while (objectIterator.hasNext()) {
            MapObject object = objectIterator.next();
            System.out.println(object.getName());
            if (object.getProperties().get("toSpawn").equals("Hero")) {
                System.out.println("FOUND THE HERO");
                nX = object.getProperties().get("x", float.class).intValue();
                nY = object.getProperties().get("y", float.class).intValue();
            }
            if (object.getProperties().get("toSpawn").equals("Enemy")) {
                System.out.println("FOUND THE BADGUY");
                System.out.println("locX: " + object.getProperties().get("x", float.class));
                System.out.println("locX: " + object.getProperties().get("y", float.class));
            }
        }
        bodyPlayer = createBox(nX, nY, 32, 32, false);  //  bodyPlayer = createBox(120, 180, 32, 32, false);

        TiledObjectUtil.parseTiledObjectLayer(world, tiledMap.getLayers().get("collision").getObjects());
        game = _game;
        batch = new SpriteBatch();

        sprExtHero = new SpriteExtended("pik.png", bodyPlayer.getPosition().x, bodyPlayer.getPosition().y);
    }

    @Override
    public void render(float fDelta) {

        update(fDelta); // to compensate for lag

        // Render
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.render();
        batch.begin();
        batch.draw(sprExtHero.getSprite(), sprExtHero.getX(), sprExtHero.getY());
        batch.end();
        b2dr.render(world, ocCam.combined.scl(fPPM));

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        ocCam.setToOrtho(false, width / fSCALE, height / fSCALE);
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
        tiledMapRenderer.dispose();
        tiledMap.dispose();
        batch.dispose();
    }

    public void update(float fDelta) {
        world.step(1 / 60f, 6, 2);

        inputUpdate(fDelta);
        ocCamUpdate();
        tiledMapRenderer.setView(ocCam);
        batch.setProjectionMatrix(ocCam.combined);
        sprExtHero.setPosition(bodyPlayer.getPosition().x * fPPM - (sprExtHero.getSprite().getWidth() / 2), bodyPlayer.getPosition().y * fPPM - (sprExtHero.getSprite().getHeight() / 2));
    }

    public void inputUpdate(float fDelta) {
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

    public void ocCamUpdate() {
        CameraStyleUtil.lerpToTarget(ocCam, bodyPlayer.getPosition().scl(fPPM));
        float fStartX = ocCam.viewportWidth / 2;
        float fStartY = ocCam.viewportHeight / 2;
        CameraStyleUtil.boundary(ocCam, fStartX, fStartY, nLevelWidth * nTileSize - fStartX * 2, nLevelHeight * nTileSize - fStartY * 2);
    }
// Polyline Source

    public Body createBox(int x, int y, int width, int height, boolean isStatic) {
        Body body;
        BodyDef bdef = new BodyDef();
        if (isStatic) {
            bdef.type = BodyDef.BodyType.StaticBody;
        } else {
            bdef.type = BodyDef.BodyType.DynamicBody;
        }

        bdef.position.set(x / fPPM, y / fPPM);
        bdef.fixedRotation = true;
        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / fPPM, height / 2 / fPPM);

        body.createFixture(shape, 1.0f);
        shape.dispose();
        return body;
    }

    @Override
    public void show() {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void hide() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
