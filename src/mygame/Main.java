package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;

/**
 * 
 * @author Daniel, Lukas, Amir
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    private int Health;
    public static Main instance;

    @Override
    public void simpleInitApp() {
        instance = this;

        Health = 10;
        stateManager.attach(new MainGame());
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (Health <= 0) {
            stateManager.detach(getStateManager().getState(MainGame.class));
            this.stop();
        }
    }

    public void reduceHealth(int amount) {
        Health -= amount;
    }

    @Override
    public void simpleRender(RenderManager rm) { }
}
