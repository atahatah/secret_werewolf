package jp.fujiwara.demo.roll_definition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ロールの分配の処理の状態を保持するためのクラス。
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
class RollDefinitionStateModel {
    /**
     * ロールを決めるときに決めた自分のランダムな値。
     * この値が２周目のループで回ってくると自分のロールが決定。
     */
    private Integer myRandomNumber;

    /**
     * ロールを決めるループにおいて何巡目か。
     */
    private Integer loop = 1;

    /**
     * ロールを決める数字を比較するときに数字を１大きくしたか。
     * １周目で自分の数字が回ってきた数字と同じであれば１大きくして次の人に渡すが、
     * 2周目でも同様に1大きくする必要があるため。
     */
    private Boolean isIncremented = false;
}
