package jp.fujiwara.demo.start;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.ParticipantModel;
import jp.fujiwara.demo.global.child.ChildDataService;
import jp.fujiwara.demo.global.parent.ParentDataService;
import jp.fujiwara.demo.utils.GetIpAddress;
import jp.fujiwara.demo.utils.ResponseStatus;

/**
 * 初期化時の主な処理の塊を定義
 */
@Service
public class StartService {
    @Autowired
    ChildDataService childDataService;

    @Autowired
    ParentDataService parentDataService;

    @Autowired
    GetIpAddress getIpAddress;

    @Autowired
    GlobalStateService globalStateService;

    /**
     * 一番最初に親に子の情報を伝え、ユーザー登録する
     * 
     * @param startModel 最初に入力された情報
     */
    public void sendToParent(StartModel startModel) {
        final String url = "http://" + startModel.getParentIpAddress() + "/parent/set_child";
        final RowChildDataModel childModel = new RowChildDataModel(startModel.getPlayerName(),
                getIpAddress.getIpAddressWithPort());

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(url, childModel, ResponseStatus.class);
    }

    /**
     * 親がこの情報の取得を締め切り、各子に全ての参加者の情報を伝える。
     */
    public void startGame() {
        // 全参加者
        final List<ParticipantModel> participantList = parentDataService.getParticipants();
        globalStateService.set(participantList);

        // 全ての子に全参加者の情報を送信
        for (final ParticipantModel child : parentDataService.children()) {
            final String url = "http://" + child.getIpAddress() + "/child/notice_participants_info";
            final RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject(url, participantList, ResponseStatus.class);
        }
    }
}
