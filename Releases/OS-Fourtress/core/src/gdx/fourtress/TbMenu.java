package gdx.fourtress;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TbMenu extends TextButton {
    String sText;

    //------------------------------------ CONSTRUCTOR ----------------------------------------
    public TbMenu(String _sText, TextButtonStyle _tbs) {
        super(_sText, _tbs);
        sText = _sText;
        this.setSize(200, 80);
        this.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                //System.out.println(sText);

            }
        });
    }
}
