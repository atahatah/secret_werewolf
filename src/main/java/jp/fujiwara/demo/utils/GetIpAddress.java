package jp.fujiwara.demo.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * IP Addressを取得する。
 */
@RequiredArgsConstructor
@Component
public class GetIpAddress {
    private String ipAddress;

    public final String unknownHostName = "Unknown Host";

    private final GetPortNum getPortNum;

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
