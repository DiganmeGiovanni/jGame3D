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
    private Material matMadera;
    
    // Elementos de la escena
    private static final Box suelo;
    private static Node montana = new Node("Montañas");
    private static Node camino  = new Node("Camino");  // Camino para subir a casa
    private static Node barandal= new Node("Barandal"); //Cerca de seguridad
    
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
        crearBarandales();
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
        
        // MADERA
        matMadera = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keyMadera = new TextureKey("Textures/maderaVieja.jpg");
        keyMadera.setGenerateMips(true);
        Texture texMadera = assetManager.loadTexture(keyMadera);
        texMadera.setWrap(Texture.WrapMode.Repeat);
        matMadera.setTexture("ColorMap", texMadera);
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
        Box caminoCentral = new Box(Vector3f.ZERO, 100, 0.1f, 550);
        caminoCentral.scaleTextureCoordinates(new Vector2f(25, 12.5f));
        Geometry geomCamCentral = new Geometry("Camino central", caminoCentral);
        geomCamCentral.setMaterial(matCamino);
        geomCamCentral.setLocalTranslation(0, 180, -350);
        geomCamCentral.rotate((float)Math.toRadians(20), 0, 0);
        camino.attachChild(geomCamCentral);
        
        Box camLateral = new Box(Vector3f.ZERO, 25, 0.1f, 550);
        camLateral.scaleTextureCoordinates(new Vector2f(75, 6f));
        Geometry geomCamLateral = new Geometry("Camino lateral", camLateral);
        geomCamLateral.setMaterial(matPasto);
        geomCamLateral.setLocalTranslation(-125, 180, -350);
        geomCamLateral.rotate((float)Math.toRadians(20), 0, 0);
        camino.attachChild(geomCamLateral);
        
        Geometry geomCamLateral2 = geomCamLateral.clone(true);
        geomCamLateral2.move(250, 0, 0);
        camino.attachChild(geomCamLateral2);
        
        Geometry geomCamLat = geomCamLateral.clone(false);
        geomCamLat.setMaterial(matTierra);
        geomCamLat.move(-50, 0, 0);
        camino.attachChild(geomCamLat);
        
        Geometry geomCamLat2 = geomCamLat.clone(true);
        geomCamLat2.move(350, 0, 0);
        camino.attachChild(geomCamLat2);
        
        rootNode.attachChild(camino);
    }

    /** Crea cercas protectores para los limites del terreno */
    private void crearBarandales() 
    {
        // Postes verticales
        Box vertical = new Box(Vector3f.ZERO, 3, 40, 3);
        vertical.scaleTextureCoordinates(new Vector2f(1, 1));
        for (int i=1,x=0; i<=6; i++,x+=30) 
        {
            Geometry verticalGeom = new Geometry("Barandal vertical", vertical);
            verticalGeom.setMaterial(matMadera);
            verticalGeom.setLocalTranslation(x, 40, 200);
            barandal.attachChild(verticalGeom);
        }
        
        // Postes horizontales
        Box horizontal = new Box(Vector3f.ZERO, 3, 75, 1.5f);
        horizontal.scaleTextureCoordinates(new Vector2f(1, 1));
        for (int i=1,y=17; i<=4; i++,y+=20) 
        {
            Geometry horizontalGeom = new Geometry("Barandal horizontal", horizontal);
            horizontalGeom.setMaterial(matMadera);
            horizontalGeom.setLocalTranslation(75, y, 200);
            horizontalGeom.rotate(0, 0, (float)Math.toRadians(90));
            barandal.attachChild(horizontalGeom);
        }
        
        posicionarBarandales(barandal);
    }

    /** Coloca barandales en las posiciones necesarias */
    private void posicionarBarandales(Node barandal) 
    {
        barandal.setLocalTranslation(-697, 0, 797);
        rootNode.attachChild(barandal);
        
        int x=0;
        for (int i=1; i<=9; i++,x+=150) 
        {
            Node tmp = barandal.clone(false);
            tmp.move(x, 0, 0);
            rootNode.attachChild(tmp);
        }
    }
}
