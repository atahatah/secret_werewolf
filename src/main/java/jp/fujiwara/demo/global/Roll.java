package jp.fujiwara.demo.global;

public enum Roll {
    /**
     * 村人
     */
    VILLAGER(new RollCode(0, 1), "村人"),
    /**
     * 占い師
     */
    FORTUNE_TALLER(new RollCode(0, 2), "占い師"),
    /**
     * 霊媒師
     */
    MEDIUM(new RollCode(0, 3), "霊媒師"),
    /**
     * 騎士
     */
    KNIGHT(new RollCode(0, 4), "騎士"),
    /**
     * 人狼
     */
    WEREWOLF(new RollCode(1, 0), "人狼"),
    ;

    public final RollCode rollCode;

    /**
     * 役職の日本語名
     */
    public final String name;

    private Roll(RollCode rollCode, String name) {
        this.rollCode = rollCode;
        this.name = name;
    }

    public static Roll from(RollCode rollCode) {
        for (final Roll roll : Roll.values()) {
            if (roll.rollCode.equals(rollCode)) {
                return roll;
            }
        }
        return Roll.VILLAGER;
    }
}
