package gdx.apollonir;

import gdx.apollonir.screens.ScrPlay;
import com.badlogic.gdx.Game;

public class GamApollonir extends Game {
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
