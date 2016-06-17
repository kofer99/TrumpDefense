package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;

/**
 * test
 *
 * @author Daniel, Lukas, Amir
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    Sound sound ;
    protected Geometry player;
    protected Geometry blas;
    private Node spheres;
    Boolean isRunning = true;
    protected Node shootables;
    private Node cubes;
    public static Main instance;
    private Geometry backgroundGeom;
    private Quad fsq;
    private Image mapImage;
    private int sphere_nr = 0;
    private float speedFactor_Ball = 30f;
    private TdMap map;
    private boolean showCursor = false;
    private Geometry cursor;
    private WaveSpawner spawner;
    private boolean previewCount = true;
    Tower preview = null;
    HUD hud;
    private DirectionalLight dl;
    private DirectionalLight dl2;

    @Override
    public void simpleInitApp() {
        instance = this;
        mapImage = assetManager.loadTexture("Textures/map1fields.png").getImage();
        map = new TdMap(mapImage, 15, 10);
        flyCam.setEnabled(false);
        inputManager.setCursorVisible(true);
        cam.setLocation(new Vector3f(0, 0, 1f));
        initKeys();
        cubes = new Node("Cube");
        spheres = new Node("Spheres");
        shootables = new Node("Shootables");
        shootables.attachChild(makeFloor());
        shootables.attachChild(spheres);
        shootables.attachChild(cubes);
        rootNode.attachChild(shootables);
        spawner = new WaveSpawner(2000);

        dl = new DirectionalLight();
        dl.setDirection(new Vector3f(0.0f,0.0f,-1.0f).normalizeLocal());
        dl.setColor(ColorRGBA.White);
        rootNode.addLight(dl);
        dl2 = new DirectionalLight();
        dl2.setDirection(new Vector3f(0.0f,0.0f,1.0f).normalizeLocal());
        dl2.setColor(ColorRGBA.White);
        rootNode.addLight(dl2);
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White);
        
        rootNode.addLight(al);
        hud = new HUD(this, assetManager, inputManager, audioRenderer, guiViewPort);
        
        sound = new Sound(assetManager);
        sound.startMusic();
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (showCursor) {
            updateCursor();
        }
        spawner.update();
    }

    private void towerPreview(Tower t) {
        initCursor(t.createGeometry());
    }
    
    private void initCursor(Geometry g) {
        cursor = createBox(new Vector3f(0, 0, 0));
        rootNode.attachChild(cursor);
        showCursor = true;
        updateCursor();
    }

    private void destroyCursor() {
        if(cursor == null) {
            return;
        }
        rootNode.detachChild(cursor);
        cursor = null;
        showCursor = false;
    }

    public Vector3f getMousePosition() {
        Vector3f position = null;
        CollisionResults results = new CollisionResults();
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = cam.getWorldCoordinates(click2d, 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(click2d, 1f).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);
        shootables.collideWith(ray, results);
        if (results.size() > 0) {
            CollisionResult closest = results.getClosestCollision();
            position = closest.getContactPoint();
        }
        return position;
    }

    private void updateCursor() {
        Vector3f position = getMousePosition();
        cursor.setLocalTranslation(position);
        if (map.towerplace(position, fsq) == true) {
            cursor.getMaterial().setColor("Color", ColorRGBA.Green);
        } else {
            cursor.getMaterial().setColor("Color", ColorRGBA.Red);
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }

//    public Geometry createBox(Vector3f as) {
//        Box b = new Box(1f, 1f, 1f);
//        Geometry boxs = new Geometry("Box", b);
//
//        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        mat.setColor("Color", ColorRGBA.Blue);
//
//        boxs.setMaterial(mat);
//        boxs.setLocalTranslation(as);
//        boxs.addControl(new CubeControl(spheres));
//        return boxs;
//    }
    public Geometry createBox(Vector3f as) {
        Box b = new Box(1f, 1f, 1f);
       
        Geometry boxs = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        boxs.setMaterial(mat);
        boxs.setLocalTranslation(as);
        return boxs;
    }

    public Geometry createSphere(Vector3f contactPoint) {
        Sphere b = new Sphere(30, 30, 1f);
        Geometry sphere = new Geometry("Sphere_" + (++sphere_nr), b);
        sphere.setUserData("Health", 100);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        sphere.setMaterial(mat);
        sphere.setLocalTranslation(contactPoint);
        //sphere.addControl(new TestControl(cubes, fsq, mapImage, speedFactor_Ball));
        //System.out.println("Create Sphere: " + sphere.getName() + "-" + sphere.getLocalTranslation());
        return sphere;
    }

    private void initKeys() {
        inputManager.addMapping("move", new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping("tower", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("left", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("right", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(actionListener, "move", "left", "right", "tower");
    }

    protected Geometry makeFloor() {
        // https://hub.jmonkeyengine.org/t/how-to-set-a-background-texture/22996
        // TODO: Die map ueberlappt noch mit der UI
        float w = this.getContext().getSettings().getWidth();
        float h = this.getContext().getSettings().getHeight();
        float ratio = w / h;
        cam.setLocation(Vector3f.ZERO.add(new Vector3f(0.0f, 0.0f, 102.5f)));//Move the Camera back
        float camZ = cam.getLocation().getZ() - 17.5f; //No Idea why I need to subtract 17.5
        float width = camZ * ratio;
        Material backgroundMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture backgroundTex = assetManager.loadTexture("Textures/map1texture.png");
        backgroundMat.setTexture("ColorMap", backgroundTex);

        fsq = new Quad(width, camZ);
        backgroundGeom = new Geometry("the Floor", fsq);
        backgroundGeom.setQueueBucket(Bucket.Sky);
        backgroundGeom.setCullHint(CullHint.Never);
        backgroundGeom.setMaterial(backgroundMat);
        backgroundGeom.setLocalTranslation(-(width / 2), -(camZ / 2), 0); //Need to Divide by two because the quad origin is bottom left
        return backgroundGeom;
    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("move")) {
                flyCam.setEnabled(true);
                inputManager.setCursorVisible(false);
                flyCam.setMoveSpeed(100);
                flyCam.setRotationSpeed(5);
            } else if (name.equals("left") && keyPressed) {
                if(hud.CurrentTower != -1){
                    Vector3f position = getMousePosition();
                    if (map.towerplace(position, fsq) == true) {
                        preview.init(position);
                        destroyCursor();
                        hud.CurrentTower = -1;
                    } }
            }else if (name.equals("right") && keyPressed) {
                    destroyTowerPreview();
                    hud.CurrentTower = -1;
            }
        }
    };

    public TdMap getTdMap() {
        return map;

    }

    public Quad getFloor() {
        return fsq;
    }

    public void attachToRootNode(Geometry g) {
        rootNode.attachChild(g);
    }
    
    public void attachToRootNode(Spatial g) {
        rootNode.attachChild(g);
    }

    public WaveSpawner getSpawner() {
        return spawner;
    }

    public void removeTower(Tower t) {
    }

    public void CreateTowerPreview() {
        preview = new Tower(hud.CurrentTower);
        towerPreview(preview);
    }
    
    public Spatial getTowerGeom(int type) {
        Spatial geom = null;
        switch(type) {
            case Tower.TYPE_UNICORN:
                geom = (Spatial) assetManager.loadModel("Models/unicornfuv2.j3o");
                geom.rotate((float) Math.toRadians(90), 0, 0);
                break;
            case Tower.TYPE_MARINE:
                geom = (Spatial) assetManager.loadModel("Models/Militower.j3o");
                geom.rotate((float) Math.toRadians(90), 0, 0);
                break;
            default:
                // Generiere einen Marinetower als default, bis wir die anderen haben
                geom = (Spatial) assetManager.loadModel("Models/trumpdefensetower.j3o");
                geom.rotate((float) Math.toRadians(90), 0, 0);
                break;
        }
        return geom;
    }

    private void destroyTowerPreview() {
        preview = null;
        destroyCursor();
    }

    Spatial createProjectile(Vector3f position, int type, Vector3f targetPosition) {
        // Box b = new Box(1f, 1f, 1f);
       switch(type) {
            case Projectile.TYPE_NORMAL:
                return createSphere(position);
            case Projectile.TYPE_LASER:
                return createRainbowLaser(position, targetPosition);
        }
        //Spatial boxs = assetManager.loadModel("Models/laserschuss.j3o");
        //Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //mat.setColor("Color", ColorRGBA.Blue);
        //boxs.setMaterial(mat);
       return null;
    }
    
    Spatial createRainbowLaser(Vector3f pos1, Vector3f pos2) {
        Vector3f vec = pos2.subtract(pos1);
        Vector3f vec2 = vec.divide(3);
        Vector3f vec3 = vec2.mult(2.0f);
        Vector3f pos3 = vec2.add(pos1);
        Vector3f pos4 = vec3.add(pos1);
        //thickness
        float t = 0.2f;
        //blau
        float x1 = pos1.getX();
        float y1 = pos1.getY();
        float z1 = pos1.getZ();
        //rot
        float x2 = pos2.getX();
        float y2 = pos2.getY();
        float z2 = pos2.getZ();
        //grün
        float x3 = pos3.getX();
        float y3 = pos3.getY();
        float z3 = pos3.getZ();
        //gelb
        float x4 = pos4.getX();
        float y4 = pos4.getY();
        float z4 = pos4.getZ();
        
        Vector3f [] vertices = new Vector3f[16];
        //blau
        vertices[0] = new Vector3f(x1-t,y1,z1+t);
        vertices[1] = new Vector3f(x1-t,y1,z1-t);
        vertices[2] = new Vector3f(x1+t,y1,z1-t);
        vertices[3] = new Vector3f(x1+t,y1,z1+t);
        //grün
        vertices[4] = new Vector3f(x3-t,y3,z3+t);
        vertices[5] = new Vector3f(x3-t,y3,z3-t);
        vertices[6] = new Vector3f(x3+t,y3,z3-t);
        vertices[7] = new Vector3f(x3+t,y3,z3+t);
        //gelb
        vertices[8] = new Vector3f(x4-t,y4,z4+t);
        vertices[9] = new Vector3f(x4-t,y4,z4-t);
        vertices[10] = new Vector3f(x4+t,y4,z4-t);
        vertices[11] = new Vector3f(x4+t,y4,z4+t);
        //rot
        vertices[12] = new Vector3f(x2-t,y2,z2+t);
        vertices[13] = new Vector3f(x2-t,y2,z2-t);
        vertices[14] = new Vector3f(x2+t,y2,z2-t);
        vertices[15] = new Vector3f(x2+t,y2,z2+t);
        float colors[] = {
            //blau
            0, 0, 1, 1,
            0, 0, 1, 1,
            0, 0, 1, 1,
            0, 0, 1, 1,
            //grün
            0, 1, 0, 1,
            0, 1, 0, 1,
            0, 1, 0, 1,
            0, 1, 0, 1,
            //gelb
            1, 1, 0, 1,
            1, 1, 0, 1,
            1, 1, 0, 1,
            1, 1, 0, 1,
            //rot
            1, 0, 0, 1,
            1, 0, 0, 1,
            1, 0, 0, 1,
            1, 0, 0, 1
        };
        int [] indexes = { 
            //blau
            0,1,2,
            0,2,3,
            //blau-->grün
            0,3,4,
            3,4,7,
            1,6,5,
            1,6,2,
            2,7,6,
            2,7,3,
            0,5,1,
            0,5,4,
            //grün-->gelb
            6,11,10,
            6,11,7,
            4,9,8,
            4,9,5,
            7,8,4,
            7,8,11,
            9,6,5,
            9,6,10,
            //gelb-->rot
            10,15,11,
            10,15,14,
            8,13,12,
            8,13,9,
            8,15,12,
            8,15,11,
            9,14,10,
            9,14,13,
            //rot
            12,13,14,
            12,14,15
        };
        Mesh mesh = new Mesh();
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(VertexBuffer.Type.Color, 4, BufferUtils.createFloatBuffer(colors));
        mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indexes));
        mesh.updateBound();
        Geometry geo = new Geometry("OurMesh", mesh);
        Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseVertexColor", true);
        mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        geo.setMaterial(mat);
        return geo;
    }
    
    public void detachFromRootNode(Spatial s) {
        rootNode.detachChild(s);
    }
}
