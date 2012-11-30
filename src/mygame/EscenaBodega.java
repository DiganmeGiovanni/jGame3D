/*
 * EscenaBodega.java
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
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
 * Construye el escenario de "Bodega", (una bodega de almacen insdustrial)
 * 
 * @author Aguirre Alvarez J Giovanni
 */
public class EscenaBodega
{
    private AssetManager assetManager;        // Administrador de texturas y modelos
    public Node raizPrincipal = new Node("Bodega");            // Nodo padre de toda la escena
    
    // Controles de rigidez solida para los elementos
    public RigidBodyControl escenaRigidBody;
    private RigidBodyControl basuraRigidBody;
    private BulletAppState bulletApp;
    
    // Materiales de elementos de escena
    private Material matSuelo;
    private Material matPared;
    private Material matCaja;
    private Material matContenedor;
    private Material matConcreto;
    private Material matTecho;
    
    // Elementos de la escena
    private static final Box suelo;
    private static final Box pared;
    private Node bloqueCajas; //Contiene varias cajas formando un bloque
    private Node bloqueContenedores; //Contiene varios contenedores
    private Node andador; //Varios elementos que forman un andador lateral
    
    static
    {
        // Inicializamos el suelo
        suelo = new Box(Vector3f.ZERO, 1000, 0.1f, 1000);
        suelo.scaleTextureCoordinates(new Vector2f(50, 50));
        
        // Inicializamos la pared
        pared = new Box(Vector3f.ZERO, 5, 180, 1000);
        pared.scaleTextureCoordinates(new Vector2f(20, 10));
    }
    
    /**
     * Construye la escena "Bodega", la escena se adjunta al nodo raizPrincipal de la
     * calse por lo cual accediendo a este se accede a toda la escena
     * 
     * @param assetManager El assetManager que administrara texturas y modelos
     */
    public EscenaBodega(AssetManager assetManager, BulletAppState bulletApp)
    {
        this.assetManager = assetManager;
        this.bulletApp    = bulletApp;
        
        // Construimos la escena
        initMaterials();
        crearSuelo();
        crearParedes();
        crearCajas();
        crearContenedores();
        crearAndadores();
        crearTecho();
        configurarLuces();
        
        raizPrincipal.setLocalScale(0.5f);
        configurarFisica();
        agregarBasura();
    }
    
    /** Configura y agrega las texturas apropiadas a cada elemento de la escena*/
    private void initMaterials()
    {
        // SUELO    
        matSuelo = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        TextureKey keySuelo = new TextureKey("Textures/pisoMetalObscuro.jpg");
        keySuelo.setGenerateMips(true);
        Texture texSuelo = assetManager.loadTexture(keySuelo);
        texSuelo.setWrap(Texture.WrapMode.Repeat);
        matSuelo.setTexture("DiffuseMap", texSuelo);
        
        // PARED
        matPared = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        TextureKey keyPared = new TextureKey("Textures/paredRoja.jpg");
        keyPared.setGenerateMips(true);
        Texture texPared = assetManager.loadTexture(keyPared);
        texPared.setWrap(Texture.WrapMode.Repeat);
        matPared.setTexture("DiffuseMap", texPared);
        
        // CAJA DE MADERA
        matCaja = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        TextureKey keyCaja = new TextureKey("Textures/cajaMadera.jpg");
        keyCaja.setGenerateMips(true);
        Texture texCaja = assetManager.loadTexture(keyCaja);
        texCaja.setWrap(Texture.WrapMode.Repeat);
        matCaja.setTexture("DiffuseMap", texCaja);
        
        // CONTENEDOR AZUL
        matContenedor = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        TextureKey keyContenedor = new TextureKey("Textures/contenedorMadera.jpg");
        keyContenedor.setGenerateMips(true);
        Texture texCont = assetManager.loadTexture(keyContenedor);
        texCont.setWrap(Texture.WrapMode.Repeat);
        matContenedor.setTexture("DiffuseMap", texCont);
        
        // CONCRETO
        matConcreto = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        TextureKey keyConcreto = new TextureKey("Textures/paredConcreto.jpg");
        keyConcreto.setGenerateMips(true);
        Texture texConcreto = assetManager.loadTexture(keyConcreto);
        texConcreto.setWrap(Texture.WrapMode.Repeat);
        matConcreto.setTexture("DiffuseMap", texConcreto);
        
        // TECHO
        matTecho = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        TextureKey keyTecho = new TextureKey("Textures/techo.jpg");
        keyTecho.setGenerateMips(true);
        Texture texTecho = assetManager.loadTexture(keyTecho);
        texTecho.setWrap(Texture.WrapMode.Repeat);
        matTecho.setTexture("DiffuseMap", texTecho);
    }
    
