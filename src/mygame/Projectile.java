/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Amir
 */
class Projectile extends AbstractControl {

    private Geometry geom;
    private IllegalImmigrant target;
    private float speedFactor = 60f;
    private final float distance = 0.1f;
    private Vector3f velocity = new Vector3f(0, 0, 0);

    public Projectile(IllegalImmigrant target, Tower tower) {
        this.target = target;
        geom = Main.instance.createSphere(tower.getPosition());
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
        velocity.multLocal(0.8f);
        //Bewegt und KOntrolliert GEschwindikeit damit auf allen pcs gleich
        spatial.move(velocity.mult(0.1f * tpf));
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
}