/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.audio.AudioNode;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.texture.Texture;

/**
 * Construye el modelo de una cabaña de madera con una chimenea
 *
 * @author Aguirre Alvarez J Giovanni
 */
public class CabanaMadera {

    private static Node nodoRaiz = new Node("Casa");     // Casa, Hogar, Cabaña
    private AssetManager assetManager;
    // Materiales para el modelo
    private Material matPisoMadera;
    private Material matParedMadera;
    private Material matTecho;
    private Material matChimeneaPiedra;
    private Material matInteriorChimenea;
    private Material matTapete;
    private Material madera;

    /**
     * Construye una nueva instancia del modelo y adjunta todos sus elementos al
     * nodo raiz
     *
     * @param assetManager El administrador de modelos, texturas para crear
     * escena
     */
    public CabanaMadera(AssetManager assetManager) {
        this.assetManager = assetManager;

        initMaterials();
        crearBase();
        crearParedes();
        crearTecho();
        creatChimenea();
        encenderChimenea();
        crearTapete();
        crearMesa();
    }

    /**
     * Inicializa los materiales utilizados en el modelo
     */
    private void initMaterials() {
        // PISO DE MADERA
        matPisoMadera = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keyPisoMad = new TextureKey("Textures/pisoMadera.jpg");
        keyPisoMad.setGenerateMips(true);
        Texture texPisoMadera = assetManager.loadTexture(keyPisoMad);
        texPisoMadera.setWrap(Texture.WrapMode.Repeat);
        matPisoMadera.setTexture("ColorMap", texPisoMadera);

        // PARED DE MADERA
        matParedMadera = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keyParedMad = new TextureKey("Textures/paredMadera.jpg");
        keyParedMad.setGenerateMips(true);
        Texture texParedMadera = assetManager.loadTexture(keyParedMad);
        texParedMadera.setWrap(Texture.WrapMode.Repeat);
        matParedMadera.setTexture("ColorMap", texParedMadera);

        // TECHO DE PALMA
        matTecho = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keyTecho = new TextureKey("Textures/techoPalma2.jpg");
        keyTecho.setGenerateMips(true);
        Texture texTecho = assetManager.loadTexture(keyTecho);
        texTecho.setWrap(Texture.WrapMode.Repeat);
        matTecho.setTexture("ColorMap", texTecho);

        // CHIMENEA DE PIEDRA
        matChimeneaPiedra = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keyChimenea = new TextureKey("Textures/paredPiedra.jpg");
        keyChimenea.setGenerateMips(true);
        Texture texChimenea = assetManager.loadTexture(keyChimenea);
        texChimenea.setWrap(Texture.WrapMode.Repeat);
        matChimeneaPiedra.setTexture("ColorMap", texChimenea);

        // CHIMENEA INTERIOR
        matInteriorChimenea = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keyInteriorChim = new TextureKey("Textures/chimeneaInterior.jpg");
        keyInteriorChim.setGenerateMips(true);
        Texture texIntChim = assetManager.loadTexture(keyInteriorChim);
        texIntChim.setWrap(Texture.WrapMode.Repeat);
        matInteriorChimenea.setTexture("ColorMap", texIntChim);

        // TAPETE
        matTapete = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keyTapete = new TextureKey("Textures/tapete.jpg");
        keyTapete.setGenerateMips(true);
        Texture texTapete = assetManager.loadTexture(keyTapete);
        texTapete.setWrap(Texture.WrapMode.Repeat);
        matTapete.setTexture("ColorMap", texTapete);

        // MADERA
        madera = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keyMadera = new TextureKey("Textures/madera.jpg");
        keyMadera.setGenerateMips(true);
        Texture texMadera = assetManager.loadTexture(keyMadera);
        texMadera.setWrap(Texture.WrapMode.Repeat);
        madera.setTexture("ColorMap", texMadera);
    }

    /**
     * Crea la plataforma de base de la cabana
     */
    private void crearBase() {
        Box base = new Box(Vector3f.ZERO, 200, 10, 300);
        base.scaleTextureCoordinates(new Vector2f(1, 2));
        Geometry geomBase = new Geometry("Base de la casa", base);
        geomBase.setMaterial(matPisoMadera);
        geomBase.setLocalTranslation(0, 100, 300);
        nodoRaiz.attachChild(geomBase);
    }

