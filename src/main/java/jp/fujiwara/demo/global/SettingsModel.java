package jp.fujiwara.demo.global;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ゲーム進行に必要な設定の情報
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
class SettingsModel {
    private Boolean isParent;
    private List<ParticipantModel> participants = new ArrayList<>();
    private GameState gameState = GameState.START;
}
