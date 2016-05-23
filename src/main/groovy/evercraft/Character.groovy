package evercraft

import static evercraft.AbilityScore.TEN
import static evercraft.Dice.d20
import static java.lang.Math.max

class Character {

    String name
    Alignment alignment

    int remainingHitPoints = 5
    int maxHitPoints = 5
    int armorClass = 10
    int experience = 0
    int damage = 1

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

    def isAlive() {
        remainingHitPoints > 0
    }

    boolean damage(int roll, int damage) {
        def takeDamage = roll >= armorClass
        remainingHitPoints -= takeDamage ? damage : 0
        takeDamage
    }

    def attack(Character defender) {
        def roll = d20.roll()

        if (defender.damage(roll, calculateDamage(roll))) {
            levelUp(experience, 10)
            experience += 10
        }
    }

    private int calculateDamage(int roll) {
        roll == 20 ? damage * 2 : damage
    }

    private def levelUp(experience, gain) {
        def levelAfterExperienceGain = calculateLevel(experience + gain)

        if (levelAfterExperienceGain > getLevel()) {
            adjustHitPoints(levelAfterExperienceGain)
        }
    }

    private def adjustHitPoints(level) {
        maxHitPoints = level * max(5 + constitution.modifier, 1)
        remainingHitPoints = maxHitPoints
    }

    private def int calculateLevel(experience) {
        1 + (experience / 1000)
    }

    def int getLevel() {
        calculateLevel(experience)
    }

    def setStrength(AbilityScore abilityScore) {
        strength = abilityScore
        adjustDamage()
    }

    def setConstitution(AbilityScore abilityScore) {
        constitution = abilityScore
        adjustHitPoints calculateLevel(experience)
    }

    def setDexterity(AbilityScore abilityScore) {
        dexterity = abilityScore
        adjustArmorClass()
    }

    private def adjustDamage() {
        damage = max((int)(1 + (getLevel() / 2)) + strength.modifier, 1)
    }

    private def adjustArmorClass() {
        armorClass = 10 + dexterity.modifier
    }

    private setDamage(damage) { }
    private getDamage() { }
}
