package jp.fujiwara.demo.start;

import lombok.Data;

/**
 * 子から親に登録してもらうために送るデータ
 */
@Data
public class RowChildDataModel {
    public RowChildDataModel(final String playerName, final String ip) {
        this.playerName = playerName;
        this.ip = ip;
    }

    private String playerName;
    /**
     * port番号まで含める
     */
    private String ip;
}
