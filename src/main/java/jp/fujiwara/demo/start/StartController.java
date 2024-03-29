package jp.fujiwara.demo.start;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.fujiwara.demo.global.GlobalStateService;
import jp.fujiwara.demo.global.child.ChildDataService;
import jp.fujiwara.demo.global.parent.ParentDataService;
import jp.fujiwara.demo.roll_definition.RollDefinitionService;
import jp.fujiwara.demo.utils.GetIpAddress;
import jp.fujiwara.demo.utils.GetPortNum;
import lombok.RequiredArgsConstructor;

/**
 * 初期化をするためのコントローラー
 * 1. /start/pageにアクセス
 * 2. 情報を入力後、/start/configにPOST
 * 3. 親が/start/startにポストすることでゲーム開始
 */
@RequiredArgsConstructor
@Controller
public class StartController {
    private final GetPortNum getPortNum;
    private final ChildDataService childDataService;
    private final ParentDataService parentDataService;
    private final StartService startService;
    private final GlobalStateService globalStateService;
    private final GetIpAddress getIpAddress;
    private final RollDefinitionService rollDefinitionService;

    @GetMapping("/")
    public String index() {
        return "redirect:/start/page";
    }

    /**
     * 一番最初の情報を入力するページ
     * 
     * @param startModel 入力された情報を渡す
     * @return 一番最初に表示されるページの名前
     */
    @GetMapping("/start/page")
    public String page(@ModelAttribute("model") StartModel startModel, Model model) {
        // ip addressの取得
        String ip = getIpAddress.getIpAddress();
        model.addAttribute("ip", ip);
        // portの取得
        String port = getPortNum.getPortNum();
        model.addAttribute("port", port);
        return "init";
    }

    /**
     * 一番最初の情報を入力後表示されるページ。
     * このページで始まるまで待つ。
     *
     * @param startModel 入力された情報を受け取る
     * @return ゲームが始まるまで待つページの名前
     */
    @PostMapping("/start/config")
    public String config(@ModelAttribute StartModel startModel) {
        globalStateService.init(startModel);
        getIpAddress.setIpAddress(startModel.getMyIpAddress());
        getPortNum.setPortNum(startModel.getMyPortNum());

        if (startModel.getIsParent()) {
            parentDataService.init(startModel);
            return "redirect:/start/waiting";
        } else {
            childDataService.init(startModel);
            startService.sendToParent(startModel);
            return "redirect:/management";
        }
    }

    /**
     * 親がスタートするまで待つ画面
     * 
     * @param model 参加者の一覧を渡す
     * @return 親がスタートするまで待つ画面
     */
    @GetMapping("/start/waiting")
    public String waiting(Model model) {
        model.addAttribute("participants", parentDataService.getParticipants());
        return "parent_start_idle";
    }

    /**
     * ゲームを開始するために親がGETする。
     * これが呼ばれることでゲームが開始する。
     *
     * @return 親の開始後の画面を返す
     */
    @PostMapping("/start/start")
    public String start() {
        startService.startGame();
        rollDefinitionService.initRollDefinition();
        return "redirect:/management";
    }
}
