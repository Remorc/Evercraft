package evercraft

import static evercraft.AbilityScore.TEN
import static evercraft.Dice.DieType.d20
import static java.lang.Math.max

class Character {

    String name
    Alignment alignment

    int hitPoints = 5
    int armorClass = 10
    int experience = 0
    int level

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
        1 + (experience / 1000)
    }

    def setConstitution(AbilityScore abilityScore) {
        constitution = abilityScore
        hitPoints = getLevel() * max(5 + abilityScore.modifier, 1)
    }

    def setDexterity(AbilityScore abilityScore) {
        dexterity = abilityScore
        armorClass = 10 + abilityScore.modifier
    }

    private def determineDamage(int armorClass) {
        int roll = Dice.roll(d20)
        int damage = 0

        if (roll >= armorClass) {
            damage = max((int)(1 + (getLevel() / 2)) + strength.modifier, 1)
            experience += 10
        }

        if (roll == 20) {
            damage *= 2
        }

        damage
    }
}
