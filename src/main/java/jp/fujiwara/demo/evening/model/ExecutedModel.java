package jp.fujiwara.demo.evening.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 処刑される人の情報を通知するためのモデル
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExecutedModel {
    /**
     * 処刑される人のID
     */
    private Integer number;
}
