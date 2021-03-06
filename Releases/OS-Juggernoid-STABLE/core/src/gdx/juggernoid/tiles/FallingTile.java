package gdx.juggernoid.tiles;
/*
* SOURCES
* Kinematic Bodies, for falling platforms: http://www.emanueleferonato.com/2012/05/11/understanding-box2d-kinematic-bodies/
*/

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.World;
import gdx.juggernoid.utils.TiledObjectUtil;

import static gdx.juggernoid.utils.Constants.fPPM;

public class FallingTile extends TiledObjectUtil {
    public boolean bHit;
    private int nCount = 0, nDelay = 0; // nCount is used to delay the fall after it's hit
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
        sFile = _sFile;
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
            if (nCount > nDelay && body.getPosition().y > -12) { // just need it to stop moving after it's off the screen, because that's pointless
                body.setLinearVelocity(x, y);
            } else {
                nCount++;
                body.setLinearVelocity(0, 0);
            }

        }
    }
}
