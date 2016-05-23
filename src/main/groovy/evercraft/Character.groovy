package evercraft

import static evercraft.AbilityScore.TEN
import static evercraft.Dice.d20
import static java.lang.Math.max

class Character {

    String name
    Alignment alignment
    List<Equippable> equippables = []

    int damageTaken = 0
    int maxHitPoints
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

    Character() {
        setMaxHp(1)
    }

    def isAlive() {
        damageTaken < maxHitPoints
    }

    boolean damage(int roll, int damage) {
        def takeDamage = roll >= armorClass
        damageTaken += takeDamage ? damage : 0
        takeDamage
    }

    def attack(Character defender) {
        def roll = d20.roll()

        if (defender.damage(roll, calculateDamage(roll))) {
            levelUp(experience, 10)
            experience += 10
        }
    }

    def getCurrentHp() {
        maxHitPoints - damageTaken
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
        setMaxHp calculateLevel(experience)
    }

    def setDexterity(AbilityScore abilityScore) {
        dexterity = abilityScore
        adjustArmorClass()
    }

    private int calculateDamage(int roll) {
        roll == 20 ? damage * 2 : damage
    }

    private def levelUp(experience, gain) {
        def levelAfterExperienceGain = calculateLevel(experience + gain)

        if (levelAfterExperienceGain > getLevel()) {
            setMaxHp(levelAfterExperienceGain)
        }
    }

    private static def int calculateLevel(experience) {
        1 + (experience / 1000)
    }

    private def adjustDamage() {
        damage = max((int)(1 + (getLevel() / 2)) + strength.modifier, 1)
    }

    private def adjustArmorClass() {
        armorClass = 10 + dexterity.modifier
    }

    private def setMaxHp(level) {
        def additionalHp = ((int) equippables.modifiers.hp.sum() ?: 0)

        maxHitPoints = level * max(5 + additionalHp + constitution.modifier, 1)
    }

    private setDamage(damage) { }
    private getDamage() { }
}
