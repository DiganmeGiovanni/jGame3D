/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
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
    /** Fisica para deteccion de colisiones en la escena*/
    private RigidBodyControl rigidBodyControl;
    private Basura basura;
    
    //Materiales de la escena
    private Material matTierra;
    private Material matPasto;
    private Material matRoca;
    private Material matCamino;
    private Material matMadera;
    private Material matPiedra;
    
    // Elementos de la escena
    private static final Box suelo;
    private static Node montana = new Node("Montañas");
    private static Node camino  = new Node("Camino");   // Camino para subir a casa
    private static Node barandal= new Node("Barandal"); // Cerca de seguridad
    private static Node puente  = new Node("Puente");   // Puente de roca
    
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
        // Ponemos azul el cielo
        viewPort.setBackgroundColor(new ColorRGBA(0f, 0.36f, 0.79f, 0f));
        
        flyCam.setMoveSpeed(300);
        basura = new Basura(this.getAssetManager());
        initMaterials();
        crearSuelo();
        crearMontanas();
        crearCamino();
        crearBarandales();
        crearPuente();
        agregarCasa();
        
        agregarBasura();
        configurarLuces();
        configurarFisica();
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
        
        // MADERA VIEJA
        matMadera = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keyMadera = new TextureKey("Textures/maderaVieja.jpg");
        keyMadera.setGenerateMips(true);
        Texture texMadera = assetManager.loadTexture(keyMadera);
        texMadera.setWrap(Texture.WrapMode.Repeat);
        matMadera.setTexture("ColorMap", texMadera);
        
        // PARED DE PIEDRA
        matPiedra = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keyPiedra = new TextureKey("Textures/paredPiedra.jpg");
        keyPiedra.setGenerateMips(true);
        Texture texPiedra = assetManager.loadTexture(keyPiedra);
        texPiedra.setWrap(Texture.WrapMode.Repeat);
        matPiedra.setTexture("ColorMap", texPiedra);
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
        
        //Configura la parte alta del camino que lleva a la cabana
        Box bloquePasto = new Box(Vector3f.ZERO, 200, 50, 50);
        bloquePasto.scaleTextureCoordinates(new Vector2f(4, 2));
        Geometry geomPasto = new Geometry("Parte alta", bloquePasto);
        geomPasto.setMaterial(matPasto);
        geomPasto.setLocalTranslation(0, 360, -950);
        rootNode.attachChild(geomPasto);
        
        Box bloqueTierra = new Box(Vector3f.ZERO, 200, 25, 25);
        bloqueTierra.scaleTextureCoordinates(new Vector2f(2, 10));
        Geometry geomTierra = new Geometry("Parte alta", bloqueTierra);
        geomTierra.setMaterial(matTierra);
        geomTierra.setLocalTranslation(0, 360, -890);
        rootNode.attachChild(geomTierra);
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
        for (int i=1,x=30; i<=5; i++,x+=30) 
        {
            Geometry verticalGeom = new Geometry("Barandal vertical", vertical);
            verticalGeom.setMaterial(matMadera);
            verticalGeom.setLocalTranslation(x, 40, 200);
            barandal.attachChild(verticalGeom);
        }
        
        // Postes horizontales
        Box horizontal = new Box(Vector3f.ZERO, 3, 75, 1.5f);
        horizontal.scaleTextureCoordinates(new Vector2f(1, 1));
        for (int i=1,y=16; i<=4; i++,y+=20) 
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
        //rootNode.attachChild(barandal);
        
        for (int i=1,x=0; i<=9; i++,x+=150) 
        {
            // Del frente
            Node tmp = barandal.clone(false);
            tmp.move(x, 0, 0);
            rootNode.attachChild(tmp);
            
            // Del fondo
            Node tmp2 = barandal.clone(false);
            tmp2.move(x, 410, -1994);
            rootNode.attachChild(tmp2);
        }
        
        // Laterales parte baja
        for (int i=1,z=50; i<=5; i++,z-=150) 
        {   
            // Lado derecho
            Node tmp = barandal.clone(false);
            tmp.move(200f, 0, z);
            tmp.rotate(0, (float)Math.toRadians(-90), 0);
            rootNode.attachChild(tmp);
            
            // Lado izquierdo
            Node tmp2 = barandal.clone(false);
            tmp2.move(1594f, 0, z);
            tmp2.rotate(0, (float)Math.toRadians(-90), 0);
            rootNode.attachChild(tmp2);
        }
        
        // Laterales de las montanas
        for (int i=1,z=47,y=110; i<=4; i++,y+=100) 
        {
            for (int j=1; j<=2; j++,z-=150) 
            {
                Node tmp = barandal.clone(false);
                tmp.setLocalTranslation(-497, y, z);
                tmp.rotate(0, (float)Math.toRadians(-90), 0);
                rootNode.attachChild(tmp);

                Node tmp2 = barandal.clone(false);
                tmp2.setLocalTranslation(897, y, z);
                tmp2.rotate(0, (float)Math.toRadians(-90), 0);
                rootNode.attachChild(tmp2);
            }
            
        }
    }

    /** Agrega una casa a la escena */
    private void agregarCasa() 
    {
        Node casa = new CabanaMadera(assetManager).getNodoRaiz();
        casa.scale(0.5f);
        casa.setLocalTranslation(600, 365, -850);
        casa.rotate(0, (float)Math.toRadians(-90), 0);
        rootNode.attachChild(casa);
    }

    /** Crea una especie de puente de piedra para caminar sobre el vacio */
    private void crearPuente()
    {
        // Piso/Base del puente
        Box suelo = new Box(Vector3f.ZERO, 20, 1, 75);
        suelo.scaleTextureCoordinates(new Vector2f(12, 4));
        Geometry piso = new Geometry("Piso del puente", suelo);
        piso.setMaterial(matPiedra);
        piso.setLocalTranslation(0, 0, 0);
        puente.attachChild(piso);
        
        // Paredes del puente (Cubre todo el puente)
        Box pared1 = new Box(Vector3f.ZERO, 2, 4, 75);
        pared1.scaleTextureCoordinates(new Vector2f(16, 1));
        Geometry barandal1 = new Geometry("Barandal 1", pared1);
        barandal1.setMaterial(matPiedra);
        barandal1.setLocalTranslation(22, 5, 0);
        puente.attachChild(barandal1);
        
        // Pared del puente (Deja una abertura para entroncar con otro)
        Box pared2 = new Box(Vector3f.ZERO, 2, 4, 60);
        pared2.scaleTextureCoordinates(new Vector2f(16, 1));
        Geometry barandal2 = new Geometry("Barandal 2", pared2);
        barandal2.setMaterial(matPiedra);
        barandal2.setLocalTranslation(-18, 5, -15);
        puente.attachChild(barandal2);
        
        posicionarPuentes(puente);
    }
    
    /** Posiciona los bloques para construir un puente sobre el vacio*/
    private void posicionarPuentes(Node puente)
    {
        // Salida trasera
        Node tmp = puente.clone(true);
        tmp.setLocalTranslation(675, 0, 1075);
        rootNode.attachChild(tmp);
        
        // Parte larga trasera
        for (int i=1,x=580; i<=10; i++,x-=150) 
        {
            Node tmp2= puente.clone(true);
            tmp2.rotate(0, (float)Math.toRadians(-90), 0);
            tmp2.setLocalTranslation(x, 0, 1130);
            rootNode.attachChild(tmp2);
        }
        
        // Parte lateral
        for (int i=0,z=1035; i<=5; i++,z-=150) 
        {
            Node tmp2= puente.clone(true);
            tmp2.rotate(0, (float)Math.toRadians(-180), 0);
            tmp2.setLocalTranslation(-825, 0, z);
            rootNode.attachChild(tmp2);
        }
        
        // Entronque lateral izquierdo
        Node tmp2 = puente.clone(true);
        tmp2.rotate(0, (float)Math.toRadians(-270), 0);
        tmp2.setLocalTranslation(-730, 0, 230);
        rootNode.attachChild(tmp2);
    }
    
    /**
     * Agrega cajas/cubos de basura a la escena
     */
    private void agregarBasura() 
    {
        // En la base del escenario
        for (int i=1,x=-400; i<=3; i++,x+=400) 
        {
            Node tmp = basura.crearCajaBasura();
            tmp.setLocalTranslation(x, 15, 400);
            rootNode.attachChild(tmp);
            
            Node tmp2 = basura.crearCajaLatas();
            tmp2.setLocalTranslation(x+100, 30, 600);
            rootNode.attachChild(tmp2);
            
            Node tmp3 = basura.crearCajaBasura();
            tmp3.setLocalTranslation(x-100, 15, 800);
            rootNode.attachChild(tmp3);
        }
    }
    
    /**
     * Activa la deteccion de colisiones en la escena
     * (Para que los jugadores no atraviesen las paredes, suelo o elementos)
     */
    private void configurarFisica() 
    {
        CollisionShape escenaShape = CollisionShapeFactory.createMeshShape(rootNode);
        rigidBodyControl = new RigidBodyControl(escenaShape, 0);
        rootNode.addControl(rigidBodyControl);
    }
    
    /** Configura las fuentes de luz de la escena*/
    private void configurarLuces() 
    {
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.Gray.mult(1.3f));
        rootNode.addLight(al);
        
        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.LightGray);
        dl.setDirection(new Vector3f(1f, -1, -1).normalizeLocal());
        rootNode.addLight(dl);
    }

}
