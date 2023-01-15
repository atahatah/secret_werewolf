package jp.fujiwara.demo.math;

import java.util.Random;

import org.springframework.stereotype.Component;

/**
 * ランダムな数字を作るクラス
 */
@Component
public class RandomNum {
    private final Random random = new Random();

    /**
     * 0以上bound以下のランダムな数字
     * 
     * @param bound 正の数
     */
    public int next(int bound) {
        if (bound <= 0) {
            bound = 1;
        }
        return random.nextInt(bound);
    }
}
