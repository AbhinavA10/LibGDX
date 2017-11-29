package gdx.griffon.tiles;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.World;
import gdx.griffon.utils.TiledObjectUtil;

public class DestructibleTile extends TiledObjectUtil {
    public boolean bHit;

    public DestructibleTile(World world, MapObject object, boolean bStatic, boolean bFixedRotate, float fDensity, float fRestitution) {
        super();
        parseTiledObject(world, object, bStatic, bFixedRotate, fDensity, fRestitution);
        bHit = false;
    }
}
