package gdx.frosch.tiles;
/*
* SOURCES
* Kinematic Bodies, for falling platforms: http://www.emanueleferonato.com/2012/05/11/understanding-box2d-kinematic-bodies/
*/

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.World;
import static gdx.frosch.utils.Constants.fPPM;
import gdx.frosch.utils.TiledObjectUtil;

public class FallingTile extends TiledObjectUtil {
    public boolean bHit;
    private int nCount = 0, nDelay = 0;
    MapObject object;
    boolean bStatic;
    boolean bFixedRotate;
    float fDensity;
    float fRestitution;

    private String sFile;
    private Texture txImg;
    private Sprite sprImg;
    //------------------------------------ CONSTRUCTOR ----------------------------------------
    public FallingTile(String _sFile, World world, MapObject _object, boolean _bStatic, boolean _bFixedRotate, float _fDensity, float _fRestitution, int _nDelay) {
        super();
        object = _object;
        bStatic = _bStatic;
        bFixedRotate = _bFixedRotate;
        fDensity = _fDensity;
        fRestitution = _fRestitution;
        nDelay = _nDelay; // this way we can add different amount of delays
        parseTiledObject(world, object, bStatic, bFixedRotate, fDensity, fRestitution);
        bHit = false;
        sFile=_sFile;
        txImg = new Texture(sFile);
        sprImg = new Sprite(txImg);
    }
//------------------------------------ GET SPRITE ----------------------------------------
    public Sprite getSprite() {
        return sprImg;
    }
    //------------------------------- GET SPRITE ----------------------------------------
    public void setPos() {
        sprImg.setPosition(body.getPosition().x * fPPM - (this.getSprite().getWidth() / 2), body.getPosition().y * fPPM - (this.getSprite().getHeight() / 2));
           
    }
    //------------------------------------ ACTIVATE ----------------------------------------
    public void activate(float x, float y) {
        if (bHit) {
            if (nCount > nDelay && body.getPosition().y > -12) { // just need it to stop moving after it's off the screen
                body.setLinearVelocity(x, y);
            } else {
                nCount++;
                body.setLinearVelocity(0, 0);
            }

        }
    }//------------------------------------ DEATH ----------------------------------------

    public void death(World world) {
        reset(world);
    }

    //------------------------------------ RESET ----------------------------------------
    private void reset(World world) {
        world.destroyBody(body);
        parseTiledObject(world, object, bStatic, bFixedRotate, fDensity, fRestitution);
        body.setLinearVelocity(0, 0);
        nCount = 0;
        bHit = false;
    }
}
