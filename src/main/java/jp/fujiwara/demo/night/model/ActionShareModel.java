package jp.fujiwara.demo.night.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActionShareModel {
    /**
     * 人狼が食い殺すための値のシェア
     * 
     * 元の値では、人狼は選択したプレイヤーのidの負の値、
     * 騎士は正の値、そのほかは０とする。
     */
    private Integer killShare;
}
