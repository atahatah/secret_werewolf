package jp.fujiwara.demo.evening;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.ParticipantModel;
import jp.fujiwara.demo.math.AdditiveSecretSharing;
import jp.fujiwara.demo.utils.Log;
import jp.fujiwara.demo.utils.ResponseStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EveningService {
    private final AdditiveSecretSharing additiveSecretSharing;
    private final GlobalStateService globalStateService;
    private final RestTemplate restTemplate;
    private final Log log;

    public void init() {
    }

    public void vote(int number) {
        // シェアの生成
        final int numOfPlayers = globalStateService.getNumberOfParticipants();
        final int[] share = additiveSecretSharing.prepare(number, numOfPlayers);

        // シェアの送信
        for (final ParticipantModel player : globalStateService.getParticipants()) {
            if (player.getNumber() == globalStateService.getMyId()) {
                continue;
            }

            final String url = "http://" + player.getIpAddress() + "/vote/share_vote";
            final ShareVoteModel shareVoteModel = new ShareVoteModel(share);
            try {
                restTemplate.postForObject(url, shareVoteModel, ResponseStatus.class);
            } catch (HttpStatusCodeException e) {
                log.error(e.getMessage());
            }
        }
    }
}
