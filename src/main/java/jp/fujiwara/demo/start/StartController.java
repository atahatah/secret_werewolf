package jp.fujiwara.demo.start;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.fujiwara.demo.utils.GetPortNum;

/**
 * 初期化をするためのコントローラー
 * 1. /start/pageにアクセス
 * 2. 情報を入力後、/start/configにPOST
 * 3. 親が/start/startにポストすることでゲーム開始
 */
@Controller
public class StartController {
    /**
     * ポート番号を取得するためのクラス
     */
    @Autowired
    private GetPortNum getPortNum;

    /**
     * 一番最初の情報を入力するページ
     * 
     * @param startModel 入力された情報を渡す
     * @return 一番最初に表示されるページの名前
     */
    @GetMapping("/start/page")
    public String page(@ModelAttribute("model") StartModel startModel, Model model) {
        // ip addressの取得
        String ip;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        } catch (UnknownHostException e) {
            ip = "Unknown Host";
        }
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
        if (startModel.getIsParent()) {
            return "parent_start_idle";
        } else {
            return "redirect:/management/child";
        }
    }

    /**
     * ゲームを開始するために親がポストする。
     * これが呼ばれることでゲームが開始する。
     *
     * @return 親の開始後の画面を返す
     */
    @PostMapping("/start/start")
    public String start() {
        return "redirect:/management/parent";
    }

    @Bean
    public InitialData getInitialData() {
        return new InitialData();
    }
}
