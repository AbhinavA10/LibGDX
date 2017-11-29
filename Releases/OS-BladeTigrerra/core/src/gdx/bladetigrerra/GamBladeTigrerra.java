package gdx.bladetigrerra;

import gdx.bladetigrerra.screens.ScrPlay;
import com.badlogic.gdx.Game;

public class GamBladeTigrerra extends Game {
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
