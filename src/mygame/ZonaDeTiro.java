/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

/**
 *
 * @author Aguirre Alvarez J Giovanni
 */
public class ZonaDeTiro extends SimpleApplication implements PhysicsCollisionListener
{
    public static void main(String[] args) 
    {
        new ZonaDeTiro().start();
    }
    
    /** Elementos para la configuracion fisica */
    private BulletAppState bulletApp;
    private RigidBodyControl fisicaEcoBalls;
    private CharacterControl fisicaPersonaje;
    private Vector3f walkDirection = new Vector3f();
    
    /** Indica el que punto fue la colision de loe elementos*/
    Geometry mark;
    
    /** Creamos el personaje a partir de su modelo */
    Spatial personaje;
    
    /** Nombre del escenario que se muestra actualmente*/
    String nomEscena = "";
    
    Interfaz interfaz;

    @Override
    public void simpleInitApp() 
    {
        initMark();
        /** Inicializamos los recursos graficos para que puedan ser usados */
        new RecursosGraficos(assetManager);
        
        personaje=assetManager.loadModel("/Models/robo.j3o");
        /** Configuramos la fisica del juego */
        bulletApp = new BulletAppState();
        stateManager.attach(bulletApp);
        
        flyCam.setMoveSpeed(200);
        
        //Agregamos la interfaz
        interfaz= new Interfaz(assetManager, guiNode, guiFont, settings.getWidth(), settings.getHeight());
        
        // Configuramos el listener para el disparo
        agregarListenerDisparo();
        
        // Agregamos el escenario
      //  EscenaBodega escena = new EscenaBodega(assetManager);
        MonkeyLand escena = new MonkeyLand(assetManager, bulletApp, rootNode);
        nomEscena = escena.raizPrincipal.getName();
        rootNode.attachChild(escena.raizPrincipal);
        
        ubicarPersonaje();
        
        // configuramos fisica de la escena
        bulletApp.getPhysicsSpace().add(escena.escenaRigidBody);
//         bulletApp.getPhysicsSpace().add(fisicaPersonaje);
        bulletApp.getPhysicsSpace().addCollisionListener(this);
    }
    
    /** Configura la posicion inicial del personaje de acuerdo a la escena actual */
    private void ubicarPersonaje()
    {
        if (nomEscena.equals("MonkeyLand")) 
        {
            personaje.setLocalTranslation(675, 10, 985);
        }
        rootNode.attachChild(personaje);
    }

    
    /** Agrega un keyListener para el evento de disparar una malla de captura */
    private void agregarListenerDisparo() 
    {
        ActionListener actionListener = new ActionListener() {
            public void onAction(String name, boolean isPressed, float tpf) 
            {
                if (name.equals("Disparo") && !isPressed) 
                {
                    dispararEcoBall();
                    Audio.playShot(assetManager).playInstance();
                }
            }
        };
        
        inputManager.addMapping("Disparo", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "Disparo");
    }
    
    /**
     * Crea una marca para ubicar el punto de las colisiones
     */
    private void initMark() 
    {
        Sphere sphere = new Sphere(30, 30, 0.2f);
        mark = new Geometry("BOOM!", sphere);
        Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mark_mat.setColor("Color", ColorRGBA.Red);
        mark.setMaterial(mark_mat);
    }
    
    /**
     * Crea una malla de captura para la basura, es una esfera la cual es
     * acelarada y vuela en direccion de la camara
     */
    public void dispararEcoBall()
    {
        /** Creamos la ecoBall y la agregamos a la escena */
        Geometry ecoBall = RecursosGraficos.crearEcoBall();
        rootNode.attachChild(ecoBall);
        
        // La movemos a la posicion de la camara
        ecoBall.setLocalTranslation(cam.getLocation());
        
        // Configuramos la fisica de la ecoBall
        fisicaEcoBalls = new RigidBodyControl(1);
        ecoBall.addControl(fisicaEcoBalls);
        bulletApp.getPhysicsSpace().add(fisicaEcoBalls);
        
        // Aplicamos aceleracion de disparo
        fisicaEcoBalls.setLinearVelocity(cam.getDirection().mult(500));
    }
    
    /** Contrla los eventos que ocurren cuando se detecta una colision */
    public void collision(PhysicsCollisionEvent event) 
    {
        if (event.getNodeA().getName().equals("Bala de captura")) 
        {   
            // Eliminamos la malla de captura de la escena
            rootNode.detachChild(event.getNodeA());
            bulletApp.getPhysicsSpace().remove(event.getNodeA().getControl(0));
                
            if (!event.getNodeB().getName().equals(nomEscena)) 
            {
                // Eliminamos el objeto golpeado (Las cajas de basura por ejemplo)
                rootNode.detachChild(event.getNodeB());
                bulletApp.getPhysicsSpace().remove(event.getNodeB().getControl(0));
                
                // Mostramos el efecto de la colision
                mark.setLocalTranslation(event.getLocalPointB());
                rootNode.attachChild(mark);
                System.out.println("Colision A sobre: " + event.getNodeB().getName());
            }
        } 
        else if(event.getNodeB().getName().equals("Bala de captura"))
        {
            // Eliminamos la malla de captura de la escena
            rootNode.detachChild(event.getNodeB());
            bulletApp.getPhysicsSpace().remove(event.getNodeB().getControl(0));
            
            if (!event.getNodeA().getName().equals(nomEscena)) 
            {
                rootNode.detachChild(event.getNodeA());
                bulletApp.getPhysicsSpace().remove(event.getNodeB().getControl(0));
                
                // Mostramos el efecto de la explosion
                mark.setLocalTranslation(event.getLocalPointA());
                rootNode.attachChild(mark);
                System.out.println("Colision B sobre: " + event.getNodeA().getName());
            }
        }
    }
    
}
