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
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
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
    
    /** Datos de ususario */
    static int score = 0;
    static int salud = 4;
    static int vidas = 3;
    
    /** Elementos para la configuracion fisica */
    private BulletAppState bulletApp;
    private RigidBodyControl fisicaEcoBalls;
    private CharacterControl fisicaPersonaje;
    private Vector3f walkDirection = new Vector3f();
    private boolean adelante=false, atras=false, izquierda=false, derecha=false;
    private boolean girarDer=false, girarIzq=false, girarArriba=false, girarAbajo=false;;
    
    /** Camara que se mueve detras del personaje */
    private ChaseCamera cam3Persona;
    
    /** Indica el que punto fue la colision de loe elementos*/
    EfectoExplosion explosion;
    ParticleEmitter llama; //Crea una flama debajo del personaje para simular sus impulsores
    
    /** Creamos el personaje a partir de su modelo */
    Spatial personaje;
    
    /** Nombre del escenario que se muestra actualmente*/
    String nomEscena = "";
    
    Interfaz interfaz;

    @Override
    public void simpleInitApp() 
    {
        /** Configuramos la fisica del juego */
        bulletApp = new BulletAppState();
        stateManager.attach(bulletApp);
        
        /** Inicializamos los recursos graficos para que puedan ser usados */
        new RecursosGraficos(assetManager);
        cargarEscena();
        configurarPersonaje();
        thirdPerson();
        
        //Agregamos la interfaz
        interfaz= new Interfaz(assetManager, guiNode, guiFont, settings.getWidth(), settings.getHeight());
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        configurarKeys();
        //encenderMotorPersonaje();
    }
    
    /** Carga y configura la fisica del escenario*/
    private void cargarEscena()
    {
        // Agregamos el escenario
        //EscenaBodega escena = new EscenaBodega(assetManager, bulletApp);
        MonkeyLand escena = new MonkeyLand(assetManager, bulletApp, rootNode);
        nomEscena = escena.raizPrincipal.getName();
        rootNode.attachChild(escena.raizPrincipal);
        
        // configuramos fisica de la escena
        bulletApp.getPhysicsSpace().add(escena.escenaRigidBody);
        bulletApp.getPhysicsSpace().addCollisionListener(this);
    }
    
    /** Carga y configura la fisica del personaje */
    private void configurarPersonaje()
    {
        // Configuramos comportamiento fisico del personaje
        CapsuleCollisionShape capsula = new CapsuleCollisionShape(3, 4);
        fisicaPersonaje = new CharacterControl(capsula, 0.5f);
        fisicaPersonaje.setJumpSpeed(120);
        fisicaPersonaje.setFallSpeed(50);
        fisicaPersonaje.setGravity(150);
        
        // Cargamos el personaje y agregamos controles fisicos
        personaje = assetManager.loadModel("Models/robo.j3o");
        personaje.addControl(fisicaPersonaje);
        bulletApp.getPhysicsSpace().add(fisicaPersonaje);
        
        // Colocamos al personaje en la escena
        if (nomEscena.equals("MonkeyLand")) {
            fisicaPersonaje.setPhysicsLocation(new Vector3f(675, 5, 985));
            fisicaPersonaje.setViewDirection(cam.getLeft());
        } else if(nomEscena.equals("Bodega")) {
            fisicaPersonaje.setPhysicsLocation(new Vector3f(0, 10, 0));
            fisicaPersonaje.setViewDirection(cam.getLeft());
        }
        rootNode.attachChild(personaje);
    }
    
    private void thirdPerson() {
        flyCam.setEnabled(false);
        cam3Persona = new ChaseCamera(cam, personaje, inputManager);
//        cam3Persona.setDragToRotate(true);
//        cam3Persona.setDefaultVerticalRotation((float)Math.toRadians(15));
//        cam3Persona.setSmoothMotion(true);
//        cam3Persona.setTrailingEnabled(false);
//        cam3Persona.setChasingSensitivity(15);
//        cam3Persona.setMaxDistance(500);
//        cam3Persona.setDefaultDistance(20);
    }
    
    /** Configura los listeners para movimiento del personaje y disparo*/
    private void configurarKeys()
    {
        inputManager.addMapping("Izquierda", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Derecha", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Adelante", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Atras", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("GirarDerecha", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("GirarIzquierda", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("GirarArriba", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("GirarAbajo", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Saltar", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping("Salir", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addMapping("Disparo", new KeyTrigger(KeyInput.KEY_SPACE));
        
        inputManager.addListener(this, "Izquierda");
        inputManager.addListener(this, "Derecha");
        inputManager.addListener(this, "Adelante");
        inputManager.addListener(this, "Atras");
        inputManager.addListener(this, "GirarDerecha");
        inputManager.addListener(this, "GirarIzquierda");
        inputManager.addListener(this, "GirarArriba");
        inputManager.addListener(this, "GirarAbajo");
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
        } else if (name.equals("GirarArriba")) {
            girarArriba = isPressed;
        } else if (name.equals("GirarAbajo")) {
            girarAbajo = isPressed;
        } else if (name.equals("Saltar")) {
            fisicaPersonaje.jump();
        } else if (name.equals("Disparo") && !isPressed) {
            dispararEcoBall();
        } else if (name.equals("Salir")) {
            System.exit(0);
        }
        if (!isPressed) 
        {
            Vector3f camDir = cam.getDirection().clone();
            camDir.y = 0;
            fisicaPersonaje.setViewDirection(camDir);
        }
    }
    
    /**
     * Crea una malla de captura para la basura, es una esfera la cual es
     * acelarada y vuela en direccion de la camara
     */
    public void dispararEcoBall()
    {
        if (!adelante && !atras) // Solo dispara si no avanza o atrasa
        {
            /** Creamos la ecoBall y la agregamos a la escena */
            Geometry ecoBall = RecursosGraficos.crearEcoBall();

            // La movemos a la posicion de la camara
            posicionarEcoBall(ecoBall);
            rootNode.attachChild(ecoBall);
            
            // Configuramos la fisica de la ecoBall
            fisicaEcoBalls = new RigidBodyControl(1);
            ecoBall.addControl(fisicaEcoBalls);
            bulletApp.getPhysicsSpace().add(fisicaEcoBalls);

            // Aplicamos aceleracion de disparo
            Vector3f camDir = cam.getDirection().clone();
            camDir.y = 0;
            fisicaEcoBalls.setLinearVelocity(camDir.mult(800));
        }
    }
    
    /** 
     *  Posiciona la ecoBall de acuerdo a la direccion en que mira le personaje
     *  siempre la posicionara delante del pecho del robot
     */
    private void posicionarEcoBall(Geometry ecoBall)
    {
        Vector3f tmp = fisicaPersonaje.getViewDirection();
        Vector3f pos = personaje.getLocalTranslation().clone();
        float dist=8, alt=5; // Distancia entre el personaje y la ecoBall
        
        if (tmp.x < 0 && tmp.z == 0) 
        {
            pos.x-=dist;
            pos.y+=alt;
            ecoBall.setLocalTranslation(pos);
        }
        if (tmp.x == 0 && tmp.z < 0) {
            pos.z-=dist;
            pos.y+=alt;
        }
        else if(tmp.x < 0 && tmp.z < 0)
        {
            pos.x-=dist;
            pos.z-=dist;
            pos.y+=alt;
            ecoBall.setLocalTranslation(pos);
        }
        else if(tmp.x > 0 && tmp.z < 0)
        {
            pos.x+=dist;
            pos.z-=dist;
            pos.y+=alt;
            ecoBall.setLocalTranslation(pos);
        }
        else if(tmp.x > 0 && tmp.z > 0)
        {
            pos.x+=dist;
            pos.z+=dist;
            pos.y+=alt;
            ecoBall.setLocalTranslation(pos);
        }
        else if(tmp.x < 0 && tmp.z > 0)
        {
            pos.x-=dist;
            pos.z+=dist;
            pos.y+=alt;
            ecoBall.setLocalTranslation(pos);
        }
    }
    
    /**
     * Agrega una flama a la chimenea, como si estuviera encendida
     */
    private void encenderMotorPersonaje() {
        // CONFIGURAMOS LA FLAMA
        Material matRojo = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        matRojo.setTexture("Texture", assetManager.loadTexture("Textures/flame.png"));

        llama = new ParticleEmitter("Emisor", ParticleMesh.Type.Triangle, 30);
        llama.setMaterial(matRojo);
        llama.setImagesX(2);
        llama.setImagesY(2); // Textura de 2X2
        llama.setEndColor(new ColorRGBA(0, 0, 1, 1));      // Rojo
        llama.setStartColor(new ColorRGBA(0, 0, 1, 1)); //Amarillo
        llama.getParticleInfluencer().setInitialVelocity(new Vector3f(0, -2, 0));
        llama.setStartSize(2f);
        llama.setEndSize(0.8f);
        llama.setGravity(0, 0, 0);
        llama.setLowLife(0f);
        llama.setHighLife(1f);
        llama.getParticleInfluencer().setVelocityVariation(0.3f);

        // Posicionamos la flama
        Vector3f persLoc = fisicaPersonaje.getPhysicsLocation();
        persLoc.y-=0;
        llama.setLocalTranslation(persLoc);
        //llama.scale(0);
        
//        Audio.playChimenea(assetManager);
        rootNode.attachChild(llama);
    }
    
    /** Contrla los eventos que ocurren cuando se detecta una colision */
    public void collision(PhysicsCollisionEvent event) 
    {
        //AdministradorDeColisiones.colision(nomEscena, rootNode, bulletApp, event, score);
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
            fisicaPersonaje.setViewDirection(walkDirection);
        }
        if (derecha) {
            walkDirection.addLocal(camLeft.negate());
            fisicaPersonaje.setViewDirection(walkDirection);
        }
        if (adelante) {
            walkDirection.addLocal(camDir);
            fisicaPersonaje.setViewDirection(walkDirection);
        }
        if (atras) {
            walkDirection.addLocal(camDir.negate());
            fisicaPersonaje.setViewDirection(walkDirection);
        }
        if (girarDer) {
            cam3Persona.setDefaultHorizontalRotation
                    (cam3Persona.getHorizontalRotation() + (float)Math.toRadians(2));
            fisicaPersonaje.setViewDirection(camDir);
        }
        if (girarIzq) {
            cam3Persona.setDefaultHorizontalRotation
                    (cam3Persona.getHorizontalRotation() + (float)Math.toRadians(-2));
            fisicaPersonaje.setViewDirection(camDir);
        }
        if (girarArriba) {
            cam3Persona.setDefaultVerticalRotation
                    (cam3Persona.getVerticalRotation() + (float)Math.toRadians(2));
        }
        if (girarAbajo) {
            cam3Persona.setDefaultVerticalRotation
                    (cam3Persona.getVerticalRotation() + (float)Math.toRadians(-2));
        }
        
        fisicaPersonaje.setWalkDirection(walkDirection);
        //posicionarFlama();
        interfaz.checarScore();
        interfaz.checarVidas();
        checarAltitud();
    }
    
    /** Revisa la altitud en caso de que el personaje haya caido al vacio */
    private void checarAltitud()
    {
        if (fisicaPersonaje.getPhysicsLocation().y < -200) {
            salud=1;;
            interfaz.checarVidas();
            vidas--;
            // Colocamos al personaje en la escena
            if (nomEscena.equals("MonkeyLand")) {
                fisicaPersonaje.setPhysicsLocation(new Vector3f(675, 5, 985));
                fisicaPersonaje.setViewDirection(cam.getLeft());
            } else if(nomEscena.equals("Bodega")) {
                fisicaPersonaje.setPhysicsLocation(new Vector3f(0, 10, 0));
                fisicaPersonaje.setViewDirection(cam.getLeft());
            }
        }
    }   

    /** Ubica la flama del robot justo debajo de el cada vez que este se mueva*/
    private void posicionarFlama() {
        Vector3f persLoc = fisicaPersonaje.getPhysicsLocation();
        persLoc.y-=1;
        llama.setLocalTranslation(persLoc);
    }
    
    
}