    /**
     * Construye las paredes de la nodoRaiz
     */
    private void crearParedes() {
        // Pared lateral
        Box pared = new Box(Vector3f.ZERO, 5, 80, 200);
        pared.scaleTextureCoordinates(new Vector2f(4, 2));
        Geometry geomPared = new Geometry("Pared de madera", pared);
        geomPared.setMaterial(matParedMadera);
        geomPared.setLocalTranslation(-150, 180, 310);
        nodoRaiz.attachChild(geomPared);

        // Pared lateral 2
        Geometry geomPared2 = geomPared.clone(true);
        geomPared2.setLocalTranslation(150, 180, 310);
        nodoRaiz.attachChild(geomPared2);

        // Pared de la parte trasera de la nodoRaiz
        Box paredTrasera = new Box(Vector3f.ZERO, 155, 80, 5);
        paredTrasera.scaleTextureCoordinates(new Vector2f(2, 2));
        Geometry paredTras = new Geometry("Pared trasera", paredTrasera);
        paredTras.setMaterial(matParedMadera);
        paredTras.setLocalTranslation(0, 180, 105);
        nodoRaiz.attachChild(paredTras);

        // Pared del frente
        Box paredFrente = new Box(Vector3f.ZERO, 55, 80, 5);
        paredFrente.scaleTextureCoordinates(new Vector2f(1, 2));
        Geometry paredFren = new Geometry("Pared frente", paredFrente);
        paredFren.setMaterial(matParedMadera);
        paredFren.setLocalTranslation(-100, 180, 515);
        nodoRaiz.attachChild(paredFren);

        // Pared del frente 2
        Geometry paredFren2 = paredFren.clone(true);
        paredFren2.setLocalTranslation(100, 180, 515);
        nodoRaiz.attachChild(paredFren2);
    }

    /**
     * Agrega el techo
     */
    private void crearTecho() {
        // Techo
        Box techo = new Box(Vector3f.ZERO, 90, 1, 220);
        techo.scaleTextureCoordinates(new Vector2f(5, 3));
        Geometry geomTecho = new Geometry("Techo", techo);
        geomTecho.setMaterial(matTecho);
        geomTecho.setLocalTranslation(86.7f, 278, 310);
        geomTecho.rotate(0, 0, (float) Math.toRadians(-15));
        nodoRaiz.attachChild(geomTecho);

        // Techo 2
        Geometry geomTecho2 = geomTecho.clone(true);
        geomTecho2.setLocalTranslation(-86.7f, 278, 310);
        geomTecho2.rotate(0, 0, (float) Math.toRadians(30));
        nodoRaiz.attachChild(geomTecho2);
    }

    /**
     * Agrega una chimenea a la cabaña
     */
    private void creatChimenea() {
        // Chimenea parte lateral
        Box chimLateral = new Box(Vector3f.ZERO, 5, 25, 25);
        chimLateral.scaleTextureCoordinates(new Vector2f(2, 1));
        Geometry chimLat1 = new Geometry("Chimenea parte lateral", chimLateral);
        chimLat1.setMaterial(matChimeneaPiedra);
        chimLat1.setLocalTranslation(-30, 135, 135);
        nodoRaiz.attachChild(chimLat1);

        // Chimenea parte lateral 2
        Geometry chimLat2 = chimLat1.clone(true);
        chimLat2.setLocalTranslation(30, 135, 135);
        nodoRaiz.attachChild(chimLat2);

        // Interior chimenea
        Box chimFondo = new Box(Vector3f.ZERO, 25, 25, 2);
        chimFondo.scaleTextureCoordinates(new Vector2f(1, 1));
        Geometry chimeneaFondo = new Geometry("Fonde de la chimenea", chimFondo);
        chimeneaFondo.setMaterial(matInteriorChimenea);
        chimeneaFondo.setLocalTranslation(0, 135, 112);
        nodoRaiz.attachChild(chimeneaFondo);

        // Base chimenea
        Geometry baseChim = chimeneaFondo.clone(true);
        baseChim.setLocalTranslation(0, 112, 135);
        baseChim.rotate((float) Math.toRadians(90), 0, 0);
        nodoRaiz.attachChild(baseChim);

        // Chimenea parte superior
        Box chimSuperior = new Box(Vector3f.ZERO, 35, 15, 25);
        chimSuperior.scaleTextureCoordinates(new Vector2f(2, 1));
        Geometry chimSup = new Geometry("Chimenea parte superior", chimSuperior);
        chimSup.setMaterial(matChimeneaPiedra);
        chimSup.setLocalTranslation(0, 175, 135);
        nodoRaiz.attachChild(chimSup);

        // Chimenea escape
        Box chimEscape = new Box(Vector3f.ZERO, 15, 80, 15);
        chimEscape.scaleTextureCoordinates(new Vector2f(1, 4));
        Geometry chimEsc = new Geometry("Chimenea escape", chimEscape);
        chimEsc.setMaterial(matChimeneaPiedra);
        chimEsc.setLocalTranslation(0, 260, 125);
        nodoRaiz.attachChild(chimEsc);

        // Techo chimenea
        Box chimeneaTecho = new Box(Vector3f.ZERO, 10, 0.2f, 20);
        chimeneaTecho.scaleTextureCoordinates(new Vector2f(1, 1));
        Geometry chimTecho = new Geometry("Techo chimenea", chimeneaTecho);
        chimTecho.setMaterial(matTecho);
        chimTecho.setLocalTranslation(9, 343.2f, 125);
        chimTecho.rotate(0, 0, (float) Math.toRadians(-20));
        nodoRaiz.attachChild(chimTecho);

        // Techo chimenea 2
        Geometry chimTecho2 = chimTecho.clone(true);
        chimTecho2.setLocalTranslation(-9, 343.2f, 125);
        chimTecho2.rotate(0, 0, (float) Math.toRadians(40));
        nodoRaiz.attachChild(chimTecho2);
    }

