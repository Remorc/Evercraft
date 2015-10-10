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
}
