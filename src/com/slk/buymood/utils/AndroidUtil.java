package com.slk.buymood.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.slk.buymood.utils.log.LogUtil;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;


public class AndroidUtil {

    private static final String TAG = "AndroidUtil";

    private static Point deviceSize = null;

    /**
     * @author: zhaguitao
     * @Title: getNumCores
     * @Description: 获取cpu核心数
     * @return
     * @date: 2014-1-21 下午1:21:26
     */
    public static int getNumCores() {
        // Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                // Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            Log.d(TAG, "CPU Count: " + files.length);
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            // Print exception
            Log.d(TAG, "CPU Count: Failed.");
            e.printStackTrace();
            // Default to return 1 core
            return 1;
        }
    }

    /**
     * @author: zhaguitao
     * @Title: getCpuFrequence
     * @Description: 获取cpu频率，单位：KHz
     * @return
     * @date: 2014-1-21 下午3:02:30
     */
    public static String getCpuFrequence() {
        String result = "0";
        ProcessBuilder cmd;
        try {
            String[] args = { "/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
            cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            result = reader.readLine();
        } catch (IOException ex) {
            LogUtil.d("IOException="+ ex);
        }
        if (TextUtils.isEmpty(result)) {
            result = "0";
        }
        return result;
    }

    /**
     * @author: zhaguitao
     * @Title: getMinCpuFreq
     * @Description: 获取cpu最小频率，单位：KHz
     * @return
     * @date: 2014-1-21 下午3:03:36
     */
    public static String getMinCpuFreq() {
        String result = "0";
        ProcessBuilder cmd;
        try {
            String[] args = { "/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq" };
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "0";
        }
        return result.trim();
    }

    /**
     * @author: zhaguitao
     * @Title: getCurCpuFreq
     * @Description: 获取cpu当前频率，单位：KHz
     * @return
     * @date: 2014-1-21 下午3:03:59
     */
    public static String getCurCpuFreq() {
        String result = "0";
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(
                    "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
            br = new BufferedReader(fr);
            String text = br.readLine();
            result = text.trim();
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        return result;
    }

    /**
     * @author: zhaguitao
     * @Title: getFreeMemory
     * @Description: 获取可用运存大小，单位：M
     * @param context
     * @return
     * @date: 2014-1-21 下午3:04:53
     */
    public static float getFreeMemory(Context context) {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存
        return mi.availMem / (1024 * 1024.0f);
    }

    /**
     * @author: zhaguitao
     * @Title: getTotalMemory
     * @Description: 获取系统总内存大小，单位：M
     * @param context
     * @return
     * @date: 2014-1-21 下午3:06:58
     */
    public static float getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        FileReader localFileReader = null;
        BufferedReader localBufferedReader = null;
        try {
            localFileReader = new FileReader(str1);
            localBufferedReader = new BufferedReader(localFileReader, 8192);
            // 读取meminfo第一行，系统总内存大小
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            // 获得系统总内存，单位是KB，乘以1024转换为Byte
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            try {
                if (localBufferedReader != null) {
                    localBufferedReader.close();
                }
                if (localFileReader != null) {
                    localFileReader.close();
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        return initial_memory / 1024.0f;
    }

    public static boolean availableExStorageMemory() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {

            /** 获取存储卡路径 */
            File sdcardDir = Environment.getExternalStorageDirectory();
            /** StatFs 看文件系统空间使用情况 */
            StatFs statFs = new StatFs(sdcardDir.getPath());
            /** Block 的 size */
            long blockSize = statFs.getBlockSize();
            /** 总 Block 数量 */
            int totalBlocks = statFs.getBlockCount();
            /** 已使用的 Block 数量 */
            int availableBlocks = statFs.getAvailableBlocks();

            if (availableBlocks * blockSize > 10 * 1024 * 1024) {
                return true;
            }
        }
        return false;
    }

    /**
     * @author: zhaguitao
     * @Title: getDeviceSize
     * @Description: 获取手机屏幕宽高
     * @param context
     * @return
     * @date: 2014-3-13 上午9:45:55
     */
    @SuppressLint("NewApi")
    public static Point getDeviceSize(Context context) {
        if (deviceSize == null) {
            deviceSize = new Point(0, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                ((WindowManager) context
                        .getSystemService(Context.WINDOW_SERVICE))
                        .getDefaultDisplay().getSize(deviceSize);
            } else {
                Display display = ((WindowManager) context
                        .getSystemService(Context.WINDOW_SERVICE))
                        .getDefaultDisplay();
                deviceSize.x = display.getWidth();
                deviceSize.y = display.getHeight();
                display = null;
            }
        }
        return deviceSize;
    }

    /** 唯一标志长度够的场合，作为补位用 */
    private static final String UNIQUEID_SUFFIX = "ABCDEFABCDEF";

    /**
     * 获取Android设备的唯一标识字符串
     * 
     * @param context
     * @return
     */
    public static String getDeviceUniqueId(Context context) {
        String uniqueId = "";
        // 1、获取mac地址
        WifiManager wifiMgr = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        if (wifiInfo != null) {
            uniqueId = wifiInfo.getMacAddress();
            Log.d(TAG, "macAddress=" + uniqueId);
        }
        if (!TextUtils.isEmpty(uniqueId)) {
            return uniqueId;
        }

        // 2、获取android_id
        uniqueId = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
        Log.d(TAG, "androidId=" + uniqueId);
        if (!TextUtils.isEmpty(uniqueId)) {
            return uniqueId;
        }

        // 3、获取imei
        TelephonyManager telephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        uniqueId = telephonyMgr.getDeviceId();
        Log.d(TAG, "diviceId=" + uniqueId);

        return uniqueId;
    }

    /**
     * 获取电视终端的唯一标识字符串
     * 
     * @return
     */
    public static String getTVUniqueId(Context context) {
        String uniqueId = getDeviceUniqueId(context);
        if (TextUtils.isEmpty(uniqueId)) {
            // 默认的唯一标识
            return "TV_008F0E2B5B3A";
        } else {
            uniqueId = uniqueId.replaceAll(":", "").toUpperCase();
            if (uniqueId.length() < 12) {
                // 补位
                uniqueId = uniqueId
                        + UNIQUEID_SUFFIX.substring(0, 12 - uniqueId.length());
            } else if (uniqueId.length() > 12) {
                // 截取
                uniqueId = uniqueId.substring(uniqueId.length() - 12);
            }
            return "TV_" + uniqueId;
        }
    }

    /**
     * 
     * Description: dp 转换 px
     * 
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 收起状态栏
     * 
     * @param context
     *            上下文对象
     * @return 成功收起状态栏返回true,否则返回false
     */
    public static boolean collapseStatusBar(Context context) {
        Object statusbarService = context.getSystemService("statusbar");
        if (statusbarService == null) {
            return false;
        }
        try {
            Class<?> statusBarManager = Class
                    .forName("android.app.StatusBarManager");
            if (statusBarManager == null) {
                return false;
            }
            Method collapseMethod;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                collapseMethod = statusBarManager.getMethod("collapsePanels");
            } else {
                collapseMethod = statusBarManager.getMethod("collapse");
            }
            if (collapseMethod == null) {
                return false;
            }
            collapseMethod.invoke(statusbarService);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getString(int id) {
        return BuymoodApplication.getContext().getString(id);
    }

    public static SparseArray<String> parseStringArray(int stringArrayResourceId) {
        String stringArray[] = BuymoodApplication.getContext().getResources()
                .getStringArray(stringArrayResourceId);
        SparseArray<String> outputArray = new SparseArray<String>(
                stringArray.length);
        for (String entry : stringArray) {
            String[] splitResult = entry.split("\\|", 2);
            outputArray.put(Integer.valueOf(splitResult[0]), splitResult[1]);
        }
        return outputArray;
    }

    public static Map<String, String> fetchCpuInfo() {
        FileReader fr = null;
        BufferedReader br = null;
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            fr = new FileReader("/proc/cpuinfo");
            br = new BufferedReader(fr, 8192);
            
            String str = "";
            String[] lineInfo = null;
            while((str = br.readLine()) != null) {
                lineInfo = str.split(":");
                if (lineInfo.length >= 2) {
                    resultMap.put(lineInfo[0].trim(), lineInfo[1].trim());
                }
            }
        } catch (IOException e) {
            LogUtil.e("fetchCpuInfo", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultMap;
    }

    /**
     * 判断GPS开关是否打开
     * 
     * @param context
     * @return
     */
    public static boolean isGPSOpen(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    
    /**
     * 判断网络位置开关是否打开
     * 
     * @param context
     * @return
     */
    public static boolean isAGPSOpen(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。
        // 主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}
