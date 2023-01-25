package jp.fujiwara.demo.math;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AdditiveSecretSharing {
    private final RandomNum randomNum;

    public Integer[] prepare(int s, int n) {
        Integer[] share = new Integer[n];
        int sum = 0;
        for (int i = 0; i < n - 1; i++) {
            share[i] = randomNum.between(-100, 100);
            sum += share[i];
        }

        share[n - 1] = s - sum;

        return share;
    }

    public int reconstruct(Integer[] share) {
        int s = 0;
        for (int n : share) {
            s += n;
        }
        return s;
    }

    public Integer[] createShareForVote(int number, int numOfPlayers) {
        final int s = (int) Math.pow(numOfPlayers + 1, number);
        return prepare(s, numOfPlayers);
    }

    public int[] reconstructVote(Integer[] share) {
        final int n = share.length;
        int s = reconstruct(share);
        final int[] votes = new int[n];

        for (int i = 0; i < n; i++) {
            votes[i] = s % (n + 1);
            s /= (n + 1);
        }

        return votes;
    }
}
