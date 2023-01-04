package jp.fujiwara.demo.global.parent;

import java.util.ArrayList;
import java.util.List;

import jp.fujiwara.demo.global.ParticipantModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
class ParentDataModel {
    List<ParticipantModel> participants = new ArrayList<>();
}
