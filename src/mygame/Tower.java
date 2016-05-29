/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

/**
 *
 * @author Amir, Daniel
 */
class Tower{
    private Geometry geom;
    private String type;
    private int firerate;
    private int damage;
    private String damageType;
    private int cooldown = 1000;
    private long startTime;
    private float range = 20f;
    private WaveSpawner s;
    
    public Tower() {
    }
    
    public void init(Vector3f position) {
        geom = Main.instance.createBox(position);
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
                new Projectile(i, this);
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
            if(d < range && (d < distance || distance == -1)) {
                nearest = i;
                distance = d;
                System.out.println("sees");
            }
        }
        return nearest;
    }
    
    public Vector3f getPosition() {
        return geom.getLocalTranslation();
    }
}
