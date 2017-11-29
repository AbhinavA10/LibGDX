//?.? Griffon - AI enemies
package gdx.griffon.screens;

/*
 SOURCES
 - How to make a Tiled Map: https://www.youtube.com/watch?v=qik60F5I6J4
 - http://www.gamefromscratch.com/post/2014/05/01/LibGDX-Tutorial-11-Tiled-Maps-Part-2-Adding-a-character-sprite.aspx
 - http://stackoverflow.com/questions/20063281/libgdx-collision-detection-with-tiledmap
 - Spawn points: https://www.youtube.com/watch?v=iZFdLJdiJe8&feature=youtu.be&t=298
 - Removing a Tile: http://www.java-gaming.org/index.php?topic=35160.0
 - http://gamedev.stackexchange.com/questions/74821/libgdx-how-do-you-remove-a-cell-from-tiledmap
 - Create a basic box2D world: https://www.youtube.com/watch?v=_y1RvNWoRFU
 - Destroying a body upon collision: http://box2d.org/forum/viewtopic.php?t=9724
 - Removing a Tile: http://www.java-gaming.org/index.php?topic=35160.0
 - http://gamedev.stackexchange.com/questions/74821/libgdx-how-do-you-remove-a-cell-from-tiledmap
 */

import com.badlogic.gdx.ApplicationAdapter;
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
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import gdx.griffon.Bullet;
import gdx.griffon.GamGriffon;
import gdx.griffon.SpriteExtended;
import gdx.griffon.tiles.*;
import gdx.griffon.utils.CameraStyleUtil;
import gdx.griffon.utils.ContactListenerUtil;
import gdx.griffon.utils.Lights;

import java.util.ArrayList;
import java.util.Iterator;

import static gdx.griffon.utils.Constants.fPPM;
import static gdx.griffon.utils.Constants.fSCALE;

public class ScrPlay extends ApplicationAdapter implements Screen {

    private OrthographicCamera ocCam;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private TiledMap tiledMap;
    private MapProperties mapProp;
    private Box2DDebugRenderer b2dr;
    public World world;
    private MapObjects objectsSpawnPoints;
    private Iterator<MapObject> objectIterator;
    private int nLevelWidth, nLevelHeight, nTileSize;
    private GamGriffon game;
    private SpriteBatch batch;
    private SpriteExtended sprExtHero;
    private SpriteExtended sprAI1;
    private Lights lights;
    private Platforms platforms;
    private Spikes spikes;
    private Mushrooms mushrooms;
    private boolean bRenderLights = false; // easy for testing the game
    public ArrayList<Bullet> alBullets;
    public ArrayList<SpriteExtended> alsprextEnemies;
    private ArrayList<DestructibleTile> alDestructibleTiles;
    private ArrayList<FallingTile> alFallingTiles;
    private TiledMapTileLayer tiledMapLayerDestructible;

    //------------------------------------ CONSTRUCTOR ----------------------------------------
    public ScrPlay(GamGriffon _game) {
        reset();
        game = _game;
    }

