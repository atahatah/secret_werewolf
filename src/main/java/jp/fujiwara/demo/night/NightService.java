package jp.fujiwara.demo.night;

import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NightService {
    @Getter
    private boolean actionDecided = false;

    public void init() {
        actionDecided = false;
    }

    public void werewolf(Integer selectedNumber) {
        actionDecided = true;
    }

    public void knight(Integer selectedNumber) {
        actionDecided = true;
    }
}
