package jp.fujiwara.demo.global;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.fujiwara.demo.math.ShamirsShare;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RollShareModel {
    /**
     * 他のプレイヤーの役職の情報のシェア
     */
    private Map<Integer, List<ShamirsShare>> shares = new HashMap<>();
}
