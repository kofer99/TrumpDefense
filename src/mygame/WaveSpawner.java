package mygame;

import java.util.ArrayList;

/**
 *
 * @author Amir, Lukas
 */

public class WaveSpawner {
    public int Wave;
    public boolean Enabled;

    int immigrantsPerWave = 5;
    int immigrantsSpawned;
    float frequency = 0.6f;
    ArrayList immigrants = new ArrayList();
    float timeLeft = frequency;
    float normalTpf = -1;
    MainGame main;

    public WaveSpawner(MainGame main) {
        this.main = main;
        newWave();
    }

    public void update(float tpf) {
        if (!Enabled)
            return;

        if(immigrantsSpawned > immigrantsPerWave) {
            if (!immigrants.isEmpty())
                return;

            newWave();
        }

        timeLeft -= getFixedTpf(tpf);
        if (timeLeft <= 0) {
            timeLeft = frequency;
            immigrantsSpawned++;
            immigrants.add(new IllegalImmigrant(this));
        }
    }

    void newWave() {
        Wave++;
        main.NeueWelle(Wave);
        immigrantsSpawned = 0;
    }

    public void setEnabled(boolean enab) {
        Enabled = enab;
    }

    float getFixedTpf(float tpf) {
        float fixedTpf = tpf;
        int i = 60;

        // Wie viel mal länger ein Tick maximal dauern darf
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

    public void remove(IllegalImmigrant i) {
        immigrants.remove(i);
    }

    public ArrayList getImmigrants() {
        return immigrants;
    }
}
