/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.audio.Environment;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import com.jme3.ui.Picture;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import sun.applet.Main;

/**
 *
 * @author Eddy
 */
public class MainPrado extends SimpleApplication implements ActionListener {

    AudioNode chimenea;
    Interfaz interfaz;
    static int s = 0;
    static int salud=4;
    static int vidas = 3;
    static int score;
    private Node personaje;
    private ChaseCamera chaseCam;
    private BulletAppState bulletApp;
    private CharacterControl personajeRigidBody;
    private Vector3f walkDirection = new Vector3f();
    private boolean izq = false, der = false, arriba = false, abajo = false;    
    

//    public static void main(String[] args) {
//        MainPrado app = new MainPrado();
//        AppSettings cfg = new AppSettings(true);
//       
//        cfg.setFrameRate(60); // set to less than or equal screen refresh rate
//        cfg.setVSync(true);   // prevents page tearing
//        cfg.setFrequency(60); // set to screen refresh rate
//        cfg.setResolution(1280, 720);
//        cfg.setFullscreen(true);
//        cfg.setSamples(2);    // anti-aliasing
//        cfg.setTitle("My jMonkeyEngine 3 Game"); // branding: window name
//        try {
//            // Branding: window icon
//            cfg.setIcons(new BufferedImage[]{ImageIO.read(new File("assets/Interface/Lifes.png"))});
//        } catch (IOException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Icon missing.", ex);
//        }
//// branding: load splashscreen from assets
//cfg.setSettingsDialogImage("Interface/gameover.png"); 
////app.setShowSettings(false); // or don't display splashscreen
//
//        app.setSettings(cfg);
//        app.start();
//
//    }
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                AppSettings settings = new AppSettings(true);
                settings.setWidth(1280);
                settings.setHeight(720);
                settings.setFullscreen(true);
                MainPrado canvasApplication = new MainPrado();
                canvasApplication.setSettings(settings);
                canvasApplication.createCanvas();
                final JmeCanvasContext ctx = (JmeCanvasContext) canvasApplication.getContext();
                ctx.setSystemListener(canvasApplication);
                Dimension dim = new Dimension(1280, 720);
                ctx.getCanvas().setPreferredSize(dim);
                Start st= new Start("Juego", ctx);
                st.pack();
                st.setVisible(true);
                canvasApplication.startCanvas();
            }
        });
    }

    @Override
    public void simpleInitApp() {
        // Configuramos comportamient fisico        
        this.setDisplayStatView(false);
        this.setDisplayFps(false);
        bulletApp = new BulletAppState();
        stateManager.attach(bulletApp);
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(500);        
        configurarKeys();
        configurarFisicaPersonaje();
        thirdPerson();
        configurarLuces();
        
        //Agregamos la interfaz
        interfaz= new Interfaz(assetManager, guiNode, guiFont, settings.getWidth(), settings.getHeight());

        // Adjuntamos la escena, el personaje y los espacios fisicos a la raiz

        EscenaPrado prado = new EscenaPrado(assetManager);
//        Audio.playChimenea(assetManager);
        rootNode.attachChild(prado.raiz);
        setAudio();
        getPhysicsSpace().add(prado.rigidBodyControl);
        getPhysicsSpace().add(personajeRigidBody);
    }

    /**
     * Configura el comportamiento de colisiones fisicas sobre el personaje o
     * jugador principal. Esto se hace creando una especie de capsula alrededor
     * del personaje Asi mismo se configura la posicion inical del personaje
     */
    private void configurarFisicaPersonaje() {                                                         // radio  alto   eje
        CapsuleCollisionShape capsula = new CapsuleCollisionShape(1.5f, 6f, 1);
        personajeRigidBody = new CharacterControl(capsula, 0.05f);
        personaje= (Node)assetManager.loadModel("/Models/robo.j3o");
        personaje.addControl(personajeRigidBody);        
        personajeRigidBody.setJumpSpeed(20);
        personajeRigidBody.setFallSpeed(30);
        personajeRigidBody.setGravity(40);
        personajeRigidBody.setPhysicsLocation(new Vector3f(0, 10, 0));
        rootNode.attachChild(personaje);
    }
    
    private void thirdPerson() {
        flyCam.setEnabled(false);
        chaseCam = new ChaseCamera(cam, personaje, inputManager);
    }

    /**
     * Mapeo de keys de navegacion
     */
    private void configurarKeys() {
        inputManager.addMapping("Izquierda", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Derecha", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Arriba", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Abajo", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Saltar", new KeyTrigger(KeyInput.KEY_SPACE));        
        inputManager.addListener(this, "Izquierda");
        inputManager.addListener(this, "Derecha");
        inputManager.addListener(this, "Arriba");
        inputManager.addListener(this, "Abajo");
        inputManager.addListener(this, "Saltar");
        
    }

    /**
     * Definimos las acciones desencadenadas por las teclas presionadas
     */
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("Izquierda")) {
            izq = isPressed;
        } else if (name.equals("Derecha")) {
            der = isPressed;
        } else if (name.equals("Arriba")) {
            arriba = isPressed;
        } else if (name.equals("Abajo")) {
            abajo = isPressed;
        } else if (name.equals("Saltar")) {
            personajeRigidBody.jump();
        }  
            
        
    }

    /**
     * Aqui controlaremos el caminado Determinaremos en que direccion se camina
     * interpretando la direccion de la camara. Tambien nos aseguramos de que la
     * camara se mueva junto al jugador
     */
    @Override
    public void simpleUpdate(float tpf) {

        Vector3f camDirection = cam.getDirection().clone().multLocal(0.6f);
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.4f);
        camDirection.y = 0;
        camLeft.y = 0;
        if (s==20) {
            salud=3;
            
        }
        if (s==40) {
            salud=2;            
        }
        if (s==60) {
            salud=1;
        }
        interfaz.checarVidas();
        
        if (s == 30) {
            score = 20;
            interfaz.textScore.setText("" + score);
        }

        walkDirection.set(0, 0, 0);
        if (izq) {
            walkDirection.addLocal(camLeft);
        }
        if (der) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (arriba) {
            walkDirection.addLocal(camDirection);
        }
        if (abajo) {
            walkDirection.addLocal(camDirection.negate());
        }
        
        personajeRigidBody.setViewDirection(walkDirection);

        personajeRigidBody.setWalkDirection(walkDirection);
        listener.setLocation(personajeRigidBody.getPhysicsLocation());
        listener.setRotation(cam.getRotation()); 
    }

    private void configurarLuces() {
//         Agregamos luces para poder ver la escena
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);
//
//        DirectionalLight dl = new DirectionalLight();
//        dl.setColor(ColorRGBA.White);
//        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        Vector3f direction = new Vector3f(-0.1f, -0.7f, -1).normalizeLocal();
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(direction);
        dl.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
        rootNode.addLight(dl);
    }

  
    private PhysicsSpace getPhysicsSpace() {
        return bulletApp.getPhysicsSpace();
    }
    
    public void setAudio(){
        chimenea= new AudioNode(assetManager, "/Sounds/effects/chimenea.wav");
        Environment hall= new Environment( new float[]{ 17, 100f, 0.270f, -1000, -2500, 0, 1.49f, 0.21f, 1f, -2780, 0.300f, 0f, 0f, 0f, -1434, 0.100f, 0f, 0f, 0f, 0.250f, 1f, 0.250f, 0f, -5f, 5000f, 250f, 0f, 0x1f}  );       
        audioRenderer.setEnvironment(hall);
        audioRenderer.setListener(listener);
        chimenea.setPositional(true);
        chimenea.setMaxDistance(10f);
        chimenea.setRefDistance(5f);
        chimenea.setReverbEnabled(true);
        chimenea.setLocalTranslation(0, 122, 135);
        rootNode.attachChild(chimenea);
        
//        chimenea.play();
        
    }
    

    
}
