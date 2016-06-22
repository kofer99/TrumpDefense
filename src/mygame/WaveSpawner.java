package mygame;

import java.util.ArrayList;

/**
 *
 * @author Amir
 */

// TODO: Add better spawning mechanics
public class WaveSpawner {
    private int wave;
    private int immigrantsLeft = 25;
    private float frequency = 0.6f;
    private ArrayList immigrants = new ArrayList();
    private float timeLeft = frequency;
    private float normalTpf = -1;

    public WaveSpawner(int immigrants) {
        immigrantsLeft = immigrants;
    }
    
    public void update(float tpf) {
        float fixedTpf = getFixedTpf(tpf);
        timeLeft -= fixedTpf;
        if(timeLeft <= 0) {
            timeLeft = frequency;
            immigrants.add(new IllegalImmigrant(this));
        }
    }
    
    private float getFixedTpf(float tpf) {
        float fixedTpf = tpf;
        int i = 60;

        // Wie viel mal länger ein Tick maximal dauern darf
        float maxTimeMult = 3;

        if (normalTpf == -1) {
            normalTpf = tpf;
        }

        if (tpf > (maxTimeMult * normalTpf)) {
            fixedTpf = normalTpf;
        }else {
            normalTpf = ((normalTpf * (i - 1)) + tpf) / i;
        }
        
        return fixedTpf;
    }
    
    public void remove(IllegalImmigrant i) {
        immigrants.remove(i);
    }
    
    public ArrayList getImmigrants() {
        return immigrants;
    }
}
