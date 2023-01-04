package jp.fujiwara.demo.roll_definition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ロールを決めるときに大小比較する数字を送り合うときに使うクラス。
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RollNumber {
    /**
     * これまでの各参加者のランダムな数字の内、最大の値
     */
    private Integer number;
}
