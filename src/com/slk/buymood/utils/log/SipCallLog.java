package com.slk.buymood.utils.log;

import com.slk.buymood.utils.CommonUtil;

import android.util.Log;


public class SipCallLog {


    public static int begin(String sipId, String msg) {
        String[] classMethod = CommonUtil.getClassMethod(new Exception(),
                "CallLog", "begin");
        return LogUtil.i(classMethod[0], classMethod[1], sipId,
                LogUtil.LOG_BEGIN, msg);
    }

    public static int end(String sipId, String msg) {
        String[] classMethod = CommonUtil.getClassMethod(new Exception(),
                "CallLog", "end");
        return LogUtil.i(classMethod[0], classMethod[1], sipId,
                LogUtil.LOG_END, msg);
    }

    public static int d(String sipId, String msg) {
        String[] classMethod = CommonUtil.getClassMethod(new Exception(),
                "CallLog", "d");
        int ret = -1;
        if (LogUtil.bOpenLog) {
            ret = Log.d(classMethod[0], classMethod[1] + "-" + sipId + "-"
                    + msg);
        }
        LogUtil.saveLogToFile(LogUtil.LOG_D, classMethod[0], classMethod[1],
                sipId, msg);
        return ret;
    }

    public static int e(String sipId, String msg, Throwable e) {
        String[] classMethod = CommonUtil.getClassMethod(new Exception(),
                "CallLog", "e");
        int ret = -1;
        if (LogUtil.bOpenLog) {
            ret = Log.e(classMethod[0], classMethod[1] + "-" + sipId + "-"
                    + msg, e);
        }
        LogUtil.saveLogToFile(LogUtil.LOG_E, classMethod[0], classMethod[1],
                sipId, msg + ":" + e.getLocalizedMessage());
        return ret;
    }

}
