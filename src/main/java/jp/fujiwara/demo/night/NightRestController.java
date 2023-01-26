package jp.fujiwara.demo.night;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.Roll;
import jp.fujiwara.demo.math.ShamirsShare;
import jp.fujiwara.demo.night.model.ActionResultModel;
import jp.fujiwara.demo.night.model.ActionShareModel;
import jp.fujiwara.demo.night.model.DeadOrAliveModel;
import jp.fujiwara.demo.night.model.KilledInfoModel;
import jp.fujiwara.demo.night.model.KilledRollShareModel;
import jp.fujiwara.demo.utils.ResponseStatus;
import lombok.RequiredArgsConstructor;

/**
 * 夜。
 * 人狼が人を狩る時間。
 * 各役職の人間が行う動作についてのAPIを定める。
 */
@RequiredArgsConstructor
@RestController
public class NightRestController {
    private final NightService nightService;
    private final GlobalStateService globalStateService;

    @PostMapping("/night/action_share")
    public ResponseStatus getActionShare(@RequestBody ActionShareModel actionShareModel) {
        final int killShare = actionShareModel.getKillShare();
        nightService.getActionShare(killShare);
        return new ResponseStatus();
    }

    @PostMapping("/night/total")
    public ResponseStatus totalShare(@RequestBody ActionShareModel actionShareModel) {
        final int killShare = actionShareModel.getKillShare();
        nightService.totalShare(killShare);
        return new ResponseStatus();
    }

    @PostMapping("/night/deadOrAlive")
    public ResponseStatus deadOrAlive(@RequestBody DeadOrAliveModel deadOrAliveMode) {
        final boolean killed = deadOrAliveMode.getKilled();
        if (killed) {
            nightService.distributeToConfirmWhoToKill();
        } else {
            nightService.nobodyKilled();
        }
        return new ResponseStatus();
    }

    @PostMapping("/night/killed_share")
    public ResponseStatus getKilledShare(@RequestBody ActionShareModel actionShareModel) {
        final int killShare = actionShareModel.getKillShare();
        nightService.getKillShare(killShare);
        return new ResponseStatus();
    }

    @PostMapping("/night/total_killed")
    public ResponseStatus totalKilledShare(@RequestBody ActionShareModel actionShareModel) {
        final int killShare = actionShareModel.getKillShare();
        nightService.totalKillShare(killShare);
        return new ResponseStatus();
    }

    @PostMapping("/night/who_killed")
    public KilledRollShareModel decideWhoWasKilled(@RequestBody ActionResultModel actionShareModel) {
        final int killedId = actionShareModel.getKilledId();
        final List<ShamirsShare> rollShare = globalStateService.getRollShareFor(killedId);
        final KilledRollShareModel killedRollShareModel = new KilledRollShareModel(rollShare);
        return killedRollShareModel;
    }

    @PostMapping("/night/killed_complete_info")
    public ResponseStatus killedCompleteInfo(@RequestBody KilledInfoModel killedInfoModel) {
        final int killedId = killedInfoModel.getNumber();
        final Roll killedRoll = killedInfoModel.getRoll();
        nightService.killedWas(killedId, killedRoll);
        return new ResponseStatus();
    }
}
