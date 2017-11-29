package gdx.fourtress.utils;
/*
 SOURCES
 - Conner Anderson, Contact Listening: https://www.youtube.com/watch?v=ien40lFovG8
 - Destroying a body upon collision: http://box2d.org/forum/viewtopic.php?t=9724
 - Try/catch: https://docs.oracle.com/javase/tutorial/essential/exceptions/catch.html
 */

import com.badlogic.gdx.physics.box2d.*;
import gdx.fourtress.Bullet;
import gdx.fourtress.SpriteExtended;
import gdx.fourtress.Mushrooms;
import gdx.fourtress.Platforms;
import gdx.fourtress.DestructibleTile;
import gdx.fourtress.Spikes;
import gdx.fourtress.screens.ScrPlay;

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
        } else if (isContactSpikes(a, b)) {
            // System.out.println("Spikes");
            scrPlay.bDead = true;
        } else if (isContactMushrooms(a, b)) {
            SpriteExtended sprDude = (SpriteExtended) b.getUserData();
            sprDude.resetJump();
            //  System.out.println("SHROOM");
        } else if (isBulletContactMushroom(a, b)) {
            // System.out.println("the bullet hit a mushroom");
            Bullet bullet;
            try {
                bullet = (Bullet) b.getUserData();
            } catch (ClassCastException cce) {
                bullet = (Bullet) a.getUserData();
            }
            scrPlay.destroy(bullet);
        } else if (isBulletContactPlatform(a, b)) {
            // System.out.println("the bullet hit a box");
            Bullet bullet;
            try {
                bullet = (Bullet) b.getUserData();
            } catch (ClassCastException cce) {
                bullet = (Bullet) a.getUserData();
            }
            scrPlay.destroy(bullet);
        } else if (isBulletContactDestructible(a, b)) {
            // System.out.println("the bullet hit a box");
            Bullet bullet;
            DestructibleTile destructibleTile;
            try {
                bullet = (Bullet) b.getUserData();
                destructibleTile = (DestructibleTile) a.getUserData();
            } catch (ClassCastException cce) {
                bullet = (Bullet) a.getUserData();
                destructibleTile = (DestructibleTile) b.getUserData();
            }
            scrPlay.destroy2(bullet, destructibleTile);
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

    //------------------------------------ IS BULLET CONTACT PLATFORM ----------------------------------------
    private boolean isBulletContactPlatform(Fixture a, Fixture b) {
        if (a.getUserData() instanceof Platforms || b.getUserData() instanceof Platforms) {
            if (a.getUserData() instanceof Bullet || b.getUserData() instanceof Bullet) {
                return true;
            }
        }
        return false;
    }

    //------------------------------------ IS BULLET CONTACT DESTRUCTIBLE ----------------------------------------
    private boolean isBulletContactDestructible(Fixture a, Fixture b) {
        if (a.getUserData() instanceof DestructibleTile || b.getUserData() instanceof DestructibleTile) {
            if (a.getUserData() instanceof Bullet || b.getUserData() instanceof Bullet) {
                return true;
            }
        }
        return false;
    }

    //------------------------------------ IS BULLET CONTACT MUSHROOM ----------------------------------------
    private boolean isBulletContactMushroom(Fixture a, Fixture b) {
        if (a.getUserData() instanceof Mushrooms || b.getUserData() instanceof Mushrooms) {
            if (a.getUserData() instanceof Bullet || b.getUserData() instanceof Bullet) {
                return true;
            }
        }
        return false;
    }
}
