package gdx.dualhydranoid;

import gdx.dualhydranoid.screens.ScrPlay;
import com.badlogic.gdx.Game;

public class GamDualHydranoid extends Game {
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
