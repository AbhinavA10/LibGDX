//https://github.com/libgdx/libgdx/wiki/Orthographic-camera
// https://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/graphics/Camera.html
// http://www.gamefromscratch.com/post/2014/12/09/LibGDX-Tutorial-Part-17-Viewports.aspx
package gdx.scrollingCam;

/* 
- can't fix WORLD_WIDTH and HEIGHT to be World-units??
- use fit viewport instead of aspect ratio?
 */
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.action;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.math.Vector3;

public class ScrScrollCam implements Screen, InputProcessor {

    Game game;
    SpriteBatch batch;
    OrthographicCamera ocCam;
    //Texture txBad;
    SprBad sprBad;
    SprBad sprWorld;
    Texture txBg;
    int fVx, fVy; // velocities for the dude.
    int nRotate, nRotateAccum;
    double dZoom;
    int nW, nH;
    static final int WORLD_WIDTH = 2560; // dimensions of the image, scaled down units don't seem to work
    static final int WORLD_HEIGHT = 1440;
    boolean bGoingLeft = false; // need this for scrolling

    /* 100 & 50 don't work. Check OrthoGraphicCamGit for more info
     - In OrthoGraphicCamGit, it's mapSprite.draw(batch), not batch.draw(sprite);
     when switched, it doesn't work for some reason
     */
    public ScrScrollCam(Game _game) {
        game = _game;
        batch = new SpriteBatch();
        //txBad = new Texture("badlogic.jpg");
        Gdx.input.setInputProcessor((this));
        nW = Gdx.graphics.getWidth();
        nH = Gdx.graphics.getHeight();
        sprBad = new SprBad("Mario.png", 25, 190);
        sprWorld = new SprBad("bg.jpg", 0, 0);
        sprWorld.setPosition(0, 0);
        sprWorld.setSize(WORLD_WIDTH, WORLD_HEIGHT); // set size of image
        sprBad.setSize(120, 190);
        System.out.println(Gdx.graphics.getWidth());
        System.out.println(Gdx.graphics.getHeight());
    }

    @Override
    public void show() {
        float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
        ocCam = new OrthographicCamera(WORLD_HEIGHT * aspectRatio, WORLD_HEIGHT);
        ocCam.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        updateCam();
    }

    @Override
    public void render(float delta) {
        updateCam();
        sprBad.update(fVx, fVy);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.setProjectionMatrix(ocCam.combined);
        batch.draw(sprWorld.getSprite(), sprWorld.getX(), sprWorld.getY());
        batch.draw(sprBad.getSprite(), sprBad.getX(), sprBad.getY());
        batch.end();
        // System.out.println(sprBad.getX() + "  " + ocCam.viewportWidth);
    }

    @Override
    public void resize(int width, int height) {
        ocCam.viewportWidth = width;
        ocCam.viewportHeight = height;
        ocCam.position.set(width / 2f, height / 2f, 0);
        /* this just shows up as a black screen, should work though
         float aspectRatio = height / width;
         ocCam.viewportWidth = WORLD_HEIGHT * aspectRatio;
         ocCam.viewportHeight = WORLD_HEIGHT;
         ocCam.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
         */
        ocCam.zoom = 50;//1;
        ocCam.rotate(-nRotateAccum);
        nRotateAccum = 0;
        updateCam();

    }

    public void updateCam() {
        Vector3 vec3ScreenCoord = ocCam.project(new Vector3(sprBad.getX() + (sprBad.getSprite().getWidth() / 2), sprBad.getY() + (sprBad.getSprite().getHeight() / 2), 0));
        // line above uses the project method we learnt from the libgdx API doc and GamesFromScratch
        if (bGoingLeft) { // when going left, we need to switch the operator signs
            if (vec3ScreenCoord.x < ocCam.viewportWidth / 2) {
                ocCam.translate(fVx, fVy, 0);
            }
        } else if (vec3ScreenCoord.x > ocCam.viewportWidth / 2) {
            ocCam.translate(fVx, fVy, 0);
        }
        nRotateAccum += nRotate;
        ocCam.rotate(nRotate);

// the zoom lines below are from the OrthoCam github link
        ocCam.zoom += dZoom;
        ocCam.zoom = MathUtils.clamp(ocCam.zoom, 0.1f, WORLD_HEIGHT / ocCam.viewportWidth);
        float fEffectiveViewportWidth = ocCam.viewportWidth * ocCam.zoom;
        float fEffectiveViewportHeight = ocCam.viewportHeight * ocCam.zoom;
        ocCam.position.x = MathUtils.clamp(ocCam.position.x, fEffectiveViewportWidth / 2f, WORLD_WIDTH - fEffectiveViewportWidth / 2f);
        ocCam.position.y = MathUtils.clamp(ocCam.position.y, fEffectiveViewportHeight / 2f, WORLD_HEIGHT - fEffectiveViewportHeight / 2f);

        ocCam.update();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        fVx = 0;
        fVy = 0;
        switch (keycode) {
            /*case Input.Keys.UP:
             fVy = 2;
             break;
             case Input.Keys.DOWN:
             fVy = -2;
             break;*/
            case Input.Keys.LEFT:
                fVx = -5;
                bGoingLeft = true;
                break;
            case Input.Keys.RIGHT:
                fVx = 5;
                bGoingLeft = false;
                break;
            /*case Input.Keys.A:
             nRotate = -1;
             break;
             case Input.Keys.D:
             nRotate = 1;
             break;*/
            case Input.Keys.W:
                dZoom = 0.02;
                break;
            case Input.Keys.S:
                dZoom = -0.02;
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        fVx = 0;
        fVy = 0;
        nRotate = 0;
        dZoom = 0;
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // http://www.gamefromscratch.com/post/2014/12/09/LibGDX-Tutorial-Part-17-Viewports.aspx
        Gdx.app.log("Mouse Event", "On Screen Click at " + screenX + "," + screenY);
        Vector3 worldCoordinates = ocCam.unproject(new Vector3(screenX, screenY, 0));
        Gdx.app.log("Mouse Event", "Projected in world at " + worldCoordinates.x + "," + worldCoordinates.y);
        Vector3 screenCoord = ocCam.project(new Vector3(worldCoordinates));
        Gdx.app.log("Mouse Event", "Clicked on screen at at " + screenCoord.x + "," + screenCoord.y);
        System.out.println(sprBad.getY());
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return true;
    }
}
