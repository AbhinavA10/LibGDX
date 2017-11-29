package gdx.bullets;

import gdx.bullets.screens.ScrPlay;
import com.badlogic.gdx.Game;

public class GamClayf extends Game {
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
