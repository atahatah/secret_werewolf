package jp.fujiwara.demo.roll_definition;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.utils.Log;
import jp.fujiwara.demo.utils.ResponseStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ShareRollRestController {
    private final Log log;
    private final ShareRollService shareRollService;
    private final GlobalStateService globalStateService;

    @PostMapping("/roll/share")
    public ResponseStatus getShare(@RequestBody ShareRollModel shareRollModel) {
        log.debug("******ShareRollRestController.getShare*****");
        log.debug(String.format("get share (%d, %d) (%d, %d) from %s(%d)",
                shareRollModel.getShare().get(0).getX(),
                shareRollModel.getShare().get(0).getY(),
                shareRollModel.getShare().get(1).getX(),
                shareRollModel.getShare().get(1).getY(),
                globalStateService.getParticipants().get(shareRollModel.getNumber()),
                shareRollModel.getNumber()));
        shareRollService.storeShare(shareRollModel.getNumber(), shareRollModel.getShare());

        // 親から送られてきた時だけ送る。
        // それによって１回だけ送るようにする。
        if (!globalStateService.getIsParent() && shareRollModel.getNumber() == 0) {
            shareRollService.share();
        }
        return new ResponseStatus();
    }
}
