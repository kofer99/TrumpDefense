/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Amir, Daniel
 */
class Tower{
    private Spatial geom;
    private String type;
    private int firerate;
    private int damage;
    private String damageType;
    private int cooldown = 1000;
    private long startTime;
    private float range = 20f;
    private WaveSpawner s;
    public String peter = "Marine";
    
    public Tower(String peter) {
        this.peter = peter;
    }
    
    public void init(Vector3f position) {
        geom = Main.instance.getTowerGeom(peter);
        geom.setLocalTranslation(position);
        Main.instance.attachToRootNode(geom);
        startTime = System.currentTimeMillis();
        s = Main.instance.getSpawner();
    }
    
    
    
    /** für die Vorschau bevor der Turm platziert wird
     * 
     */
    public Geometry createGeometry() {
        return Main.instance.createBox(Vector3f.ZERO);
    }
    
    
    public void update() {
        if((System.currentTimeMillis() - startTime) > cooldown) {
            IllegalImmigrant i = getNearestImmigrant();
            if(i != null) {
                if(peter=="Police") {
                    //new Projectile(i, this, "Taser");              
                    Spatial rainbow = Main.instance.createRainbowLaser(geom.getLocalTranslation(), i.getPosition());
                    Main.instance.attachToRootNode(rainbow);
                    i.hitWithLaser();
                    int laserRemoveMillis = 100;
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.MILLISECOND, laserRemoveMillis);
                    new Timer().schedule(new LaserRemoveTask(rainbow), cal.getTime() );
                }else if(peter=="Marine") {
                    new Projectile(i, this, "Normal");
                }
            }
            startTime = System.currentTimeMillis();
        }
    }
    
    public IllegalImmigrant getNearestImmigrant() {
        float distance = -1.0f;
        IllegalImmigrant nearest = null;
        for(Object o : s.getImmigrants()) {
            IllegalImmigrant i = (IllegalImmigrant) o;
            float d = getPosition().distance(i.getPosition());
            if(d < range && (d < distance || distance == -1)&& (i.targeted==false || peter != "Police")) {
                nearest = i;
                distance = d;
            }
        }
        if(nearest != null) {
            nearest.targeted=true;
        }
        return nearest;
    }
    
    public Vector3f getPosition() {
        return geom.getLocalTranslation();
    }
}
class LaserRemoveTask extends TimerTask {
    private Spatial s;
    public LaserRemoveTask(Spatial s) {
        this.s = s;
    }
    @Override
    public void run() {
        Main.instance.detachFromRootNode(s);
    }
    
}
