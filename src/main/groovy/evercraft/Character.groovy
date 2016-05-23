package evercraft

import static evercraft.AbilityScore.TEN
import static evercraft.Dice.d20
import static java.lang.Math.max

class Character {

    String name
    Alignment alignment
    List<Equippable> equippables = []

    int damageTaken = 0
    int experience = 0

    AbilityScore strength = TEN
    AbilityScore dexterity = TEN
    AbilityScore constitution = TEN
    AbilityScore wisdom = TEN
    AbilityScore intelligence = TEN
    AbilityScore charisma = TEN

    enum Alignment {
        Good,
        Neutral,
        Evil
    }

    boolean isAlive() {
        damageTaken < maxHitPoints
    }

    void attack(Character defender) {
        def roll = d20.roll()

        if (defender.attemptToDamage(roll, calculateDamage(roll))) {
            experience += 10
        }
    }

    boolean attemptToDamage(int roll, int damage) {
        def shouldTakeDamage = roll >= armorClass

        damageTaken += shouldTakeDamage ? damage : 0
        shouldTakeDamage
    }

    int getCurrentHp() {
        maxHitPoints - damageTaken
    }

    int getLevel() {
        calculateLevel(experience)
    }

    int getArmorClass() {
        10 + dexterity.modifier
    }

    int getMaxHitPoints() {
        def additionalHp = ((int) equippables.modifiers.hp.sum() ?: 0)

        level * max(5 + additionalHp + constitution.modifier, 1)
    }

    void setConstitution(AbilityScore abilityScore) {
        constitution = abilityScore
    }

    void setDexterity(AbilityScore abilityScore) {
        dexterity = abilityScore
    }

    private int calculateDamage(int roll) {
        def equippableDamage = ((int) equippables.modifiers.damage.sum() ?: 0)
        def levelDamage = (int) (1 + (level / 2))

        def damage = max(levelDamage + strength.modifier + equippableDamage, 1)

        roll == 20 ? damage * 2 : damage
    }

    private static int calculateLevel(experience) {
        1 + (experience / 1000)
    }

    private setDamage(damage) { }
    private getDamage() { }
}
