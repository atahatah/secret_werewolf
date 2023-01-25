package jp.fujiwara.demo.evening;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jp.fujiwara.demo.evening.model.CompleteExecutedInfoModel;
import jp.fujiwara.demo.evening.model.ExecutedModel;
import jp.fujiwara.demo.evening.model.ExecutedPlayersJobModel;
import jp.fujiwara.demo.evening.model.ShareVoteModel;
import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.Roll;
import jp.fujiwara.demo.math.ShamirsShare;
import jp.fujiwara.demo.utils.ResponseStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class EveningRestController {
    private final EveningService eveningService;
    private final GlobalStateService globalStateService;

    @PostMapping("/evening/share_vote")
    public ResponseStatus vote(@RequestBody ShareVoteModel shareVoteModel) {
        eveningService.gatherShare(shareVoteModel.getShare());
        return new ResponseStatus();
    }

    @PostMapping("/evening/total")
    public ResponseStatus total(@RequestBody ShareVoteModel shareVoteModel) {
        eveningService.totalShare(shareVoteModel.getShare());
        return new ResponseStatus();
    }

    @PostMapping("/evening/executed")
    public ExecutedPlayersJobModel total(@RequestBody ExecutedModel executedModel) {
        final int executedId = executedModel.getNumber();

        final List<ShamirsShare> share = globalStateService.getRollShareFor(executedId);
        return new ExecutedPlayersJobModel(share);
    }

    @PostMapping("/evening/executed_complete_info")
    public ResponseStatus getExecutedPlayersInfo(@RequestBody CompleteExecutedInfoModel completeExecutedInfoModel) {
        final int executedId = completeExecutedInfoModel.getNumber();
        final Roll executedRoll = completeExecutedInfoModel.getRoll();

        eveningService.executedIs(executedId, executedRoll);

        return new ResponseStatus();
    }
}
