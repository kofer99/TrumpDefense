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
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;

/**
 *
 * @author Amir, Lukas, Daniel
 */
public class HUD extends AbstractAppState implements ScreenController {
    public int CurrentTower = -1;
    public boolean FirstBind;

    Element Welle;
    Element Money;
    Element GameOver;
    Element Leben;
    Element UnMittel;
    Element PressStart;
    Nifty nifty;
    NiftyJmeDisplay niftyDisplay;
    MainGame main;

    public HUD(MainGame main, final AssetManager assetManager, final InputManager inputManager, final AudioRenderer audioRenderer, final ViewPort guiViewPort) {
        this.main = main;
        niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        guiViewPort.addProcessor(niftyDisplay);
        nifty = niftyDisplay.getNifty();
    }

    public void bind(Nifty nifty, Screen screen) {
        if (!screen.getScreenId().equals("sidebar"))
            return;

        Welle = screen.getRootElement().findElementByName("layer")
                .findElementByName("ingameStats")
                .findElementByName("Welle");
        Money = screen.getRootElement().findElementByName("layer")
                .findElementByName("ingameStats")
                .findElementByName("Money");
        GameOver = screen.getRootElement().findElementByName("layer")
                .findElementByName("GameOver");
        GameOver.setVisible(false);
        Leben = screen.getRootElement().findElementByName("layer")
                .findElementByName("Leben");
        UnMittel = screen.getRootElement().findElementByName("layer")
                .findElementByName("UnMittel");
        UnMittel.setVisible(false);
        PressStart = screen.getRootElement().findElementByName("layer")
                .findElementByName("StartGameText");
        PressStart.setVisible(false);

        // HACK: Überschreibe die hacky default value
        // Haben wir gebraucht, dass der background groß genug ist
        Welle.getRenderer(TextRenderer.class).setText("Welle: 0");
        Money.getRenderer(TextRenderer.class).setText("Geld: 0");

        // HACK HACK HACK: ugh
        if (FirstBind) {
            setMoney(main.money);
            StartText();
        }
    }

    public void onStartScreen() { }
    public void onEndScreen() { }

    public void LoadMainMenu() {
        Main.instance.isRunning = false;
        Main.instance.triggered = true;
        nifty.fromXml("Interface/MainMenu.xml", "main menu", this);
    }

    public void setMoney(int money) {
        Money.getRenderer(TextRenderer.class).setText("Geld: " + money);
    }

    public void setzeWelle(int welle) {
        Welle.getRenderer(TextRenderer.class).setText("Welle: " + welle);
    }

    public void setzeLeben(int leben) {
        Leben.getRenderer(TextRenderer.class).setText("Leben: " + leben);
    }

    public void GameOver() {
        GameOver.setVisible(true);
    }

    public void StartText() {
        PressStart.setVisible(true);
    }

    public void UnzureichendGeld(boolean sichtbar) {
        UnMittel.setVisible(sichtbar);
    }

    public void startGame() {
        main.StartNewGame();
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
        int upgrade = -1;

        switch (Integer.parseInt(type)) {
            case 0:
                upgrade = UpgradeManager.TYPE_CAP;
                break;
            case 1:
                upgrade = UpgradeManager.TYPE_TOUPE;
                break;
            case 2:
                upgrade = UpgradeManager.TYPE_FLAG;
                break;
        }

        if (upgrade == -1)
            throw new IllegalArgumentException("Failed to parse given upgradetype in selectUpgrade.");

        main.AktiviereUpgrade(upgrade);
    }

    public void pause() {
        Main.instance.isRunning = false;
        Main.instance.triggered = true;
        nifty.fromXml("Interface/IngameUI.xml", "pause", this);
    }

    public void nextWave() {
        // Setze es unsichtbar beim ersten Starten
        PressStart.setVisible(false);
        main.SkipWave();
    }

    public void openMenu() {
        Main.instance.isRunning = false;
        Main.instance.triggered = true;
        nifty.fromXml("Interface/IngameUI.xml", "menu", this);
    }

    public void openSkilltree() {
        Main.instance.isRunning = false;
        Main.instance.triggered = true;
        nifty.fromXml("Interface/SkillTree.xml", "SkillTree", this);
    }

    public void resume() {
        Main.instance.isRunning = true;
        Main.instance.triggered = true;
        nifty.fromXml("Interface/IngameUI.xml", "sidebar", this);
    }
}
