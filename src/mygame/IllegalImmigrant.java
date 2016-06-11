/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author Amir
 */
public class IllegalImmigrant extends AbstractControl {
    
    
    public boolean targeted = false;
    private Geometry geom;
    private int[] xn;
    private int[] yn;
    private Vector3f velocity;
    private Vector3f[] sa;
    private float ratioxr;
    private float ratioyr;
    private Quad bgObject;
    private int counter = 0;
    private int nrCheckpoints = 0;
    private float speedFactor;
    private TdMap m;
    private WaveSpawner w;
    private float taserTicks = 0.0f;

    public IllegalImmigrant(WaveSpawner w) {
        m = Main.instance.getTdMap();
        xn = m.getnodex();
        yn = m.getnodey();
        this.speedFactor = 30.0f;
        velocity = new Vector3f(0, 0, 0);
        nrCheckpoints = xn.length;
        bgObject = Main.instance.getFloor();
        ratioxr = bgObject.getWidth() / m.getWidth();
        ratioyr = bgObject.getHeight() / m.getHeight();
        Vector3f spawn = new Vector3f(-(bgObject.getWidth() / 2) + xn[0] * ratioxr, -(bgObject.getHeight() / 2) + yn[0] * ratioyr, 0.0f);
        geom = Main.instance.createSphere(spawn);
        Main.instance.attachToRootNode(geom);
        geom.addControl(this);
        this.w = w;
        
    }

    @Override
    protected void controlUpdate(float tpf) {
        if(taserTicks == 0.0f) {
            if (counter < nrCheckpoints) {
            Vector3f checkPointPosition = new Vector3f(-(bgObject.getWidth() / 2) + xn[counter] * ratioxr, -(bgObject.getHeight() / 2) + yn[counter] * ratioyr, spatial.getLocalTranslation().getZ());
            Vector3f sphereDirection = checkPointPosition.subtract(spatial.getLocalTranslation());
            sphereDirection.normalizeLocal();
            sphereDirection.multLocal(speedFactor);
            velocity.addLocal(sphereDirection);
            //Kontrolliert geschwindigkeit damit nicht außer kontrolle bewegt wird ( macht komische Ellipsen)
            velocity.multLocal(0.9f);
            //Bewegt und KOntrolliert GEschwindikeit damit auf allen pcs gleich
            spatial.move(velocity.mult(0.1f * tpf));
            if (checkPointPosition.distance(spatial.getLocalTranslation()) < 1f) {
                counter++;
            }

        } else {
            spatial.removeFromParent();
            w.remove(this);
         }
        } else{
            taserTicks -= tpf;
            if(taserTicks < 0.0f) {
                taserTicks = 0.0f;
                targeted = false;
            }
        }
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
    
    public void hit(Projectile p) {
        if(p.getPeter() == "Normal") {
            remove();
            p.remove();
        }else if(p.getPeter() == "Taser") {
            taserTicks = 2.0f;
            p.remove();
        }
        
    }
    
    public Vector3f getPosition() {
        return spatial.getLocalTranslation();
    }
    
    public void remove() {
        spatial.removeFromParent();
        w.remove(this);
    }
    
    public boolean isTargeted() {
        return targeted;
    }
    
    public void setTargeted(boolean targeted) {
        this.targeted = targeted;
    }
}