    /**
     * Activa la deteccion de colisiones en la escena
     * (Para que los jugadores no atraviesen las paredes, suelo o elementos)
     */
    private void configurarFisica() 
    {
        CollisionShape escenaShape = CollisionShapeFactory.createMeshShape(raizPrincipal);
        escenaRigidBody = new RigidBodyControl(escenaShape, 0);
        raizPrincipal.addControl(escenaRigidBody);
    }
    
    /** Crea el suelo y lo agrega a la escena */
    private void crearSuelo()
    {
        Geometry geomSuelo = new Geometry("Suelo", suelo);
        geomSuelo.setMaterial(matSuelo);
        geomSuelo.setLocalTranslation(0, -0.1f, 0);
        raizPrincipal.attachChild(geomSuelo);
        
        // Configurar fisica del suelo
//        escenaRigidBody = new RigidBodyControl(0.0f);
//        geomSuelo.addControl(escenaRigidBody);
//        controlesRigidos.add(escenaRigidBody);
    }
    
    /** Crea las paredes y las agrega a la escena */
    private void crearParedes()
    {
        Geometry paredIzq = new Geometry("Pared", pared);
        paredIzq.setMaterial(matPared);
        paredIzq.setLocalTranslation(-1000, 180, 0);
        raizPrincipal.attachChild(paredIzq);
        
        Geometry paredDer = paredIzq.clone(true);
        paredDer.setLocalTranslation(1000, 150, 0);
        raizPrincipal.attachChild(paredDer);
        
        Geometry paredFondo = paredIzq.clone(true);
        paredFondo.setLocalTranslation(0, 150, -1000);
        paredFondo.rotate(0f,(float) Math.toRadians(90), 0f);
        raizPrincipal.attachChild(paredFondo);
        
        Geometry paredFrente = paredIzq.clone(true);
        paredFrente.setLocalTranslation(0, 150, 1000);
        paredFrente.rotate(0f,(float) Math.toRadians(90), 0f);
        raizPrincipal.attachChild(paredFrente);
    }

    /** Crea los montones de cajas de la escena*/
    private void crearCajas() 
    {
        bloqueCajas = new Node("Cajas");
        
        Box caja_2X2X3 = new Box(Vector3f.ZERO, 20, 30, 20);
        caja_2X2X3.scaleTextureCoordinates(new Vector2f(2, 3));
        for (int i=1,x=-20; i<=3; i++,x+=40) 
        {
            Geometry geom_2X2X3 = new Geometry("Caja 2X2X3", caja_2X2X3);
            geom_2X2X3.setMaterial(matCaja);
            geom_2X2X3.setLocalTranslation(x, 30, 0);
            bloqueCajas.attachChild(geom_2X2X3);
        }
        
        Box caja_2X2X2 = new Box(Vector3f.ZERO, 20, 20, 20);
        caja_2X2X2.scaleTextureCoordinates(new Vector2f(2, 2));
        for (int i=1, x=-20; i<=2; i++,x+=40) 
        {
            Geometry geom_2X2X2 = new Geometry("Caja 2X2X2", caja_2X2X2);
            geom_2X2X2.setMaterial(matCaja);
            geom_2X2X2.setLocalTranslation(x, 20, -40);
            bloqueCajas.attachChild(geom_2X2X2);
        }
        
        Box cajaSencilla = new Box(Vector3f.ZERO, 10, 10, 10);
        cajaSencilla.scaleTextureCoordinates(new Vector2f(1, 1));
        Geometry geomCaja = new Geometry("Caja sencilla", cajaSencilla);
        geomCaja.setMaterial(matCaja);
        geomCaja.setLocalTranslation(50, 10, -30);
        bloqueCajas.attachChild(geomCaja);
        
        posicionarCajas(bloqueCajas);
    }
    
