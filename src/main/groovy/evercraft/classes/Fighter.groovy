package evercraft.classes

class Fighter implements CharacterClass {
    @Override
    Map getModifiers() {
        [hp: 5, damage: 1]
    }
}
