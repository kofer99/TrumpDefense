/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import java.io.IOException;

/**
 *
 * @author Daniel
 */
public class CubeControl extends AbstractControl {

    private AssetManager assetManager;
    private Node player;
    private Vector3f velocity;
    private Vector3f dist;
    private Vector3f playerDirection;
    private Spatial temp;

    public CubeControl(Node player) {
        this.player = player;

        dist = new Vector3f(0, 0, 0);


    }

    @Override
    protected void controlUpdate(float tpf) {
        if (player.getQuantity() > 0) {
            for (int i = 0; i < player.getQuantity(); i++) {
                Spatial temp = player.getChild(i);
                if (temp.getLocalTranslation().distance(spatial.getLocalTranslation()) < 5f) {
                    /*      Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                     mat.setColor("Color", ColorRGBA.randomColor());
                     spatial.setMaterial(mat);*/
                    //Spatial temps = temp.clone();
                    
                    temp.removeFromParent();

                    // temp.removeFromParent();
                }
            }


        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        //TODO: load properties of this Control, e.g.
        //this.value = in.readFloat("name", defaultValue);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule out = ex.getCapsule(this);
        //TODO: save properties of this Control, e.g.
        //out.write(this.value, "name", defaultValue);
    }
}
