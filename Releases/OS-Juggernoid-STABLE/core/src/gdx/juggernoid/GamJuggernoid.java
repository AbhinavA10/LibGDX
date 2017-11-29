//4.1 Juggernoid - sounds
package gdx.juggernoid;
/*
 SOURCES
 For sounds:
 - https://github.com/libgdx/libgdx/wiki/Sound-effects
 - https://github.com/libgdx/libgdx/wiki/Streaming-music
 */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import gdx.juggernoid.screens.ScrGameover;
import gdx.juggernoid.screens.ScrMenu;
import gdx.juggernoid.screens.ScrOptions;
import gdx.juggernoid.screens.ScrPlay;

import java.util.ArrayList;

public class GamJuggernoid extends Game {

    private ScrMenu scrMenu;
    private ScrPlay scrPlay;
    private ScrGameover scrGameover;
    private ScrOptions scrOptions;
    private int nScreen; // 0 for menu, 1 for options, 2 for play, and 3 for game over
    public ArrayList<Sound> alSoundEffects;
    public ArrayList<Music> alMusic;

    //------------------------------------ UPDATE STATE ----------------------------------------
    public void updateState(int _nScreen) {
        nScreen = _nScreen;
        switch (nScreen) {
            case 0:
                alMusic.get(0).play(); // start menu theme
                alMusic.get(0).setLooping(true);
                setScreen(scrMenu);
                break;
            case 1:
                setScreen(scrOptions);
                break;
            case 2:
                alMusic.get(0).stop(); // stop menu theme
                alMusic.get(2).stop(); // stop lose music (sound) effect
                alMusic.get(1).play(); // start game theme
                alMusic.get(1).setLooping(true);
                scrPlay.reset();
                setScreen(scrPlay);
                break;
            case 3:
                alMusic.get(1).stop(); // stop game theme
                alMusic.get(2).play(); // start lose music effect
                setScreen(scrGameover);
                break;
            default:
                break;
        }
    }

    //------------------------------------ CREATE ----------------------------------------
    @Override
    public void create() {
        alSoundEffects = new ArrayList<Sound>();
        alMusic = new ArrayList<Music>();
        for (int i = 0; i < 4; i++) {
            alSoundEffects.add(Gdx.audio.newSound(Gdx.files.internal("sounds/effect" + Integer.toString(i) + ".wav")));
            /*
            The sound effects are as follows:
            0. button
            1. jump
            2. shoot
            3. pickup
            */
        }
        for (int i = 0; i < 4; i++) {
            alMusic.add(Gdx.audio.newMusic(Gdx.files.internal("sounds/music" + Integer.toString(i) + ".wav")));
            /*
            The sound effects are as follows:
            0. menu theme
            1. game theme
            2. lose sound // we had to add the "lose" and "win" sound to the music list because the file was too big for a "sound"
            3. win sound
            */
        }
        alMusic.get(0).play(); // start menu theme
        alMusic.get(0).setLooping(true);
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