    /**
     * Posiciona montones de cajas sobre la escena
     * @param pivote El nodo padre de todas las cajas que conforman un bloke
     *               se clonara varias veces para crear varios blokes
     */
    private void posicionarCajas(Node pivote)
    {
        // BLOKES DEL CENTRO
        pivote.setLocalTranslation(-450, 0, -450);
        Node blokeCajas = pivote.clone(false);
        blokeCajas.setLocalTranslation(450, 0, 450);
        blokeCajas.rotate(0, (float) Math.toRadians(180), 0);
        raizPrincipal.attachChild(pivote);
        raizPrincipal.attachChild(blokeCajas);
        
        // PARED DEL FRENTE
        for (int i=1, x=-750; i<=3; i++,x+=750) 
        {
            Node tmp = pivote.clone(false);
            tmp.setLocalTranslation(x, 0, 975);
            raizPrincipal.attachChild(tmp);
        }
        
        // PARED DEL FONDO
        for (int i=1, x=-750; i<=3; i++,x+=750) 
        {
            Node tmp = pivote.clone(false);
            tmp.setLocalTranslation(x, 0, -975);
            tmp.rotate(0, (float) Math.toRadians(180), 0);
            raizPrincipal.attachChild(tmp);
        }
        
    }

    /** Crea bloques de contenedores*/
    private void crearContenedores() 
    {
        bloqueContenedores = new Node("Contenedores");
        
        Box contenedor = new Box(Vector3f.ZERO, 40, 20, 20);
        contenedor.scaleTextureCoordinates(new Vector2f(1, 1));
        for (int i=1, x=-100; i<=3; i++,x+=100) 
        {
            for (int j=1,z=-60; j<=3; j++,z+=60) 
            {
                Geometry geomCont = new Geometry("Contenedor", contenedor);
                geomCont.setMaterial(matContenedor);
                geomCont.setLocalTranslation(x, 20, z);
                bloqueContenedores.attachChild(geomCont);
            }
        }
        
        posicionarContenedores(bloqueContenedores);
    }

    /**
     * Posiciona bloques de contenedores en la escena
     * @param bloqueContenedores El nodo padre de los contenedores que forman
     *                           un bloque, este se clonara varias veces para
     *                           tener distintos bloques
     */
    private void posicionarContenedores(Node bloqueContenedores) 
    {
        for (int i=1,z=-500; i<=3; i++,z+=200) 
        {
            Node tmp = bloqueContenedores.clone(false);
            tmp.setLocalTranslation(400, 0, z);
            raizPrincipal.attachChild(tmp);
        }
        
        for (int i=1,z=500; i<=3; i++,z-=200) 
        {
            Node tmp = bloqueContenedores.clone(false);
            tmp.setLocalTranslation(-400, 0, z);
            raizPrincipal.attachChild(tmp);
        }
    }

