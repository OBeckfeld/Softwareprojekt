package skilltree;

import entities.PlayerTypeEntity;
import main.GamePanel;
import tools.SpriteSheet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class SkillTree {


    public boolean isActive = false;
    private Ability dash;
    private Ability dmgBoost;
    private Ability dmgBoost2;
    private Ability dmgNegation;
    private Ability dmgNegation2;
    private Ability earthquake;
    private Ability heal;
    private Ability krit;
    private Ability krit2;
    private Ability lifesteal;
    private Ability parry;
    private Ability poisonCloud;
    private Ability speedBoost;
    private int height;
    private int width;
    private BufferedImage[] abilityIcons;
    private Ability[] abilities;
    private final int lv1 = 350;
    private final int lv2 = 600;
    private final int lv3 = 850;
    private final int r1 = 50;
    private final int r2 = 200;
    private final int r3 = 350;
    private final int r4 = 500;
    private final int r5 = 650;
    private GamePanel gamePanel;
    private PlayerTypeEntity owner;


    public SkillTree(PlayerTypeEntity owner, GamePanel gamePanel) {
        loadIcons();
        this.gamePanel = gamePanel;
        this.owner=owner;

        abilities = new Ability[]{
            dash = new Dash(owner, lv1, r3, abilityIcons[11], gamePanel, this),
            dmgBoost = new DMGBoost(owner, lv1, (r1+r2)/2, abilityIcons[0], gamePanel, this),
            dmgBoost2 = new DMGBoost2(owner, lv2, r2, abilityIcons[1], gamePanel, this),
            dmgNegation = new DMGNegation(owner, lv1, (r4+r5)/2, abilityIcons[5], gamePanel, this),
            dmgNegation2 = new DMGNegation2(owner, lv2, r5, abilityIcons[6], gamePanel, this),
            earthquake = new Earthquake(owner, lv3, r2, abilityIcons[9], gamePanel, this),
            heal = new Heal(owner, lv2, r4, abilityIcons[4], gamePanel, this),
            krit = new Krit(owner, lv2, r1, abilityIcons[2], gamePanel, this),
            krit2 = new Krit2(owner, lv3, r1, abilityIcons[3], gamePanel, this),
            lifesteal = new Lifesteal(owner, lv3, r4, abilityIcons[8], gamePanel, this),
            parry = new Parry(owner, lv3, r5, abilityIcons[7], gamePanel, this),
            poisonCloud = new PoisonCloud(owner, lv3, r3, abilityIcons[12], gamePanel, this),
            speedBoost = new SpeedBoost(owner, lv2, r3, abilityIcons[10], gamePanel, this)
        };
        dash.setAccessible();
        dmgBoost.setAccessible();
        dmgNegation.setAccessible();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = (int)screenSize.getWidth();
        height = (int)screenSize.getHeight();

        gamePanel.addAbility(dash);
        gamePanel.addAbility(dmgBoost);
        gamePanel.addAbility(dmgBoost2);
        gamePanel.addAbility(dmgNegation);
        gamePanel.addAbility(dmgNegation2);
        gamePanel.addAbility(earthquake);
        gamePanel.addAbility(heal);
        gamePanel.addAbility(krit);
        gamePanel.addAbility(krit2);
        gamePanel.addAbility(lifesteal);
        gamePanel.addAbility(parry);
        gamePanel.addAbility(poisonCloud);
        gamePanel.addAbility(speedBoost);


    }

    public void open(){
        isActive = true;
    }

    public void close(){
        isActive = false;
    }

    public void unlock(Ability ability){
        if(owner.getSkillPoints() >= ability.getCost()) {
            ability.unlock();
            if (dmgBoost.isUnlocked()) {  //Welche Fähigkeiten werden wodurch freigeschaltet
                dmgBoost2.setAccessible();
                krit.setAccessible();
            }
            if (krit.isUnlocked()) {
                krit2.setAccessible();
            }
            if (dmgNegation.isUnlocked()) {
                dmgNegation2.setAccessible();
                heal.setAccessible();
            }
            if (dash.isUnlocked()) {
                speedBoost.setAccessible();
            }
            if (speedBoost.isUnlocked() && heal.isUnlocked()) {
                lifesteal.setAccessible();
            }
            if (dmgNegation2.isUnlocked()) {
                parry.setAccessible();
            }
            if (dmgBoost2.isUnlocked() && speedBoost.isUnlocked()) {
                earthquake.setAccessible();
            }
            if (speedBoost.isUnlocked()) {
                poisonCloud.setAccessible();
            }
        }
    }

    /**
     * Gibt ein Array mit den Namen aller freigeschalteten Fähigkeiten zurück
     * @return
     */
    public String[] getUnlockedAbilities() {
        ArrayList<String> unlockedAbilities = new ArrayList<>();
        // "Stapelung" nach accessibility der abilities, um die Effizienz zu erhöhen
        if (dash.isUnlocked()) {
            unlockedAbilities.add("Dash");
            if (speedBoost.isUnlocked()) {
                unlockedAbilities.add("SpeedBoost");
                if (earthquake.isUnlocked()) {
                    unlockedAbilities.add("Earthquake");
                }
                if (poisonCloud.isUnlocked()) {
                    unlockedAbilities.add("PoisonCloud");
                }
                if (lifesteal.isUnlocked()) {
                    unlockedAbilities.add("Lifesteal");
                }
            }
        }
        if (dmgBoost.isUnlocked()) {
            unlockedAbilities.add("DMGBoost");
            if (dmgBoost2.isUnlocked()) {
                unlockedAbilities.add("DMGBoost2");
            }
            if (krit.isUnlocked()) {
                unlockedAbilities.add("Krit");
                if (krit2.isUnlocked()) {
                    unlockedAbilities.add("Krit2");
                }
            }
        }
        if (dmgNegation.isUnlocked()) {
            unlockedAbilities.add("DMGNegation");
            if (dmgNegation2.isUnlocked()) {
                unlockedAbilities.add("DMGNegation2");
                if(parry.isUnlocked()){
                    unlockedAbilities.add("Parry");
                }
            }
            if (heal.isUnlocked()) {
            unlockedAbilities.add("Heal");
            }
        }

        return unlockedAbilities.toArray(new String[unlockedAbilities.size()]);
    }

    /**
     * Gibt auf Angabe des Namens einer Ability eine Referenz auf das im SkillTree hinterlegte Ability-Objekt zurück
     * @param abilityName
     */
    public Ability getAbilityReference(String abilityName) {
        switch(abilityName){
            case "Dash": 
                return dash;
            case "DMGBoost": 
                return dmgBoost;
            case "DMGBoost2": 
                return dmgBoost2;
            case "DMGNegation": 
                return dmgNegation;
            case "DMGNegation2": 
                return dmgNegation2;
            case "Earthquake":
                return earthquake;
            case "Heal":
                return heal;
            case "Krit":
                return krit;
            case "Krit2":
                return krit2;
            case "Lifesteal":
                return lifesteal;
            case "Parry":
                return parry;
            case "PoisonCloud":
                return poisonCloud;
            case "SpeedBoost":
                return speedBoost;
            default:
                throw new IllegalArgumentException("Ability name not found: " + abilityName);
        }
    }

    public boolean getActive(){
        return isActive;
    }
    public void update(){

    }
    public void draw(Graphics g){
        g.setColor(new Color(0,0,0,180));
        g.drawRect(0,0,width,height);
        g.fillRect(0,0,width,height);
        g.drawLine(lv1, (r1+r2)/2, lv2, r1);
        g.drawLine(lv1, (r1+r2)/2, lv2, r2);
        g.drawLine(lv1, r3, lv2, r3);
        g.drawLine(lv1, (r4+r5)/2, lv2, r4);
        g.drawLine(lv1, (r4+r5)/2, lv2, r5);
        g.drawLine(lv2, r1, lv3, r1);
        g.drawLine(lv2, r2, lv3, r2);
        g.drawLine(lv2, r3, lv3, r2);
        g.drawLine(lv2, r3, lv3, r3);
        g.drawLine(lv2, r3, lv3, r4);
        g.drawLine(lv2, r4, lv3, r4);
        g.drawLine(lv2, r5, lv3, r5);

        for (Ability node : abilities) {    // zeichnet die Skills
                node.draw(g);
        }
    }

    private void loadIcons() {
        try {
            BufferedImage sheet = ImageIO.read(
                    getClass().getResource("../data/sprites/AbilitySpritesheet.png")
            );
            SpriteSheet ss = new SpriteSheet(sheet);
            int size = 512;
            abilityIcons = new BufferedImage[]{
                    ss.getSprite(0,   0, size, size),
                    ss.getSprite(size,  0, size, size),
                    ss.getSprite(size*2, 0, size, size),
                    ss.getSprite(size*3, 0, size, size),
                    ss.getSprite(size*4, 0, size, size),
                    ss.getSprite(size*5, 0, size, size),
                    ss.getSprite(size*6, 0, size, size),
                    ss.getSprite(size*7, 0, size, size),
                    ss.getSprite(size*8, 0, size, size),
                    ss.getSprite(size*9, 0, size, size),
                    ss.getSprite(size*10, 0, size, size),
                    ss.getSprite(size*11, 0, size, size),
                    ss.getSprite(size*12, 0, size, size)

            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
