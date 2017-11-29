package gdx.weapons;
//Connar Anderson, Contact Listening: https://www.youtube.com/watch?v=ien40lFovG8&index=9&list=PLD_bW3UTVsElsuvyKcYXHLnWb8bD0EQNI

import com.badlogic.gdx.physics.box2d.*;

public class ContactListenerUtil implements ContactListener {
    private WeaponScratch weaponScratch;

    public ContactListenerUtil(WeaponScratch _weaponScratch) {
        weaponScratch = _weaponScratch;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a == null || b == null) {
            return;
        }
        if (a.getUserData() == null || b.getUserData() == null) {
            return;
        }
        if (isContact(a, b)) {
            Bullet bullet;
            //https://docs.oracle.com/javase/tutorial/essential/exceptions/catch.html
            try {
                bullet = (Bullet) b.getUserData();
            } catch (ClassCastException cce) {
                bullet = (Bullet) a.getUserData();
            }
            weaponScratch.destroy(bullet);
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
        if (a.getUserData() instanceof Bullet || b.getUserData() instanceof Bullet) {
            if (a.getUserData() instanceof StaticBox || b.getUserData() instanceof StaticBox) {
                return true;
            }
        }
        return false;
    }
}
