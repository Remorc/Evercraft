package evercraft.classes

import evercraft.Equippable

class Fighter implements Equippable {
    @Override
    Map getModifiers() {
        [hp: 5, damage: 1]
    }
}