    //------------------------------------ RESET ----------------------------------------
    public void reset() {
        int nX = 0, nY = 0;
        int nXai1 = 0, nYai1 = 0;
        ocCam = new OrthographicCamera();
        ocCam.setToOrtho(false, Gdx.graphics.getWidth() / fSCALE, Gdx.graphics.getHeight() / fSCALE);

        world = new World(new Vector2(0, -9.8f), false);
        world.setContactListener(new ContactListenerUtil(this));
        b2dr = new Box2DDebugRenderer();
        tiledMap = new TmxMapLoader().load("Map.tmx");
        mapProp = tiledMap.getProperties();
        nLevelWidth = mapProp.get("width", Integer.class);
        nLevelHeight = mapProp.get("height", Integer.class);
        nTileSize = mapProp.get("tilewidth", Integer.class);

        alsprextEnemies = new ArrayList<SpriteExtended>();
        objectsSpawnPoints = tiledMap.getLayers().get("SpawnPoint").getObjects();
        objectIterator = objectsSpawnPoints.iterator();
        while (objectIterator.hasNext()) {
            MapObject object = objectIterator.next();
            // System.out.println(object.getName());
            if (object.getProperties().get("toSpawn").equals("Hero")) {
                //System.out.println("FOUND THE HERO");
                nX = object.getProperties().get("x", float.class).intValue();
                nY = object.getProperties().get("y", float.class).intValue();
            }
            if (object.getProperties().get("toSpawn").equals("Enemy")) {
                // System.out.println("FOUND A BADGUY");
                nXai1 = object.getProperties().get("x", float.class).intValue();
                nYai1 = object.getProperties().get("y", float.class).intValue();
                alsprextEnemies.add(new SpriteExtended("Vlad.png", nXai1, nYai1, nXai1, nXai1 + 100, 70, world, this));
            }
        }
        platforms = new Platforms();

        platforms.parseTiledObjectLayer(world, tiledMap.getLayers().get("platforms").getObjects(), true, true, 1.0f, 0);
        spikes = new Spikes();
        spikes.parseTiledObjectLayer(world, tiledMap.getLayers().get("spikes").getObjects(), true, true, 1.0f, 0);
        mushrooms = new Mushrooms();
        mushrooms.parseTiledObjectLayer(world, tiledMap.getLayers().get("Mushrooms").getObjects(), true, true, 1.0f, 0.8f);

        batch = new SpriteBatch();

        sprExtHero = new SpriteExtended("Vlad.png", nX, nY, 10, world, this);
        lights = new Lights(world, sprExtHero.body);
        alBullets = new ArrayList<Bullet>();
        alDestructibleTiles = new ArrayList<DestructibleTile>();
        alFallingTiles = new ArrayList<FallingTile>();

        tiledMapLayerDestructible = (TiledMapTileLayer) tiledMap.getLayers().get("Midground");
        MapObjects mapObjects = tiledMap.getLayers().get("DestructibleTiles").getObjects();
        for (MapObject object : mapObjects) {
            alDestructibleTiles.add(new DestructibleTile(world, object, true, true, 1.0f, 0));
            //System.out.println("TileBody added");
        }
        mapObjects = tiledMap.getLayers().get("FallingTiles").getObjects();
        for (MapObject object : mapObjects) {
            alFallingTiles.add(new FallingTile("fallingtile.png", world, object, false, true, 1.0f, 0, 20));
            // System.out.println("Falling Plat added");
        }

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    //------------------------------------ RENDER ----------------------------------------
    @Override
    public void render(float fDelta) {

        update(fDelta); // to compensate for lag

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.render();
        batch.begin();
        batch.draw(sprExtHero.getSprite(), sprExtHero.getX(), sprExtHero.getY());
        for (SpriteExtended sprExtEnemy : alsprextEnemies) {
            batch.draw(sprExtEnemy.getSprite(), sprExtEnemy.getX(), sprExtEnemy.getY());
        }
        for (Bullet bullet : alBullets) {
            batch.draw(bullet.getSprite(), bullet.getX(), bullet.getY());
        }
        for (FallingTile fallingTile : alFallingTiles) {
            batch.draw(fallingTile.getSprite(), fallingTile.getSprite().getX(), fallingTile.getSprite().getY());
        }
        batch.end();
        b2dr.render(world, ocCam.combined.scl(fPPM));
        if (bRenderLights) {
            lights.renderLights();
        }
    }

    //------------------------------------ RESIZE ----------------------------------------
    @Override
    public void resize(int width, int height) {
        ocCam.setToOrtho(false, width / fSCALE, height / fSCALE);
    }

    //------------------------------------ DISPOSE ----------------------------------------
    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
        tiledMapRenderer.dispose();
        tiledMap.dispose();
        batch.dispose();
        lights.dispose();
    }

    //------------------------------------ UPDATE ----------------------------------------
    private void update(float fDelta) {
        if (sprExtHero.bDead) {
            death();
        } else {
            world.step(1 / 60f, 6, 2);
            bulletUpdate();
            platUpdate();
            inputUpdate();
            ocCamUpdate();
            tiledMapRenderer.setView(ocCam);
            batch.setProjectionMatrix(ocCam.combined);
            lights.update(ocCam);
            sprExtHero.setPosition(sprExtHero.body.getPosition().x * fPPM - (sprExtHero.getSprite().getWidth() / 2), sprExtHero.body.getPosition().y * fPPM - (sprExtHero.getSprite().getHeight() / 2));
            enemyUpdate();
        }
    }

