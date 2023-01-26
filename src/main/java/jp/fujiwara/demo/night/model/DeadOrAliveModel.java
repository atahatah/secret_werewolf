package jp.fujiwara.demo.night.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeadOrAliveModel {
    /**
     * 殺されていたらtrue
     */
    private Boolean killed;
}
