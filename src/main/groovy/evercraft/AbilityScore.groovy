package evercraft

enum AbilityScore {
    ONE(-5),
    TWO(-4),
    THREE(-4),
    FOUR(-3),
    FIVE(-3),
    SIX(-2),
    SEVEN(-2),
    EIGHT(-1),
    NINE(-1),
    TEN(0),
    ELEVEN(0),
    TWELVE(1),
    THIRTEEN(1),
    FOURTEEN(2),
    FIFTEEN(2),
    SIXTEEN(3),
    SEVENTEEN(3),
    EIGHTEEN(4),
    NINTEEN(4),
    TWENTY(5)

    final int modifier

    private AbilityScore(modifier) {
        this.modifier = modifier
    }
}
