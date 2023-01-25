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
        Integer[] share = { 21, 32, -40 };
        final int s = additiveSecretSharing.reconstruct(share);
        assertEquals(13, s);
    }

    @Test
    public void シェアの作成と復元() {
        final Integer[] ans = { 0, 1, 10, 23, 23, 59, 99, 100 };
        final int n = 5;

        for (int i = 0; i < ans.length; i++) {
            final int s = ans[i];
            Integer[] share = additiveSecretSharing.prepare(s, n);
            assertEquals(s, additiveSecretSharing.reconstruct(share));
        }
    }

    @Test
    public void 加法的秘密分散の加法性() {
        final int s0 = 11;
        final int s1 = 32;
        final int n = 5;

        final Integer[] share0 = additiveSecretSharing.prepare(s0, 5);
        final Integer[] share1 = additiveSecretSharing.prepare(s1, 5);

        final Integer[] addedShare = new Integer[n];
        for (int i = 0; i < n; i++) {
            addedShare[i] = share0[i] + share1[i];
        }

        assertEquals(s0 + s1, additiveSecretSharing.reconstruct(addedShare));
    }

    @Test
    public void 一人分の投票の秘密分散と復元() {
        final int vote = 1;
        final int numOfPlayers = 5;

        final Integer[] share = additiveSecretSharing.createShareForVote(vote, numOfPlayers);
        final int[] voteNum = additiveSecretSharing.reconstructVote(share);

        assertEquals(numOfPlayers, voteNum.length);
        assertEquals(0, voteNum[0]);
        assertEquals(1, voteNum[1]);
        assertEquals(0, voteNum[2]);
        assertEquals(0, voteNum[3]);
        assertEquals(0, voteNum[4]);
    }

    @Test
    public void 複数人の投票の秘密分散と復元() {
        // 初期条件
        final int votes[] = { 1, 2, 1, 0, 3 };
        final int numOfPlayers = votes.length;
        final Integer[][] share = new Integer[numOfPlayers][];

        // シェアの作成
        for (int i = 0; i < numOfPlayers; i++) {
            share[i] = additiveSecretSharing.createShareForVote(votes[i], numOfPlayers);
        }

        // j番目の人 -> i番目の人
        // i番目の人でシェアを合成
        Integer[] totalShare = new Integer[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            for (int j = 0; j < numOfPlayers; j++) {
                totalShare[i] += share[j][i];
            }
        }

        // シェアの復元
        final int[] voteNum = additiveSecretSharing.reconstructVote(totalShare);
        final int[] ansVoteNum = new int[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            ansVoteNum[votes[i]]++;
        }

        // 確認
        for (int i = 0; i < numOfPlayers; i++) {
            assertEquals(ansVoteNum[i], voteNum[i]);
        }
    }
}
