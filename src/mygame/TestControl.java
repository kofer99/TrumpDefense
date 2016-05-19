/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Image;

/**
 * asgfasgasgasgjhakjsghaouisghaioushgiuashgiuash
 * @author Daniel
 */
class TestControl extends AbstractControl {

    private Vector3f velocity;
    private TdMap map;
    private Vector3f[] sa;
    private int[] xn;
    private int[] yn;
    private float ratioxr;
    private float ratioyr;
    private Quad bgObject;
    private int counter = 0;
    private int nrCheckpoints = 0;
    //Geschwindigkeitsanpassung : 10f langsam 100 schnell; Je Höher desto wakeliger die Bewegung
    private final float speedFactor ;

    public TestControl(Node player, Quad ver, Image mapImage,float speedFactor ) {

        this.speedFactor = speedFactor;
        map = new TdMap(mapImage, 15, 10);
        //   sa = new Vector3f[m.getVectors().length];
        velocity = new Vector3f(0,0,0);
        nrCheckpoints = map.getnodex().length;
        bgObject = ver;
        xn = map.getnodex();
        yn = map.getnodey();
        ratioxr = bgObject.getWidth() / map.getWidth();
        ratioyr = bgObject.getHeight() / map.getHeight();

    }

    @Override
    protected void controlUpdate(float tpf) {

        if (counter < nrCheckpoints) {
            Vector3f checkPointPosition = new Vector3f(-(bgObject.getWidth() / 2) + xn[counter] * ratioxr, -(bgObject.getHeight() / 2) + yn[counter] * ratioyr, spatial.getLocalTranslation().getZ());

            Vector3f sphereDirection = checkPointPosition.subtract(spatial.getLocalTranslation());
            sphereDirection.normalizeLocal();

            sphereDirection.multLocal(speedFactor);
            velocity.addLocal(sphereDirection);
            //Kontrolliert geschwindigkeit damit nicht außer kontrolle bewegt wird ( macht komische Ellipsen)
            velocity.multLocal(0.8f);
            //Bewegt und KOntrolliert GEschwindikeit damit auf allen pcs gleich
            spatial.move(velocity.mult(0.1f * tpf));


            if (checkPointPosition.distance(spatial.getLocalTranslation()) < 1f) {
               // velocity = Vector3f.ZERO;
                System.out.println("Collision with Checkpoint: " + spatial.getName() + " Distance:" + checkPointPosition.distance(spatial.getLocalTranslation()) + "Velocity:" + velocity.toString());
                System.out.println(spatial.getUserData("Health"));
                int value = spatial.getUserData("Health");
                spatial.setUserData("Health",value-10);
                counter++;
            }

        } else {
            spatial.removeFromParent();
        }


    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
