package com.aiscratch;

import com.badlogic.gdx.physics.box2d.*;

import static com.aiscratch.AiScratch.fPPM;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

public class AI implements Steerable<Vector2> {

    Body body;
    boolean bTagged;
    float fBoundingRadius;
    float fMaxLinearSpeed, fMaxLinearAcceleration;
    float fMaxAngularSpeed, fMaxAngularAcceleration;
    SteeringBehavior<Vector2> behavior;
    SteeringAcceleration<Vector2> steerOutput;
    
    public AI(Body body, float BoundingRadius) {
        this.body = body;
        this.fBoundingRadius = fBoundingRadius;
        
        this.fMaxLinearSpeed = 500;
        this.fMaxLinearAcceleration = 500;
        this.fMaxAngularSpeed = 5000;
        this.fMaxAngularAcceleration = 500;
        
        this.bTagged = false;
        
        this.steerOutput = new SteeringAcceleration<Vector2>(new Vector2());
        this.body.setUserData(this);
        
    }
    
    public void update(float fDelta) {
        if(behavior != null) {
            behavior.calculateSteering(steerOutput);
            applySteering(fDelta);
        }
    }
    
    private void applySteering(float fDelta) {
        boolean bAnyAccelerations = false;
        
        if (!steerOutput.linear.isZero()) {
            Vector2 force = steerOutput.linear.scl(fDelta);
            body.applyForceToCenter(force, true);
            bAnyAccelerations = true;
        }
        
        if(steerOutput.angular != 0) {
            body.applyTorque(steerOutput.angular * fDelta, true);
        } else {
            Vector2 linVel = getLinearVelocity();
            if (!linVel.isZero()) {
                float newOrientation = vectorToAngle(linVel);
                body.setAngularVelocity((newOrientation - getAngularVelocity()) * fDelta);
                body.setTransform(body.getPosition(), newOrientation);
            }
        }
        
        if (bAnyAccelerations) {
            Vector2 velocity = body.getLinearVelocity();
            float fCurrentSpeedSquare = velocity.len2();
            //Linear capping
            if (fCurrentSpeedSquare > fMaxLinearSpeed * fMaxLinearSpeed) {
                body.setLinearVelocity(velocity.scl(fMaxLinearSpeed / (float) Math.sqrt(fCurrentSpeedSquare)));
            }
            //angular capping
            if (body.getAngularVelocity() > fMaxAngularSpeed) {
                body.setAngularVelocity(fMaxAngularSpeed);
            }
        }
    }
    public Vector2 newVector() {
        return new Vector2();
    }

    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return fBoundingRadius;
    }

    @Override
    public boolean isTagged() {
        return bTagged;
    }

    @Override
    public void setTagged(boolean bln) {
        this.bTagged = bTagged;
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        return body.getAngle();
    }

    @Override
    public void setOrientation(float f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float vectorToAngle(Vector2 v) {
        return com.utils.SteeringUtils.vectorToAngle(v);
    }

    @Override
    public Vector2 angleToVector(Vector2 v, float fAngle) {
        return com.utils.SteeringUtils.angleToVector(v, fAngle);
    }

    @Override
    public Location<Vector2> newLocation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setZeroLinearSpeedThreshold(float f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float getMaxLinearSpeed() {
        return fMaxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float f) {
        this.fMaxLinearSpeed = f;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return fMaxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float f) {
        this.fMaxLinearAcceleration = f;
    }

    @Override
    public float getMaxAngularSpeed() {
        return fMaxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float f) {
        this.fMaxAngularSpeed = f;
    }

    @Override
    public float getMaxAngularAcceleration() {
         return fMaxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float f) {
        this.fMaxAngularAcceleration = f;
    }
    
    public Body getBody() {
        return body;
    }
    
    public void setBehavior(SteeringBehavior<Vector2> behavior) {
        this.behavior = behavior;
    }
    
    public SteeringBehavior<Vector2> getBehavior() {
        return behavior;
    }
}
