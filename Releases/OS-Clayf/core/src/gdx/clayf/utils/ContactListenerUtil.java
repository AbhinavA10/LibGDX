package gdx.clayf.utils;
//Connar Anderson, Contact Listening: https://www.youtube.com/watch?v=ien40lFovG8&index=9&list=PLD_bW3UTVsElsuvyKcYXHLnWb8bD0EQNI

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import gdx.clayf.SpriteExtended;

public class ContactListenerUtil implements ContactListener {

    @Override
    public void beginContact(Contact cntct) {
        Fixture a = cntct.getFixtureA();
        Fixture b = cntct.getFixtureB();

        if (a == null || b == null) {
            return;
        }
        if (a.getUserData() == null || b.getUserData() == null) {
            return;
        }
        System.out.println("Collision!");
        if (isContact(a, b)) {
            SpriteExtended sprDude = (SpriteExtended) b.getUserData();
            sprDude.resetJump();
            sprDude.hit();
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private boolean isContact(Fixture a, Fixture b) {
        if (a.getUserData() instanceof SpriteExtended || b.getUserData() instanceof SpriteExtended) {
            if (a.getUserData() instanceof TiledObjectUtil || b.getUserData() instanceof TiledObjectUtil) {
                return true;
            }
        }
        return false;
    }
}
