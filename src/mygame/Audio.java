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

    public static void playChimenea(AssetManager assetManager) {
        AudioNode chimenea = new AudioNode(assetManager, "Sounds/effects/chimenea.wav");
        chimenea.setPositional(true);
        chimenea.setRefDistance(.01f);
        chimenea.setMaxDistance(.010f);
        chimenea.setPitch(0.5f);
        chimenea.setLocalTranslation(new Vector3f(10, -9, 135));
        chimenea.setReverbEnabled(true);
        chimenea.setLooping(true);        
        chimenea.play();
    }
    
    public static AudioNode playShot(AssetManager assetManager){
        AudioNode shot = new AudioNode(assetManager, "Sounds/effects/shot.wav");
        return shot;
    }
}
