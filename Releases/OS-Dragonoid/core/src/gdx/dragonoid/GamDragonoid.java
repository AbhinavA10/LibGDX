package gdx.dragonoid;

import gdx.dragonoid.screens.ScrPlay;
import com.badlogic.gdx.Game;

public class GamDragonoid extends Game {
//

    @Override
    public void create() {
        this.setScreen(new ScrPlay(this));
    }

    @Override
    public void render() {
        super.render();
    }
}
