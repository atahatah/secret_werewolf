package jp.fujiwara.demo.global;

/**
 * ゲームの状態を管理する。
 * 全てのユーザはこの情報を共有できている必要がある。
 */
public enum GameStatus {
    /**
     * 最初の初期設定
     */
    START,
    /**
     * 役職決め
     */
    ROLL_DEFINITION,
    /**
     * 人狼は人を殺し、占い師や霊媒師は占い、狩人は守る。
     */
    NIGHT,
    /**
     * 議論する時間。
     */
    NOON,
    /**
     * 投票する時間。
     */
    EVENING,
    /**
     * 処刑、あるいは人狼に殺された時間。
     */
    DEAD,
    /**
     * 人狼サイドが勝利した状態。
     */
    WEREWOLF_WON,
    /**
     * 人間サイドが勝利した状態。
     */
    PEOPLE_WON,
    ;
}
