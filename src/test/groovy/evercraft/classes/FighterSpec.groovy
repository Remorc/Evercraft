package evercraft.classes

import spock.lang.Specification
import spock.lang.Unroll

class FighterSpec extends Specification {

    @Unroll
    def 'it should have a #field modifier of #value'() {
        expect:
        new Fighter().modifiers[field] == value

        where:
        field    | value
        'hp'     | 5
        'damage' | 1
    }
}
