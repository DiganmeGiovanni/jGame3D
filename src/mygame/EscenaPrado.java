/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Eddy
 */
public class EscenaPrado {
    
    private AssetManager assetManager;        // Administrador de texturas y modelos
    public Node raiz = new Node();            // Nodo padre de toda la escena
    public RigidBodyControl rigidBodyControl; // Para deteccion de colisiones
    
    //Elementos de la Escena
//    Spatial escena;
//    Spatial tree;
//    Spatial pino;
    
    public EscenaPrado(AssetManager assetManager){
        this.assetManager=assetManager;
        setScene();
        setPinosRandom(100, true);
        setPinosRandom(100, false);
        setTreesRandom(100, true);
        setTreesRandom(100, false);
        
        setPhisycs();
    }
    
    private void setScene(){
        Spatial escena = assetManager.loadModel("Scenes/PradoConBasura.j3o");
        escena.setLocalScale(4f);
        escena.setLocalTranslation(0f, -10f, 0f);
        raiz.attachChild(escena);
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

            raiz.attachChild(pino);
        }
    }

    private void setTreesRandom(int n, boolean a) {
        float x, z;

        for (int i = 0; i < n; i++) {
          Spatial  tree = assetManager.loadModel("Models/arbol/arbol_hojassecas.j3o");
            x = (float) (Math.random() * 1000);
            //y = (float) (Math.random() * 1000);
            z = (float) (Math.random() * 1000);
            tree.setLocalScale(8.0f);
            if (a) {
                tree.setLocalTranslation(x, -9.5f, z);
            } else {
                tree.setLocalTranslation(-x, -9.5f, -z);
            }

            raiz.attachChild(tree);
        }

    }
    
    private void setPhisycs(){
         CollisionShape escenaShape = CollisionShapeFactory.createMeshShape(raiz);
        rigidBodyControl = new RigidBodyControl(escenaShape, 0);
        raiz.addControl(rigidBodyControl);
    }
    
}
