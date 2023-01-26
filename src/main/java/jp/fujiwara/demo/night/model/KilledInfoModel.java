package jp.fujiwara.demo.night.model;

import jp.fujiwara.demo.global.Roll;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KilledInfoModel {
    private Integer number;
    private Roll roll;
}
