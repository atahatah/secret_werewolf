package jp.fujiwara.demo.night.model;

import java.util.List;

import jp.fujiwara.demo.math.ShamirsShare;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KilledRollShareModel {
    private List<ShamirsShare> rollShare;
}
