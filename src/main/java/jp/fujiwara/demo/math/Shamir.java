package jp.fujiwara.demo.math;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class Shamir {
    private final RandomNum randomNum;

    /**
     * @param s 分散したい値
     * @param k 閾値
     * @param n 作りたいシェアの数
     * @return 作られたシェア
     */
    public ShamirsShare[] prepare(int s, int k, int n) {
        final int degree = k - 1;

        // 定数項がsであるk-1次多項式を適当に作る
        int[] coefficient = new int[degree];
        coefficient[0] = s;
        for (int i = 1; i < degree; i++) {
            coefficient[i] = randomNum.between(1, 10);
        }

        // シェアの生成
        ShamirsShare[] share = new ShamirsShare[n];
        for (int i = 0; i < n; i++) {
            int x = i + 1;
            int y = 0;
            for (int j = 0; j < degree; j++) {
                y += coefficient[j] * Math.pow(x, j);
            }
            share[i] = new ShamirsShare(x, y);
        }

        return share;
    }

    /**
     * @param share シェア
     * @param k     閾値
     * @return 復元された値
     */
    public int reconstruct(ShamirsShare[] share, int k) {
        int s = 0;
        int degree = k - 1;
        for (int i = 0; i < degree + 1; i++) {
            int p = share[i].getY();
            for (int j = 0; j < degree + 1; j++) {
                if (i == j) {
                    continue;
                }
                p *= (0 - share[j].getX());
            }
            for (int j = 0; j < degree + 1; j++) {
                if (i == j) {
                    continue;
                }
                p /= (share[i].getX() - share[j].getX());
            }
            s += p;
        }
        return s;
    }
}
