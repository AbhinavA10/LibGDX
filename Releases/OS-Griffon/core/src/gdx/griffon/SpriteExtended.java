package gdx.griffon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import gdx.griffon.screens.ScrPlay;

import static gdx.griffon.utils.Constants.fPPM;

public class SpriteExtended extends Sprite {

    private int nNumJumps = 2;
    private World world;
    private float fX, fY; // just for original spawn points, or last alive point when we reset for deaths
    private String sFile;
    private Texture txImg;
    private Sprite sprImg;
    private Animation araniPlayer[];
    private Sprite sprPlayer, spr;
    private float fSx, fSy, fW, fH;
    private Texture txSheet;
    private TextureRegion trTemp;
    private int nFrame;
    private float fLeft, fRight;
    private ScrPlay scrPlay;
    private int nShootDelay;
    public Body body;
    public int nDir, nPos; // nDir: 1 is Left, 2 is Right; nPos: 7 is left, 0 is right
    public int nShootCounter = 0;
    public boolean bDead = false;
    public boolean isMoving;

    //------------------------------------ HERO'S CONSTRUCTOR ----------------------------------------
    public SpriteExtended(String _sFile, float _fX, float _fY, int _nShootDelay, World _world, ScrPlay _scrPlay) {
        world = _world;
        createBoxBody(world, _fX, _fY);
        scrPlay = _scrPlay;
        nShootDelay = _nShootDelay;
        nPos = 0;
        nFrame = 0;
        sFile = _sFile;
        fX = body.getPosition().x * fPPM;
        fY = body.getPosition().y * fPPM;
        txImg = new Texture(sFile);
        txSheet = new Texture(sFile);
        sprImg = new Sprite(txImg);
        araniPlayer = new Animation[8];
        fW = txSheet.getWidth() / 8;
        fH = txSheet.getHeight() / 8;
        for (int i = 0; i < 8; i++) {
            Sprite[] arSprPlayer = new Sprite[8];
            for (int j = 0; j < 8; j++) {
                fSx = j * fW;
                fSy = i * fH;
                sprPlayer = new Sprite(txSheet, Math.round(fSx), Math.round(fSy), Math.round(fW), Math.round(fH));
                arSprPlayer[j] = new Sprite(sprPlayer);
            }
            araniPlayer[i] = new Animation(10f, arSprPlayer);
        }
    }

    //------------------------------------ ENEMY'S CONSTRUCTOR ----------------------------------------
    public SpriteExtended(String _sFile, float _fX, float _fY, float _fLeft, float _fRight, int _nShootDelay, World _world, ScrPlay _scrPlay) {
        world = _world;
        createBoxBody(world, _fX, _fY);
        fX = _fX;
        fY = _fY;
        scrPlay = _scrPlay;
        nShootDelay = _nShootDelay;
        isMoving = true;
        nPos = 0;
        nFrame = 0;
        sFile = _sFile;
        txImg = new Texture(sFile);
        txSheet = new Texture(sFile);
        sprImg = new Sprite(txImg);
        araniPlayer = new Animation[8];
        fW = txSheet.getWidth() / 8;
        fH = txSheet.getHeight() / 8;
        fLeft = _fLeft / fPPM; // max left
        fRight = _fRight / fPPM; // max right
        for (int i = 0; i < 8; i++) {
            Sprite[] arSprPlayer = new Sprite[8];
            for (int j = 0; j < 8; j++) {
                fSx = j * fW;
                fSy = i * fH;
                sprPlayer = new Sprite(txSheet, Math.round(fSx), Math.round(fSy), Math.round(fW), Math.round(fH));
                arSprPlayer[j] = new Sprite(sprPlayer);
            }
            araniPlayer[i] = new Animation(10f, arSprPlayer);
        }
    }

    //------------------------------------ DEATH ----------------------------------------
    public void death() {
        this.bDead = false;
        world.destroyBody(body);
        //reset();
    }

    public void shoot() {
        if (nShootCounter >= nShootDelay) {
            if (this.nDir == 1) {
                scrPlay.alBullets.add(new Bullet("bullet.png", this.getX(), this.getY() + this.getSprite().getHeight() / 2, world, this.nDir));
            } else if (this.nDir == 2) {
                scrPlay.alBullets.add(new Bullet("bullet.png", this.getX() + this.getSprite().getWidth(), this.getY() + this.getSprite().getHeight() / 2, world, this.nDir));
            }
            nShootCounter = 0;
        }
    }

    //------------------------------------ AI SHOOTING ----------------------------------------
    public boolean aiShooting(float fPlayersY, float fPlayersX, int nDir) {
        /*  System.out.println("playerY" + fPlayersY);
         System.out.println("playerX" + fPlayersX);*/
        nShootCounter++;
        if (fPlayersY < body.getPosition().y + 1 && fPlayersY > body.getPosition().y - 1 && fPlayersX > body.getPosition().x - 5 && fPlayersX < body.getPosition().x + 5) {
            shoot();
            return true;
        }

        return false;
    }

    //------------------------------------ AI MOVEMENT ----------------------------------------
    public void aiMovement(float fPlayersY, float fPlayersX) {

        if (aiShooting(fPlayersY, fPlayersX, nDir)) {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
            if (fPlayersX < body.getPosition().x) {
                nDir = 1;
                nPos = 7;
                nFrame = 0;
                //System.out.println("Shoot to the left");

            } else {
                nDir = 2;
                nPos = 0;
                nFrame = 0;
                //System.out.println("Shoot to the right");

            }
            animate();
        } else {
            animate();
            if (body.getPosition().x <= fLeft) {
                nPos = 0;
                nDir = 2;
            } else if (body.getPosition().x >= fRight) {
                nPos = 7;
                nDir = 1;
            }

            if (nDir == 1) {
                body.setLinearVelocity(-3, body.getLinearVelocity().y);
            } else {
                body.setLinearVelocity(3, body.getLinearVelocity().y);
            }
        }/*
                  System.out.println("fLeft" + fLeft);
         System.out.println("fRight" + fRight);
        System.out.println("fX " + body.getPosition().x);
        System.out.println("fPlayersX " + fPlayersX);*/
    }

    //------------------------------------ JUMP ----------------------------------------
    public void jump() {
        if (this.nNumJumps > 0) {
            body.applyForceToCenter(0, 225, false);
            nNumJumps--;
        }
    }

    //------------------------------------ RESET JUMP ----------------------------------------
    public void resetJump() {
        nNumJumps = 2;
    }

    //------------------------------------ ANIMATE ----------------------------------------
    private void animate() {
        if (isMoving) {
            nFrame++;
        } else {
            nFrame = 0;
        }
        trTemp = araniPlayer[nPos].getKeyFrame(nFrame, true);
    }

    //------------------------------------ GET SPRITE ----------------------------------------
    public Sprite getSprite() {
        animate();
        spr = new Sprite(trTemp);
        return spr;
    }

    //------------------------------------ CREATE BOX BODY ----------------------------------------
    private void createBoxBody(World world, float x, float y) {
        BodyDef bDef = new BodyDef();
        bDef.fixedRotation = true;
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(x / fPPM, y / fPPM);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(26 / 2 / fPPM, 30 / 2 / fPPM);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 1.0f;

        body = world.createBody(bDef);
        body.createFixture(fixDef).setUserData(this);
        shape.dispose();
    }
}
