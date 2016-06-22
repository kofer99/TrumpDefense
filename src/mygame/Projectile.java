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
 * @author Amir, Daniel
 */
class Projectile extends AbstractControl {

    public static final int TYPE_LASER = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_TASER = 2;
    private Spatial geom;
    private IllegalImmigrant target;
    private float speedFactor = 60f;
    private final float distance = 0.3f;
    private Vector3f velocity = new Vector3f(0, 0, 0);
    private float lifetime = 0;
    private int type = -1;
    private float normalTpf = -1;

    public Projectile(IllegalImmigrant target, Tower tower, int type) {
        this.type = type;
        target.setTargeted(true);
        this.target = target;
        geom = GeometryCreator.instance.createProjectile(tower.getPosition(), type, target.getPosition());
        geom.addControl(this);
        Main.instance.attachToRootNode(geom);
        if(type == TYPE_LASER) {
            target.hit(this);
            lifetime = 100;
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        switch(type) {
            case TYPE_LASER:
                if(lifetime > 0) {
                    lifetime -= 1000*tpf;;
                } else {
                    remove();
                }
                break;
            case TYPE_NORMAL:
                moveToTarget(tpf);
                break;
            case TYPE_TASER:
                moveToTarget(tpf);
                break;
        }
    }
    
    public void moveToTarget(float tpf) {
        float fixedTpf = getFixedTpf(tpf);
        Vector3f targetPosition = target.getPosition();
        Vector3f direction = targetPosition.subtract(spatial.getLocalTranslation());
        direction.normalizeLocal();
        direction.multLocal(speedFactor);
        velocity.addLocal(direction);
        //Kontrolliert geschwindigkeit damit nicht außer kontrolle bewegt wird ( macht komische Ellipsen)
        velocity.multLocal(0.86f);
        //Bewegt und KOntrolliert GEschwindikeit damit auf allen pcs gleich
        spatial.move(velocity.mult(0.15f * fixedTpf));
        if (target.getPosition().distance(spatial.getLocalTranslation()) < distance) {
            hit();
        }
    }
    //fix für die verbuggten Positionen wenn das Fenster minimiert wird

    private float getFixedTpf(float tpf) {
        float fixedTpf = tpf;
        int i = 60;
        //wie viel mal länger ein Tick maximal dauern darf
        float maxTimeMult = 3;

        if (normalTpf == -1) {
            normalTpf = tpf;
        }

        if (tpf > (maxTimeMult * normalTpf)) {
            fixedTpf = normalTpf;
        } else {
            normalTpf = ((normalTpf * (i - 1)) + tpf) / i;
        }
        return fixedTpf;
    }
    
    public void hit() {
        target.hit(this);
        remove();
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }

    public void remove() {
        spatial.removeFromParent();
    }
    
    public int getType() {
        return type;
    }
}