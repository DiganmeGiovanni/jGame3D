/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 *
 * @author Aguirre Alvarez J Giovanni
 */
public class DulceHogar extends SimpleApplication
{
    //Materiales de la escena
    private Material matTierra;
    private Material matPasto;
    private Material matRoca;
    private Material matCamino;
    
    // Elementos de la escena
    private static final Box suelo;
    private static Node montana = new Node("Montañas");
    private static Node camino  = new Node("Camino");  // Camino para subir a casa
    
    static
    {
        suelo = new Box(Vector3f.ZERO, 700, 0.1f, 1000);
        suelo.scaleTextureCoordinates(new Vector2f(5, 5));
        
    }
    
    public static void main(String[] args) 
    {
        new DulceHogar().start();
    }
    
    @Override
    public void simpleInitApp() 
    {
        flyCam.setMoveSpeed(150);
        initMaterials();
        crearSuelo();
        crearMontanas();
        crearCamino();
    }
    
    /** Crea y configura los materiales utilizados en la escena */
    private void initMaterials()
    {
        // SUELO    
        matTierra = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keySuelo = new TextureKey("Textures/DulceHogar/tierra.jpg");
        keySuelo.setGenerateMips(true);
        Texture texSuelo = assetManager.loadTexture(keySuelo);
        texSuelo.setWrap(Texture.WrapMode.Repeat);
        matTierra.setTexture("ColorMap", texSuelo);
        
        // ROCA
        matRoca = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keyRoca = new TextureKey("Textures/DulceHogar/arena.jpg");
        keyRoca.setGenerateMips(true);
        Texture texRoca = assetManager.loadTexture(keyRoca);
        texRoca.setWrap(Texture.WrapMode.Repeat);
        matRoca.setTexture("ColorMap", texRoca);
        
        // PASTO
        matPasto = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keyPasto = new TextureKey("Textures/DulceHogar/pasto.jpg");
        keyPasto.setGenerateMips(true);
        Texture texPasto = assetManager.loadTexture(keyPasto);
        texPasto.setWrap(Texture.WrapMode.Repeat);
        matPasto.setTexture("ColorMap", texPasto);
        
        // CAMINO
        matCamino = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keyCamino = new TextureKey("Textures/pisoMarmol.jpg");
        keyCamino.setGenerateMips(true);
        Texture texCamino = assetManager.loadTexture(keyCamino);
        texCamino.setWrap(Texture.WrapMode.Repeat);
        matCamino.setTexture("ColorMap", texCamino);
    }

    /** Crea el suelo y lo agrega a la raiz */
    private void crearSuelo() 
    {
        Geometry geomSuelo = new Geometry("Suelo", suelo);
        geomSuelo.setMaterial(matTierra);
        geomSuelo.setLocalTranslation(0, -1f, 0);
        rootNode.attachChild(geomSuelo);
    }
    
    /** Crea las montanas de la escena */
    private void crearMontanas()
    {
        //Configura la parte de tierra de la montana
        Box bloqueTierra = new Box(Vector3f.ZERO, 250, 300, 150);
        bloqueTierra.scaleTextureCoordinates(new Vector2f(4, 2));
        Geometry geomMontanas = new Geometry("Montana", bloqueTierra);
        geomMontanas.setMaterial(matRoca);
        geomMontanas.setLocalTranslation(0, -200, 0);
        montana.attachChild(geomMontanas);
        
        //Configura la parte de pasto de la montana
        Box bloquePasto = new Box(Vector3f.ZERO, 250, 5, 150);
        bloquePasto.scaleTextureCoordinates(new Vector2f(4, 2));
        Geometry geomPasto = new Geometry("Pasto", bloquePasto);
        geomPasto.setMaterial(matPasto);
        geomPasto.setLocalTranslation(0, 105, 0);
        montana.attachChild(geomPasto);
        
        posicionaMontanas(montana);
    }
    
    /** Posiciona las montañas en el lugar adecuado */
    private void posicionaMontanas(Node montana)
    {
        for (int i=1,z=50,y=0; i<=4; i++,z-=300,y+=100) 
        {
            // lado izquierdo
            Node tmp = montana.clone(false);
            tmp.setLocalTranslation(-450, y, z);
            rootNode.attachChild(tmp);
            
            // lado derecho
            Node tmp2 = montana.clone(false);
            tmp2.setLocalTranslation(450, y, z);
            rootNode.attachChild(tmp2);
        }
    }

    /** Crea una rampa para llegar a la parte alta de la escena*/
    private void crearCamino() 
    {
        Box caminoCentral = new Box(Vector3f.ZERO, 100, 0.1f, 600);
        caminoCentral.scaleTextureCoordinates(new Vector2f(5, 50));
        Geometry geomCamCentral = new Geometry("Camino central", caminoCentral);
        geomCamCentral.setMaterial(matCamino);
        geomCamCentral.setLocalTranslation(0, 200, -250);
        
        rootNode.attachChild(geomCamCentral);
    }
}
