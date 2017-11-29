//circle objects do not work
/*
SOURCES
- Converting tiled object to box2d shapes http://gamedev.stackexchange.com/questions/66924/how-can-i-convert-a-tilemap-to-a-box2d-world
- Parsing a Tiled file to a Box2d World: https://www.youtube.com/watch?v=BcbjBEnIWKU
*/
package gdx.fourtress.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static gdx.fourtress.utils.Constants.fPPM;

public class TiledObjectUtil {


    public Body body;
    BodyDef bdef;
    FixtureDef fixDef;

    //------------------------------------ CONSTRUCTOR ----------------------------------------
    public TiledObjectUtil() {
    }
    //------------------------------------ PARSE TILED OBJECT LAYER ----------------------------------------
    // Polyline Source
    public void parseTiledObjectLayer(World world, MapObjects objects, boolean bStatic, boolean bFixedRotate, float fDensity, float fRestitution) {
        for (MapObject object : objects) {
            Shape shape;
            if (object instanceof RectangleMapObject) {
                shape = createRectangle((RectangleMapObject) object);
            } else if (object instanceof PolylineMapObject) {
                shape = createPolyline((PolylineMapObject) object);
            } else if (object instanceof CircleMapObject) {
                shape = createCircle((CircleMapObject) object);
            } else {
                continue;
            }
            bdef = new BodyDef();
            if (bStatic) {
                bdef.type = BodyDef.BodyType.StaticBody;
            } else {
                bdef.type = BodyDef.BodyType.KinematicBody;
            }
            bdef.fixedRotation = bFixedRotate;
            body = world.createBody(bdef);

            fixDef = new FixtureDef();
            fixDef.shape = shape;
            fixDef.density = fDensity;
            fixDef.restitution = fRestitution;

            body = world.createBody(bdef);
            body.createFixture(fixDef).setUserData(this);
            shape.dispose();

        }
    }

    //------------------------------------ PARSE TILED OBJECT ----------------------------------------
    public void parseTiledObject(World world, MapObject object, boolean bStatic, boolean bFixedRotate, float fDensity, float fRestitution) {
        Shape shape = new Shape() {
            @Override
            public Type getType() {
                return null;
            }
        };
        boolean bSkip = true;
        if (object instanceof RectangleMapObject) {
            shape = createRectangle((RectangleMapObject) object);
            bSkip = false;
        } else if (object instanceof PolylineMapObject) {
            shape = createPolyline((PolylineMapObject) object);
            bSkip = false;
        } else if (object instanceof CircleMapObject) {
            shape = createCircle((CircleMapObject) object);
            bSkip = false;
        }
        if (!bSkip) {
            bdef = new BodyDef();
            if (bStatic) {
                bdef.type = BodyDef.BodyType.StaticBody;
            } else {
                bdef.type = BodyDef.BodyType.KinematicBody;
            }
            bdef.fixedRotation = bFixedRotate;
            body = world.createBody(bdef);

            fixDef = new FixtureDef();
            fixDef.shape = shape;
            fixDef.density = fDensity;
            fixDef.restitution = fRestitution;

            body = world.createBody(bdef);
            body.createFixture(fixDef).setUserData(this);
            shape.dispose();
        }
    }

    //------------------------------------ CREATE POLYLINE ----------------------------------------
    private ChainShape createPolyline(PolylineMapObject polyline) {
        float[] arfVertices = polyline.getPolyline().getTransformedVertices();
        Vector2[] arvec2WorldVertices = new Vector2[arfVertices.length / 2];
        for (int i = 0; i < arvec2WorldVertices.length; i++) {
            arvec2WorldVertices[i] = new Vector2(arfVertices[i * 2] / fPPM, arfVertices[i * 2 + 1] / fPPM);
        }
        ChainShape cs = new ChainShape();
        cs.createChain(arvec2WorldVertices);
        return cs;
    }


    //------------------------------------ CREATE RECTANGLE ----------------------------------------
    // from game dev source
    private PolygonShape createRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 vec2Size = new Vector2((rectangle.x + rectangle.width * 0.5f) / fPPM,
                (rectangle.y + rectangle.height * 0.5f) / fPPM);
        polygon.setAsBox(rectangle.width * 0.5f / fPPM,
                rectangle.height * 0.5f / fPPM,
                vec2Size,
                0.0f);
        return polygon;
    }

    //------------------------------------ CREATE CIRCLE ----------------------------------------
    private CircleShape createCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / fPPM);
        circleShape.setPosition(new Vector2(circle.x / fPPM, circle.y / fPPM));
        return circleShape;
    }
}
