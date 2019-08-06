package phonemsg.yhsh.cn.togetherrequest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * @author DELL
 */
public class PhoneMessageActivity extends Activity {

    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            finish();
            return;
        }
        setContentView(R.layout.activity_phone_message);
        //请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1008);
                return;
            }
        }
        setPhoneData();
    }

    private void setPhoneData() {
        getGPS();
        setDataToID(R.id.tv_one, "IP", getIpAddress(this));
        if (TextUtils.isEmpty(GetMacIpUtils.readMac(this))) {
            setDataToID(R.id.tv_two, "MAC", getMacAddressFromIp(this));
            setDataToID(R.id.tv_six, "WIFI MAC", getMacAddressFromIp(this));
        } else {
            setDataToID(R.id.tv_two, "MAC", GetMacIpUtils.readMac(this));
            setDataToID(R.id.tv_six, "WIFI MAC", GetMacIpUtils.readMac(this));
        }
        setDataToID(R.id.tv_three, "IMEI", getIMEI(this));
        setDataToID(R.id.tv_four, "IMSI", getIMSI(this));
        setDataToID(R.id.tv_five, "ICCID", getICCID(this));
    }

    public String getMacAddressFromIp(Context context) {
        String mac_s = "null";
        StringBuilder buf = new StringBuilder();
        try {
            byte[] mac;
            NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(getIpAddress(context)));
            mac = ne.getHardwareAddress();
            for (byte b : mac) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            mac_s = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mac_s;
    }

    public String getIpAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // 3/4g网络
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //  wifi网络
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
                return ipAddress;
            } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                // 有限网络
                return getLocalIp();
            } else {
                //没有任何网络，强制开启WiFi
                //判断wifi是否开启
                if (!wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(true);
                }
            }
        }
        return "null";
    }

    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    // 获取有限网IP
    private static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return "null";

    }


    /**
     * 获取手机IMEI
     *
     * @param context
     * @return
     */
    public final String getIMEI(Context context) {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMEI号
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1008);
                }
            }
            String imei = telephonyManager.getDeviceId();
            //在次做个验证，也不是什么时候都能获取到的啊
            if (imei == null) {
                imei = "";
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 获取手机IMSI
     */
    public String getIMSI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMSI号
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1008);
                }
            }
            String imsi = telephonyManager.getSubscriberId();
            if (null == imsi) {
                imsi = "";
            }
            return imsi;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取手机ICCID
     */
    public String getICCID(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMSI号
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1008);
                }
            }
            String iccid = telephonyManager.getSimSerialNumber();
            if (null == iccid) {
                iccid = "";
            }
            return iccid;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void getGPS() {
        //1.获取位置的管理者
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //2.获取定位方式
        //2.1获取所有的定位方式，true:表示返回所有可用定位方式
        List<String> providers = locationManager.getProviders(true);
        for (String string : providers) {
            System.out.println(string);
        }
        //2.2获取最佳的定位方式
        Criteria criteria = new Criteria();
        //设置是否可以定位海拔,如果设置定位海拔，返回一定是gps
        criteria.setAltitudeRequired(true);
        //criteria : 设置定位属性
        //enabledOnly : true如果定位可用就返回
        String bestProvider = locationManager.getBestProvider(criteria, true);
        System.out.println("最佳的定位方式:" + bestProvider);
        //3.定位
        MyLocationListener myLocationListener = new MyLocationListener();
        //provider : 定位的方式
        //minTime : 定位的最小时间间隔
        //minDistance : 定位最小的间隔距离
        //LocationListener : 定位监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1009);
                return;
            }
        }
        //原因是API本身是GPS优先的，这样在室内测试时会出现bestprovider得到的是GPS方式，但是却无法定位的情况（室内GPS信号很弱，基本不可用）。所以我改成了优先选择网络定位，然后再选择GPS定位。
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            bestProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.PASSIVE_PROVIDER)) {
            bestProvider = LocationManager.GPS_PROVIDER;
        } else {
            // 当没有可用的位置提供器时，弹出Toast提示用户
            Toast.makeText(this, "Please Open Your GPS or Location Service", Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.requestLocationUpdates(bestProvider, 0, 0, myLocationListener);
    }

    class MyLocationListener implements LocationListener {
        private String latLongString;

        /**
         * 当定位位置改变的调用的方法
         *
         * @param location 当前的位置
         */
        @Override
        public void onLocationChanged(Location location) {
            //获取精确位置
            float accuracy = location.getAccuracy();
            //获取海拔
            double altitude = location.getAltitude();
            //获取纬度，平行
            final double latitude = location.getLatitude();
            //获取经度，垂直
            final double longitude = location.getLongitude();
            Log.e("打印经纬度：", "longitude:" + longitude + "  latitude:" + latitude + "精确位置" + accuracy + "海拔" + altitude);
            setDataToID(R.id.tv_seven, "GPS", longitude + "," + latitude);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Address> addsList = null;
                    Geocoder geocoder = new Geocoder(PhoneMessageActivity.this);
                    try {
                        //得到的位置可能有多个当前只取其中一个
                        addsList = geocoder.getFromLocation(latitude, longitude, 10);
                        Log.e("打印拿到的城市", addsList.toString());
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                    if (addsList != null && addsList.size() > 0) {
                        for (int i = 0; i < addsList.size(); i++) {
                            final Address ads = addsList.get(i);
                            //拿到城市
                            latLongString = ads.getLocality();
//                            latLongString = ads.getAddressLine(0);//拿到地址
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("打印拿到的城市的地址", latLongString + ads.getAddressLine(0) + ads.getAddressLine(1) + ads.getAddressLine(4));
                                    Toast.makeText(PhoneMessageActivity.this, "当前所在的城市为" + latLongString + ads.getAddressLine(0) + ads.getAddressLine(4) + ads.getAddressLine(1), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }
            }).start();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }


    private void setDataToID(int classid, String left, String right) {
        TextView classId = findViewById(classid);
        classId.setText(left + ":" + right);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1009) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getGPS();
            } else {
                finish();
            }
        } else if (requestCode == 1008) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //读取手机信息
                setPhoneData();
            } else {
                finish();
            }
        }
    }
}
