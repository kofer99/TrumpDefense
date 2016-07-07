package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.renderer.RenderManager;

/**
 * 
 * @author Daniel, Lukas, Amir
 */
public class Main extends SimpleApplication {

    public boolean isRunning = true;
    public boolean triggered = false;
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    public static Main instance;

    @Override
    public void simpleInitApp() {
        instance = this;

        stateManager.attach(new MainGame());
    }

    public void closeAppState(AbstractAppState a) {
        stateManager.detach(a);
    }

    public void openAppState(AbstractAppState a) {
        stateManager.attach(a);
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (triggered) {
            MainGame.instance.setEnabled(isRunning);
            triggered = false;
        }
    }

    @Override
    public void simpleRender(RenderManager rm) { }
}
