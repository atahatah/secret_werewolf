package jp.fujiwara.demo.global.parent;

import java.util.ArrayList;
import java.util.List;

import jp.fujiwara.demo.global.ParticipantModel;
import lombok.Data;

@Data
class ParentDataModel {
    List<ParticipantModel> participants = new ArrayList<>();
}
