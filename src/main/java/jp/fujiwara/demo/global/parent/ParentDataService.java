package jp.fujiwara.demo.global.parent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import jp.fujiwara.demo.global.ParticipantModel;
import jp.fujiwara.demo.start.RowChildDataModel;
import jp.fujiwara.demo.start.StartModel;
import jp.fujiwara.demo.utils.GetIpAddress;
import lombok.RequiredArgsConstructor;

/**
 * 親が保持すべき情報について管理する。
 * 特にParentDataModel classを管理する。
 */
@RequiredArgsConstructor
@Service
public class ParentDataService {
    final ParentDataModel model = new ParentDataModel();

    private final GetIpAddress getIpAddress;

    public void init() {
        model.setParticipants(new ArrayList<>());
    }

    public void init(final StartModel startModel) {
        init();
        // 最初の参加者として親自身を追加する。
        final String ip = getIpAddress.getIpAddressWithPort();
        final String playerName = startModel.getPlayerName();
        model.participants.add(new ParticipantModel(0, playerName, ip));
    }

    /**
     * はじめに親が子の情報を収集するときに用いる。
     * 
     * @param childModel
     */
    public void addChild(final RowChildDataModel childModel) {
        final int number = model.participants.size();
        final ParticipantModel childInfo = new ParticipantModel(number, childModel);
        model.participants.add(childInfo);
    }

    /**
     * 子のループを回すのに使う
     */
    public Iterable<ParticipantModel> children() {
        return new Iterable<ParticipantModel>() {
            @Override
            public Iterator<ParticipantModel> iterator() {
                final Iterator<ParticipantModel> iterator = model.participants.iterator();
                iterator.next();
                return iterator;
            }
        };
    }

    public List<ParticipantModel> getParticipants() {
        return model.participants;
    }

}
