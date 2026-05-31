package entities.managers;

import entities.PlayerTypeEntity;
import skilltree.Ability;

import java.util.ArrayList;
import java.util.Arrays;

public class AbilityManager {
    private PlayerTypeEntity entity;
    private ArrayList<Ability> abilities;
    private Ability [] equipedAbiltities;
    public AbilityManager(PlayerTypeEntity entity){
        this.entity = entity;
        abilities = new ArrayList<>();
        equipedAbiltities = new Ability [5];//man kann 5 Abilties equipen
    }
    public boolean unlock (Ability ability){
        if (abilities.contains(ability)) {
            return false;
        }
        abilities.add(ability);
        entity.getSkillTree().unlock(ability);
        return true;//feedback
    }
    public void equip(Ability ability){
        int slot = 0;
        for(int i=0;i==4;i++){if(equipedAbiltities[i]!= null){slot = i;}};//für array
        if(slot > 4 || slot < 0){ return; }//error vermeidung
        if (Arrays.asList(equipedAbiltities).contains(ability)){
            return;//die Ability ist schon in einem anderen slot equiped
        }
        if (!ability.isActiveAbility()){
            return;//die Ability eine passive Ability
        }
        equipedAbiltities [slot] = ability;
    }
    public void update (){

        for (Ability ability : equipedAbiltities){
            if (ability == null){continue;}
            ability.update();
        }
    }
    public void use(int slot){
        slot --; //für array
        if (equipedAbiltities [slot] == null){return;}
        equipedAbiltities [slot].use();
    }

}
