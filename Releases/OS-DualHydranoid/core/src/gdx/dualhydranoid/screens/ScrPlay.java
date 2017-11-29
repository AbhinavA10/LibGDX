//?.?? Dual Hydranoid - Shooting
// - bullets don't work properly yet (moving), but they get removed.
package gdx.dualhydranoid.screens;
//-http://www.gamefromscratch.com/post/2014/05/01/LibGDX-Tutorial-11-Tiled-Maps-Part-2-Adding-a-character-sprite.aspx
//-http://stackoverflow.com/questions/20063281/libgdx-collision-detection-with-tiledmap
//-Spawn points: https://www.youtube.com/watch?v=iZFdLJdiJe8&feature=youtu.be&t=298
//-Create a basic world: https://www.youtube.com/watch?v=_y1RvNWoRFU

import gdx.dualhydranoid.*;
import gdx.dualhydranoid.utils.CameraStyleUtil;
import gdx.dualhydranoid.utils.Lights;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import gdx.dualhydranoid.utils.ContactListenerUtil;

import java.util.ArrayList;
import java.util.Iterator;

import static gdx.dualhydranoid.utils.Constants.fPPM;
import static gdx.dualhydranoid.utils.Constants.fSCALE;

public class ScrPlay extends ApplicationAdapter implements Screen {

    private OrthographicCamera ocCam;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    TiledMap tiledMap;
    MapProperties mapProp;
    Box2DDebugRenderer b2dr;
    private World world;
    MapObjects objectsSpawnPoints;
    Iterator<MapObject> objectIterator;
    int nLevelWidth, nLevelHeight, nTileSize;
    Game game;
    SpriteBatch batch;
    SpriteExtended sprExtHero;
    double dZoom; // for camera, eventually
    private Lights lights;
    private Platforms platforms;
    private Spikes spikes;
    private Mushrooms mushrooms;
    private boolean bRenderLights = false;
    static public boolean bDead = false;
    private ArrayList<Bullet> alBullets;
    private ArrayList<Bullet> alDeadBullets;

