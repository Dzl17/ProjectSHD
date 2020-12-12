package com.a02.entity;

import com.a02.component.Inventory;
import com.a02.screens.GameScreen;
import com.a02.component.HealthBar;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

import static com.a02.game.Utils.*;

public class Enemy extends Entity {
    private int id;
    private int hp;
    private int attackDamage;
    private float speed;
    private int goldValue;
    private int startTime;  //Tiempo de inicio de movimiento
    private int effectTimer;    //Timer de efectos de las trampas
    private String wakpath;
    private String attackpath;
    private String deathpath;
    protected Animation<TextureRegion> walkAnimation;
    protected Animation<TextureRegion> attackAnimation;
    protected Animation<TextureRegion> deathAnimation;

    public enum State {
        IDLE, WALKING, ATTACKING, DYING, DEAD
    }
    public enum TrapEffect {
        BURNING, FREEZE, CONFUSED, NEUTRAL
    }

    public State state;
    public TrapEffect trapEffect;

    public Enemy(float x, float y, int width, int height, int id, int hp, int attackDamage, float speed, int startTime, int goldValue, String walkpath, String attackpath, String deathpath) {  //Constructor de enemigos
        super(x, y, width, height);
        this.id = id;
        this.hp = hp;
        this.attackDamage = attackDamage;
        this.speed = speed;
        this.state = State.IDLE;
        this.trapEffect = TrapEffect.NEUTRAL;

        //*
        this.walkAnimation = createAnimation("e1-walk.png", 3, 1, 0.2f);
        this.attackAnimation = createAnimation("e1-attack.png", 2, 2, 0.2f);
        /*/
        this.walkAnimation = createAnimation(walkpath, 3, 1, 0.2f);
        this.attackAnimation = createAnimation(attackpath, 2, 2, 0.2f);
        //*/

        this.deathAnimation = createAnimation("e1-death.png", 2, 2, 0.25f);
        this.deathAnimation = createAnimation(deathpath, 2, 2, 0.25f);
        this.startTime = startTime;
        this.goldValue = goldValue;
        this.hpBar = new HealthBar(this, hp);
    }

