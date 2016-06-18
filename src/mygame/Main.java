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
    Sound sound;
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
    private GeometryCreator geometryCreator;

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
        dl.setDirection(new Vector3f(0.0f, 0.0f, -1.0f).normalizeLocal());
        dl.setColor(ColorRGBA.White);
        rootNode.addLight(dl);
        dl2 = new DirectionalLight();
        dl2.setDirection(new Vector3f(0.0f, 0.0f, 1.0f).normalizeLocal());
        dl2.setColor(ColorRGBA.White);
        rootNode.addLight(dl2);
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White);

        rootNode.addLight(al);
        hud = new HUD(this, assetManager, inputManager, audioRenderer, guiViewPort);

        sound = new Sound(assetManager);
        sound.startMusic();

        geometryCreator = new GeometryCreator();
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (showCursor) {
            updateCursor();
        }
        spawner.update(tpf);
    }

    private void towerPreview(Tower t) {
        initCursor(t.createGeometry());
    }

    private void initCursor(Geometry g) {
        cursor = g;
        rootNode.attachChild(cursor);
        showCursor = true;
        updateCursor();
    }

    private void destroyCursor() {
        if (cursor == null) {
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
                if (hud.CurrentTower != -1) {
                    Vector3f position = getMousePosition();
                    if (map.towerplace(position, fsq) == true) {
                        preview.init(position);
                        destroyCursor();
                        hud.CurrentTower = -1;
                    }
                }
            } else if (name.equals("right") && keyPressed) {
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

    private void destroyTowerPreview() {
        preview = null;
        destroyCursor();
    }

    public void detachFromRootNode(Spatial s) {
        rootNode.detachChild(s);
    }
}