    //@Override
    public ScrPlay(Game _game) {
        int nX = 0, nY = 0;
        ocCam = new OrthographicCamera();
        ocCam.setToOrtho(false, Gdx.graphics.getWidth() / fSCALE, Gdx.graphics.getHeight() / fSCALE);

        world = new World(new Vector2(0, -9.8f), false);
        world.setContactListener(new ContactListenerUtil(this));
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
                //System.out.println("FOUND THE HERO");
                nX = object.getProperties().get("x", float.class).intValue();
                nY = object.getProperties().get("y", float.class).intValue();
            }
            /*if (object.getProperties().get("toSpawn").equals("Enemy")) {
             System.out.println("FOUND THE BADGUY");
             System.out.println("locX: " + object.getProperties().get("x", float.class));
             System.out.println("locX: " + object.getProperties().get("y", float.class));
             }*/
        }
        platforms = new Platforms();
        //boolean bStatic, boolean bFixedRotate, float fDensity, float fRestitution) {
        platforms.parseTiledObjectLayer(world, tiledMap.getLayers().get("platforms").getObjects(), true, true, 1.0f, 0);
        spikes = new Spikes();
        spikes.parseTiledObjectLayer(world, tiledMap.getLayers().get("spikes").getObjects(), true, true, 1.0f, 0);
        mushrooms = new Mushrooms();
        mushrooms.parseTiledObjectLayer(world, tiledMap.getLayers().get("Mushrooms").getObjects(), true, true, 1.0f, 0.8f);
        game = _game;
        batch = new SpriteBatch();

        sprExtHero = new SpriteExtended("Vlad.png", nX, nY, world);
        lights = new Lights(world, sprExtHero.body);
        alBullets = new ArrayList<Bullet>();
        alDeadBullets = new ArrayList<Bullet>();
    }

    @Override
    public void render(float fDelta) {

        update(fDelta); // to compensate for lag

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.render();
        batch.begin();
        batch.draw(sprExtHero.getSprite(), sprExtHero.getX(), sprExtHero.getY());
        for (Bullet bullet : alBullets) {
            batch.draw(bullet.getSprite(), bullet.getX(), bullet.getY());
        }
        batch.end();
        b2dr.render(world, ocCam.combined.scl(fPPM));
        if (bRenderLights) {
            lights.renderLights();
        }
        // System.out.println(sprExtHero.body.getPosition().scl(fPPM));
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
        lights.dispose();
        /* Objects that can be disposed
         AssetManager, AsyncExecutor, BaseShader, BatchTiledMapRenderer, 
         BillboardRenderer, BitmapFont, Box2DDebugRenderer, CameraGroupStrategy,
         ColorInfluencer, ColorInfluencer.Random, ColorInfluencer.Single, 
         CpuSpriteBatch, Cubemap, DecalBatch, DefaultShader, DepthShader, 
         DirectionalShadowLight, DistanceFieldFont, DynamicsInfluencer, 
         DynamicsModifier, DynamicsModifier.Angular, DynamicsModifier.BrownianAcceleration
         DynamicsModifier.CentripetalAcceleration, DynamicsModifier.FaceDirection, 
         DynamicsModifier.PolarAcceleration, DynamicsModifier.Rotational2D, 
         DynamicsModifier.Rotational3D, DynamicsModifier.Strength, 
         DynamicsModifier.TangentialAcceleration, Emitter, ETC1.ETC1Data, 
         FloatFrameBuffer, FrameBuffer, FrameBufferCubemap, Gdx2DPixmap, 
         GLFrameBuffer, GLTexture, HexagonalTiledMapRenderer, IndexArray, 
         IndexBufferObject, IndexBufferObjectSubData, Influencer, 
         IsometricStaggeredTiledMapRenderer, IsometricTiledMapRenderer, Map, Mesh, 
         Model, ModelBatch, ModelCache, ModelCache.SimpleMeshPool, ModelCache.TightMeshPool, 
         ModelInfluencer, ModelInfluencer.Random, ModelInfluencer.Single, 
         ModelInstanceRenderer, NetJavaServerSocketImpl, NetJavaSocketImpl, 
         OrthoCachedTiledMapRenderer, OrthogonalTiledMapRenderer, 
         ParticleControllerComponent, ParticleControllerControllerRenderer, 
         ParticleControllerFinalizerInfluencer, ParticleControllerInfluencer, 
         ParticleControllerInfluencer.Random, ParticleControllerInfluencer.Single, 
         ParticleControllerRenderer, ParticleEffect, ParticleEffect, 
         ParticleEffectPool.PooledEffect, ParticleShader, Pixmap, PixmapIO.PNG, 
         PixmapPacker, PointSpriteRenderer, PolygonSpriteBatch, RegionInfluencer, 
         RegionInfluencer.Animated, RegionInfluencer.Random, RegionInfluencer.Single, 
         RegularEmitter, ScaleInfluencer, ShaderProgram, ShapeCache, ShapeRenderer, 
         SimpleInfluencer, Skin, SpawnInfluencer, SpriteBatch, SpriteCache, Stage, 
         Texture, TextureArray, TextureAtlas, TiledMap, VertexArray, VertexBufferObject, 
         VertexBufferObjectSubData, VertexBufferObjectWithVAO, World
         */
    }

    public void update(float fDelta) {
        if (!bDead) {
            world.step(1 / 60f, 6, 2);
            bulletUpdate();
            inputUpdate();
            ocCamUpdate();
            tiledMapRenderer.setView(ocCam);
            batch.setProjectionMatrix(ocCam.combined);
            lights.update(ocCam);
            sprExtHero.setPosition(sprExtHero.body.getPosition().x * fPPM - (sprExtHero.getSprite().getWidth() / 2), sprExtHero.body.getPosition().y * fPPM - (sprExtHero.getSprite().getHeight() / 2));
        } else {
            death();
        }
    }

    public void inputUpdate() {
        int nHorizontalForce = 0;
        int nMultiplier = 4;
        sprExtHero.isMoving = false;
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
            nMultiplier = 9;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            if (sprExtHero.nNumJumps > 0) {
                sprExtHero.body.applyForceToCenter(0, 300, false);
                sprExtHero.jump();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (sprExtHero.nDir == 1) {
                alBullets.add(new Bullet("bullet.png", sprExtHero.getX(), sprExtHero.getY() + sprExtHero.getSprite().getHeight() / 2, world, sprExtHero.nDir));
            } else if (sprExtHero.nDir == 2) {
                alBullets.add(new Bullet("bullet.png", sprExtHero.getX() + sprExtHero.getSprite().getWidth(), sprExtHero.getY() + sprExtHero.getSprite().getHeight() / 2, world, sprExtHero.nDir));
            }
        }

        sprExtHero.body.setLinearVelocity(nHorizontalForce * nMultiplier, sprExtHero.body.getLinearVelocity().y);
    }

    public void ocCamUpdate() {
        CameraStyleUtil.lerpToTarget(ocCam, sprExtHero.body.getPosition().scl(fPPM));
        float fStartX = ocCam.viewportWidth / 2;
        float fStartY = ocCam.viewportHeight / 2;
        CameraStyleUtil.boundary(ocCam, fStartX, fStartY, nLevelWidth * nTileSize - fStartX * 2, nLevelHeight * nTileSize - fStartY * 2);
    }

    public void bulletUpdate() {
        for (int i = alDeadBullets.size() - 1; i >= 0; i--) {
            Bullet bullet = alDeadBullets.get(i);
            world.destroyBody(bullet.body);
          //  System.out.println("Bullet removed");
        }
        alDeadBullets.clear();
        for (Bullet bullet : alBullets) {
            bullet.move();
            bullet.setPosition(bullet.body.getPosition().x * fPPM - (bullet.getSprite().getWidth() / 2), bullet.body.getPosition().y * fPPM - (bullet.getSprite().getHeight() / 2));

        }
    }

    public void destroy(Bullet bullet, Platforms platform) {
        if (!alDeadBullets.contains(bullet)) {
            alDeadBullets.add(bullet);
            alBullets.remove(bullet);
        }/*
         if (!alDestroyBoxes.contains(platform)) {
         alDestroyBoxes.add(platform);
         alBoxes.remove(platform);
         }*/
    }

    @Override
    public void show() {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void hide() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void death() {
        sprExtHero.death(world);
        bDead = false;
    }
}
