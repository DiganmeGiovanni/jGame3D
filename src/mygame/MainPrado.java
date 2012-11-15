/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;

/**
 *
 * @author Eddy
 */
public class MainPrado extends SimpleApplication implements ActionListener {

    static int s = 0;
    private int vidas = 3, xV1 = 630, xV2 = 670, xV3 = 710;
    private int score;
    private BulletAppState bulletApp;
    private CharacterControl personajeRigidBody;
    private Vector3f walkDirection = new Vector3f();
    private boolean izq = false, der = false, arriba = false, abajo = false;
    private BitmapText textScore;

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(800, 600);
        MainPrado mp = new MainPrado();
        mp.setSettings(settings);
        mp.start();
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
        configurarLuces();



        segundero();
//        m.encabezado("Puntuacion:", 100);
//        m.encabezado("Vidas:", 580);
        setLife("life1", xV1);
        setLife("life2", xV2);
        setLife("life3", xV3);
        score();

        // Adjuntamos la escena, el personaje y los espacios fisicos a la raiz
        EscenaPrado prado = new EscenaPrado(assetManager);
        rootNode.attachChild(prado.raiz);
        bulletApp.getPhysicsSpace().add(prado.rigidBodyControl);
        bulletApp.getPhysicsSpace().add(personajeRigidBody);
    }

    /**
     * Configura el comportamiento de colisiones fisicas sobre el personaje o
     * jugador principal. Esto se hace creando una especie de capsula alrededor
     * del personaje Asi mismo se configura la posicion inical del personaje
     */
    private void configurarFisicaPersonaje() {                                                         // radio  alto   eje
        CapsuleCollisionShape capsula = new CapsuleCollisionShape(1.5f, 6f, 1);
        personajeRigidBody = new CharacterControl(capsula, 0.05f);
        personajeRigidBody.setJumpSpeed(20);
        personajeRigidBody.setFallSpeed(30);
        personajeRigidBody.setGravity(30);
        personajeRigidBody.setPhysicsLocation(new Vector3f(0, 10, 0));
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
        inputManager.addMapping("Salir", new KeyTrigger(KeyInput.KEY_ESCAPE));

        inputManager.addListener(this, "Izquierda");
        inputManager.addListener(this, "Derecha");
        inputManager.addListener(this, "Arriba");
        inputManager.addListener(this, "Abajo");
        inputManager.addListener(this, "Saltar");
        inputManager.addListener(this, "Salir");
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
        } else if (name.equals("Salir")) {
            System.exit(0);
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
        if (s == 20) {
            vidas = 2;
        }
        
        if (s==40) {
            vidas=1;
        }
        
        if (s==60) {
            vidas=0;
        }
        if (vidas==0) {
            gameover();
        }
        checarVidas();
        if (s == 30) {
            score = 20;
            textScore.setText("" + score);
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

        personajeRigidBody.setWalkDirection(walkDirection);
        cam.setLocation(personajeRigidBody.getPhysicsLocation());
    }

    private void configurarLuces() {
        // Agregamos luces para poder ver la escena
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl);
    }

    private void setLife(String life, int xl) {
        Picture pLife = new Picture(life);
        pLife.setImage(assetManager, "Interface/Lifes.png", true);
        pLife.setWidth(32);
        pLife.setHeight(32);
        pLife.setPosition(xl, 21 * 26);
        guiNode.attachChild(pLife);
    }

    private void checarVidas() {
//        
        if (vidas == 2) {
            quitLife("life3");

        } else if (vidas == 1) {
            quitLife("life2");
        } else if (vidas == 0) {
            quitLife("life1");
        }

    }

    private void quitLife(String name) {
        guiNode.detachChildNamed(name);
    }

    public void segundero() {
        // Display a line of text with a default font
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText text = new BitmapText(guiFont, false);
        text.setSize(guiFont.getCharSet().getRenderedSize());
        text.setColor(ColorRGBA.Blue);
        HiloSegundos hs = new HiloSegundos(text);
        hs.start();
        text.setLocalTranslation(380, 21 * 27, 0);
        guiNode.attachChild(text);

    }

    public void score() {
        Picture pScore = new Picture("HUD score");
        pScore.setImage(assetManager, "Interface/score.png", true);
        pScore.setWidth(32);
        pScore.setHeight(32);
        pScore.setPosition(100, 21 * 26);
        guiNode.attachChild(pScore);

        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        textScore = new BitmapText(guiFont, false);
        textScore.setSize(guiFont.getCharSet().getRenderedSize());
        textScore.setColor(ColorRGBA.Blue);
        textScore.setText("" + score);
        textScore.setLocalTranslation(150, 21 * 27, 0);
        guiNode.attachChild(textScore);
    }

    private void gameover() {
        guiNode.detachAllChildren();
        Picture pOver = new Picture("HUD gameover");
        pOver.setImage(assetManager, "Interface/gameover.png", true);
        pOver.setWidth(settings.getWidth() / 2);
        pOver.setHeight(settings.getHeight() / 2);
        pOver.setPosition(settings.getWidth() / 4, settings.getHeight() / 4);
        guiNode.attachChild(pOver);
    }
       
}
