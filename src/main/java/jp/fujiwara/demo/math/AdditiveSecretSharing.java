package jp.fujiwara.demo.math;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AdditiveSecretSharing {
    private final RandomNum randomNum;

    public int[] prepare(int s, int n) {
        int[] share = new int[n];
        int sum = 0;
        for (int i = 0; i < n - 1; i++) {
            share[i] = randomNum.between(-100, 100);
            sum += share[i];
        }

        share[n - 1] = s - sum;

        return share;
    }

    public int reconstruct(int[] share) {
        int s = 0;
        for (int n : share) {
            s += n;
        }
        return s;
    }
}
