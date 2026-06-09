package entities;

import Weapons.*;
import entities.components.MovementComponent;
import entities.managers.AttackRegistry;
import main.GamePanel;
import skilltree.SkillTree;
import tools.Animation;
import tools.SpriteSheet;
import tools.TextBox;
import tools.TileManager;
import entities.managers.EntityRegistry;
import inputs.KeyboardInputs;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Repräsentiert den vom Spieler gesteuerten Avatar.
 * Verwaltet Tastatureingaben, Bewegung, Animationen, Angriffe,
 * Skillpunkte und den SkillTree.
 * Erbt von PlayerTypeEntity und erweitert diese um spielerspezifische Logik.
 */
public class Player extends PlayerTypeEntity {

    /** Tastatureingaben des Spielers. */
    protected final KeyboardInputs inputs;

    /** Lauf-Animationen für alle 4 Richtungen (rechts, unten, links, oben). */
    private Animation[] walkAnimations;

    /** Angriffs-Animationen für alle 4 Richtungen. */
    private Animation[] attackAnimation;

    /** Gibt an ob gerade eine Angriffsanimation abgespielt wird. */
    private boolean isAnimatingAttack = false;

    /** SpriteSheet mit allen Animations-Frames des Spielers. */
    private SpriteSheet sheet;

    /** Aktuell abgespielte Animation. */
    private Animation currentAnimation;

    /** Letzte Richtung, um unnötige Animations-Wechsel zu vermeiden. */
    private int lastDirection = -1;

    private boolean moving = false;
    private boolean press = false;

    private StarterSword starterSword;
    private IronSword ironSword;
    private Gun gun;
    private MiniGun miniGun;
    private ShotGun shotGun;
    private Rifle rifle;
    private ArrayList <Weapon> weapons = new ArrayList<>();
    private int currentEquipCooldown = 0;
    private int equipCooldown = 30;

    /**
     * Erstellt einen neuen Spieler und initialisiert alle Komponenten.
     */
    public Player(int x, int y, int width, int height, EntityRegistry registry,
                  KeyboardInputs keyboardInputs, AttackRegistry attackRegistry,
                  TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, 20, 60, registry, attackRegistry, tileManager, gamePanel);
        this.inputs = Objects.requireNonNull(keyboardInputs, "keyboardInputs must not be null");
        movement = new MovementComponent(keyboardInputs, tileManager);
        mass = 3;
        currentHealth = 100;
        starterSword = new StarterSword(this, attackRegistry, tileManager);
        ironSword = new IronSword(this, attackRegistry, tileManager);
        gun = new Gun(this, attackRegistry, tileManager);
        rifle = new Rifle(this, attackRegistry, tileManager);
        miniGun = new MiniGun(this, attackRegistry, tileManager);
        shotGun = new ShotGun(this, attackRegistry, tileManager);
        weapons.add(starterSword);
        weapon = weapons.get(0); //aktuelle Waffe
        skillPoints = 0;

