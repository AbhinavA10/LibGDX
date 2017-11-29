package gdx.gravitydetection;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class GravityDetection extends ApplicationAdapter implements InputProcessor {

    SpriteBatch batch;
    Sprite2 sprVlad;
    Sprite sprWall;
    Texture txSheet, txTemp, txOne, txWall, txHero;
    Animation araniVlad[];
    TextureRegion trTemp; // a single temporary texture region
    Array<Sprite> arsprWall;
    int fW, fH, fSx, fSy; // height and width of SpriteSheet image - and the starting upper coordinates on the Sprite Sheet
    int nFrame, nPos;
    int nX, nY, nXWall, nYWall;
    int nSpeed;
    int nUpDown, nLeftRight;
    float fTime;
    boolean canJump;
    double dVel;
    double dVelLimit = 10;
    double dAccel = 1;

    @Override
    public void create() {
        Gdx.input.setInputProcessor((this));
        fTime = 0;
        nXWall = 300;
        nYWall = 100;
        nSpeed = 5;
        nFrame = 0;
        nPos = 0; // the position in the SpriteSheet - 0 to 7
        araniVlad = new Animation[8];
        batch = new SpriteBatch();
        txSheet = new Texture("Vlad.png");
        txWall = new Texture("Wall.png");
        txHero = new Texture("alGore.png");
        sprWall = new Sprite(txWall);
        sprWall.setPosition(300, 100);
        fW = txHero.getWidth();
        fH = txHero.getHeight();
        System.out.println(fW + " " + fH);

        sprVlad = new Sprite2(txHero, fSx, fSy, fW, fH, 300, 500, 0.5, 0, 8);

        //sprVlad.setPosition(300, 800);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //trTemp = araniVlad[nPos].getKeyFrame(nFrame, true);

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            //nX -= Gdx.graphics.getDeltaTime() * SpriteSpeed;
            //CurrentFrame = animation.getKeyFrame(4 + Time);
            sprVlad.dLastX = sprVlad.dX;
            sprVlad.dX -= nSpeed;

            if (isHit(sprVlad, sprWall)) {
                //sprVlad.dX = nXWall + Math.round(sprWall.getWidth());
                //if (!canJump(sprVlad, sprWall)) { //if you check for this, if you hit the side of the platform canJump sends you to the top of it, if you dont check, you can not slide
                //sprVlad.dX = sprVlad.dLastX;
                sprVlad.dX += nSpeed;
                //}
                //sprVlad.dX = (nXWall + Math.round(sprWall.getWidth()));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            //nX += Gdx.graphics.getDeltaTime() * SpriteSpeed;
            //CurrentFrame = animation.getKeyFrame(8 + Time);
            sprVlad.dLastX = sprVlad.dX;
            sprVlad.dX += nSpeed;

            if (isHit(sprVlad, sprWall)) {
                //nX = nXWall - Math.round(sprVlad.getWidth());
                //sprVlad.dX = (nXWall - Math.round(sprVlad.getWidth()));
                //if (!canJump(sprVlad, sprWall)) {
                //sprVlad.dX = sprVlad.dLastX;
                sprVlad.dX -= nSpeed;
                //}
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            //nY += Gdx.graphics.getDeltaTime() * SpriteSpeed;
            //CurrentFrame = animation.getKeyFrame(12 + Time);
            //nUpDown = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            //nY -= Gdx.graphics.getDeltaTime() * SpriteSpeed;
            //CurrentFrame = animation.getKeyFrame(0 + Time);
            nUpDown = 2;
        }

        //dVel = gravity(sprVlad, sprWall, dVel, dVelLimit, dAccel);
        //sprVlad.gravity(); //may be called in canJump
        //sprVlad.setPosition(Math.round(sprVlad.dX), Math.round(sprVlad.dY));
        System.out.println("y" + sprVlad.getY());
        vJump(sprVlad, sprWall);
        System.out.println(sprVlad.getY());
        batch.begin();
        batch.draw(txHero, Math.round((float) sprVlad.dX), Math.round((float) sprVlad.dY));
        batch.draw(sprWall, sprWall.getX(), sprWall.getY());
        batch.end();

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.W) {
            if (sprVlad.nNumJumps > 0) {
                sprVlad.jump();
            }
        }
        /*
         if (keycode == Keys.A) {
         nLeftRight = 1;
         } else if (keycode == Keys.D) {
         nLeftRight = 2;
         }*/
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        nFrame = 0;
        if (keycode == Keys.A || keycode == Keys.D) {
            nLeftRight = 0;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        sprVlad.reset();
        return false;
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

    @Override
    public void dispose() {
        txHero.dispose();
        txWall.dispose();
        batch.dispose();
    }

    public static void setXY(Sprite spr, int nX, int nY) {
        spr.setX(nX);
        spr.setY(nY);
    }

    /*public static double gravity(Sprite spr, Sprite sprWall, double dVel, double dVelLimit, double dAccel) {
     dVel += dAccel;
     if (dVel >= dVelLimit) {
     dVel = dVelLimit;
     }
     spr.setY(spr.getY() - Math.round(dVel));

     //spr.setY(spr.getY() - 1);

     if (isHit(spr, sprWall)) {
     if (spr.getY() > sprWall.getY()) {
     spr.setY(sprWall.getY() + sprWall.getHeight());
     dVel = 0;
                
     }
     }
        
     System.out.println(dVel);
     return dVel;
     }*/

    /*public static double jump(Sprite spr, double dVel) {
     dVel = -7;
     return dVel;
     }*/
    public static void vJump(Sprite2 spr, Sprite sprWall) {
        spr.gravity(); //calling gravity here makes hit detecting work, an issue is if someone presses "w" to jump
        if (isHit(spr, sprWall)) {
            if (spr.dY > sprWall.getY()) {
                spr.dY = sprWall.getHeight() + sprWall.getY();
                spr.dVel = 0;
                spr.nNumJumps = 2; // 1 lets them jump once, two lets them double jump, etc
            } else if (spr.dY <= sprWall.getY()) {
                spr.dY = sprWall.getY() - spr.fH;
                spr.dVel = 0;
            }
            //System.out.println("is hit");
        }
    }

    public static boolean isHit(Sprite2 spr1, Sprite spr2) {
        setXY(spr1, Math.round(spr1.getX()), Math.round(spr1.getY()));
        setXY(spr2, Math.round(spr2.getX()), Math.round(spr2.getY()));
        //System.out.println(spr1.retRect());
        //System.out.println(spr2.getBoundingRectangle());
        return spr1.retRect().overlaps(spr2.getBoundingRectangle()); //System.out.println("is hit");
    }
}
