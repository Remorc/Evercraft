package evercraft

import spock.lang.Specification
import spock.lang.Unroll

import static evercraft.AbilityScore.*
import static evercraft.Character.Alignment.*
import static evercraft.Dice.d20

class CharacterSpec extends Specification {

    def final STARTING_HP = 5

    def setup() {
        GroovySpy(Random, global: true)
    }

    def 'should be able to set character name'() {
        given:
        def expectedName = 'sample name'
        def character = new Character(name: expectedName)

        expect:
        expectedName == character.name
    }

    def 'should not take damage if the roll is less than armor class'() {
        given:
        def character = new Character(maxHitPoints: 10, armorClass: 10)

        when:
        def result = character.damage(5, 7)

        then:
        !result
        10 == character.currentHp
    }

    def 'should take damage if the roll is greater than armor class and not affect max HP'() {
        given:
        def character = new Character(maxHitPoints: 10, armorClass: 10)

        when:
        def result = character.damage(15, 7)

        then:
        result
        3 == character.currentHp
        10 == character.maxHitPoints
    }

    def 'should take damage if the roll is equal to armor class and not affect max HP'() {
        given:
        def character = new Character(maxHitPoints: 10, armorClass: 10)

        when:
        def result = character.damage(10, 7)

        then:
        result
        3 == character.currentHp
        10 == character.maxHitPoints
    }

    @Unroll
    def 'should default #attribute to #expected'() {
        given:
        def character = new Character()

        expect:
        character[attribute] == expected

        where:
        attribute      | expected
        'strength'     | TEN
        'dexterity'    | TEN
        'constitution' | TEN
        'wisdom'       | TEN
        'intelligence' | TEN
        'charisma'     | TEN
        'experience'   | 0
    }

    def 'should increase experience on a successful hit'() {
        given:
        def character = new Character()
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
        def character = new Character(maxHitPoints: 1, damageTaken: damageTaken)

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
        given:
        def character = new Character(alignment: alignment)

        expect:
        alignment == character.alignment

        where:
        alignment << [Good, Neutral, Evil]
    }

    @Unroll
    def 'level #level with a strength of #str should deal #damage damage to defender'() {
        given:
        def character = new Character(experience: (level - 1) * 1000, strength: str)
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
        def character = new Character(experience: 990)
        def defender = Mock Character
        Random.nextInt(d20.value) >> 17
        defender.damage(18, 1) >> true

        when:
        character.attack defender

        then:
        10 == character.currentHp
        10 == character.maxHitPoints
    }

    @Unroll
    def 'should be level #level when starting with #xp experience points and have hp of #hp with a constitution of #con'() {
        given:
        def character = new Character(experience: xp, constitution: con)

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
        def character = new Character(dexterity: dex)

        expect:
        ac == character.armorClass

        where:
        dex      | ac
        SIX      | 10 + SIX.modifier
        TEN      | 10 + TEN.modifier
        THIRTEEN | 10 + THIRTEEN.modifier
    }

    def 'should include hp modifiers'() {
        given:
        def character = new Character(equippables: [new TestEquippable()], constitution: TEN)

        expect:
        character.maxHitPoints == STARTING_HP + new TestEquippable().modifiers.hp
    }

    def class TestEquippable implements Equippable {
        @Override
        Map getModifiers() {
            [hp: 1]
        }
    }
}