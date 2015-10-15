package evercraft

import static evercraft.AbilityScore.TEN
import static evercraft.Dice.d20
import static java.lang.Math.max

class Character {

    String name
    Alignment alignment

    int hitPoints = 5
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

    def takeDamage(int damage) {
        hitPoints -= damage
    }

    def attack(Character defender) {
        defender.takeDamage determineDamage(defender.armorClass)
    }

    def isAlive() {
        hitPoints > 0
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

    private def determineDamage(int armorClass) {
        int roll = d20.roll()
        int damage = 0

        if (roll >= armorClass) {
            damage = this.damage
            levelUp(experience, 10)
            experience += 10
        }

        if (roll == 20) {
            damage *= 2
        }

        damage
    }

    private def levelUp(experience, gain) {
        def levelAfterExperienceGain = calculateLevel(experience + gain)

        if (levelAfterExperienceGain > getLevel()) {
            adjustHitPoints(levelAfterExperienceGain)
        }
    }

    private def adjustDamage() {
        damage = max((int)(1 + (getLevel() / 2)) + strength.modifier, 1)
    }

    private def adjustArmorClass() {
        armorClass = 10 + dexterity.modifier
    }

    private def adjustHitPoints(level) {
        hitPoints = level * max(5 + constitution.modifier, 1)
    }

    private def int calculateLevel(experience) {
        1 + (experience / 1000)
    }

    private setDamage(damage) { }
    private getDamage() { }
}