    /** Crea un andador */
    private void crearAndadores()
    {
        andador = new Node("Andador");
        
        Box base = new Box(Vector3f.ZERO, 10, 60, 10);
        base.scaleTextureCoordinates(new Vector2f(1, 3));
        for (int i=1,x=-90; i<=2; i++,x+=180) 
        {
            Geometry geomBase = new Geometry("Base", base);
            geomBase.setMaterial(matConcreto);
            geomBase.setLocalTranslation(x, 60, 855);
            andador.attachChild(geomBase);
        }
        
        Box plataforma = new Box(Vector3f.ZERO, 100, 5, 75);
        plataforma.scaleTextureCoordinates(new Vector2f(4, 3));
        Geometry geomPlataforma = new Geometry("Plataforma", plataforma);
        geomPlataforma.setMaterial(matConcreto);
        geomPlataforma.setLocalTranslation(0, 125, 920);
        andador.attachChild(geomPlataforma);
        
        Box rampa = new Box(Vector3f.ZERO, 150, 5, 25);
        rampa.scaleTextureCoordinates(new Vector2f(1, 6));
        Geometry geomRampa = new Geometry("Rampa", rampa);
        geomRampa.setMaterial(matConcreto);
        geomRampa.setLocalTranslation(233, 61f, 970);
        geomRampa.rotate(0, 0, (float) Math.toRadians(-25));
        andador.attachChild(geomRampa);
        
        posicionarAndadores(andador);
    }

    /** Coloca los andadores en la poscicion correcta */
    private void posicionarAndadores(Node andador) 
    {
        Node andadorFondo = andador.clone(false);
        andadorFondo.setLocalTranslation(0, 0, 0);
        andadorFondo.rotate(0, (float)Math.toRadians(180), 0);
        raizPrincipal.attachChild(andador);
        raizPrincipal.attachChild(andadorFondo);
    }

    /** Configura el techo de la bodega */
    private void crearTecho() 
    {
        Box techo = new Box(Vector3f.ZERO, 550, 5, 1100);
        techo.scaleTextureCoordinates(new Vector2f(10, 10));
        
        Geometry techoIzq = new Geometry("Techo izquierdo", techo);
        techoIzq.setMaterial(matTecho);
        techoIzq.setLocalTranslation(-500, 280, 0);
        techoIzq.rotate(0, 0, (float) Math.toRadians(5));
        raizPrincipal.attachChild(techoIzq);
        
        Geometry techoDer = new Geometry("Techo derecho", techo);
        techoDer.setMaterial(matTecho);
        techoDer.setLocalTranslation(500, 280, 0);
        techoDer.rotate(0, 0, (float) Math.toRadians(-5));
        raizPrincipal.attachChild(techoDer);
    }

    /** Agrega cajas de basura a la escena */
    private void agregarBasura()
    {
        for (int i=1,x=-500,z=-500; i<=3; i++,x+=600,z+=500) 
        {
            Node tmp = RecursosGraficos.crearCajaBasura();
            tmp.setLocalTranslation(x, 20, -500);
            raizPrincipal.attachChild(tmp);
            configFisicaBasura(tmp);
            
//            Node tmp2 = RecursosGraficos.crearCajaLatas();
//            tmp2.setLocalTranslation(x+100, 30, z+250);
//            raizPrincipal.attachChild(tmp2);
//            configFisicaBasura(tmp2);
//            
//            Node tmp3 = RecursosGraficos.crearCajaBasura();
//            tmp3.setLocalTranslation(x-100, 15, z+300);
//            raizPrincipal.attachChild(tmp3);
//            configFisicaBasura(tmp3);
        }
    }
    
    /** Agrega configuracion fisica a la basura*/
    private void configFisicaBasura(Node tmp)
    {
        basuraRigidBody = new RigidBodyControl();
        tmp.addControl(basuraRigidBody);
        bulletApp.getPhysicsSpace().add(basuraRigidBody);
    }
    
    /** Configura las fuentes de luz de la escena*/
    private void configurarLuces() 
    {
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        raizPrincipal.addLight(al);
        
        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(1f, -1, -1).normalizeLocal());
        raizPrincipal.addLight(dl);
    }
    
}
