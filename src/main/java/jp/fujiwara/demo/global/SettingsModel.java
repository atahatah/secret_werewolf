package jp.fujiwara.demo.global;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * ゲーム進行に必要な設定の情報
 */
@Data
class SettingsModel {
    private Boolean isParent;
    private List<ParticipantModel> participants = new ArrayList<>();
    private GameStatus gameStatus = GameStatus.START;
}
