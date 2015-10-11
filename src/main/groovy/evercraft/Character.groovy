package evercraft

import static evercraft.AbilityScore.TEN
import static evercraft.Dice.DieType.d20
import static java.lang.Math.max

class Character {

    String name

    int hitPoints = 5

    int armorClass = 10

    Alignment alignment

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

    def setConstitution(AbilityScore abilityScore) {
        constitution = abilityScore
        hitPoints = max(5 + abilityScore.modifier, 1)
    }

    def setDexterity(AbilityScore abilityScore) {
        dexterity = abilityScore
        armorClass = 10 + abilityScore.modifier
    }

    def takeDamage(int damage) {
        hitPoints -= damage
    }

    def attack(Character defender) {
        defender.takeDamage determineDamage(defender.armorClass, Dice.roll(d20))
    }

    def isAlive() {
        hitPoints > 0
    }

    private def determineDamage(armorClass, roll) {
        int damage = 0

        if (roll >= armorClass) {
            damage = max(1 + strength.modifier, 1)
        }

        if (roll == 20) {
            damage *= 2
        }

        damage
    }
}
