/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.state.AbstractAppState;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import com.jme3.niftygui.NiftyJmeDisplay;

/**
 *
 * @author Amir, Lukas
 */
public class HUD extends AbstractAppState implements ScreenController {
    public int CurrentTower = -1;

    NiftyJmeDisplay niftyDisplay;
    Nifty nifty;
    Main main;

    public HUD(Main main, final AssetManager assetManager, final InputManager inputManager, final AudioRenderer audioRenderer, final ViewPort guiViewPort) {
        this.main = main;
        niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        guiViewPort.addProcessor(niftyDisplay);
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/IngameUI.xml", "start", this);
    }

    public void bind(Nifty nifty, Screen screen) {
        
    }

    public void onStartScreen() {
        
    }

    public void onEndScreen() {
        
    }

    public void placeTower(String type) {
        CurrentTower = -1;

        switch (Integer.parseInt(type)) {
            case 0:
                CurrentTower = Tower.TYPE_MARINE;
                break;
            case 1:
                CurrentTower = Tower.TYPE_POLICE;
                break;
            case 2:
                CurrentTower = Tower.TYPE_UNICORN;
                break;
            case 3:
                CurrentTower = Tower.TYPE_MURICA;
                break;
        }

        if (CurrentTower == -1)
            throw new IllegalArgumentException("Failed to parse given towertype in placeTower.");

        main.CreateTowerPreview();
    }

    public void selectUpgrade(String type) {
        // 0 == cap
        // 1 == toupe
        // 2 == flag
    }

    public void openMenu() {
        
    }

    public void pause() {
        
    }

    public void nextWave() {
        
    }

    public void openSkilltree() {
        
    }
}
