package gdx.frosch.tiles;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import gdx.frosch.utils.TiledObjectUtil;

import static gdx.frosch.utils.Constants.fPPM;

public class DestructibleTile extends TiledObjectUtil {
    public boolean bDead;

    public DestructibleTile(World world, MapObject object, boolean bStatic, boolean bFixedRotate, float fDensity, float fRestitution) {
        super();
        parseTiledObject(world, object, bStatic, bFixedRotate, fDensity, fRestitution);
        bDead = false;
    }
}
