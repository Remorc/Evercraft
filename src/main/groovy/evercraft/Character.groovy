package evercraft

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
        if (Dice.roll() >= defender.armorClass) {
            defender.takeDamage(1)
        }
    }
}
