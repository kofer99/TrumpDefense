/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

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
class Tower extends AbstractControl {
    public static final int TYPE_MARINE = 0;
    public static final int TYPE_POLICE = 1;
    public static final int TYPE_UNICORN = 2;
    public static final int TYPE_MURICA = 3;
    private Spatial geom;
    private int firerate;
    private int damage;
    private String damageType;
    private int cooldown = 1000;
    private long startTime;
    private float range = 20f;
    private WaveSpawner s;
    private int projectileType;
    public int type = 0;

    public Tower(int type) {
        this.type = type;

        if (type == TYPE_UNICORN)
            projectileType = Projectile.TYPE_LASER;
        else if (type == TYPE_MARINE)
            projectileType = Projectile.TYPE_NORMAL;
    }

    public void init(Vector3f position) {
        geom = GeometryCreator.instance.createTowerGeom(type);
        geom.setLocalTranslation(position);
        Main.instance.attachToRootNode(geom);
        startTime = System.currentTimeMillis();
        s = Main.instance.getSpawner();
        geom.addControl(this);
    }

    /** für die Vorschau bevor der Turm platziert wird
     * 
     */
    public Geometry createGeometry() {
        return GeometryCreator.instance.createBox(Vector3f.ZERO);
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (System.currentTimeMillis() - startTime >= cooldown) {
            IllegalImmigrant i = getNearestImmigrant();
            if (i != null)
                new Projectile(i, this, projectileType);

            startTime = System.currentTimeMillis();
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
      
    }

    public IllegalImmigrant getNearestImmigrant() {
        float distance = -1.0f;
        IllegalImmigrant nearest = null;
        for (Object o : s.getImmigrants()) {
            IllegalImmigrant i = (IllegalImmigrant) o;
            float d = getPosition().distance(i.getPosition());
            switch(type) {
                case TYPE_MARINE:
                    if(d < range && (d < distance || distance == -1)&& (i.targeted==false )) {
                        nearest = i;
                        distance = d;
                    }
                    break;
                case TYPE_POLICE:
                    if(d < range && (d < distance || distance == -1)) {
                        if(i.getTaserTicks() == 0) {
                            nearest = i;
                            distance = d;
                        }
                    }
                    break;
                case TYPE_UNICORN:
                    if(d < range && (d < distance || distance == -1)&& (i.targeted==false )) {
                        nearest = i;
                        distance = d;
                    }
                    break;
            }
        }

        if (nearest != null) {
            nearest.targeted=true;
        }
        return nearest;
    }

    public Vector3f getPosition() {
        return geom.getLocalTranslation();
    }
}

