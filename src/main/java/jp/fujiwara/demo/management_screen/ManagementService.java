package jp.fujiwara.demo.management_screen;

import org.springframework.stereotype.Service;

import jp.fujiwara.demo.evening.EveningService;
import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.child.ChildDataService;
import jp.fujiwara.demo.global.parent.ParentDataService;
import jp.fujiwara.demo.night.NightService;
import jp.fujiwara.demo.noon.NoonService;
import jp.fujiwara.demo.parent_child.ParentService;
import jp.fujiwara.demo.roll_definition.RollDefinitionService;
import jp.fujiwara.demo.start.StartService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ManagementService {
    private final StartService startService;
    private final ParentService parentService;
    private final NoonService noonService;
    private final ParentDataService parentDataService;
    private final ChildDataService childDataService;
    private final RollDefinitionService rollDefinitionService;
    private final NightService nightService;
    private final EveningService eveningService;
    private final GlobalStateService globalStateService;

    public void initAll() {
        startService.init();
        parentService.init();
        noonService.init();
        nightService.init();
        eveningService.init();
        globalStateService.init();
        parentDataService.init();
        childDataService.init();
        rollDefinitionService.init();
    }
}
