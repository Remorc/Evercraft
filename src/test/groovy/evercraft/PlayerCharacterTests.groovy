package evercraft

import spock.lang.Specification
import spock.lang.Unroll

import static evercraft.AbilityScore.ONE
import static evercraft.AbilityScore.SEVEN
import static evercraft.AbilityScore.SEVENTEEN
import static evercraft.AbilityScore.SIX
import static evercraft.AbilityScore.SIXTEEN
import static evercraft.AbilityScore.TEN
import static evercraft.AbilityScore.THIRTEEN
import static evercraft.AbilityScore.THREE
import static evercraft.AbilityScore.TWELVE
import static evercraft.AbilityScore.TWENTY
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

    def 'should be able to take damage'() {
        given:
        character.hitPoints = 10

        when:
        character.takeDamage 1

        then:
        9 == character.hitPoints
    }

    def 'should be considered #status when HP is #hp'() {
        given:
        character.hitPoints = hp

        expect:
        (status == 'alive') == character.isAlive()

        where:
        status  | hp
        'alive' | 1
        'dead'  | 0
        'dead'  | -1
    }

    def 'should default all attributes to TEN'() {
        expect:
        TEN == character.strength
        TEN == character.dexterity
        TEN == character.constitution
        TEN == character.wisdom
        TEN == character.intelligence
        TEN == character.charisma
    }

    def 'should start with no experience'() {
        expect:
        0 == character.experience
    }

    def 'should increase experience on a successful hit'() {
        given:
        def defender = Mock Character
        Dice.roll(d20) >> 19

        when:
        character.attack defender

        then:
        10 == character.experience
    }

    @Unroll
    def 'should be level #level with #xp experience points'() {
        given:
        character.experience = xp

        expect:
        level == character.level

        where:
        level | xp
        1     | 0
        1     | 999
        2     | 1000
        3     | 2000
    }

    @Unroll
    def 'should set hp to #hp with a consitution of #con'() {
        given:
        character.constitution = con

        expect:
        hp == character.hitPoints

        where:
        con    | hp
        ONE    | 1
        THREE  | 5 + THREE.modifier
        TEN    | 5 + TEN.modifier
        TWENTY | 5 + TWENTY.modifier
    }

    @Unroll
    def 'should set AC to #ac with a dexterity of #dex'() {
        given:
        character.dexterity = dex

        expect:
        ac == character.armorClass

        where:
        dex      | ac
        SIX      | 10 + SIX.modifier
        TEN      | 10 + TEN.modifier
        THIRTEEN | 10 + THIRTEEN.modifier
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
    def 'should deal #damage damage to defender with AC of #ac when dice roll is #roll with a strength of #str'() {
        given:
        character.strength = str
        def defender = Mock Character
        defender.armorClass >> ac
        Dice.roll(d20) >> roll

        when:
        character.attack defender

        then:
        1 * defender.takeDamage(damage)

        where:
        ac | roll | str     | damage
        10 | 2    | TEN     | 0
        12 | 11   | TEN     | 0
        24 | 20   | TEN     | 0
        10 | 10   | ONE     | 1
        10 | 15   | SEVEN   | 1
        10 | 10   | TEN     | 1 + TEN.modifier
        10 | 19   | TEN     | 1 + TEN.modifier
        14 | 20   | TEN     | 2 * (1 + TEN.modifier)
        10 | 20   | SIXTEEN | 2 * (1 + SIXTEEN.modifier)
    }
}