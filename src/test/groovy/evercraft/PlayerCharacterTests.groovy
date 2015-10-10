package evercraft

import spock.lang.Specification
import spock.lang.Unroll

import static evercraft.Character.Alignment.*
import static evercraft.Dice.DieType.d20

class PlayerCharacterTests extends Specification {

    def character;

    def setup() {
        character = new Character()
        GroovySpy(Dice, global: true)
    }

    def 'should be able to set character name'() {
        given:
        def expectedName = 'sample name'

        when:
        character.name = expectedName

        then:
        expectedName == character.name
    }

    def 'should have default HP of 5'() {
        expect:
        5 == character.hitPoints
    }

    def 'should have default armor class of 10'() {
        expect:
        10 == character.armorClass
    }

    @Unroll
    def 'should be able to set character alignment to #alignment'() {
        when:
        character.alignment = alignment

        then:
        alignment == character.alignment

        where:
        alignment << [Good, Neutral, Evil]
    }

    @Unroll
    def '#takeDamage deal damage to defender with armor class of #ac with dice roll of #roll'() {
        given:
        def defender = Mock Character
        defender.armorClass >> ac
        Dice.roll(d20) >> roll

        when:
        character.attack defender

        then:
        (takeDamage == 'should' ? 1 : 0) * defender.takeDamage(1)

        where:
        takeDamage   | ac | roll
        'should not' | 10 | 2
        'should not' | 12 | 11
        'should'     | 10 | 10
        'should'     | 10 | 19
    }
}