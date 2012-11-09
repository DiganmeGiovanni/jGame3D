package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 * Muestra simple de como agregar y mostrar un cubo en un escenario, por default
 * JME nos genera los controles para mover la camara con las teclas de direccion
 * y para mover el cubo con w,s,d,a
 *
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private CharacterControl player;
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    private RigidBodyControl landscape2;

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(100);
        setUpKeys();
        configurarLuces();

        Spatial escena = assetManager.loadModel("Scenes/PradoConBasura.j3o");
        escena.setLocalScale(4f);
        escena.setLocalTranslation(0f, -10f, 0f);



        CollisionShape sceneShape =
                CollisionShapeFactory.createMeshShape((Node) escena);
        landscape = new RigidBodyControl(sceneShape, 0);
        escena.addControl(landscape);

        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(30);
        player.setPhysicsLocation(new Vector3f(0, 10, 0));
        rootNode.attachChild(escena);

        setTrees(3.0f, -9.5f, -2.0f, 6.0f);
        setTrees(50.0f, -9.5f, 65.0f, 5.0f);
        setTrees(-50.0f, -9.5f, -65.0f, 8.0f);
        setPinos(-70.0f, -9.5f, 50.0f, 3.0f);
        setHouse(70.0f, -9.5f, 50.0f, 1.0f);
        setTreesRandom(100, true);
        setTreesRandom(100, false);
        setPinosRandom(100, true);
        setPinosRandom(100, false);
        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().add(player);


    }

    private void setTrees(float x, float y, float z, float scale) {
        Spatial tree = assetManager.loadModel("Models/arbol/arbol_hojassecas.j3o");
        tree.setLocalScale(scale);
        tree.setLocalTranslation(x, y, z);
        rootNode.attachChild(tree);
    }

    private void setPinosRandom(int n, boolean a) {
        float x, z;
        for (int i = 0; i < n; i++) {
            Spatial pino = assetManager.loadModel("Models/pino2/pino2.j3o");
            x = (float) (Math.random() * 1000);
            z = (float) (Math.random() * 1000);
            pino.setLocalScale(6.0f);
            if (a) {
                pino.setLocalTranslation(-x, -9.5f, z);
            } else {
                pino.setLocalTranslation(x, -9.5f, -z);
            }

            rootNode.attachChild(pino);
        }
    }

    private void setTreesRandom(int n, boolean a) {
        float x, z;

        for (int i = 0; i < n; i++) {
            Spatial tree = assetManager.loadModel("Models/arbol/arbol_hojassecas.j3o");
            x = (float) (Math.random() * 1000);
            //y = (float) (Math.random() * 1000);
            z = (float) (Math.random() * 1000);
            tree.setLocalScale(8.0f);
            if (a) {
                tree.setLocalTranslation(x, -9.5f, z);
            } else {
                tree.setLocalTranslation(-x, -9.5f, -z);
            }

            rootNode.attachChild(tree);
        }

    }

    private void setPinos(float x, float y, float z, float scale) {
        Spatial pino = assetManager.loadModel("Models/pino2/pino2.j3o");
        pino.setLocalScale(scale);
        pino.setLocalTranslation(x, y, z);
        rootNode.attachChild(pino);
    }

    private void setHouse(float x, float y, float z, float scale) {
        Spatial pino = assetManager.loadModel("Models/LongHouse/LongHouse.j3o");
        pino.setLocalScale(scale);
        pino.setLocalTranslation(x, y, z);
        rootNode.attachChild(pino);
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

    @Override
    public void simpleUpdate(float tpf) {

        Vector3f camDir = cam.getDirection().clone().multLocal(0.6f);
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.4f);
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    private void setUpKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");
    }

    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Left")) {
            left = value;
        } else if (binding.equals("Right")) {
            right = value;
        } else if (binding.equals("Up")) {
            up = value;
        } else if (binding.equals("Down")) {
            down = value;
        } else if (binding.equals("Jump")) {
            player.jump();


        }
    }
}
