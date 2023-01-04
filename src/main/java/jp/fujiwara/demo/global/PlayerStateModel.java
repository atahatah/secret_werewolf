package jp.fujiwara.demo.global;

import lombok.Data;

/**
 * 役職などプレイヤーの情報
 */
@Data
class PlayerStateModel {
    /**
     * 役職
     */
    private Roll roll;
    /**
     * 殺されたか
     */
    private Boolean hasKilled;
    /**
     * プレイヤー名
     */
    private String playerName;
    /**
     * ID
     * 親が0で、連番
     */
    private int number;
}
