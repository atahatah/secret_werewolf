package jp.fujiwara.demo.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ShamirTest {
    @Autowired
    private Shamir shamir;

    @Test
    public void シェアの復元() {
        ShamirsShare[] share = new ShamirsShare[] {
                new ShamirsShare(1, 0),
                new ShamirsShare(2, 3),
                new ShamirsShare(3, 8),
        };
        final int s = shamir.reconstruct(share, 3);
        assertEquals(-1, s);
    }

    @Test
    public void シェアの作成と復元() {
        final int[] ans = { 0, 1, 10, 23, 23, 59, 99, 100 };
        final int k = 4;
        final int n = 5;

        for (int i = 0; i < ans.length; i++) {
            final int s = ans[i];
            ShamirsShare[] share = shamir.prepare(s, k, n);
            assertEquals(s, shamir.reconstruct(share, k));
        }
    }
}
