package evercraft

enum Dice {
    d4(4),
    d6(6),
    d8(8),
    d10(10),
    d12(12),
    d20(20),
    d100(100);

    private static final int MIN_ROLL = 1

    private int value;

    private Dice(int value){
        this.value = value;
    }

    public int roll() {
        MIN_ROLL + Random.nextInt(value)
    }
}