        sheet = new SpriteSheet("src/data/sprites/playerCrawler.png", 1024, 1024);
        loadWeaponAnimations();
    }

    /**
     * Lädt die passenden Lauf- und Angriffsanimationen abhängig von der
     * aktuell ausgerüsteten Waffe aus dem SpriteSheet.
     * Jede Waffe hat eine eigene Zeile im SpriteSheet.
     */
    private void loadWeaponAnimations() {
        walkAnimations = new Animation[4];
        attackAnimation = new Animation[4];

        int walkRow, attackRow;

        if (weapon instanceof StarterSword) {
            walkRow = 1; attackRow = 0;
        } else if (weapon instanceof IronSword) {
            walkRow = 3; attackRow = 2;
        } else if (weapon instanceof Gun) {
            walkRow = 5; attackRow = 4;
        } else if (weapon instanceof Rifle) {
            walkRow = 7; attackRow = 6;
        } else if (weapon instanceof MiniGun) {
            walkRow = 9; attackRow = 8;
        } else if (weapon instanceof ShotGun) {
            walkRow = 11; attackRow = 10;
        } else {
            walkRow = 1; attackRow = 0;
        }

        // Lauf-Animationen für alle 4 Richtungen laden
        walkAnimations[0] = new Animation(new BufferedImage[]{
                sheet.getFrame(walkRow, 0), sheet.getFrame(walkRow, 1), sheet.getFrame(walkRow, 2)
        }, 8, true);
        walkAnimations[2] = new Animation(new BufferedImage[]{
                sheet.getFrame(walkRow, 3), sheet.getFrame(walkRow, 4), sheet.getFrame(walkRow, 5)
        }, 8, true);
        walkAnimations[1] = new Animation(new BufferedImage[]{
                sheet.getFrame(walkRow, 6), sheet.getFrame(walkRow, 7), sheet.getFrame(walkRow, 8)
        }, 8, true);
        walkAnimations[3] = new Animation(new BufferedImage[]{
                sheet.getFrame(walkRow, 9), sheet.getFrame(walkRow, 10), sheet.getFrame(walkRow, 11)
        }, 8, true);

        // Angriffs-Animationen für alle 4 Richtungen laden
        attackAnimation[0] = new Animation(new BufferedImage[]{
                sheet.getFrame(attackRow, 0), sheet.getFrame(attackRow, 1), sheet.getFrame(attackRow, 2)
        }, 15, false);
        attackAnimation[2] = new Animation(new BufferedImage[]{
                sheet.getFrame(attackRow, 3), sheet.getFrame(attackRow, 4), sheet.getFrame(attackRow, 5)
        }, 15, false);
        attackAnimation[1] = new Animation(new BufferedImage[]{
                sheet.getFrame(attackRow, 6), sheet.getFrame(attackRow, 7), sheet.getFrame(attackRow, 8)
        }, 15, false);
        attackAnimation[3] = new Animation(new BufferedImage[]{
                sheet.getFrame(attackRow, 9), sheet.getFrame(attackRow, 10), sheet.getFrame(attackRow, 11)
        }, 15, false);

        currentAnimation = walkAnimations[direction];
    }

    /**
     * Setzt eine neue Waffe und lädt die dazugehörigen Animationen neu.
     *
     * @param newWeapon Neue Waffe des Spielers
     */
    public void setWeapon(Weapon newWeapon) {
        this.weapon = newWeapon;
        loadWeaponAnimations();
        setDirection(getDirection());
    }

    /**
     * Aktualisiert den Spieler jeden Tick.
     * Verarbeitet SkillTree-Eingaben, Bewegung, Tod, Animationen,
     * Angriffe und das Benutzen von Fähigkeiten.
     */
    public void update() {
        currentEquipCooldown --;
        if (inputs == null) return;

        // SkillTree aktualisieren wenn aktiv
        boolean skillTreeActive = skillTree != null && skillTree.isActive;
        if (skillTreeActive) {
            skillTree.update();
        }

        // SkillTree öffnen oder schließen mit P
        if (inputs.getHeldKeys().contains(KeyEvent.VK_P)) {
            if (skillTree != null) {
                if (!skillTree.isActive) {
                    skillTree.open();
                    skillTree.update();
                } else {
                    skillTree.close();
                }
            }
            // Kurze Pause damit der SkillTree sich nicht sofort wieder schließt
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Bewegung und Logik nur wenn SkillTree geschlossen und kein DeathScreen
        if (skillTree == null || !skillTree.isActive) {
            if (!gamePanel.getDeathScreen()) {
                super.update();
                if (currentHealth <= 0) {
                    registry.unregister(this);
                    return;
                }
                movement.move(this);
            }
        }

        // Angriffs- oder Laufanimation aktualisieren
        if (isAnimatingAttack) {
            attackAnimation[direction].update();
            if (attackAnimation[direction].isFinished()) {
                isAnimatingAttack = false;
            }
        } else {
            if(moving) {
                if (this.walkAnimations != null && direction != lastDirection) {
                    currentAnimation = walkAnimations[direction];
                    lastDirection = direction;
                }
                if (currentAnimation != null) currentAnimation.update();
            }
        }

        // Angriff mit J auslösen
        if (inputs.getHeldKeys().contains(KeyEvent.VK_J)) {
                if (weapon.use()){
                    isAnimatingAttack = true;
                    attackAnimation[direction].reset();
                }




        }


        // Fähigkeiten 1-4 benutzen
        if (inputs.getHeldKeys().contains(KeyEvent.VK_1)) { abilityManger.use(1); }
        if (inputs.getHeldKeys().contains(KeyEvent.VK_2)) { abilityManger.use(2); }
        if (inputs.getHeldKeys().contains(KeyEvent.VK_3)) { abilityManger.use(3); }
        if (inputs.getHeldKeys().contains(KeyEvent.VK_4)) { abilityManger.use(4); }
        if (inputs.getHeldKeys().contains(KeyEvent.VK_5)) { abilityManger.use(5); }
        if (inputs.getHeldKeys().contains(KeyEvent.VK_L)) {
            if(!press) {
                press = true;
                if (currentEquipCooldown <= 0) {
                    currentEquipCooldown = equipCooldown;
                    if (weapons.indexOf(weapon) >= weapons.indexOf(weapons.get(weapons.size() - 1))) {
                        weapon = weapons.get(0);
                        weapon.setLastUsed(System.currentTimeMillis());
                    } else {
                        weapon = weapons.get(weapons.indexOf(weapon) + 1);
                    }
                    String displayText = weapon.getClass().getSimpleName();//direkterNameDerKlasse
                    int displayWidth = displayText.length()*25;
                    new TextBox(
                            weapon.getClass().getSimpleName(),
                            (int) getX(),
                            (int) getY() - 60,
                            displayWidth,
                            43,
                            30,
                            gamePanel.getTextBoxManager()
                    );
                    loadWeaponAnimations();
                }
            }
        }
        else {
            press = false;
        }
        if (inputs.getHeldKeys().contains(KeyEvent.VK_K)) {
            if(!press) {
                press = true;
                if (currentEquipCooldown <= 0) {
                    currentEquipCooldown = equipCooldown;
                    weapon.setLastUsed(System.currentTimeMillis()); //damit man nicht direkt angreifen kann
                    int index = weapons.indexOf(weapon);
                    if (index == weapons.indexOf(weapons.get(0))) {
                        weapon = weapons.get(weapons.size() - 1);
                    } else {

                        weapon = weapons.get(index - 1);
                    }
                    String displayText = weapon.getClass().getSimpleName();//direkterNameDerKlasse
                    int displayWidth = displayText.length()*25;
                    new TextBox(
                            weapon.getClass().getSimpleName(),
                            (int) getX(),
                            (int) getY() - 60,
                            displayWidth,
                            43,
                            30,
                            gamePanel.getTextBoxManager()
                    );
                    loadWeaponAnimations();
                }
            }
        }
        else {
            press = false;
        }
    }

    public Weapon getStarterSword(){
        return starterSword;
    }

    public void unlockWeapon(int index){
        weapons.clear();
        weapons.add(starterSword);
        if(index >= 2){
            weapons.add(gun);
        }
        if(index >= 3){
            weapons.add(ironSword);
        }
        if(index >= 4){
            weapons.add(rifle);
        }
        if(index >= 5){
            weapons.add(shotGun);
        }
        if(index >= 6){
            weapons.add(miniGun);
        }
        if (!weapons.contains(weapon)) {
            weapon = weapons.get(0);
            loadWeaponAnimations();
        }
    }

    /**
     * Gibt das aktuelle Sprite des Spielers zurück.
     * Während eines Angriffs wird der Angriffsframe genutzt,
     * ansonsten der aktuelle Laufanimationsframe.
     *
     * @return Aktueller Animations-Frame als BufferedImage
     */
    public BufferedImage getSprite() {
        if (isAnimatingAttack) return attackAnimation[direction].getCurrentFrame();
        if (currentAnimation == null) return null;
        return currentAnimation.getCurrentFrame();
    }

    public void setMoving(boolean move){
        moving = move;
    }
    /**
     * Gibt die aktuellen Skillpunkte des Spielers zurück.
     *
     * @return Skillpunkte
     */
    public int getSkillpoints() { return skillPoints; }

    /**
     * Setzt die Skillpunkte des Spielers auf den angegebenen Wert.
     *
     * @param number Neuer Skillpunktestand
     */
    public void setSkillpoints(int number) { skillPoints = number; }
}