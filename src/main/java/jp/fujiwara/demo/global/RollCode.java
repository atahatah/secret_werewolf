package jp.fujiwara.demo.global;

import java.util.Arrays;
import java.util.List;

import lombok.Data;

@Data
public class RollCode {
    RollCode(Integer... codes) {
        this.codes = Arrays.asList(codes);
    }

    private final List<Integer> codes;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RollCode)) {
            return false;
        }
        RollCode rollCode = (RollCode) obj;
        for (int i = 0; i < codes.size(); i++) {
            if (codes.get(i) != rollCode.codes.get(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (final Integer code : codes) {
            hashCode += code.hashCode();
        }
        return hashCode;
    }

}
