package evercraft

import spock.lang.Specification
import spock.lang.Unroll

import static evercraft.AbilityScore.*
import static evercraft.Character.Alignment.*
import static evercraft.Dice.d20

class PlayerCharacterTests extends Specification {

    def character;

    def setup() {
        character = new Character()
        GroovySpy(Random, global: true)
    }

    def 'should be able to set character name'() {
        given:
        def expectedName = 'sample name'

        when:
        character.name = expectedName

        then:
        expectedName == character.name
    }

    def 'should not take damage if the roll is less than armor class'() {
        given:
        character.remainingHitPoints = 10
        character.armorClass = 10

        when:
        def result = character.damage(5, 7)

        then:
        10 == character.remainingHitPoints
        false == result
    }

    def 'should take damage if the roll is greater than armor class and not affect max HP'() {
        given:
        character.remainingHitPoints = 10
        character.maxHitPoints = 10
        character.armorClass = 10

        when:
        def result = character.damage(15, 7)

        then:
        3 == character.remainingHitPoints
        10 == character.maxHitPoints
        true == result
    }

    def 'should take damage if the roll is equal to armor class and not affect max HP'() {
        given:
        character.remainingHitPoints = 10
        character.maxHitPoints = 10
        character.armorClass = 10

        when:
        def result = character.damage(10, 7)

        then:
        3 == character.remainingHitPoints
        10 == character.maxHitPoints
        true == result
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
        Random.nextInt(d20.value) >> 17
        defender.damage(18, 1) >> true

        when:
        character.attack defender

        then:
        10 == character.experience
    }

    @Unroll
    def 'should be considered #status when taking #damageTaken damage'() {
        given:
        character.maxHitPoints = 1
        character.damageTaken = damageTaken

        expect:
        (status == 'alive') == character.isAlive()

        where:
        status  | damageTaken
        'alive' | 0
        'dead'  | 1
        'dead'  | 100
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
    def 'of level #level should deal #damage damage to defender with a strength of #str'() {
        given:
        character.experience = (level - 1) * 1000
        character.strength = str

        def defender = Mock Character
        Random.nextInt(d20.value) >> roll - 1

        when:
        character.attack defender

        then:
        1 * defender.damage(roll, damage)

        where:
        level | roll | str     | damage
        1     | 10   | ONE     | 1
        1     | 15   | SEVEN   | 1
        1     | 10   | TEN     | 1 + TEN.modifier
        1     | 19   | TEN     | 1 + TEN.modifier
        1     | 20   | TEN     | 2 * (1 + TEN.modifier)
        1     | 20   | SIXTEEN | 2 * (1 + SIXTEEN.modifier)
        2     | 15   | SIXTEEN | 2 + SIXTEEN.modifier
        3     | 15   | SIXTEEN | 2 + SIXTEEN.modifier
        4     | 15   | SIXTEEN | 3 + SIXTEEN.modifier
        4     | 20   | TWENTY  | 2 * (3 + TWENTY.modifier)
        6     | 15   | NINTEEN | 4 + NINTEEN.modifier
    }

    def 'should increase HP when leveling up from combat'() {
        given:
        def defender = Mock Character
        Random.nextInt(d20.value) >> 17
        defender.damage(18, 1) >> true
        character.experience = 990

        when:
        character.attack defender

        then:
        10 == character.currentHp
        10 == character.maxHitPoints
    }

    @Unroll
    def 'should be level #level when starting with #xp experience points and have hp of #hp with a constitution of #con'() {
        given:
        character.experience = xp
        character.constitution = con

        expect:
        level == character.level
        hp == character.currentHp

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
}