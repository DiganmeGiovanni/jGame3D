/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;

/**
 *
 * @author Aguirre Alvarez J Giovanni
 */
public class MainBodega extends SimpleApplication implements ActionListener
{
    private BulletAppState bulletApp;
    private CharacterControl personajeRigidBody;
    private Vector3f walkDirection = new Vector3f();
    private boolean izq = false, der = false, arriba = false, abajo = false;
    
    public static void main(String[] args) {
        new MainBodega().start();
    }

    @Override
    public void simpleInitApp() 
    {
        // Configuramos comportamient fisico
        bulletApp = new BulletAppState();
        stateManager.attach(bulletApp);
        
        flyCam.setMoveSpeed(500);
        configurarKeys();
        configurarFisicaPersonaje();
        
        // Adjuntamos la escena, el personaje y los espacios fisicos a la raiz
        EscenaBodega bodega = new EscenaBodega(assetManager);
        //bodega.raiz.setLocalScale(0.5f);
        rootNode.attachChild(bodega.raiz);
        bulletApp.getPhysicsSpace().add(bodega.rigidBodyControl);
        bulletApp.getPhysicsSpace().add(personajeRigidBody);
    }
    
    /**
     * Configura el comportamiento de colisiones fisicas sobre el personaje
     * o jugador principal.
     * Esto se hace creando una especie de capsula alrededor del personaje
     * Asi mismo se configura la posicion inical del personaje
     */
    private void configurarFisicaPersonaje()
    {                                                         // radio  alto   eje
        CapsuleCollisionShape capsula = new CapsuleCollisionShape(10.5f,   26f,   1);
        personajeRigidBody = new CharacterControl(capsula, 0.05f);
        personajeRigidBody.setJumpSpeed(20);
        personajeRigidBody.setFallSpeed(30);
        personajeRigidBody.setGravity(30);
        personajeRigidBody.setPhysicsLocation(new Vector3f(0, 30, 0));
    }

    /** Mapeo de keys de navegacion*/
    private void configurarKeys() 
    {
        inputManager.addMapping("Izquierda", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Derecha"  , new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Arriba"   , new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Abajo"    , new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Saltar"   , new KeyTrigger(KeyInput.KEY_SPACE));
        
        inputManager.addListener(this, "Izquierda");
        inputManager.addListener(this, "Derecha");
        inputManager.addListener(this, "Arriba");
        inputManager.addListener(this, "Abajo");
        inputManager.addListener(this, "Saltar");
    }
    
    /** Definimos las acciones desencadenadas por las teclas presionadas */
    public void onAction(String name, boolean isPressed, float tpf) 
    {
        if (name.equals("Izquierda")) 
        {
            izq = isPressed;
        } 
        else if (name.equals("Derecha"))
        {
            der = isPressed;
        } 
        else if (name.equals("Arriba"))
        {
            arriba = isPressed;
        } 
        else if (name.equals("Abajo"))
        {
            abajo = isPressed;
        } 
        else if (name.equals("Saltar"))
        {
            personajeRigidBody.jump();
        }
    }
    
    /**
     * Aqui controlaremos el caminado
     * Determinaremos en que direccion se camina interpretando la direccion
     * de la camara.
     * Tambien nos aseguramos de que la camara se mueva junto al jugador
     */
    @Override
    public void simpleUpdate(float tpf)
    {
        Vector3f camDirection = cam.getDirection().clone().multLocal(0.6f);
        Vector3f camLeft      = cam.getLeft().clone().multLocal(0.4f);
        
        walkDirection.set(0, 0, 0);
        if (izq)    { walkDirection.addLocal(camLeft);               }
        if (der)    { walkDirection.addLocal(camLeft.negate());      }
        if (arriba) { walkDirection.addLocal(camDirection);          }
        if (abajo)  { walkDirection.addLocal(camDirection.negate()); }
        
        personajeRigidBody.setWalkDirection(walkDirection);
        cam.setLocation(personajeRigidBody.getPhysicsLocation());
    }
    
}
