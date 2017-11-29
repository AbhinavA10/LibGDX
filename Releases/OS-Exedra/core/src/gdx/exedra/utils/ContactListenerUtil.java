package gdx.exedra.utils;
/*
SOURCES
- Connar Anderson, Contact Listening: https://www.youtube.com/watch?v=ien40lFovG8
- Destroying a body upon collision: http://box2d.org/forum/viewtopic.php?t=9724
- Try/catch: https://docs.oracle.com/javase/tutorial/essential/exceptions/catch.html
*/

import com.badlogic.gdx.physics.box2d.*;
import gdx.exedra.*;
import gdx.exedra.screens.ScrPlay;

public class ContactListenerUtil implements ContactListener {
    ScrPlay scrPlay;

    //------------------------------------ CONSTRUCTOR ----------------------------------------
    public ContactListenerUtil(ScrPlay _scrPlay) {
        scrPlay = _scrPlay;
    }

    //------------------------------------ BEGIN CONTACT ----------------------------------------
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
        // System.out.println("Collision!");
        // seems that b is always the sprite?
        if (isContactPlatforms(a, b)) {
            SpriteExtended sprDude = (SpriteExtended) b.getUserData();
            sprDude.resetJump();
            // System.out.println("Platforms");
        }
        if (isContactSpikes(a, b)) {
            // System.out.println("Spikes");
            ScrPlay.bDead = true;
        }
        if (isContactMushrooms(a, b)) {
            SpriteExtended sprDude = (SpriteExtended) b.getUserData();
            sprDude.resetJump();
            //  System.out.println("SHROOM");
        }
        if (isContactBullets(a, b)) {
            // System.out.println("the bullet hit a box");
            Bullet bullet;
            Platforms platform;
            try {
                bullet = (Bullet) b.getUserData();
                platform = (Platforms) a.getUserData();
            } catch (ClassCastException cce) {
                bullet = (Bullet) a.getUserData();
                platform = (Platforms) a.getUserData();
            }
            scrPlay.destroy(bullet, platform);
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

    //------------------------------------ IS CONTACT PLATFORMS ----------------------------------------
    private boolean isContactPlatforms(Fixture a, Fixture b) {
        if (a.getUserData() instanceof SpriteExtended || b.getUserData() instanceof SpriteExtended) {
            if (a.getUserData() instanceof Platforms || b.getUserData() instanceof Platforms) {
                return true;
            }
        }
        return false;
    }

    //------------------------------------ IS CONTACT SPIKES ----------------------------------------
    private boolean isContactSpikes(Fixture a, Fixture b) {
        if (a.getUserData() instanceof SpriteExtended || b.getUserData() instanceof SpriteExtended) {
            if (a.getUserData() instanceof Spikes || b.getUserData() instanceof Spikes) {
                return true;
            }
        }
        return false;
    }

    //------------------------------------ IS CONTACT MUSHROOMS ----------------------------------------
    private boolean isContactMushrooms(Fixture a, Fixture b) {
        if (a.getUserData() instanceof SpriteExtended || b.getUserData() instanceof SpriteExtended) {
            if (a.getUserData() instanceof Mushrooms || b.getUserData() instanceof Mushrooms) {
                return true;
            }
        }
        return false;
    }

    //------------------------------------ IS CONTACT BULLETS ----------------------------------------
    private boolean isContactBullets(Fixture a, Fixture b) {
        if (a.getUserData() instanceof Platforms || b.getUserData() instanceof Platforms) {
            if (a.getUserData() instanceof Bullet || b.getUserData() instanceof Bullet) {
                return true;
            }
        }
        return false;
    }
}
