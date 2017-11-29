package gdx.fourtress.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import gdx.fourtress.GamFourtress;
import gdx.fourtress.TbMenu;
import gdx.fourtress.TbsMenu;

public class ScrMenu implements Screen, InputProcessor {
    GamFourtress game;
    TbsMenu tbsMenu;
    TbMenu tbPlay, tbOptions;
    Stage stage;
    SpriteBatch batch;
    BitmapFont screenName;

    //------------------------------------ CONSTRUCTOR ----------------------------------------
    public ScrMenu(GamFourtress _game) {  //Referencing the main class.
        game = _game;
    }

    //------------------------------------ SHOW ----------------------------------------
    public void show() {
        stage = new Stage();
        tbsMenu = new TbsMenu();
        batch = new SpriteBatch();
        screenName = new BitmapFont();
        tbPlay = new TbMenu("PLAY", tbsMenu);
        tbOptions = new TbMenu("OPTIONS", tbsMenu);
        tbOptions.setY(0);
        tbOptions.setX(0);
        tbPlay.setY(0);
        tbPlay.setX(440);
        stage.addActor(tbPlay);
        stage.addActor(tbOptions);
        Gdx.input.setInputProcessor(stage);
        btnPlayListener();
        btnOptionsListener();
    }

    //------------------------------------ RENDER ----------------------------------------
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1); //Green background.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        screenName.draw(batch, "MENU", 230, 275);
        batch.end();
        stage.act();
        stage.draw();
    }

    //------------------------------------ BTN PLAY LISTENER ----------------------------------------
    public void btnPlayListener() {
        tbPlay.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {

                game.updateState(2); // switch to Play screen.
            }
        });
    }

    //------------------------------------ BTN MENU LISTENER ----------------------------------------
    public void btnOptionsListener() {
        tbOptions.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.updateState(1);
            }
        });
    }

    @Override
    public void resize(int width, int height) {

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

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
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