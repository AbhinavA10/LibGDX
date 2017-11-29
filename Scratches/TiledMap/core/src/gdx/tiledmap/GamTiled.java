package gdx.tiledmap;
//http://www.gamefromscratch.com/post/2014/05/01/LibGDX-Tutorial-11-Tiled-Maps-Part-2-Adding-a-character-sprite.aspx
//http://stackoverflow.com/questions/20063281/libgdx-collision-detection-with-tiledmap
//Spawn points: https://www.youtube.com/watch?v=iZFdLJdiJe8&feature=youtu.be&t=298
//Polyline stuff, with box2d: https://www.youtube.com/watch?v=BcbjBEnIWKU
//polygon and rect, w/o box2d http://stackoverflow.com/questions/28522313/java-libgdx-how-to-check-polygon-collision-with-rectangle-or-circle
/* need to find a way to check intersection, but the isCollision function never seems to work.
 box2D just keeps getting more pros
 */

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import java.util.Iterator;

public class GamTiled extends ApplicationAdapter implements InputProcessor {

    Texture img;
    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
    SpriteBatch sb;
    Texture texture;
    Sprite sprHero;
    MapLayer collisionObjectLayer;
    MapObjects objectsCollision;
    MapLayer spawnObjectLayer;
    MapObjects objectsSpawnPoints;
    Iterator<MapObject> objectIterator;
    int nHitCount;

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();
        tiledMap = new TmxMapLoader().load("MyMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        Gdx.input.setInputProcessor(this);

        sb = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("pik.png"));
        sprHero = new Sprite(texture);
        // Spawn Points Source;
        collisionObjectLayer = tiledMap.getLayers().get("collision");
        objectsCollision = collisionObjectLayer.getObjects();
        spawnObjectLayer = tiledMap.getLayers().get("SpawnPoint");
        objectsSpawnPoints = spawnObjectLayer.getObjects();
        objectIterator = objectsSpawnPoints.iterator();

        while (objectIterator.hasNext()) {
            MapObject object = objectIterator.next();
            System.out.println(object.getName());
            if (object.getProperties().get("toSpawn").equals("Hero")) {
                System.out.println("FOUND THE HERO");
                sprHero.setX(object.getProperties().get("x", float.class));
                sprHero.setY(object.getProperties().get("y", float.class));
            }
            if (object.getProperties().get("toSpawn").equals("Enemy")) {
                System.out.println("FOUND THE BADGUY");
                System.out.println("locX: " + object.getProperties().get("x", float.class));
                System.out.println("locX: " + object.getProperties().get("y", float.class));
            }
        }

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(sprHero.getTexture(), sprHero.getX(), sprHero.getY());
        sb.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.LEFT) {
            camera.translate(-32, 0);
        }
        if (keycode == Input.Keys.RIGHT) {
            camera.translate(32, 0);
        }
        if (keycode == Input.Keys.UP) {
            camera.translate(0, 32);
        }
        if (keycode == Input.Keys.DOWN) {
            camera.translate(0, -32);
        }
        if (keycode == Input.Keys.NUM_1) {
            tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
        }
        if (keycode == Input.Keys.NUM_2) {
            tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
        }
        if (keycode == Input.Keys.NUM_3) {
            tiledMap.getLayers().get(2).setVisible(!tiledMap.getLayers().get(2).isVisible());
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
        Vector3 position = camera.unproject(clickCoordinates);
        position.x -= sprHero.getWidth() / 2;
        position.y -= sprHero.getHeight() / 2;
        sprHero.setPosition(position.x, position.y);
        for (RectangleMapObject rectangleObject : objectsCollision.getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if (Intersector.overlaps(rectangle, sprHero.getBoundingRectangle())) {
                System.out.println("Rect isHIT-MY-DUDE " + nHitCount);
                nHitCount++;
                sprHero.setPosition((rectangle.x - sprHero.getWidth()) - 1, rectangle.y + 1);
            }
        }
        for (PolygonMapObject polygonObj : objectsCollision.getByType(PolygonMapObject.class)) {
            Polygon polygon = polygonObj.getPolygon();
            if (isCollision(polygon, sprHero.getBoundingRectangle())) {
                System.out.println("GON isHIT-MY-DUDE " + nHitCount);
                nHitCount++;
                //sprHero.setPosition((rectangle.x - sprHero.getWidth()) - 1, rectangle.y + 1);
            }
            System.out.println("found a 'gon");
        }
        for (PolylineMapObject polyineObject : objectsCollision.getByType(PolylineMapObject.class)) {
            Polyline polyine = polyineObject.getPolyline();

            System.out.println("found a polyline");
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    // Polyline Source, needs Box2D
    /*public void parseTiledObject(MapObjects objects) {
     for (MapObject object : objects) {
     if (object instanceof PolylineMapObject) {

     }
     }
     }*/
    // Check if Polygon intersects Rectangle
    // last source
    private boolean isCollision(Polygon p, Rectangle r) {
        Polygon rPoly = new Polygon(new float[]{0, 0, r.width, 0, r.width, r.height, 0, r.height});
        rPoly.setPosition(r.x, r.y);
        if (Intersector.overlapConvexPolygons(rPoly, p)) {

            return true;
        }
        return false;
    }
}
