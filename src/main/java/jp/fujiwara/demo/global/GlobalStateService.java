package jp.fujiwara.demo.global;

import java.util.List;

import org.springframework.stereotype.Service;

import jp.fujiwara.demo.start.StartModel;

/**
 * アプリ全体の情報を一括管理する。
 * 特に、PlayerStateModel.javaとSettingsModel.java。
 */
@Service
public class GlobalStateService {
    final PlayerStateModel playerStateModel = new PlayerStateModel();
    final SettingsModel settingsModel = new SettingsModel();

    public void init(StartModel startModel) {
        playerStateModel.setHasKilled(false);
        playerStateModel.setPlayerName(startModel.getPlayerName());
        settingsModel.setIsParent(startModel.getIsParent());
    }

    public void set(List<ParticipantModel> participantList) {
        settingsModel.setParticipants(participantList);
    }

}
