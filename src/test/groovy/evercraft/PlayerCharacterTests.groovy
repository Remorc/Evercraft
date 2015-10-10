package evercraft

import spock.lang.Specification
import spock.lang.Unroll

import static evercraft.Character.Alignment.*

class PlayerCharacterTests extends Specification {

    def character;

    def setup() {
        character = new Character()
    }

    def "should be able to set character name"() {
        given:
        def expectedName = 'sample name'

        when:
        character.name = expectedName

        then:
        expectedName == character.name
    }

    def "should have default HP of 5"() {
        expect:
        5 == character.hitPoints
    }

    def "should have default armor class of 10"() {
        expect:
        10 == character.armorClass
    }

    @Unroll
    def "should be able to set character alignment to #alignment"() {
        when:
        character.alignment = alignment

        then:
        alignment == character.alignment

        where:
        alignment << [Good, Neutral, Evil]
    }
}