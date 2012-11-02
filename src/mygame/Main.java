package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 * Muestra simple de como agregar y mostrar un cubo en un escenario, por 
 * default JME nos genera los controles para mover la camara con
 * las teclas de direccion y para mover el cubo con w,s,d,a
 * 
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() 
    {
        flyCam.setMoveSpeed(100);
        Spatial escena = assetManager.loadModel("Scenes/PradoConBasura.j3o");
        escena.setLocalScale(4f);
        escena.setLocalTranslation(0f, -10f, 0f);
        rootNode.attachChild(escena);
        
        configurarLuces();
    }
    
    private void configurarLuces() 
    {
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
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
