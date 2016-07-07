package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;

public class Sound {

    private AudioNode music;
    private AssetManager assetManager;

    public Sound(AssetManager assetManager) {
        this.assetManager = assetManager;

        loadSounds();
    }

    private void loadSounds() {
            
        music = new AudioNode(assetManager,"Sounds/Music.ogg");
        music.setPositional(false);
        music.setReverbEnabled(false);
        music.setLooping(true);
    }

    public void startMusic() {
        music.play();
    }
    public void stopMusic(){
    music.stop();
    
    }
}
