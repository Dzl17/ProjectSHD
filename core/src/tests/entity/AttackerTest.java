package tests.entity;

import com.a02.entity.Attacker;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttackerTest {

    Attacker attackerTest = new Attacker(1,2,3,4, 5,"electrico",6,true,7,"Electrico",1.1f);
    //attackerTest.hpBar.update(attackerTest, attackerTest.getHP());

    @Test
    public void getAttackDamage() {
        assertEquals(1.1f,attackerTest.getAttackDamage(), 0);
    }


    @Test
    public void getAttackType() {
        assertEquals("Electrico", attackerTest.getAttackType());
    }


    @Test
    public void update() {
    }
}