package jp.fujiwara.demo.global;

import java.util.ArrayList;
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

    public void init() {
        playerStateModel.setHasKilled(false);
        settingsModel.setParticipants(new ArrayList<>());
        settingsModel.setGameState(GameState.START);
    }

    /**
     * 最初の設定画面入力後の初期化
     * 
     * @param startModel 設定の入力内容
     */
    public void init(StartModel startModel) {
        init();
        playerStateModel.setPlayerName(startModel.getPlayerName());
        settingsModel.setIsParent(startModel.getIsParent());
    }

    /**
     * 参加者リストの設定
     * 
     * @param participantsList 参加者リスト
     */
    public void set(List<ParticipantModel> participantsList) {
        // 参加者リストを保存
        settingsModel.setParticipants(participantsList);
        // 参加者リストから自分を探す
        final String myPlayerName = playerStateModel.getPlayerName();
        ParticipantModel me = null;
        for (final ParticipantModel participant : participantsList) {
            if (participant.getPlayerName().equals(myPlayerName)) {
                me = participant;
                break;
            }
        }
        if (me == null) {
            throw new Error("参加者として登録されていない。");
        }
        // 設定された参加者情報を保存する。
        playerStateModel.setNumber(me.getNumber());
    }

    /**
     * 自分のIDを取得する
     */
    public int getMyId() {
        return playerStateModel.getNumber();
    }

    /**
     * 参加者の人数
     */
    public int getNumberOfParticipants() {
        return settingsModel.getParticipants().size();
    }

    /**
     * 次の参加者を取得する。
     * つまり、IDが1つ次の参加者。
     * IDが最大の場合は親(ID=0)。
     */
    public ParticipantModel getNextParticipant() {
        int number = getMyId() + 1;
        if (number >= getNumberOfParticipants()) {
            number = 0;
        }
        return settingsModel.getParticipants().get(number);
    }

    /**
     * 自分のロールを取得する。
     * まだ決定してない時はnullを返す
     */
    public Roll getRoll() {
        return playerStateModel.getRoll();
    }

    /**
     * 自分が親であるかどうかを取得する。
     */
    public boolean getIsParent() {
        return settingsModel.getIsParent();
    }

    /**
     * @param state 新たなゲームの進行状態
     */
    public void set(GameState state) {
        settingsModel.setGameState(state);
    }

    /**
     * 役職を保存
     */
    public void set(Roll roll) {
        playerStateModel.setRoll(roll);
    }

    /**
     * @return ゲームの状態を取得
     */
    public GameState getState() {
        return settingsModel.getGameState();
    }
}
