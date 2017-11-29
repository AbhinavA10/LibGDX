package gdx.dualhydranoid.utils;

// camera boundries and other Camera Styles https://www.youtube.com/watch?v=Lb2vZ5lBgCY 
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraStyleUtil {

    private static Vector3 vec3Position;

    public static void lerpToTarget(Camera camera, Vector2 vec2Target) {
        // a + (b-a) * lerpFactor
        // a = camera position
        // b = target position (player)
        vec3Position = camera.position;
        vec3Position.x = camera.position.x + (vec2Target.x - camera.position.x) * .1f;
        vec3Position.y = camera.position.y + (vec2Target.y - camera.position.y) * .1f;
        setCamPos(camera, vec3Position);

    }

    public static void lockOnTarget(Camera camera, Vector2 vec2Target) {
        vec3Position = camera.position;
        vec3Position.x = vec2Target.x;
        vec3Position.y = vec2Target.y;
        setCamPos(camera, vec3Position);
    }

    public static void lockAverageBetweenTargets(Camera camera, Vector2 vec2TargetA, Vector2 vec2TargetB) {
        vec3Position = camera.position;
        vec3Position.x = (vec2TargetA.x + vec2TargetB.x) / 2;
        vec3Position.y = (vec2TargetA.y + vec2TargetB.y) / 2;
        setCamPos(camera, vec3Position);
    }

    public static void lerpAverageBetweenTargets(Camera camera, Vector2 vec2TargetA, Vector2 vec2TargetB) {
        vec3Position = camera.position;
        float fAvgX = (vec2TargetA.x + vec2TargetB.x) / 2;
        float fAvgY = (vec2TargetA.y + vec2TargetB.y) / 2;
        vec3Position.x = camera.position.x + (fAvgX - camera.position.x) * .1f;
        vec3Position.y = camera.position.y + (fAvgY - camera.position.y) * .1f;
        setCamPos(camera, vec3Position);
    }

    public static void boundary(Camera camera, float fStartX, float fStartY, float fWidth, float fHeight) {
        vec3Position = camera.position;
        vec3Position.x = MathUtils.clamp(vec3Position.x, fStartX, fStartX + fWidth);
        vec3Position.y = MathUtils.clamp(vec3Position.y, fStartY, fStartY + fHeight);
        setCamPos(camera, vec3Position);
    }

    private static void setCamPos(Camera cam, Vector3 vec3Pos) {
        cam.position.set(vec3Pos);
        cam.update();
    }
}
