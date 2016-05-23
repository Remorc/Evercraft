package evercraft.classes

import spock.lang.Specification

class FighterSpec extends Specification {

    def 'it should have an hp modifier of 5'() {
        expect:
        new Fighter().modifiers.hp == 5
    }
}
