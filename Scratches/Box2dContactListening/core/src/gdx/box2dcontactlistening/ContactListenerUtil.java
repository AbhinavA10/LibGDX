package gdx.box2dcontactlistening;
//Connar Anderson, Contact Listening: https://www.youtube.com/watch?v=ien40lFovG8&index=9&list=PLD_bW3UTVsElsuvyKcYXHLnWb8bD0EQNI

import com.badlogic.gdx.physics.box2d.*;

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
            PlayerBox pA = (PlayerBox) a.getUserData();
            pA.hit();
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
        if (a.getUserData() instanceof PlayerBox || b.getUserData() instanceof PlayerBox) {
            if (a.getUserData() instanceof StaticBox || b.getUserData() instanceof StaticBox) {
                return true;
            }
        }
        return false;
    }
}
