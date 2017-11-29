// converting tiled object to box2d shapes http://gamedev.stackexchange.com/questions/66924/how-can-i-convert-a-tilemap-to-a-box2d-world
//Parsing a Tiled file to a Box2d World: https://www.youtube.com/watch?v=BcbjBEnIWKU
//circle objects do not work
package gdx.bullets.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import static gdx.bullets.screens.ScrPlay.fPPM;

public class TiledObjectUtil {

    public TiledObjectUtil() {
    }

    // Polyline Source
    public void parseTiledObjectLayer(World world, MapObjects objects) {
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

            Body body;
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.fixedRotation = true;
            body = world.createBody(bdef);

            FixtureDef fixDef = new FixtureDef();
            fixDef.shape = shape;
            fixDef.density = 1.0f;

            body = world.createBody(bdef);
            body.createFixture(fixDef).setUserData(this);
            shape.dispose();

        }
    }

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
// game dev source

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

    private CircleShape createCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / fPPM);
        circleShape.setPosition(new Vector2(circle.x / fPPM, circle.y / fPPM));
        return circleShape;
    }
}
