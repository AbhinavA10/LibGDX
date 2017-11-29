//2.2 Clayf: proper jumping and ability to run
package gdx.clayf.screens;
//-http://www.gamefromscratch.com/post/2014/05/01/LibGDX-Tutorial-11-Tiled-Maps-Part-2-Adding-a-character-sprite.aspx
//-http://stackoverflow.com/questions/20063281/libgdx-collision-detection-with-tiledmap
//-Spawn points: https://www.youtube.com/watch?v=iZFdLJdiJe8&feature=youtu.be&t=298
//-Create a basic world: https://www.youtube.com/watch?v=_y1RvNWoRFU

import gdx.clayf.utils.TiledObjectUtil;
import gdx.clayf.utils.CameraStyleUtil;
import gdx.clayf.SpriteExtended;
import gdx.clayf.utils.Lights;
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
import gdx.clayf.utils.ContactListenerUtil;

import java.util.Iterator;

public class ScrPlay extends ApplicationAdapter implements Screen {

    private final float fSCALE = 2.0f;
    private OrthographicCamera ocCam;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    TiledMap tiledMap;
    MapProperties mapProp;
    Box2DDebugRenderer b2dr;
    private World world;
    //    private Body bodyPlayer;
    public static final float fPPM = 32; // pixels per meter
    // when giving Box2D units, divide by fPPM
    MapObjects objectsSpawnPoints;
    Iterator<MapObject> objectIterator;
    int nLevelWidth, nLevelHeight, nTileSize;

    Game game;
    SpriteBatch batch;
    SpriteExtended sprExtHero;
    double dZoom; // for camera, eventually
    private Lights lights;
    private TiledObjectUtil tiledObjectUtil;

    //@Override
    public ScrPlay(Game _game) {
        int nX = 0, nY = 0;
        ocCam = new OrthographicCamera();
        ocCam.setToOrtho(false, Gdx.graphics.getWidth() / fSCALE, Gdx.graphics.getHeight() / fSCALE);

        world = new World(new Vector2(0, -9.8f), false);
        world.setContactListener(new ContactListenerUtil());
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
        tiledObjectUtil = new TiledObjectUtil();
        tiledObjectUtil.parseTiledObjectLayer(world, tiledMap.getLayers().get("collision").getObjects());
        game = _game;
        batch = new SpriteBatch();

        sprExtHero = new SpriteExtended("Vlad.png", nX, nY, world);
        lights = new Lights(world, sprExtHero.body);
    }

    @Override
    public void render(float fDelta) {

        update(fDelta); // to compensate for lag

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.render();
        batch.begin();
        batch.draw(sprExtHero.getSprite(), sprExtHero.getX(), sprExtHero.getY());
        batch.end();
        b2dr.render(world, ocCam.combined.scl(fPPM));
        lights.renderLights();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
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
        world.step(1 / 60f, 6, 2);
        inputUpdate(fDelta);
        ocCamUpdate();
        tiledMapRenderer.setView(ocCam);
        batch.setProjectionMatrix(ocCam.combined);
        lights.update(ocCam);
        sprExtHero.setPosition(sprExtHero.body.getPosition().x * fPPM - (sprExtHero.getSprite().getWidth() / 2), sprExtHero.body.getPosition().y * fPPM - (sprExtHero.getSprite().getHeight() / 2));
    }

    public void inputUpdate(float fDelta) {
        int nHorizontalForce = 0;
        int nMultiplier = 4;
        sprExtHero.isMoving = false;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            sprExtHero.nPos = 7;
            nHorizontalForce -= 1;
            lights.nDegrees = 180;
            lights.inputUpdate(sprExtHero.body);
            sprExtHero.isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            sprExtHero.nPos = 0;
            nHorizontalForce += 1;
            lights.nDegrees = 0;
            lights.inputUpdate(sprExtHero.body);
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

        sprExtHero.body.setLinearVelocity(nHorizontalForce * nMultiplier, sprExtHero.body.getLinearVelocity().y);
    }

    public void ocCamUpdate() {
        CameraStyleUtil.lerpToTarget(ocCam, sprExtHero.body.getPosition().scl(fPPM));
        float fStartX = ocCam.viewportWidth / 2;
        float fStartY = ocCam.viewportHeight / 2;
        CameraStyleUtil.boundary(ocCam, fStartX, fStartY, nLevelWidth * nTileSize - fStartX * 2, nLevelHeight * nTileSize - fStartY * 2);
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
