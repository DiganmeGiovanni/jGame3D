/*
 * Objetos3D.java
 * Created on 21/11/2012
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 * Esta clase es utilizada como una especie de herramienta para generar
 * recursos graficos para colocar en escena
 * Contiene metodos que fabrican objetos que aparecen mas de una vez en los
 * escenarios del juego
 * 
 * @author Aguirre Alvarez J Giovanni
 * 
 */
public class Basura
{
    private AssetManager assetManager;
    
    // Materiales utilizados
    private Material matBasura;
    private Material matCajaLatas;
    
    public Basura(AssetManager assetManager)
    {
        this.assetManager = assetManager;
        initMaterials();
    }
    
    /** Configura los materiales para la basura*/
    public void initMaterials()
    {
        // CAJA DE BASURA    
        matBasura = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keyBasura = new TextureKey("Textures/Basura/basuraBloque.jpg");
        keyBasura.setGenerateMips(true);
        Texture texBasura = assetManager.loadTexture(keyBasura);
        texBasura.setWrap(Texture.WrapMode.Repeat);
        matBasura.setTexture("ColorMap", texBasura);
        
        // CAJA DE LATAS Y BOTELLAS
        matCajaLatas = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keyLatas = new TextureKey("Textures/Basura/latas.jpg");
        keyLatas.setGenerateMips(true);
        Texture texLatas = assetManager.loadTexture(keyLatas);
        texLatas.setWrap(Texture.WrapMode.Repeat);
        matCajaLatas.setTexture("ColorMap", texLatas);
    }
    
    /**
     * Crea un conjunto de cajas de basura y las adjunta a un nodo principal
     * @return  El nodo que contiene todo el bloque de cajas
     */
    public Node crearCajaBasura()
    {
        Node raiz = new Node("Cajas de basura");
        
        Box box = new Box(Vector3f.ZERO, 15, 15, 15);
        box.scaleTextureCoordinates(new Vector2f(1, 1));
        Geometry caja = new Geometry("Caja basura", box);
        caja.setMaterial(matBasura);
        caja.rotate(0, (float)Math.toRadians(45), 0);
        raiz.attachChild(caja);
        
        return raiz;
    }
    
    /**
     * Crea una caja que contiene latas, una caja de malla dentro de la cual
     * hay latas (Este efecto se consigue con una textura)
     * @return El nodo del que cuelgan las cajas de latas
     */
    public Node crearCajaLatas()
    {
        Node raiz = new Node("Cajas de latas");
        
        Box box = new Box(Vector3f.ZERO, 30, 30, 40);
        box.scaleTextureCoordinates(new Vector2f(1, 1));
        Geometry caja = new Geometry("Caja de latas", box);
        caja.setMaterial(matCajaLatas);
        raiz.attachChild(caja);
        
        return raiz;
    }

}
