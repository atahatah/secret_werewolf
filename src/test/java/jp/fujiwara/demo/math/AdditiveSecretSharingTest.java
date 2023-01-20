package jp.fujiwara.demo.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdditiveSecretSharingTest {
    @Autowired
    private AdditiveSecretSharing additiveSecretSharing;

    @Test
    public void シェアの復元() {
        int[] share = { 21, 32, -40 };
        final int s = additiveSecretSharing.reconstruct(share);
        assertEquals(13, s);
    }

    @Test
    public void シェアの作成と復元() {
        final int[] ans = { 0, 1, 10, 23, 23, 59, 99, 100 };
        final int n = 5;

        for (int i = 0; i < ans.length; i++) {
            final int s = ans[i];
            int[] share = additiveSecretSharing.prepare(s, n);
            assertEquals(s, additiveSecretSharing.reconstruct(share));
        }
    }

    @Test
    public void 加法的秘密分散の加法性() {
        final int s0 = 11;
        final int s1 = 32;
        final int n = 5;

        final int[] share0 = additiveSecretSharing.prepare(s0, 5);
        final int[] share1 = additiveSecretSharing.prepare(s1, 5);

        final int[] addedShare = new int[n];
        for (int i = 0; i < n; i++) {
            addedShare[i] = share0[i] + share1[i];
        }

        assertEquals(s0 + s1, additiveSecretSharing.reconstruct(addedShare));
    }
}
