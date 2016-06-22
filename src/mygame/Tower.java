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
    private int cooldown = 1000;
    private long startTime;
    private float range = 40f;
    private WaveSpawner s;
    private int projectileType;
    public int type = 0;
    private IllegalImmigrant target;

    public Tower(int type) {
        this.type = type;

        projectileType = type == TYPE_UNICORN ? Projectile.TYPE_LASER : Projectile.TYPE_NORMAL;
    }

    public void init(Vector3f position) {
        geom = GeometryCreator.instance.createTowerGeom(type);
        geom.setLocalTranslation(position);
        MainGame.instance.attachToRootNode(geom);
        startTime = System.currentTimeMillis();
        s = MainGame.instance.getSpawner();
        geom.addControl(this);
    }

    // Für die Vorschau bevor der Turm platziert wird
    public Geometry createGeometry() {
        return GeometryCreator.instance.createBox(Vector3f.ZERO);
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (target == null) {
            target = getNearestImmigrant();
        }

        // Versuche nicht jemanden auserhalb der Reichweite zu erwischen
        if (target != null && !IsInRange(target, true)) {
            target.targeted = false;
            target = getNearestImmigrant();
        }

        // Immernoch kein target gefunden, tue nichts
        if (target == null) {
            return;
        }

        spatial.lookAt(target.getPosition(), new Vector3f(0, 0, 1));
        if (System.currentTimeMillis() - startTime >= cooldown) {
            new Projectile(target, this, projectileType);
            startTime = System.currentTimeMillis();
            target = getNearestImmigrant();
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) { }

    public IllegalImmigrant getNearestImmigrant() {
        IllegalImmigrant nearest = null;
        for (Object o : s.getImmigrants()) {
            IllegalImmigrant i = (IllegalImmigrant) o;
            switch (type) {
                case TYPE_MARINE:
                    if (IsInRange(i, false)) {
                        nearest = i;
                    }
                    break;
                case TYPE_POLICE:
                    if (IsInRange(i, true) && i.getTaserTicks() == 0) {
                        nearest = i;
                    }
                    break;
                case TYPE_UNICORN:
                    if (IsInRange(i, false)) {
                        nearest = i;
                    }
                    break;
            }
        }

        if (nearest != null) {
            nearest.targeted = true;
        }
        return nearest;
    }

    public boolean IsInRange(IllegalImmigrant targeted, boolean ignoreTargeted) {

        // Ignoriere schon angezielte Ziele
        if (!ignoreTargeted && targeted.targeted) {
            return false;
        }

        return getPosition().distance(targeted.getPosition()) < range || range == -1;
    }

    public Vector3f getPosition() {
        return geom.getLocalTranslation();
    }
}
