package phonemsg.yhsh.cn.togetherrequest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.UUID;

/**
 * @author DELL
 */
public class YhshUtil {

    private static final String REAPAL_UTIL_TAG = "YhshUtil ";

    /**
     * 获得一个UUID
     *
     * @return String UUID
     */
    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }


    /**
     * 获取设备IP地址的方法
     */
    @SuppressLint("MissingPermission")
    public static String getLocalIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
//        if (!wifiManager.isWifiEnabled()) {
//			wifiManager.setWifiEnabled(true);
//        }
        WifiInfo wifiInfo = null;
        if (null != wifiManager) {
            wifiInfo = wifiManager.getConnectionInfo();
        }
        int ipAddress = 0;
        if (null != wifiInfo) {
            ipAddress = wifiInfo.getIpAddress();
        }
        return intToIp(ipAddress);

    }

    private static String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    /**
     * 获取设备MAC地址的方法
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = null;
        if (null != wifi) {
            info = wifi.getConnectionInfo();
        }
        String macAddress = null;
        if (null != info) {
            macAddress = info.getMacAddress();
        }
        return macAddress;
    }

    /**
     * 获取设备Ip地址
     * 规避小米手机无法获取数据连接状态下的Ip地址
     */
    public static String getLocalIPAddress() {
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = enumeration.nextElement();

                if (networkInterface.isUp()) {
                    Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses();
                    while (addressEnumeration.hasMoreElements()) {
                        String ip = addressEnumeration.nextElement().getHostAddress();
                        final String regxIP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
                        if (ip.matches(regxIP) && !"127.0.0.1".equals(ip)) {
                            //IP_Final
                            return ip;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(REAPAL_UTIL_TAG, "getLocalIPAddress() 获取本机ip出现异常，异常信息为：" + e.getMessage());
        }
        return null;
    }


}
