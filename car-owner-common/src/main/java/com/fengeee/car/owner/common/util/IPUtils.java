package com.fengeee.car.owner.common.util;

import com.fengeee.car.owner.common.constant.BaseConstant;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static com.seewo.mis.frame.common.constant.BaseConstant.WINDOWS_OS;

/**
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-12-12
 * @version 1.0
 */
public class IPUtils {

    /**
     * 获取本地IP地址
     *
     * @throws SocketException
     */
    public static String getLocalIP() throws IOException {
        if (isWindowsOS()) {
            return InetAddress.getLocalHost().getHostAddress();
        } else {
            return getLinuxLocalIp();
        }
    }

    /**
     * 判断操作系统是否是Windows
     *
     * @return
     */
    private static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains(BaseConstant.WINDOWS_OS)) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    /**
     * 获取Linux下的IP地址
     *
     * @return IP地址
     */
    private static String getLinuxLocalIp() throws SocketException {

        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            String name = intf.getName();
            if (!name.contains("docker") && !name.contains("lo") && !name.contains("br")) {
                return getIp(intf);
            }
        }
        return null;
    }

    private static String getIp(NetworkInterface networkInterface) {
        for (Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
            InetAddress inetAddress = enumIpAddr.nextElement();
            if (inetAddress.isLoopbackAddress()) {
                continue;
            }
            String address = inetAddress.getHostAddress();
            if (!address.contains("::") && !address.contains("0:0:") && !address.contains("fe80")) {
                return address;
            }
        }
        return null;
    }

}
