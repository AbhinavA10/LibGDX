//?.? Griffon - AI Enemies
package gdx.griffon;

import com.badlogic.gdx.Game;
import gdx.griffon.screens.ScrGameover;
import gdx.griffon.screens.ScrMenu;
import gdx.griffon.screens.ScrOptions;
import gdx.griffon.screens.ScrPlay;

public class GamGriffon extends Game {

    private ScrMenu scrMenu;
    private ScrPlay scrPlay;
    private ScrGameover scrGameover;
    private ScrOptions scrOptions;
    private int nScreen; // 0 for menu, 1 for options, 2 for play, and 3 for game over

    //------------------------------------ UPDATE STATE ----------------------------------------
    public void updateState(int _nScreen) {
        nScreen = _nScreen;
        switch (nScreen) {
            case 0:
                setScreen(scrMenu);
                break;
            case 1:
                setScreen(scrOptions);
                break;
            case 2:
                scrPlay.reset();
                setScreen(scrPlay);
                break;
            case 3:
                setScreen(scrGameover);
                break;
            default:
                break;
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