package jp.fujiwara.demo.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import jp.fujiwara.demo.math.ShamirsShare;
import jp.fujiwara.demo.start.StartModel;
import jp.fujiwara.demo.utils.Log;
import lombok.RequiredArgsConstructor;

/**
 * アプリ全体の情報を一括管理する。
 * 特に、PlayerStateModel.javaとSettingsModel.java。
 */
@RequiredArgsConstructor
@Service
public class GlobalStateService {
    private final Log log;

    final PlayerStateModel playerStateModel = new PlayerStateModel();
    final SettingsModel settingsModel = new SettingsModel();
    final RollShareModel rollShareModel = new RollShareModel();

    public void init() {
        playerStateModel.setHasKilled(false);
        playerStateModel.setRoll(null);
        settingsModel.setParticipants(new ArrayList<>());
        settingsModel.setGameState(GameState.START);
        rollShareModel.setShares(new HashMap<>());
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
     * 次の参加者を取得する。
     * つまり、IDが1つ次の参加者。
     * IDが最大の場合は親(ID=0)。
     */
    public ParticipantModel getPreviousParticipant() {
        int number = getMyId() - 1;
        if (number < 0) {
            number = getNumberOfParticipants() - 1;
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
        log.info(String.format("the next game state is %s", state.name()));
        settingsModel.setGameState(state);
    }

    /**
     * 役職を保存
     */
    public void set(Roll roll) {
        playerStateModel.setRoll(roll);
    }

    /**
     * @param id    親から振られたプレイヤーの番号
     * @param share 渡された職業に関するシェア
     */
    public void set(int id, List<ShamirsShare> share) {
        rollShareModel.getShares().put(id, share);
    }

    /**
     * @return ゲームの状態を取得
     */
    public GameState getState() {
        return settingsModel.getGameState();
    }

    /**
     * @param id 親から振られたプレイヤーの番号
     * @return 職業情報のシェア
     */
    public List<ShamirsShare> getRollShareFor(int id) {
        return rollShareModel.getShares().get(id);
    }

    /**
     * 参加者をidから探す
     * 
     * @param id numberで保存されている
     * @return 見つからなければnull
     */
    public ParticipantModel findPlayerNameById(int id) {
        final List<ParticipantModel> participants = settingsModel.getParticipants();
        for (final ParticipantModel participantModel : participants) {
            if (participantModel.getNumber() == id) {
                return participantModel;
            }
        }
        return null;
    }

    public List<ParticipantModel> getParticipants() {
        return settingsModel.getParticipants();
    }

    /**
     * プレイヤーが殺された場合にそのように記録
     * 
     * @param id 殺されたプレイヤーのid
     */
    public void killed(int id) {
        settingsModel.getParticipants().get(id).setKilled(true);
        if (id == getMyId()) {
            playerStateModel.setHasKilled(true);
        }
    }
}
