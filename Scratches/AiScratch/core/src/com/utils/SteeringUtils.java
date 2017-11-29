/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utils;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author mattm
 */
public class SteeringUtils {
    public static float vectorToAngle (Vector2 v) {
        return (float)Math.atan2(-v.x, v.y);
    }
    public static Vector2 angleToVector (Vector2 v, float fAngle) {
        v.x = -(float)Math.sin(fAngle);
        v.y = -(float)Math.cos(fAngle);
        return v;
    }
            
}
