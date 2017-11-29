package gdx.bullets;
//Connar Anderson, Contact Listening: https://www.youtube.com/watch?v=ien40lFovG8&index=9&list=PLD_bW3UTVsElsuvyKcYXHLnWb8bD0EQNI

import com.badlogic.gdx.physics.box2d.*;

public class ContactListenerUtil implements ContactListener {
    private BulletScratch bulletScratch;

    public ContactListenerUtil(BulletScratch _bulletScratch) {
        bulletScratch = _bulletScratch;
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
        System.out.println("Collision!");
        if (isContact(a, b)) {
            System.out.println("the bullet hit a box");
            Bullet bullet;
            StaticBox staticBox;
            //https://docs.oracle.com/javase/tutorial/essential/exceptions/catch.html
            try {
                bullet = (Bullet) b.getUserData();
                staticBox = (StaticBox) a.getUserData();
            } catch (ClassCastException cce) {
                bullet = (Bullet) a.getUserData();
                staticBox = (StaticBox) b.getUserData();
            }
            bulletScratch.destroy(bullet, staticBox);
        }
        if (isBulletsHit(a, b)) {
            System.out.println("the bullet hit a bullet");
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

    private boolean isBulletsHit(Fixture a, Fixture b) {
        if (a.getUserData() instanceof Bullet && b.getUserData() instanceof Bullet) {

            return true;

        }
        return false;
    }
}