    //------------------------------------ INPUT UPDATE ----------------------------------------
    private void inputUpdate() {
        int nHorizontalForce = 0;
        int nMultiplier = 4;
        sprExtHero.isMoving = false;
        sprExtHero.nShootCounter++;
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            bRenderLights = (bRenderLights) ? false : true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            sprExtHero.nDir = 1;
            sprExtHero.nPos = 7;
            nHorizontalForce -= 1;
            lights.inputUpdate(sprExtHero.body, 180);
            sprExtHero.isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            sprExtHero.nDir = 2;
            sprExtHero.nPos = 0;
            nHorizontalForce += 1;
            lights.inputUpdate(sprExtHero.body, 0);
            sprExtHero.isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            nMultiplier = 7;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            sprExtHero.jump();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            sprExtHero.shoot();
        }

        sprExtHero.body.setLinearVelocity(nHorizontalForce * nMultiplier, sprExtHero.body.getLinearVelocity().y);
    }

    //------------------------------------ OCCAM UPDATE ----------------------------------------
    private void ocCamUpdate() {
        CameraStyleUtil.lerpToTarget(ocCam, sprExtHero.body.getPosition().scl(fPPM));
        float fStartX = ocCam.viewportWidth / 2;
        float fStartY = ocCam.viewportHeight / 2;
        CameraStyleUtil.boundary(ocCam, fStartX, fStartY, nLevelWidth * nTileSize - fStartX * 2, nLevelHeight * nTileSize - fStartY * 2);
    }

    //------------------------------------ BULLET UPDATE ----------------------------------------
    private void bulletUpdate() {
        for (int i = alBullets.size() - 1; i >= 0; i--) {
            Bullet bullet = alBullets.get(i);
            if (bullet.bDead) {
                world.destroyBody(bullet.body);
                alBullets.remove(bullet);
            } else {
                bullet.move();
                bullet.setPosition(bullet.body.getPosition().x * fPPM - (bullet.getSprite().getWidth() / 2), bullet.body.getPosition().y * fPPM - (bullet.getSprite().getHeight() / 2));
            }
        }
    }

    //------------------------------------ PLAT UPDATE ----------------------------------------
    private void platUpdate() {
        for (int i = alDestructibleTiles.size() - 1; i >= 0; i--) {
            DestructibleTile destructTile = alDestructibleTiles.get(i);
            // System.out.println(destructTile.bHit);
            // System.out.println(destructTile.body.getPosition());
            if (destructTile.bHit) {
                removeTile(destructTile.body.getPosition());
                world.destroyBody(destructTile.body);
                //System.out.println("TileBody Removed");
                alDestructibleTiles.remove(destructTile);
                //System.out.println("TileBody Removed from al");
            }
        }
        for (FallingTile fallingTile : alFallingTiles) {
            fallingTile.activate(0, -5);
            fallingTile.setPos();
        }
    }

    //------------------------------------ ENEMY UPDATE ----------------------------------------
    private void enemyUpdate() {
        for (int i = alsprextEnemies.size() - 1; i >= 0; i--) {
            SpriteExtended sprextEnemy = alsprextEnemies.get(i);
            sprextEnemy.setPosition(sprextEnemy.body.getPosition().x * fPPM - (sprextEnemy.getSprite().getWidth() / 2), sprextEnemy.body.getPosition().y * fPPM - (sprextEnemy.getSprite().getHeight() / 2));
            sprextEnemy.aiMovement(sprExtHero.body.getPosition().y, sprExtHero.body.getPosition().x);
            if (sprextEnemy.bDead) {
                sprextEnemy.death();
                alsprextEnemies.remove(sprextEnemy);
            }
        }
    }

    //------------------------------------ REMOVE TILE ----------------------------------------
    private void removeTile(Vector2 position) {
        int nX = (int) position.x;
        int nY = (int) position.y;
        // System.out.println("X: " + nX + "   Y: " + nY);
        tiledMapLayerDestructible.setCell(nX, nY, null);
    }

    //------------------------------------ DESTROY BULLET ----------------------------------------
    public void destroyBullet(Bullet bullet) {
        bullet.bDead = true;
    }

    //------------------------------------ DESTROY BULLET TILE ----------------------------------------
    public void destroyBulletTile(Bullet bullet, DestructibleTile _destructibleTile) {
        bullet.bDead = true;
        _destructibleTile.bHit = true;
    }

    //------------------------------------ DEATH ----------------------------------------
    private void death() {
        game.updateState(3);
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
