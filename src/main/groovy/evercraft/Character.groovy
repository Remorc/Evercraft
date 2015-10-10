package evercraft

import static evercraft.Dice.DieType.d20

class Character {

    String name

    int hitPoints = 5

    int armorClass = 10

    Alignment alignment

    enum Alignment {
        Good,
        Neutral,
        Evil
    }

    def takeDamage(int damage) {

    }

    def attack(Character defender) {
        if (Dice.roll(d20) >= defender.armorClass) {
            defender.takeDamage(1)
        }
    }
}
