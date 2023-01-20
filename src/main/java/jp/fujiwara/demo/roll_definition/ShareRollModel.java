package jp.fujiwara.demo.roll_definition;

import java.util.List;

import jp.fujiwara.demo.math.ShamirsShare;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareRollModel {
    /**
     * 最初に親に与えられた番号
     */
    private Integer number;

    /**
     * 役職のシェア
     * 長さは2
     */
    private List<ShamirsShare> share;
}
