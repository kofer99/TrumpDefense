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
    public String CurrentTower = "";

    NiftyJmeDisplay niftyDisplay;
    Nifty nifty;
    Main main;

    public HUD(Main main, final AssetManager assetManager, final InputManager inputManager, final AudioRenderer audioRenderer, final ViewPort guiViewPort) {
        this.main = main;
        niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        guiViewPort.addProcessor(niftyDisplay);
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/newNiftyGui.xml", "start", this);
    }

    public void bind(Nifty nifty, Screen screen) {
        
    }

    public void onStartScreen() {
        
    }

    public void onEndScreen() {
        
    }

    public void placeMarineTower() {
        CurrentTower = "Marine";
        main.CreateTowerPreview();
    }

    public void red() {
        System.out.println("Make America great again!");
        CurrentTower ="Police";
        main.CreateTowerPreview();
                
      //  test.changeColor();
    }
    
}
