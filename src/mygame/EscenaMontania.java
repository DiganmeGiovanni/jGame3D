/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;

/**
 *
 * @author Aguirre Alvarez J Giovanni
 */
public class EscenaMontania
{
    SimpleApplication padre;
    AssetManager assetManager;
    
    public Node raiz = new Node("Terreno");
    public RigidBodyControl rigidBodyControl;
    private TerrainQuad terreno;
    
    // Materiales de la escena
    private Material matTerreno;
    
    public EscenaMontania(SimpleApplication padre)
    {
        this.padre = padre;
        this.assetManager = padre.getAssetManager();
        
        construirTerreno();
        configurarCielo();
        configurarFisica();
    }

    /** Construye y configura el terreno */
    private void construirTerreno() 
    {
        matTerreno = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
        matTerreno.setTexture("Alpha", assetManager.loadTexture("Textures/Montana/alphamap.png"));
        
        // Textura de pasto
        Texture pasto = assetManager.loadTexture("Textures/Montana/pasto.jpg");
        pasto.setWrap(Texture.WrapMode.Repeat);
        matTerreno.setTexture("Tex1", pasto);
        matTerreno.setFloat("Tex1Scale", 64f);
        
        // Textura de nieve
        Texture nieve = assetManager.loadTexture("Textures/Montana/nieve.jpg");
        nieve.setWrap(Texture.WrapMode.Repeat);
        matTerreno.setTexture("Tex2", nieve);
        matTerreno.setFloat("Tex2Scale", 32f);
        
        // Textura de camino
        Texture camino = assetManager.loadTexture("Textures/pisoMarmol.jpg");
        camino.setWrap(Texture.WrapMode.Repeat);
        matTerreno.setTexture("Tex3", camino);
        matTerreno.setFloat("Tex3Scale", 128f);
        
        // CREAMOS EL MAPA DE ALTITUD
        Texture mapaAltitud = assetManager.loadTexture("Textures/Montana/montana.png");
        AbstractHeightMap mapaAltura = new ImageBasedHeightMap(mapaAltitud.getImage());
        mapaAltura.load();
        
        //
        int patchSize = 64;
        terreno = new TerrainQuad("Montana", patchSize, 513, mapaAltura.getHeightMap());
        
        //Asignamos material, posicion y escalacion
        terreno.setMaterial(matTerreno);
        terreno.setLocalTranslation(0, -500, 0);
        //terreno.setLocalScale(2f, 1f, 2f);
        terreno.scale(2);
        raiz.attachChild(terreno);
        
        // El lod (level of detail) depende de donde este la camara
        TerrainLodControl control = new TerrainLodControl(terreno, padre.getCamera());
        terreno.addControl(control);
    }

    /**
     * Activa la deteccion de colisiones en la escena
     * (Para que los jugadores no atraviesen las paredes, suelo o elementos)
     */
    private void configurarFisica() 
    {
        CollisionShape escenaShape = CollisionShapeFactory.createMeshShape(raiz);
        rigidBodyControl = new RigidBodyControl(escenaShape, 0);
        raiz.addControl(rigidBodyControl);
    }

    /** Configura el color del cielo*/
    private void configurarCielo() 
    {
        padre.getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
    }
    
}
