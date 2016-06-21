/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import static mygame.Tower.TYPE_MARINE;

/**
 *
 * @author Daniel
 */
public abstract class ABstractTowerControl extends AbstractControl {

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

    public abstract void init(Vector3f position);

    @Override
    protected void controlUpdate(float tpf) {
        if (target == null) {
            target = getNearestImmigrant();
        }
        if (target != null) {
            spatial.lookAt(target.getPosition(), new Vector3f(0, 0, 1));


            if (target.getPosition().distance(spatial.getLocalTranslation()) <= range) {
                if (System.currentTimeMillis() - startTime >= cooldown) {

                    if (target != null) {
                        shoot();
                        target = getNearestImmigrant();

                        startTime = System.currentTimeMillis();
                    }
                }
            }
        }
    }

    public IllegalImmigrant getNearestImmigrant() {
        float distance = -1.0f;
        IllegalImmigrant nearest = null;
        for (Object o : s.getImmigrants()) {
            IllegalImmigrant i = (IllegalImmigrant) o;
            float d = getPosition().distance(i.getPosition());
            if (d < visibility && (d < distance || distance == -1) && (i.targeted == false)) {
                nearest = i;
                distance = d;
            }
        }
        if (nearest != null) {
            nearest.targeted = true;
        }
        return nearest;
    }

    public Vector3f getPosition() {
        return geom.getLocalTranslation();
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    private void shoot() {
       getProjectile();
    }
  abstract void getProjectile();
  abstract int getProjectileType();
}
