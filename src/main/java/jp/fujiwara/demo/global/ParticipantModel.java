package jp.fujiwara.demo.global;

import jp.fujiwara.demo.start.RowChildDataModel;
import lombok.Data;

/**
 * 参加者の情報を管理する。
 */
@Data
public class ParticipantModel {
    public ParticipantModel(int number, String playerName, String ipAddress) {
        this.number = number;
        this.playerName = playerName;
        this.ipAddress = ipAddress;
    }

    public ParticipantModel(int number, RowChildDataModel childModel) {
        this.number = number;
        playerName = childModel.getPlayerName();
        ipAddress = childModel.getIp();
    }

    private int number;
    private String playerName;
    /**
     * port番号まで含める
     */
    private String ipAddress;
}
