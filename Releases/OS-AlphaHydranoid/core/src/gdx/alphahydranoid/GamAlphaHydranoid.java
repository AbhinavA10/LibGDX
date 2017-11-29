package gdx.alphahydranoid;

import gdx.alphahydranoid.screens.ScrPlay;
import com.badlogic.gdx.Game;

public class GamAlphaHydranoid extends Game {
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
