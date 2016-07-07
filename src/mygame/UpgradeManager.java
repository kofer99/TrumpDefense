package mygame;

/**
 *
 * @author Lukas
 */
public class UpgradeManager {
    public static final int TYPE_CAP = 0;
    public static final int TYPE_TOUPE = 1;
    public static final int TYPE_FLAG = 2;
    public float RangeModifier = 1;
    public float SpeedModifier = 1;
    public float DamageModifier = 1;

    MainGame main;

    public UpgradeManager(MainGame main) {
        this.main = main;
    }

    public void Aktiviere(int upgrade) {
        switch (upgrade) {
            case TYPE_CAP:
                SpeedModifier *= 0.9f;
                break;
            case TYPE_TOUPE:
                DamageModifier *= 1.2f;
                break;
            case TYPE_FLAG:
                RangeModifier *= 1.2f;
                break;
        }
    }
}
