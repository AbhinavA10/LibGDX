package gdx.exedra.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import gdx.exedra.GamExedra;
import gdx.exedra.TbMenu;
import gdx.exedra.TbsMenu;

public class ScrOptions implements Screen, InputProcessor {
    GamExedra game;
    TbsMenu tbsMenu;
    TbMenu tbBack;
    Stage stage;
    SpriteBatch batch;
    BitmapFont screenName;

    //------------------------------------ CONSTRUCTOR ----------------------------------------
    public ScrOptions(GamExedra _game) {  //Referencing the main class.
        game = _game;
    }

    //------------------------------------ SHOW ----------------------------------------
    public void show() {
        stage = new Stage();
        tbsMenu = new TbsMenu();
        batch = new SpriteBatch();
        screenName = new BitmapFont();
        tbBack = new TbMenu("BACK", tbsMenu);
        tbBack.setY(0);
        tbBack.setX(440);
        stage.addActor(tbBack);
        Gdx.input.setInputProcessor(stage);
        btnBackListener();
    }

    //------------------------------------ RENDER ----------------------------------------
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); //black background.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        screenName.draw(batch, "OPTIONS: For Later Release", 230, 275);
        batch.end();
        stage.act();
        stage.draw();
    }

    //------------------------------------ BTN BACK LISTENER ----------------------------------------
    public void btnBackListener() {
        tbBack.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {

                game.updateState(0);
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