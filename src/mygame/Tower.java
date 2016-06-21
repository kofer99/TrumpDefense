/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Amir, Daniel, Lukas
 */
class Tower extends ABstractTowerControl{

    public static final int TYPE_MARINE = 0;
    public static final int TYPE_POLICE = 1;
    public static final int TYPE_UNICORN = 2;
    public static final int TYPE_MURICA = 3;
   
    private int cooldown = 1000;
    private long startTime;
    private float visibility = 40f;
    private float range = 20f;
    private WaveSpawner s;
    private int projectileType;
    public int type = 0;
    private Quaternion q;
    private IllegalImmigrant target;
    private Spatial geom;
      private AssetManager assetManager;
  /*  public Tower(int type) {
        this.type = type;

        if (type == TYPE_UNICORN) {
            projectileType = Projectile.TYPE_LASER;
        } else if (type == TYPE_MARINE) {
            projectileType = Projectile.TYPE_NORMAL;
        }
    }

    public void init(Vector3f position) {
        geom = GeometryCreator.instance.createTowerGeom(type);
        geom.setLocalTranslation(position);
        Main.instance.attachToRootNode(geom);
        startTime = System.currentTimeMillis();
        s = Main.instance.getSpawner();
        geom.addControl(this);
    }

    /**
     * für die Vorschau bevor der Turm platziert wird
     *
     *//*
    public Geometry createGeometry() {
        return GeometryCreator.instance.createBox(Vector3f.ZERO);
    }
*/
  

    @Override
    public void init(Vector3f position) {
        this.assetManager = Main.instance.getAssetManager();
       geom = createGeometry();
                geom.rotate((float) Math.toRadians(90), 0, 0);
        geom.setLocalTranslation(position);
        Main.instance.attachToRootNode(geom);
        startTime = System.currentTimeMillis();
       // s = Main.instance.getSpawner();
        geom.addControl(this);
    }
  public Spatial createGeometry() {
     Spatial geome =  assetManager.loadModel("Models/unicornfuv2.j3o");
        return   geome;
    }
  
    @Override
     void getProjectile() {
       new Projectile(target, this, getProjectileType());
        
    }

    @Override
     int getProjectileType() {
       return 0;
    }






   
}
