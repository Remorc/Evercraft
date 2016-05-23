package evercraft

import spock.lang.Specification
import static evercraft.Dice.*


class DiceSpec extends Specification {

    def setup() {
        GroovySpy(Random, global: true)
    }

    def 'should be able to roll a d4'() {
        Random.nextInt(4) >> 2

        expect:
        3 == d4.roll()
    }

    def 'should be able to roll a d6'() {
        Random.nextInt(6) >> 5

        expect:
        6 == d6.roll()
    }

    def 'should be able to roll a d8'() {
        Random.nextInt(8) >> 5

        expect:
        6 == d8.roll()
    }

    def 'should be able to roll a d10'() {
        Random.nextInt(10) >> 0

        expect:
        1 == d10.roll()
    }

    def 'should be able to roll a d12'() {
        Random.nextInt(12) >> 3

        expect:
        4 == d12.roll()
    }

    def 'should be able to roll a d20'() {
        Random.nextInt(20) >> 19

        expect:
        20 == d20.roll()
    }

    def 'should be able to roll a d100'() {
        Random.nextInt(100) >> 72

        expect:
        73 == d100.roll()
    }
}
