package gdx.scrollingCam;
//http://gamedev.stackexchange.com/questions/106234/camera-controll-in-libgdx
//http://stackoverflow.com/questions/27671014/libgdx-sidescroller-with-tiledmap-and-two-actors

import com.badlogic.gdx.graphics.OrthographicCamera;

public class ExtendedCamera extends OrthographicCamera {
    /*
    public Player player;

    public ExtendedCamera(Player player) {
        super(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        this.player = player;
    }

    public void followPlayer() {
        if (player.body.getPosition().x - position.x > Constants.CAMERA_FOLLOW_LINE_X) {
            position.x = player.body.getPosition().x
                    - Constants.CAMERA_FOLLOW_LINE_X;
            update();
        } else if (player.body.getPosition().x - position.x < -Constants.CAMERA_FOLLOW_LINE_X) {
            position.x = player.body.getPosition().x
                    + Constants.CAMERA_FOLLOW_LINE_X;
            update();
        }
        if (player.body.getPosition().y - position.y > Constants.CAMERA_FOLLOW_LINE_Y) {
            position.y = player.body.getPosition().y
                    - Constants.CAMERA_FOLLOW_LINE_Y;
            update();
        } else if (player.body.getPosition().y - position.y < -Constants.CAMERA_FOLLOW_LINE_Y) {
            position.y = player.body.getPosition().y
                    + Constants.CAMERA_FOLLOW_LINE_Y;
            update();
        }
    }
     */
}
