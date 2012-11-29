/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author Aguirre Alvarez J Giovanni
 */
public class ZonaDeTiro extends SimpleApplication implements PhysicsCollisionListener, ActionListener
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
    private boolean adelante=false, atras=false, izquierda=false, derecha=false;
    private boolean girarDer=false, girarIzq=false;
    
    /** Camara que se mueve detras del personaje */
    private ChaseCamera cam3Persona;
    
    /** Indica el que punto fue la colision de loe elementos*/
    Geometry mark;
    
    /** Creamos el personaje a partir de su modelo */
    Spatial personaje;
    
    /** Nombre del escenario que se muestra actualmente*/
    String nomEscena = "";
    
    Interfaz interfaz;
    private ChaseCamera chaseCam;

    @Override
    public void simpleInitApp() 
    {
        /** Configuramos la fisica del juego */
        bulletApp = new BulletAppState();
        stateManager.attach(bulletApp);
        
        initMark();
        
        /** Inicializamos los recursos graficos para que puedan ser usados */
        new RecursosGraficos(assetManager);
        cargarEscena();
        configurarPersonaje();
        thirdPerson();
//        flyCam.setEnabled(false);
//        cam3Persona = new ChaseCamera(cam, personaje, inputManager);
//        cam3Persona.setDragToRotate(true);
//        cam3Persona.setDefaultVerticalRotation((float)Math.toRadians(15));
//        cam3Persona.setSmoothMotion(true);
//        cam3Persona.setTrailingEnabled(false);
//        cam3Persona.setChasingSensitivity(15);
//        cam3Persona.setMaxDistance(500);
//        cam3Persona.setDefaultDistance(20);
        
        
        //Agregamos la interfaz
        interfaz= new Interfaz(assetManager, guiNode, guiFont, settings.getWidth(), settings.getHeight());
        
        // Configuramos el listener para el disparo
        //agregarListenerDisparo();
        configurarKeys();
        
    }
    
    /** Carga y configura la fisica del escenario*/
    private void cargarEscena()
    {
        // Agregamos el escenario
      //  EscenaBodega escena = new EscenaBodega(assetManager);
        MonkeyLand escena = new MonkeyLand(assetManager, bulletApp, rootNode);
        nomEscena = escena.raizPrincipal.getName();
        rootNode.attachChild(escena.raizPrincipal);
        
        // configuramos fisica de la escena
        bulletApp.getPhysicsSpace().add(escena.escenaRigidBody);
//         bulletApp.getPhysicsSpace().add(fisicaPersonaje);
        bulletApp.getPhysicsSpace().addCollisionListener(this);
    }
    
    /** Carga y configura la fisica del personaje */
    private void configurarPersonaje()
    {
        // Configuramos comportamiento fisico del personaje
        CapsuleCollisionShape capsula = new CapsuleCollisionShape(3, 4);
        fisicaPersonaje = new CharacterControl(capsula, 0.5f);
        fisicaPersonaje.setJumpSpeed(0);
        fisicaPersonaje.setFallSpeed(20);
        fisicaPersonaje.setGravity(0);
        
        // Cargamos el personaje y agregamos controles fisicos
        personaje = assetManager.loadModel("Models/robo.j3o");
        //personaje.rotate(0, (float)Math.toRadians(180), 0);
        personaje.addControl(fisicaPersonaje);
        bulletApp.getPhysicsSpace().add(fisicaPersonaje);
        
        // Colocamos al personaje en la escena
        if (nomEscena.equals("MonkeyLand")) 
        {
            fisicaPersonaje.setPhysicsLocation(new Vector3f(675, 5, 985));
//            fisicaPersonaje.setViewDirection(cam.getLeft());
        }
        rootNode.attachChild(personaje);
    }
    private void thirdPerson() {
        flyCam.setEnabled(false);
        chaseCam = new ChaseCamera(cam, personaje, inputManager);
    }
    /** Configura los listeners para movimiento del personaje y disparo*/
    private void configurarKeys()
    {
        inputManager.addMapping("Izquierda", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Derecha", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Adelante", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Atras", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Saltar", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Salir", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addMapping("Disparo", new KeyTrigger(KeyInput.KEY_SPACE));
        
        inputManager.addListener(this, "Izquierda");
        inputManager.addListener(this, "Derecha");
        inputManager.addListener(this, "Adelante");
        inputManager.addListener(this, "Atras");
        inputManager.addListener(this, "GirarDerecha");
        inputManager.addListener(this, "GirarIzquierda");
        inputManager.addListener(this, "Saltar");
        inputManager.addListener(this, "Salir");
        inputManager.addListener(this, "Disparo");
    }
    
    /** Definimos las acciones desencadenadas por las teclas presionadas */
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("Izquierda")) {
            izquierda = isPressed;
        } else if (name.equals("Derecha")) {
            derecha = isPressed;
        } else if (name.equals("Adelante")) {
            adelante = isPressed;
        } else if (name.equals("Atras")) {
            atras = isPressed;
        } else if (name.equals("GirarDerecha")) {
            girarDer = isPressed;
        } else if (name.equals("GirarIzquierda")) {
            girarIzq = isPressed;
        } else if (name.equals("Saltar")) {
            //personajeRigidBody.jump();
        } else if (name.equals("Disparo") && !isPressed) {
            dispararEcoBall();
        } else if (name.equals("Salir")) {
            System.exit(0);
        } 
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
                
            if (!event.getNodeB().getName().equals(nomEscena)
                    && !event.getNodeB().getName().equals("Textures/untitled.blend")) 
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
            
            if (!event.getNodeA().getName().equals(nomEscena)
                && !event.getNodeA().getName().equals("Textures/untitled.blend")) 
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
    
    
    @Override
    public void simpleUpdate(float tpf)
    {
        Vector3f camDir = cam.getDirection().clone().multLocal(2f);
        Vector3f camLeft= cam.getLeft().clone().multLocal(2f);
        
        camDir.y = 0f;
        camLeft.y= 0f;
        walkDirection.set(0, 0, 0);
        
        if (izquierda) {
            walkDirection.addLocal(camLeft);
        }
        if (derecha) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (adelante) {
            walkDirection.addLocal(camDir);
        }
        if (atras) {
            walkDirection.addLocal(camDir.negate());
        }
        fisicaPersonaje.setViewDirection(walkDirection);
        fisicaPersonaje.setWalkDirection(walkDirection);
        //cam.setLocation(personaje.getLocalTranslation().clone().addLocal(0, 0, 15));
    }
}
