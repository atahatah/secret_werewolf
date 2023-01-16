package jp.fujiwara.demo.global.parent;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jp.fujiwara.demo.global.ParticipantModel;
import jp.fujiwara.demo.start.RowChildDataModel;
import jp.fujiwara.demo.start.StartModel;

@SpringBootTest
public class ParentDataServiceTest {
    @Autowired
    ParentDataService parentDataService;

    @Test
    public void 子に対するループ() {
        parentDataService.init(new StartModel("parent name", true, "192.168.12.5", "192.168.12.5", "8080"));
        parentDataService.addChild(new RowChildDataModel("child 1", "sample.com:8081"));
        parentDataService.addChild(new RowChildDataModel("child 2", "sample.com:8082"));
        parentDataService.addChild(new RowChildDataModel("child 3", "sample.com:8083"));
        parentDataService.addChild(new RowChildDataModel("child 4", "sample.com:8084"));

        String childrenNames = "";
        for (final ParticipantModel children : parentDataService.children()) {
            childrenNames += children.getPlayerName() + ";";
        }

        assertEquals(childrenNames, "child 1;child 2;child 3;child 4;");

        String childrenNames2 = "";
        for (final ParticipantModel children : parentDataService.children()) {
            childrenNames2 += children.getPlayerName() + ";";
        }

        assertEquals(childrenNames2, "child 1;child 2;child 3;child 4;");
    }
}
