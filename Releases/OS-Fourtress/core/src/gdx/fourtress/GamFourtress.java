//3.2 Fourtress - removable tiles
package gdx.fourtress;

import com.badlogic.gdx.Game;
import gdx.fourtress.screens.ScrGameover;
import gdx.fourtress.screens.ScrMenu;
import gdx.fourtress.screens.ScrOptions;
import gdx.fourtress.screens.ScrPlay;

public class GamFourtress extends Game {

    ScrMenu scrMenu;
    ScrPlay scrPlay;
    ScrGameover scrGameover;
    ScrOptions scrOptions;
    int nScreen; // 0 for menu, 1 for options, 2 for play, and 3 for game over

    //------------------------------------ UPDATE STATE ----------------------------------------
    public void updateState(int _nScreen) {
        nScreen = _nScreen;
        if (nScreen == 0) {
            setScreen(scrMenu);
        } else if (nScreen == 1) {
            setScreen(scrOptions);
        } else if (nScreen == 2) {
            setScreen(scrPlay);
        } else if (nScreen == 3) {
            setScreen(scrGameover);
        }
    }
//------------------------------------ CREATE ----------------------------------------
    @Override
    public void create() {
        nScreen = 0;
        scrMenu = new ScrMenu(this);
        scrPlay = new ScrPlay(this);
        scrGameover = new ScrGameover(this);
        scrOptions = new ScrOptions(this);
        updateState(0);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}