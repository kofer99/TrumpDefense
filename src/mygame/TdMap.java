/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.input.InputManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Image;
import com.jme3.texture.image.ImageRaster;

/**
 *
 * @author Amir
 */
public class TdMap {

    private int width = 0;
    private int height = 0;
    private int[][] tower;
    private int fieldsx;
    private int fieldsy;
    private int[] nodex;
    private int[] nodey;
    private int[] distances;
    private int nodecount;
    private ImageRaster ir;

    public TdMap(Image fieldmap, int fieldsx, int fieldsy) {
        this.fieldsx = fieldsx;
        this.fieldsy = fieldsy;
        tower = new int[fieldsx][fieldsy];
        ir = ImageRaster.create(fieldmap);
        int ppfx = ir.getWidth() / fieldsx;
        int ppfy = ir.getHeight() / fieldsy;
        if (ppfx != ppfy) {
            System.out.println("Die Felder sind nicht quadratisch!");
        }
        if (ppfx * fieldsx != ir.getWidth() || ppfy * fieldsy != ir.getHeight()) {
            System.out.println("Die Bildgröße ist nicht durch die Felder teilbar!");
        }
        //scanTowerFields(fieldsx, fieldsy, ppfx, ppfy, ir);
        scanNodes(ir);
        //print();
        //System.out.println(getMaxDistance());
        height = ir.getHeight();
        width = ir.getWidth();
    }

    private int scanTowerFields(int fieldsx, int fieldsy, int ppfx, int ppfy, ImageRaster ir) {
        for (int i = 0; i < fieldsx; i++) {
            for (int j = 0; j < fieldsy; j++) {
                boolean t = true;
                for (int x = i * ppfx; x < ((i + 1) * ppfx); x++) {
                    for (int y = j * ppfy; y < ((j + 1) * ppfy); y++) {
                        ColorRGBA c = ir.getPixel(x, y);
                        int r = (int) (255 * c.getRed());
                        int g = (int) (255 * c.getGreen());
                        int b = (int) (255 * c.getBlue());
                        if (r == 255 && g == 0 && b == 255) {
                            t = false;
                        }
                    }
                }
                if (t == true) {
                    tower[i][j] = 1;
                } else if (t == false) {
                    tower[i][j] = 0;
                }
            }
        }
        return 0;
    }

    private int scanNodes(ImageRaster ir) {
        int n = 0;
        int[] nodesx = new int[32];
        int[] nodesy = new int[32];
        for (int x = 0; x < ir.getWidth(); x++) {
            for (int y = 0; y < ir.getHeight(); y++) {
                ColorRGBA c = ir.getPixel(x, y);
                int r = (int) (255 * c.getRed());
                int g = (int) (255 * c.getGreen());
                int b = (int) (255 * c.getBlue());

                if (r == 0 && g == 255 && b < 32) {
                    nodesx[b] = x;
                    nodesy[b] = y;
                    n++;
                }
            }
        }
        nodex = new int[n];
        nodey = new int[n];
        for (int i = 0; i < n; i++) {
            nodex[i] = nodesx[i];
            nodey[i] = nodesy[i];

        }
        return n;
    }

    private void print() {
        for (int i = 0; i < nodey.length; i++) {
            System.out.println("(" + nodex[i] + "/" + nodey[i] + ")");
        }

        for (int i = fieldsy - 1; i >= 0; i--) {
            String s = "";
            for (int j = 0; j < fieldsx; j++) {
                s += tower[j][i];
            }
            System.out.println(s);
        }
    }

    public void a() {
    }

    private float getMaxDistance() {
        distances = new int[nodey.length - 1];
        float d = 0;
        for (int i = 0; i < (nodey.length - 1); i++) {
            distances[i] = (int) Math.sqrt((Math.abs(nodex[i] - nodex[i + 1]) * Math.abs(nodex[i] - nodex[i + 1])) + (Math.abs(nodey[i] - nodey[i + 1]) * Math.abs(nodey[i] - nodey[i + 1])));
            System.out.println(distances[i]);
            d += distances[i];

        }
        return d;
    }

    public Vector3f[] getVectors() {
        Vector3f[] ret = new Vector3f[nodex.length];
        for (int i = 0; i < nodex.length; i++) {
            //InputManager in = Main.instance.getInputManager();
            Vector2f temp = new Vector2f();
            temp.setX(nodex[i]);
            temp.setY(nodey[i]);
            ret[i] = Main.instance.getCamera().getWorldCoordinates(temp, 0f).clone();
        }
        return ret;
    }

    public int[] getPosition(int d) {
        int[] ret = new int[2];
        int a = d;
        int i = 0;
        while (a > 0) {
            a -= distances[i];
            i++;
        }
        int basex = nodex[i];
        int basey = nodey[i];
        //ret[0] = basex + ;
        return ret;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int[] getnodex() {
        return nodex;
    }

    public int[] getnodey() {
        return nodey;
    }

    public boolean towerplace(Vector3f mousePosition, Quad rat) {
        int max = 20;
        boolean ret = true;
        float ratiox = getWidth() / rat.getWidth();
        float ratioy = getHeight() / rat.getHeight();
        int positionx = (int) (ratiox * mousePosition.getX()) + (int) (rat.getWidth() / 2 * ratiox);
        int positiony = Math.abs((int) (rat.getHeight() / 2 * ratioy) - (int) (ratioy * mousePosition.getY()));
        positionx -= max / 2;
        positiony -= max / 2;
        if (positionx < 0 || positiony < 0 || ((positionx + max) >= getWidth()) || (positiony + max) >= getHeight()) {
            return false;
        }
        try {
            for (int x = positionx; x < positionx + max; x++) {
                for (int y = positiony; y < positiony + max; y++) {
                    if (x <= getWidth() - 1 && y <= getHeight() - 1) {
                        ColorRGBA c = ir.getPixel(x, getHeight() - y);
                        int r = (int) (255 * c.getRed());
                        int g = (int) (255 * c.getGreen());
                        int b = (int) (255 * c.getBlue());

                        if (r == 255 && g == 0 && b == 255) {
                            ret = false;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return ret;

    }
}