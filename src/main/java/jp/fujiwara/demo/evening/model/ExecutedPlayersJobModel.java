package jp.fujiwara.demo.evening.model;

import java.util.List;

import jp.fujiwara.demo.math.ShamirsShare;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExecutedPlayersJobModel {
    private List<ShamirsShare> share;
}
