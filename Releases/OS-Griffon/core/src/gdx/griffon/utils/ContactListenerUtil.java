package gdx.griffon.utils;
/*
 SOURCES
 - Conner Anderson, Contact Listening: https://www.youtube.com/watch?v=ien40lFovG8
 - Destroying a body upon collision: http://box2d.org/forum/viewtopic.php?t=9724
 - Try/catch: https://docs.oracle.com/javase/tutorial/essential/exceptions/catch.html
 */

import com.badlogic.gdx.physics.box2d.*;
import gdx.griffon.Bullet;
import gdx.griffon.SpriteExtended;
import gdx.griffon.screens.ScrPlay;
import gdx.griffon.tiles.*;

public class ContactListenerUtil implements ContactListener {

    private ScrPlay scrPlay;

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
        if (isSpriteExtContactPlatforms(a, b) || isSpriteExtContactMushrooms(a, b) || isSpriteExtContactDestructible(a, b) || isSpriteExtContactSpriteExt(a, b)) {
            resetJump(a, b);
        } else if (isSpriteExtContactMoving(a, b)) {
            SpriteExtended sprDude;
            FallingTile ftB;
            try {
                ftB = (FallingTile) a.getUserData();
                sprDude = (SpriteExtended) b.getUserData();
            } catch (ClassCastException cce) {
                ftB = (FallingTile) b.getUserData();
                sprDude = (SpriteExtended) a.getUserData();
            }
            ftB.bHit = true;
            sprDude.resetJump();
        } else if (isSpriteExtContactSpikes(a, b)) {
            SpriteExtended sprDude;
            try {
                sprDude = (SpriteExtended) b.getUserData();
            } catch (ClassCastException cce) {
                sprDude = (SpriteExtended) a.getUserData();
            }
            sprDude.bDead = true;
        } else if (isBulletContactMushroom(a, b) || isBulletContactPlatform(a, b) || isBulletContactFalling(a, b) || isBulletContactSpike(a, b)) {
            destroyBullet(a, b);
        } else if (isBulletContactSprite(a, b)) {
            SpriteExtended sprDude;
            Bullet bullet;
            try {
                bullet = (Bullet) a.getUserData();
                sprDude = (SpriteExtended) b.getUserData();
            } catch (ClassCastException cce) {
                bullet = (Bullet) b.getUserData();
                sprDude = (SpriteExtended) a.getUserData();
            }
            scrPlay.destroyBullet(bullet);
            sprDude.bDead = true;

        } else if (isBulletContactDestructible(a, b)) {
            Bullet bullet;
            DestructibleTile destructibleTile;
            try {
                bullet = (Bullet) b.getUserData();
                destructibleTile = (DestructibleTile) a.getUserData();
            } catch (ClassCastException cce) {
                bullet = (Bullet) a.getUserData();
                destructibleTile = (DestructibleTile) b.getUserData();
            }
            scrPlay.destroyBulletTile(bullet, destructibleTile);
        } else if (isBulletContactBullet(a, b)) {
            Bullet bullet;
            Bullet bullet2;
            bullet = (Bullet) a.getUserData();
            bullet2 = (Bullet) b.getUserData();
            scrPlay.destroyBullet(bullet);
            scrPlay.destroyBullet(bullet2);
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

    //------------------------------------ RESET JUMP ----------------------------------------
    private void resetJump(Fixture a, Fixture b) {
        SpriteExtended sprDude;
        try {
            sprDude = (SpriteExtended) b.getUserData();
        } catch (ClassCastException cce) {
            sprDude = (SpriteExtended) a.getUserData();
        }
        sprDude.resetJump();
    }

    //------------------------------------ DESTROY BULLET ----------------------------------------
    private void destroyBullet(Fixture a, Fixture b) {
        Bullet bullet;
        try {
            bullet = (Bullet) b.getUserData();
        } catch (ClassCastException cce) {
            bullet = (Bullet) a.getUserData();
        }
        scrPlay.destroyBullet(bullet);
    }

    //------------------------------------ IS SPRITE CONTACT SPRITE ----------------------------------------
    private boolean isSpriteExtContactSpriteExt(Fixture a, Fixture b) {
        return a.getUserData() instanceof SpriteExtended && b.getUserData() instanceof SpriteExtended;
    }

    //------------------------------------ IS SPRITE CONTACT MOVING ----------------------------------------
    private boolean isSpriteExtContactMoving(Fixture a, Fixture b) {
        if (a.getUserData() instanceof SpriteExtended || b.getUserData() instanceof SpriteExtended) {
            if (a.getUserData() instanceof FallingTile || b.getUserData() instanceof FallingTile) {
                return true;
            }
        }
        return false;
    }

    //------------------------------------ IS SPRITE CONTACT PLATFORMS ----------------------------------------
    private boolean isSpriteExtContactPlatforms(Fixture a, Fixture b) {
        if (a.getUserData() instanceof SpriteExtended || b.getUserData() instanceof SpriteExtended) {
            if (a.getUserData() instanceof Platforms || b.getUserData() instanceof Platforms) {
                return true;
            }
        }
        return false;
    }

    //------------------------------------ IS SPRITE CONTACT SPIKES ----------------------------------------
    private boolean isSpriteExtContactSpikes(Fixture a, Fixture b) {
        if (a.getUserData() instanceof SpriteExtended || b.getUserData() instanceof SpriteExtended) {
            if (a.getUserData() instanceof Spikes || b.getUserData() instanceof Spikes) {
                return true;
            }
        }
        return false;
    }

    //------------------------------------ IS SPRITE CONTACT MUSHROOMS ----------------------------------------
    private boolean isSpriteExtContactMushrooms(Fixture a, Fixture b) {
        if (a.getUserData() instanceof SpriteExtended || b.getUserData() instanceof SpriteExtended) {
            if (a.getUserData() instanceof Mushrooms || b.getUserData() instanceof Mushrooms) {
                return true;
            }
        }
        return false;
    }//------------------------------------ IS SPRITE CONTACT MUSHROOMS ----------------------------------------

    private boolean isSpriteExtContactDestructible(Fixture a, Fixture b) {
        if (a.getUserData() instanceof SpriteExtended || b.getUserData() instanceof SpriteExtended) {
            if (a.getUserData() instanceof DestructibleTile || b.getUserData() instanceof DestructibleTile) {
                return true;
            }
        }
        return false;
    }


    //------------------------------------ IS BULLET CONTACT SPRITE ----------------------------------------
    private boolean isBulletContactSprite(Fixture a, Fixture b) {
        if (a.getUserData() instanceof SpriteExtended || b.getUserData() instanceof SpriteExtended) {
            if (a.getUserData() instanceof Bullet || b.getUserData() instanceof Bullet) {
                return true;
            }
        }
        return false;
    }

    //------------------------------------ IS BULLET CONTACT BULLET ----------------------------------------
    private boolean isBulletContactBullet(Fixture a, Fixture b) {
        return a.getUserData() instanceof Bullet && b.getUserData() instanceof Bullet;
    }

    //------------------------------------ IS BULLET CONTACT PLATFORM ----------------------------------------
    private boolean isBulletContactPlatform(Fixture a, Fixture b) {
        if (a.getUserData() instanceof Platforms || b.getUserData() instanceof Platforms) {
            if (a.getUserData() instanceof Bullet || b.getUserData() instanceof Bullet) {
                return true;
            }
        }
        return false;
    }//------------------------------------ IS BULLET CONTACT SPIKE ----------------------------------------

    private boolean isBulletContactSpike(Fixture a, Fixture b) {
        if (a.getUserData() instanceof Spikes || b.getUserData() instanceof Spikes) {
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
    }//------------------------------------ IS BULLET CONTACT FALLING ----------------------------------------

    private boolean isBulletContactFalling(Fixture a, Fixture b) {
        if (a.getUserData() instanceof FallingTile || b.getUserData() instanceof FallingTile) {
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
