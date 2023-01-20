package jp.fujiwara.demo.evening;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jp.fujiwara.demo.utils.ResponseStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class EveningRestController {

    @PostMapping("/evening/share_vote")
    public ResponseStatus vote(@RequestBody ShareVoteModel shareVoteModel) {
        // TODO
        return new ResponseStatus();
    }
}
