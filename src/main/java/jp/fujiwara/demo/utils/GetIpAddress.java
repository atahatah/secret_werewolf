package jp.fujiwara.demo.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * IP Addressを取得する。
 */
@Component
public class GetIpAddress {
    private String ipAddress;

    public final String unknownHostName = "Unknown Host";

    @Autowired
    GetPortNum getPortNum;

    /**
     * port番号を含まないIP Addressを取得
     */
    public String getIpAddress() {
        if (ipAddress != null && ipAddress.equals(unknownHostName)) {
            return ipAddress;
        }
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ipAddress = addr.getHostAddress();
        } catch (UnknownHostException e) {
            ipAddress = unknownHostName;
        }
        return ipAddress;
    }

    /**
     * port番号つきのIP Addressを取得
     */
    public String getIpAddressWithPort() {
        return getIpAddress() + ":" + getPortNum.getPortNum();
    }
}
