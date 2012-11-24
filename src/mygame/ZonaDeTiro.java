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
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

/**
 *
 * @author Aguirre Alvarez J Giovanni
 */
public class ZonaDeTiro extends SimpleApplication implements PhysicsCollisionListener
{
    public static void main(String[] args) {
        new ZonaDeTiro().start();
    }
    
    /** Material para las redes disparadas */
    private Material matMalla;
    
    /** Estado de la aplicacion jBullet */
    private BulletAppState bulletApp;
    
    /** Nodos para las propiedades fisicas de las mallas disparadas */
    private RigidBodyControl fisicaMallas;
    
    /** Esfera con textura de malla para capturar a las basuras */
    private static final Sphere malla;
    
    /** Indica el que punto fue la colision de loe elementos*/
    Geometry mark;

    static
    {
        malla = new Sphere(32, 32, 7f, true, false);
        malla.scaleTextureCoordinates(new Vector2f(1, 1));
    }
    
    @Override
    public void simpleInitApp() 
    {
        initMark();
        
        /** Configuramos la fisica del juego */
        bulletApp = new BulletAppState();
        stateManager.attach(bulletApp);
        
        initMaterials();
        flyCam.setMoveSpeed(50);
        
        // Configuramos el listener para el disparo
        agregarListenerDisparo();
        
        // Agregamos el escenario
        EscenaBodega bodega = new EscenaBodega(assetManager);
        rootNode.attachChild(bodega.raiz);
        
        // configuramos fisica de la escena
        bulletApp.getPhysicsSpace().add(bodega.escenaRigidBody);
        bulletApp.getPhysicsSpace().addCollisionListener(this);
    }
    
    public void initMaterials()
    {
        // Material para las mallas de captura
        matMalla = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keyMalla = new TextureKey("Textures/mallaGris.jpg");
        keyMalla.setGenerateMips(true);
        Texture texMalla = assetManager.loadTexture(keyMalla);
        matMalla.setTexture("ColorMap", texMalla);
    }

    /** Agrega un keyListener para el evento de disparar una malla de captura */
    private void agregarListenerDisparo() 
    {
        ActionListener actionListener = new ActionListener() {
            public void onAction(String name, boolean isPressed, float tpf) 
            {
                if (name.equals("Disparo") && !isPressed) 
                {
                    crearMallaCapturadora();
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
    public void crearMallaCapturadora()
    {
        /** Creamos la esfera y la agregamos a la escena */
        Geometry mallaCaptura = new Geometry("Bala de captura", malla);
        mallaCaptura.setMaterial(matMalla);
        rootNode.attachChild(mallaCaptura);
        
        // La movemos a la posicion de la camara
        mallaCaptura.setLocalTranslation(cam.getLocation());
        
        // Configuramos la fisica de las mallas
        fisicaMallas = new RigidBodyControl(1);
        mallaCaptura.addControl(fisicaMallas);
        bulletApp.getPhysicsSpace().add(fisicaMallas);
        
        // Aplicamos aceleracion de disparo
        fisicaMallas.setLinearVelocity(cam.getDirection().mult(500));
    }
    
    /** Contrla los eventos que ocurren cuando se detecta una colision */
    public void collision(PhysicsCollisionEvent event) 
    {
        if (event.getNodeA().getName().equals("Bala de captura")) 
        {
            rootNode.detachChild(event.getNodeA());
            bulletApp.getPhysicsSpace().remove(fisicaMallas);
            mark.setLocalTranslation(event.getLocalPointB());
            rootNode.attachChild(mark);
            //System.out.println("Colision A sobre: " + event.getNodeB().getName());
        } 
        else if(event.getNodeB().getName().equals("Bala de captura"))
        {
            rootNode.detachChild(event.getNodeB());
            bulletApp.getPhysicsSpace().remove(fisicaMallas);
            mark.setLocalTranslation(event.getLocalPointA());
            rootNode.attachChild(mark);
            //System.out.println("Colision B");
        }
    }
    
}