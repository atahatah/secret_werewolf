package jp.fujiwara.demo.roll_definition;

import lombok.Data;

/**
 * ロールを決めるときに大小比較する数字を送り合うときに使うクラス。
 */
@Data
public class RollNumber {
    public RollNumber(int number) {
        this.number = number;
    }

    /**
     * これまでの各参加者のランダムな数字の内、最大の値
     */
    private Integer number;
}
