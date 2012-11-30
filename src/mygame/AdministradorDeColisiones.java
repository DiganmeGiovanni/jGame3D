/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.scene.Node;

/**
 *
 * @author Giovanni
 */
public class AdministradorDeColisiones {
    
    public static void colision
    (String nomEscena, Node rootNode, BulletAppState bulletApp, PhysicsCollisionEvent event, int score)
    {
        if (nomEscena.equals("MonkeyLand")) {
            colisionMonkeyLand(rootNode, bulletApp, event, score);
        } else if (nomEscena.equals("Bodega"))
        {
            colisionMonkeyLand(rootNode, bulletApp, event, score);
        }
    }

    private static void colisionMonkeyLand
    ( Node rootNode, BulletAppState bulletApp, PhysicsCollisionEvent event, int score) 
    {
        if (event.getNodeA().getName().equals("Bala de captura"))
        {   
            // Eliminamos la malla de captura de la escena
            rootNode.detachChild(event.getNodeA());
            bulletApp.getPhysicsSpace().remove(event.getNodeA().getControl(0));
                
            if (event.getNodeB().getName().equals("Basura"))
            {
                rootNode.detachChild(event.getNodeB());
                bulletApp.getPhysicsSpace().remove(event.getNodeB().getControl(0));
                score++;
            }
        } 
        else if(event.getNodeB().getName().equals("Bala de captura"))
        {
            // Eliminamos la malla de captura de la escena
            rootNode.detachChild(event.getNodeB());
            bulletApp.getPhysicsSpace().remove(event.getNodeB().getControl(0));
            
            if (event.getNodeA().getName().equals("Basura"))
            {
                rootNode.detachChild(event.getNodeA());
                bulletApp.getPhysicsSpace().remove(event.getNodeB().getControl(0));
                score++;
            }
        }
    }
    
}
