/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.util.ArrayList;

/**
 *
 * @author Amir
 */

//TODO Add better spawning mechanics 
public class WaveSpawner {
    private long startTime;
    private int wave;
    private int immigrantsLeft = 25;
    private int frequency = 600;
    private ArrayList immigrants = new ArrayList();
    
    public WaveSpawner(int immigrants) {
        startTime = System.currentTimeMillis();
    }
    
    public void update() {
        if((System.currentTimeMillis() - startTime) > frequency) {
            startTime = System.currentTimeMillis();
            immigrants.add(new IllegalImmigrant(this));
        }
    }
    
    public void remove(IllegalImmigrant i) {
        immigrants.remove(i);
    }
    
    public ArrayList getImmigrants() {
        return immigrants;
    }
}
