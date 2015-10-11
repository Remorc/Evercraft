package evercraft

import spock.lang.Specification
import spock.lang.Unroll

import static evercraft.AbilityScore.ELEVEN
import static evercraft.AbilityScore.FOUR
import static evercraft.AbilityScore.NINTEEN
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
import static evercraft.AbilityScore.TWO
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

    @Unroll
    def 'should be level #level with #xp experience points and have hp of #hp with a constitution of #con'() {
        given:
        character.experience = xp
        character.constitution = con


        expect:
        level == character.level
        hp == character.hitPoints

        where:
        level | xp   | con     | hp
        1     | 0    | ONE     | 1
        1     | 0    | THREE   | 5 + THREE.modifier
        1     | 0    | TEN     | 5 + TEN.modifier
        1     | 0    | TWENTY  | 5 + TWENTY.modifier
        1     | 999  | FOUR    | 5 + FOUR.modifier
        2     | 1000 | NINTEEN | 2 * (5 + NINTEEN.modifier)
        3     | 2000 | ELEVEN  | 3 * (5 + ELEVEN.modifier)
        4     | 3912 | ONE     | 4 * 1
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