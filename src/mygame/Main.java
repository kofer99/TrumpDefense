package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
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
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import de.lessvoid.nifty.screen.Screen;

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
    private Geometry mark;
    private Vector3f sf = new Vector3f(0, 0, 0);
    private String peter = "cube";
    private Node inventory;
    private boolean isInvOpen = true;
    private Node cubes;
    private BitmapText helloText;
    private BitmapText hellosText;
    public static Main instance;
    private Geometry backgroundGeom;
    private Quad fsq;
    private Image mapImage;
    private int sphere_nr = 0;
    private float speedFactor_Ball = 30f;
    private TdMap map;

    @Override
    public void simpleInitApp() {
        instance = this;
        mapImage = assetManager.loadTexture("Textures/map1fields.png").getImage();
        map = new TdMap(mapImage, 15, 10);
        flyCam.setEnabled(false);
        inputManager.setCursorVisible(true);

        //cam.setParallelProjection(true);
        cam.setLocation(new Vector3f(0, 0, 1f));
        // rootNode.attachChild(player);
        initKeys();
        //initCrossHairs();
        //  initMark();
        cubes = new Node("Cube");
        spheres = new Node("Spheres");
        inventory = new Node("Inventory");
        shootables = new Node("Shootables");
        inventory.setLocalTranslation(settings.getWidth() * 9 / 10, settings.getHeight() / 2, 0);
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setColor(ColorRGBA.Blue);
        helloText.setText("Hello World");
        helloText.setLocalTranslation(inventory.getLocalTranslation().getX() + 20, inventory.getLocalTranslation().getY(), 0);
        inventory.scale(20);
        guiNode.attachChild(helloText);
        guiNode.attachChild(inventory);


        hellosText = new BitmapText(guiFont, false);
        hellosText.setSize(guiFont.getCharSet().getRenderedSize());
        hellosText.setText("Inv is :");
        hellosText.setLocalTranslation(300, helloText.getLineHeight(), 0);
        guiNode.attachChild(hellosText);
        hellosText.setColor(ColorRGBA.Blue);
        shootables.attachChild(makeFloor());
        shootables.attachChild(spheres);
        shootables.attachChild(cubes);
        rootNode.attachChild(shootables);
        //rootNode.attachChild(cubes);
        //  rootNode.attachChild(spheres);

        System.out.print("" + fsq.getWidth() + " ," + fsq.getHeight());
        //  guiNode.attachChild(makeMap());

        //rootNode.attachChild(makeMap());
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    public Geometry createBox(Vector3f as) {
        Box b = new Box(1f, 1f, 1f);
        Geometry boxs = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        boxs.setUserData("Size", new Vector2f(b.getXExtent(),b.getYExtent()));
        boxs.setMaterial(mat);
        boxs.setLocalTranslation(as);
        boxs.addControl(new CubeControl(spheres));
        //   rootNode.attachChild(player);
        return boxs;
    }

    private Geometry createSphere(Vector3f contactPoint) {
        Sphere b = new Sphere(30, 30, 1f);

        Geometry sphere = new Geometry("Sphere_" + (++sphere_nr), b);
        sphere.setUserData("Health", 100);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        sphere.setMaterial(mat);
        sphere.setLocalTranslation(contactPoint);
        sphere.addControl(new TestControl(cubes, fsq, mapImage, speedFactor_Ball));
        //   rootNode.attachChild(player);
        System.out.println("Create Sphere: " + sphere.getName() + "-" + sphere.getLocalTranslation());
        return sphere;
    }

    private void initKeys() {
        // You can map one or several inputs to one named action
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("Cube", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Sphere", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE),
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("closeinv", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("rotR", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("rechtsklick", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        // Add the names to the action listener.
        inputManager.addListener(actionListener, "Pause", "Rotate", "rechtsklick", "closeinv");
        inputManager.addListener(analogListener, "Sphere", "Cube", "up", "down", "left", "right", "rotR");

    }

    protected Geometry makeFloor() {
        // https://hub.jmonkeyengine.org/t/how-to-set-a-background-texture/22996

        //   float camZ = cam.getLocation().getZ()+0.03f;
        float w = this.getContext().getSettings().getWidth();
        float h = this.getContext().getSettings().getHeight();
        float ratio = w / h;
        cam.setLocation(Vector3f.ZERO.add(new Vector3f(0.0f, 0.0f, 102.5f)));//Move the Camera back
        float camZ = cam.getLocation().getZ() - 17.5f; //No Idea why I need to subtract 17.5
        float width = camZ * ratio;
        //  float ratio = m.getWidth()/m.getHeight();
        //  float width = camZ * ratio;

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


        /*
         //Box box = new Box(settings.getWidth()/m.getWidth(), settings.getHeight()/m.getHeight(), -0.1f);
         Box box = new Box(settings.getWidth(), settings.getHeight(), 0.1f);
         Geometry floor = new Geometry("the Floor", box);
         floor.setLocalTranslation(0, 0, -1);
         Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
         // mat1.setColor("Color", ColorRGBA.White);
         //  mat1.setColor("Color", new ColorRGBA(0, 0, 0,0));
         mat1.setTexture("ColorMap", assetManager.loadTexture("Textures/map1fields.png"));
         //  mat1.setTexture("ColorMap", 
         //      assetManager.loadTexture("Textures/map1texture.png"));

         floor.setMaterial(mat1);

         return floor;*/
    }
    private ActionListener actionListener = new ActionListener() {
        private CollisionResult closest;
        //    private CollisionResults results;
        private int lisa = 0;
        private int sphere = 0;
        private int cube = 0;

        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Pause") && !keyPressed) {
                peter = "nischt";
            }
            if (name.equals("closeinv") && !keyPressed) {
                if (isInvOpen) {
                    isInvOpen = false;
                    hellosText.setText("inv is " + isInvOpen);
                } else {
                    isInvOpen = true;
                    hellosText.setText("inv is " + isInvOpen);
                }
            }

            /*      if (name.equals("rechtsklick") && !keyPressed) {
             System.out.println("evt");
             results = new CollisionResults();
             Vector2f click2d = inputManager.getCursorPosition();
             Vector3f click3d = cam.getWorldCoordinates(click2d, 0f).clone();
             Vector3f dir = cam.getWorldCoordinates(click2d, 1f).subtractLocal(click3d).normalizeLocal();
             Ray ray = new Ray(click3d, dir);
             shootables.collideWith(ray, results);
             // 4. Print results.
             System.out.println("----- Collisions? " + results.size() + "-----");
             // sf.add(s.getX(), s.getY(), 1);
             // mark.setLocalTranslation(sf);
             //  rootNode.attachChild(mark);
             // 5. Use the results (we mark the hit object)

             if (results.size() > 0) {
             // The closest collision point is what was truly hit:
             closest = results.getClosestCollision();
             if (closest.getGeometry().getName().equals("the Floor") && inventory.getQuantity() < 1) {
             } else {

             if (!isInvOpen) {

             player = closest.getGeometry();
             //     player.removeControl(TestControl);
             } else if (inventory.getQuantity() > 0) {
             System.out.println(inventory.getQuantity());
             Spatial temp = inventory.getChild(lisa);
             shootables.attachChild(temp);

             temp.setLocalTranslation(closest.getContactPoint());
             shootables.collideWith(ray, results);
             closest = results.getClosestCollision();
             if (closest.getGeometry().getName().equals("Sphere")) {
             sphere--;
             helloText.setText("" + sphere + " " + cube);
             } else {
             cube--;
             helloText.setText("" + sphere + " " + cube);
             }

             //  inventory.getChild(lisa).setLocalTranslation(closest.getContactPoint());
             // inventory.detachChildAt(lisa);
             //    lisa++;
             //  
             //
             //inventory.getChild(lisa).removeFromParent();

             //lisa++;
             }
             }
             }



             }*/


            if (name.equals("Rotate") && !keyPressed) {
                System.out.println("event");
                // player.rotate(0, value*speed, 0);
                // 1. Reset results list.
                //   Vector2f s =inputManager.getCursorPosition();
                // 2. Aim the ray from cam loc to cam direction.
                //Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                // 3. Collect intersections between Ray and Shootables in results list.

                CollisionResults results = new CollisionResults();
                Vector2f click2d = inputManager.getCursorPosition();
                Vector3f click3d = cam.getWorldCoordinates(click2d, 0f).clone();
                Vector3f dir = cam.getWorldCoordinates(click2d, 1f).subtractLocal(click3d).normalizeLocal();
                Ray ray = new Ray(click3d, dir);
                shootables.collideWith(ray, results);
                // 4. Print results.
                System.out.println("----- Collisions? " + results.size() + "-----");
                // sf.add(s.getX(), s.getY(), 1);
                // mark.setLocalTranslation(sf);
                //  rootNode.attachChild(mark);
                // 5. Use the results (we mark the hit object)
                if (results.size() > 0) {
                    // The closest collision point is what was truly hit:
                    closest = results.getClosestCollision();


                    // Let's interact - we mark the hit with a red dot.
                    if ("cube".equals(peter)) {

                        for (int i = (int) (-fsq.getWidth() / 2); i < (int) (fsq.getWidth() / 2); i++) {
                            for (int j = (int) (-fsq.getHeight() / 2); j < (int) (fsq.getHeight() / 2); j++) {
                                Vector3f position = new Vector3f(i, j, closest.getContactPoint().getZ()); //closest.getContactPoint();
                                //   Vector3f position = closest.getContactPoint();
                                //    if (map.towerplace(position, fsq) == true) {
                                Geometry cube = createBox(position);
                                if (map.towerplace(position, fsq, (Vector2f) cube.getUserData("Size")) == true) {
                                    cubes.attachChild(cube);
                                } else {
                                    cube.removeFromParent();
                                }
                            }
                        }
                        //shootables.attachChild(cubes);
                        //shootables.attachChild(player = createBox(closest.getContactPoint()));
                        peter = "nichts";
                    } else if ("sphere".equals(peter)) {
                        Vector3f x = closest.getContactPoint();
                        System.out.println("Closest getContactpoint: Koordinaten für Kugel: " + x + "----");
                        spheres.attachChild(createSphere(closest.getContactPoint()));
                        //shootables.attachChild(spheres);
                        peter = "nichts";

                    } else if ("nischt".equals(peter)) {

                        if (closest.getGeometry().getName().equals("the Floor")) {
                        } else {

                            inventory.attachChild(closest.getGeometry());
                            shootables.detachChild(closest.getGeometry());
                            if (closest.getGeometry().getName().equals("Sphere")) {
                                closest.getGeometry().setLocalTranslation(0, 0, 0);
                                sphere++;
                                helloText.setText("" + sphere + " " + cube);
                            } else {
                                closest.getGeometry().setLocalTranslation(0, -1f, 0);
                                cube++;
                                helloText.setText("" + sphere + " " + cube);
                            }
                        }
                    }

                    //System.out.println(1);
                    //  rootNode.attachChild(mark);
                }
            }


        }
    };
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {

            if (isRunning) {


                if (name.equals("Cube")) {
                    peter = "cube";

                }
                if (name.equals("Sphere")) {
                    peter = "sphere";

                }
                if (player != null) {
                    if (name.equals("up")) {
                        player.move(0, (float) (0.5 * tpf), 0);

                    }
                    if (name.equals("down")) {
                        player.move(0, (float) (-0.5 * tpf), 0);

                    }
                    if (name.equals("left")) {
                        player.move((float) (-0.5 * tpf), 0, 0);

                    }
                    if (name.equals("right")) {
                        player.move((float) (0.5 * tpf), 0, 0);

                    }
                    if (name.equals("rotR")) {
                        player.rotate(0, (float) (0.5 * tpf), 0);

                    }
                }
                if (name.equals("left")) {
                    //   cam.setLocation(cam.getLocation().add(new Vector3f(0, 0, 0.9f * tpf)));
                    //System.out.println();
                    helloText.setText(" " + cam.getLocation().z);
                }
                if (name.equals("right")) {
                    //    cam.setLocation(cam.getLocation().add(new Vector3f(0, 0, -0.9f * tpf)));
                    helloText.setText(" " + cam.getLocation().z);
                }
            }
        }
    };
}
