package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import java.util.ArrayList;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
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
    //Turm, der gerade platziert wird (oder nischt)
    private String peter = "nischt";
    private boolean showCursor = false;
    private Geometry cursor;
    private WaveSpawner spawner;
    private ArrayList towers = new ArrayList();

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
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (showCursor) {
            updateCursor();
        }
        spawner.update();
        for(Object o : towers) {
            Tower t = (Tower) o;
            t.update();
        }
    }

    private void initCursor() {
        cursor = createBox(new Vector3f(0, 0, 0));
        rootNode.attachChild(cursor);
        showCursor = true;
        updateCursor();
    }

    private void destroyCursor() {
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
        inputManager.addMapping("pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("tower", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("left", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("right", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(actionListener, "pause", "left", "right", "tower");

    }

    protected Geometry makeFloor() {
        // https://hub.jmonkeyengine.org/t/how-to-set-a-background-texture/22996
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
            if (name.equals("Pause") && !keyPressed) {
            } else if (name.equals("left") && keyPressed) {
                if ("tower".equals(peter)) {
                    Vector3f position = getMousePosition();
                    if (map.towerplace(position, fsq) == true) {
                        towers.add(new Tower(position));
                        destroyCursor();
                        peter = "nischt";
                    } else {
                    }
                }
            } else if (name.equals("tower") && keyPressed) {
                peter = "tower";
                initCursor();
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
    public WaveSpawner getSpawner() {
        return spawner;
    }
    public void removeTower(Tower t) {
        towers.remove(t);
    }
}
