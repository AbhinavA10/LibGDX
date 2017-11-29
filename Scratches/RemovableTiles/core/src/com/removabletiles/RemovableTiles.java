package com.removabletiles;
//Removing a Tile: http://www.java-gaming.org/index.php?topic=35160.0
//http://gamedev.stackexchange.com/questions/74821/libgdx-how-do-you-remove-a-cell-from-tiledmap

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class RemovableTiles extends ApplicationAdapter implements InputProcessor {

    private final float SCALE = 2.0f;
    private OrthographicCamera camera;
    OrthogonalTiledMapRenderer tmr;
    TiledMap tiledMap;
    int nX, nY;
    private final float fPPM = 32;

    TiledMapTileLayer tiledMapLayer;

    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);
        int x = 0, y = 0;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w / SCALE, h / SCALE);

        tiledMap = new TmxMapLoader().load("MyMap.tmx");
        tiledMapLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Midground");
        tmr = new OrthogonalTiledMapRenderer(tiledMap);
        nX = 0;
        nY = 0;
    }

    @Override
    public void render() {
        update();
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tmr.render();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / SCALE, height / SCALE);
    }

    @Override
    public void dispose() {
        tmr.dispose();
        tiledMap.dispose();
    }

    public void update() {
        inputUpdate();
        cameraUpdate();
        tmr.setView(camera);
    }

    public void inputUpdate() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            nX -= 5;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            nY += 5;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            nX += 5;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            nY -= 5;
        }
    }

    public void cameraUpdate() {
        Vector3 position = camera.position;
        position.x = nX;
        position.y = nY;
        camera.position.set(position);
        camera.update();
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
        Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
        Vector3 position = camera.unproject(clickCoordinates);
        removeTile(position);
        return true;
    }

    public void removeTile(Vector3 position) {
        position.x = position.x / fPPM;
        position.y = position.y / fPPM;
        System.out.println(position);
        int nX = (int) position.x;
        int nY = (int) position.y;
        System.out.println("X: " + nX + "   Y: " + nY);
        tiledMapLayer.setCell(nX, nY, null);
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
