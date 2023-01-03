package jp.fujiwara.demo.global;

import org.springframework.context.annotation.Scope;

import lombok.Data;

/**
 * 役職などプレイヤーの情報
 * 
 * アプリ全体で共有される
 */
@Data
@Scope("Singleton")
public class PlayerState {
    /**
     * 役職
     */
    private Roll roll;
    /**
     * 殺されたか
     */
    private Boolean hasKilled;
}
