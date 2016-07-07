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
    ArrayList<Integer> immigrantsSpawned;
    float frequency = 0.6f;
    ArrayList immigrants = new ArrayList();
    ArrayList<Float> timeLeft;
    MainGame main;

    public WaveSpawner(MainGame main) {
        this.main = main;
    }

    public void update(float tpf) {
        if (!Enabled || immigrantsSpawned == null)
            return;

        for (int i = 0; i < immigrantsSpawned.size(); i++) {
            int immigrantNumber = immigrantsSpawned.get(i);
            if (immigrantNumber >= immigrantsPerWave) {
                if (!immigrants.isEmpty())
                    continue;

                newWave();
            }

            float cooldown = timeLeft.get(i);
            cooldown -= main.GetFixedTpf(tpf);
            if (cooldown <= 0) {
                cooldown = frequency;
                immigrantsSpawned.set(i, ++immigrantNumber);
                immigrants.add(new IllegalImmigrant(this));
            }
            timeLeft.set(i, cooldown);
        }
    }

    public void newWave() {
        Wave++;
        main.NeueWelle(Wave);

        // Die aktuelle Welle ist noch nicht zuende
        if (!immigrants.isEmpty()) {
            immigrantsSpawned.add(0);
            timeLeft.add(frequency);
        } else {
            immigrantsSpawned = new ArrayList<Integer>();
            immigrantsSpawned.add(0);
            timeLeft = new ArrayList<Float>();
            timeLeft.add(frequency);
        }
    }

    public void setEnabled(boolean enab) {
        Enabled = enab;
    }

    public void remove(IllegalImmigrant i) {
        immigrants.remove(i);
    }

    public ArrayList getImmigrants() {
        return immigrants;
    }
}
