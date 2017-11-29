package gdx.scrollingCam;

import com.badlogic.gdx.Game;

public class GamScrollCam extends Game {
//
    @Override
    public void create() {
        this.setScreen(new ScrScrollCam(this));
    }

    @Override
    public void render() {
        super.render();
    }
}