    /**
     * Agrega una flama a la chimenea, como si estuviera encendida
     */
    private void encenderChimenea() {
        // CONFIGURAMOS LA FLAMA
        Material matRojo = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        matRojo.setTexture("Texture", assetManager.loadTexture("Textures/flame.png"));

        ParticleEmitter llama = new ParticleEmitter("Emisor", ParticleMesh.Type.Triangle, 30);
        llama.setMaterial(matRojo);
        llama.setImagesX(2);
        llama.setImagesY(2); //Textura de 2X2
        llama.setEndColor(new ColorRGBA(1, 0, 0, 1));      // Rojo
        llama.setStartColor(new ColorRGBA(1, 0.5f, 0, 0.5f)); //Amarillo
        llama.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
        llama.setStartSize(10f);
        llama.setEndSize(10f);
        llama.setGravity(0, 0, 0);
        llama.setLowLife(1f);
        llama.setHighLife(3f);
        llama.getParticleInfluencer().setVelocityVariation(0.3f);

        // Posicionamos la flama

        llama.setLocalTranslation(0, 122, 135);
        llama.scale(50);
        
//        Audio.playChimenea(assetManager);
        nodoRaiz.attachChild(llama);
    }

    /**
     * Agrega un tapete/alfombra a la casa
     */
    private void crearTapete() {
        Box tapete = new Box(Vector3f.ZERO, 70, 0.1f, 45);
        tapete.scaleTextureCoordinates(new Vector2f(1, 1));
        Geometry geomTapete = new Geometry("Tapete", tapete);
        geomTapete.setMaterial(matTapete);
        geomTapete.setLocalTranslation(0, 110.1f, 235);
        nodoRaiz.attachChild(geomTapete);
    }

    /**
     * Agrega una mesa de centro sobre el tapete
     */
    private void crearMesa() {
        // Base de la mesa
        Box base = new Box(Vector3f.ZERO, 8, 0.5f, 8);
        base.scaleTextureCoordinates(new Vector2f(1, 1));
        Geometry geomBase = new Geometry("Base de la mesa", base);
        geomBase.setMaterial(madera);
        geomBase.setLocalTranslation(0, 110.5f, 235);
        nodoRaiz.attachChild(geomBase);
        
        // Parte media de la mesa
        Box medio = new Box(Vector3f.ZERO, 2, 7, 2);
        medio.scaleTextureCoordinates(new Vector2f(1, 1));
        Geometry geomMedia = new Geometry("Base de la mesa", medio);
        geomMedia.setMaterial(madera);
        geomMedia.setLocalTranslation(0, 117.5f, 235);
        nodoRaiz.attachChild(geomMedia);

        // Parte alta de la mesa
        Box mesa = new Box(Vector3f.ZERO, 15, 1, 15);
        mesa.scaleTextureCoordinates(new Vector2f(1, 1));
        Geometry geomMesa = new Geometry("Base de la mesa", mesa);
        geomMesa.setMaterial(madera);
        geomMesa.setLocalTranslation(0, 125.5f, 235);
        nodoRaiz.attachChild(geomMesa);
    }

    /**
     * Devuelve una referencia al nodo raiz
     *
     * @returns El nodo del cual cuelga todo el modelo
     */
    public Node getNodoRaiz() {
        return nodoRaiz;
    }

    
}
