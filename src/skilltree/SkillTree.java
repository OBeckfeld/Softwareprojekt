package skilltree;

import entities.PlayerTypeEntity;

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



    public SkillTree(PlayerTypeEntity owner){
        dash = new Dash(owner);
        dash.setAccessible();
        dmgBoost = new DMGBoost(owner);
        dmgBoost.setAccessible();
        dmgBoost2 = new DMGBoost2(owner);
        dmgNegation = new DMGNegation(owner);
        dmgNegation.setAccessible();
        dmgNegation2 = new DMGNegation2(owner);
        earthquake = new Earthquake(owner);
        heal = new Heal(owner);
        krit = new Krit(owner);
        krit2 = new Krit2(owner);
        lifesteal = new Lifesteal(owner);
        parry = new Parry(owner);
        poisonCloud = new PoisonCloud(owner);
        speedBoost = new SpeedBoost(owner);

    }

    public void open(){
        isActive = true;
    }

    public void close(){
        isActive = false;
    }

    public void unlock(Ability ability){
        ability.unlock();
        if(dmgBoost.isUnlocked()){  //Welche Fähigkeiten werden wodurch freigeschaltet
            dmgBoost2.setAccessible();
            krit.setAccessible();
        }
        if(krit.isUnlocked()){
            krit2.setAccessible();
        }
        if(dmgNegation.isUnlocked()){
            dmgNegation2.setAccessible();
            heal.setAccessible();
        }
        if(dash.isUnlocked()){
            speedBoost.setAccessible();
        }
        if(speedBoost.isUnlocked() && heal.isUnlocked()){
            lifesteal.setAccessible();
        }
        if(dmgNegation2.isUnlocked()){
            parry.setAccessible();
        }
        if(dmgBoost.isUnlocked() && speedBoost.isUnlocked()){
            earthquake.setAccessible();
        }
        if(parry.isUnlocked() && speedBoost.isUnlocked()){
            poisonCloud.setAccessible();
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

    public boolean getActive(){
        return isActive;
    }
}
