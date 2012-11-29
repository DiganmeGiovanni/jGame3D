/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author Giovanni
 */
public class ImplementarExplosion extends SimpleApplication{

    public static void main(String[] args) {
        new ImplementarExplosion().start();
    }
    
    EfectoExplosion efecto;
    
    @Override
    public void simpleInitApp() 
    {
        efecto = new EfectoExplosion(assetManager, renderManager);
        flyCam.setMoveSpeed(100);
        
        cam.setLocation(new Vector3f(0, 3.5135868f, 10));
        cam.setRotation(new Quaternion(1.5714673E-4f, 0.98696727f, -0.16091813f, 9.6381607E-4f));
        rootNode.attachChild(efecto.explosion);
    }
    
    @Override
    public void simpleUpdate(float tpf)
    {
        efecto.actualizar(tpf, speed);
    }
    
}
