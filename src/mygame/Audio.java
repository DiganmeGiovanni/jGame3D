/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.math.Vector3f;

/**
 *
 * @author Eddy
 */
public class Audio {
        
    public static AudioNode playShot(AssetManager assetManager){
        AudioNode shot = new AudioNode(assetManager, "Sounds/effects/disparo.wav");
        return shot;
    }
    public static AudioNode playExplosion(AssetManager assetManager){
        AudioNode explosion = new AudioNode(assetManager, "Sounds/effects/explosion.wav");
        return explosion;
    }
    public static AudioNode playPropulsor(AssetManager assetManager){
        AudioNode shot = new AudioNode(assetManager, "Sounds/effects/propulsor.wav");
        return shot;
    }
}
