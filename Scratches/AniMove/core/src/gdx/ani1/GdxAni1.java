package gdx.ani1;

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

public class GdxAni1 extends ApplicationAdapter implements InputProcessor {

    SpriteBatch batch;
    Sprite sprVlad;
    Texture txSheet, txTemp, txOne;
    Animation araniVlad[];
    TextureRegion trTemp; // a single temporary texture region
    int fW, fH, fSx, fSy; // height and width of SpriteSheet image - and the starting upper coordinates on the Sprite Sheet
    int nFrame, nPos;
    int nX, nY;
    int nSpeed;
    int nUpDown, nLeftRight;

    @Override
    public void create() {
        Gdx.input.setInputProcessor((this));
        nX = 100;
        nY = 100;
        nSpeed = 3;
        nFrame = 0;
        nPos = 0; // the position in the SpriteSheet - 0 to 7
        araniVlad = new Animation[8];
        batch = new SpriteBatch();
        txSheet = new Texture("Vlad.png");
        fW = txSheet.getWidth() / 8;
        fH = txSheet.getHeight() / 8;
        System.out.println(fW + " " + fH);
        for (int i = 0; i < 8; i++) {
            Sprite[] arSprVlad = new Sprite[8];
            for (int j = 0; j < 8; j++) {
                fSx = j * fW;
                fSy = i * fH;
                sprVlad = new Sprite(txSheet, fSx, fSy, fW, fH);
                arSprVlad[j] = new Sprite(sprVlad);
            }
            araniVlad[i] = new Animation(2f, arSprVlad);

        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        System.out.println(nPos + " " + nFrame);
        trTemp = araniVlad[nPos].getKeyFrame(nFrame, true);

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
           // nX -= Gdx.graphics.getDeltaTime() * SpriteSpeed;
            //CurrentFrame = animation.getKeyFrame(4 + Time);
            nLeftRight = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            //nX += Gdx.graphics.getDeltaTime() * SpriteSpeed;
            //CurrentFrame = animation.getKeyFrame(8 + Time);
            nLeftRight = 2;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            //nY += Gdx.graphics.getDeltaTime() * SpriteSpeed;
            //CurrentFrame = animation.getKeyFrame(12 + Time);
            nUpDown = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            //nY -= Gdx.graphics.getDeltaTime() * SpriteSpeed;
            //CurrentFrame = animation.getKeyFrame(0 + Time);
            nUpDown = 2;
        }
        if (nUpDown == 1) {
            nY += nSpeed;
            nPos = 1;
        } else if (nUpDown == 2) {
            nY -= nSpeed;
            nPos = 4;
        }

        if (nLeftRight == 1) {
            nX -= nSpeed;
            nPos = 7;
        } else if (nLeftRight == 2) {
            nX += nSpeed;
            nPos = 0;
        }

        if (nLeftRight == 1 && nUpDown == 1) {
            nPos = 3;
        } else if (nLeftRight == 1 && nUpDown == 2) {
            nPos = 6;
        } else if (nLeftRight == 2 && nUpDown == 1) {
            nPos = 2;
        } else if (nLeftRight == 2 && nUpDown == 2) {
            nPos = 5;
        }

        if (nLeftRight > 0 || nUpDown > 0) {
            nFrame++;
            if (nFrame > 7) {
                nFrame = 0;
            }
        }

        batch.begin();
        batch.draw(trTemp, nX, nY);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        /*if (keycode == Keys.W) {
         nUpDown = 1;
         } else if (keycode == Keys.S) {
         nUpDown = 2;
         }

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
        if (keycode == Keys.W || keycode == Keys.S) {
            nUpDown = 0;
        }
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
}