    public Enemy() {        //Constructor vacio de enemigos
        super();
        this.id = -1;
        this.hp = 0;
        this.attackDamage = 0;
        this.speed = 0;

        this.goldValue = 50;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getEffectTimer() {
        return effectTimer;
    }

    public void setEffectTimer(int effectTimer) {
        this.effectTimer = effectTimer;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getGoldValue() {
        return goldValue;
    }

    public void setGoldValue(int goldValue) {
        this.goldValue = goldValue;
    }

    public String getWakpath() {
        return wakpath;
    }

    public void setWakpath(String wakpath) {
        this.wakpath = wakpath;
    }

    public String getAttackpath() {
        return attackpath;
    }

    public void setAttackpath(String attackpath) {
        this.attackpath = attackpath;
    }

    public String getDeathpath() {
        return deathpath;
    }

    public void setDeathpath(String deathpath) {
        this.deathpath = deathpath;
    }

    /**
     * Actualiza la posición, estado y efectos de un enemigo.
     * @param gs GameScreen utilizada
     */
    public void update(GameScreen gs) {
        switch (this.state) {
            case IDLE:
                if (gs.secTimer >= this.startTime){
                    if (this.trapEffect != TrapEffect.FREEZE) {
                        this.state = State.WALKING;
                    }
                }
                break;

            case WALKING: //Movimiento a beacon
                if (this.trapEffect == TrapEffect.CONFUSED) {
                    this.move(2,2); //TODO: posición random
                }
                else {
                    this.move(gs.beacon.getX(), gs.beacon.getY());
                }

                if (this.getHp() <= 0) {
                    this.state = State.DYING;
                }
                else if (this.overlappedObject(gs.objects, gs.inventory) != null || this.overlappedObject(gs.objects, gs.inventory) instanceof Trap) {
                    if (!this.overlappedObject(gs.objects, gs.inventory).isGrabbed()) this.state = State.ATTACKING;
                }
                break;

            case ATTACKING:
                if (this.overlappedObject(gs.objects, gs.inventory) != null) {
                    GameObject tempObj = this.overlappedObject(gs.objects, gs.inventory);    //Objeto siendo colisionado

                    if (tempObj.getHp() > 0 && !tempObj.isGrabbed() && gs.secTimer % 60 == 0) { //Pegar 1 vez por segundo
                        tempObj.setHp(tempObj.getHp() - this.attackDamage);
                    } else if (tempObj.getHp() <= 0) {
                        //target.delete()?
                        this.state = State.WALKING;
                    }
                }
                else {
                    this.state = State.WALKING;
                }

                if (this.getHp() <= 0) {
                    this.state = State.DYING;
                }

                break;

            case DYING:
                //playAnimation();
                try {
                    gs.enemies.remove(this);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                this.state = State.DEAD;
                GameScreen.setGold(GameScreen.getGold() + this.goldValue);
                break;

            case DEAD:
                break;
        }

        switch (this.trapEffect) {
            case BURNING:
                if ((gs.secTimer % 30 == 0) && (gs.secTimer < this.effectTimer + 180)) this.hp -= 30;
                break;
            case FREEZE:
                this.state = State.IDLE;
                if (gs.secTimer > this.effectTimer + 300) this.state = State.WALKING;
                break;
            case CONFUSED:
                if (gs.secTimer > this.effectTimer + 280) this.trapEffect = TrapEffect.NEUTRAL;
            case NEUTRAL:
                break;
        }
        this.hpBar.update(this, this.getHp());
    }

    protected void move(float x, float y) {
        double angle = Math.toDegrees(-Math.atan((this.getY() - y) / (this.getX() - x)));

        this.setX((float) (this.getX() + Math.sin(angle) * Gdx.graphics.getDeltaTime() * this.speed));
        this.setY((float) (this.getY() + Math.cos(angle) * Gdx.graphics.getDeltaTime() * this.speed));
    }

    /**
     * Comprueba si existe colisión con algún objeto dentro de una Lista.
     * @param objects List de objetos con los que es posible hallar colisión.
     * @return True si hay colisión, false si no la hay.
     */
    protected boolean overlapsArray(List<GameObject> objects) {
        for (GameObject object: objects) {
            if (object instanceof Trap || object.isGrabbed()) {
                continue;
            }
            else if (this.getX() < object.getX() + object.getWidth() && this.getX() + this.getWidth() > object.getX() &&
                    this.getY() < object.getY() + object.getHeight() && this.getY() + this.getHeight() > object.getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Halla qué objeto está colisionando con el enemigo, excluyendo los objetos del inventario.
     * @param objects List de objetos con los que es posible hallar colisión.
     * @return Objecto colisionado.
     */
    protected GameObject overlappedObject(List<GameObject> objects, Inventory inv) {
        for (GameObject object: objects) {
            if (this.getX() < object.getX() + object.getWidth() && this.getX() + this.getWidth() > object.getX() &&
                    this.getY() < object.getY() + object.getHeight() && this.getY() + this.getHeight() > object.getY() &&
                    !inv.contains(object)) {
                return object;
            }
        }
        return null;
    }

    /**
     * Devuelve un TextureRegion con la animación correspondiente al estado
     * @param animationTimer Reloj de la animación
     * @return El TextureRegion de animación
     */
    public TextureRegion getCurrentAnimation(float animationTimer) {
        switch (this.state) {
            case IDLE:
                return new TextureRegion(new Texture(Gdx.files.internal("e1-idle.png")));
            case WALKING:
                return this.walkAnimation.getKeyFrame(animationTimer, true);
            case ATTACKING:
                return this.attackAnimation.getKeyFrame(animationTimer, true);
            case DYING:
                return this.deathAnimation.getKeyFrame(animationTimer, false);
        }
        return null;
    }
}