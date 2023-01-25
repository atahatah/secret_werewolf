package jp.fujiwara.demo.evening.model;

import jp.fujiwara.demo.global.Roll;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
/**
 * 処刑されたプレイヤーの完全な情報を親から子に伝えるためのモデル
 */
public class CompleteExecutedInfoModel {
    /**
     * 処刑されたプレイヤーのid
     */
    private Integer number;
    /**
     * 処刑されたプレイヤーの役職
     */
    private Roll roll;
}
