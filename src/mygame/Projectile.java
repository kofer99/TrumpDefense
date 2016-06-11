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

    private Spatial geom;
    private IllegalImmigrant target;
    private float speedFactor = 60f;
    private final float distance = 0.3f;
    private Vector3f velocity = new Vector3f(0, 0, 0);
    private String peter = "Normal";

    public Projectile(IllegalImmigrant target, Tower tower, String peter) {
        this.peter = peter;
        target.setTargeted(true);
        this.target = target;
        geom = Main.instance.createProjectile(tower.getPosition());
        geom.addControl(this);
        Main.instance.attachToRootNode(geom);
    }

    @Override
    protected void controlUpdate(float tpf) {
        Vector3f targetPosition = target.getPosition();
        Vector3f direction = targetPosition.subtract(spatial.getLocalTranslation());
        direction.normalizeLocal();
        direction.multLocal(speedFactor);
        
        velocity.addLocal(direction);
        //Kontrolliert geschwindigkeit damit nicht außer kontrolle bewegt wird ( macht komische Ellipsen)
        velocity.multLocal(0.86f);
        //Bewegt und KOntrolliert GEschwindikeit damit auf allen pcs gleich
        spatial.move(velocity.mult(0.15f * tpf));
        if (target.getPosition().distance(spatial.getLocalTranslation()) < distance) {
            target.hit(this);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public void remove() {
        spatial.removeFromParent();
    }
    
    public String getPeter() {
        return peter;
    }
}