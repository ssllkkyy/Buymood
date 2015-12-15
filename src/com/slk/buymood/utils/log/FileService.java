package com.slk.buymood.utils.log;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.slk.buymood.utils.CommonConstant;
import com.slk.buymood.utils.CommonUtil;

import android.text.TextUtils;
import android.util.Log;


/**不要忘记权限配置
 * @author Administrator
 *
 */
public class FileService {


    public static final String LOG_FILE_PREFIX = "testbutelLog";
    public static final String LOG_FILE_EXTENSION = ".txt";
	
    public static final int LOG_FILE_DELETE_DELAY = 10;

    public static final boolean bOpenSaveLogToFile = true;

    private String curDate = "";

    private File curLogFile = null;
    
    private String filePath = "";

    public static String getFilePath() {
        String sdCardPath = CommonUtil.getSDPath();
        if (TextUtils.isEmpty(sdCardPath)) {
            return "";
        } else {
            return sdCardPath + File.separator + CommonConstant.SAVE_DIR_NAME
                    + File.separator + "log";
        }
    }

    public FileService() {
        filePath = getFilePath();
        Log.d("FileService", "FileService, filePath=" + filePath);
    }

    public synchronized void saveLogToFile(String logLevel, String tag,
            String method, String sipId, String status, String content) {

        if (TextUtils.isEmpty(filePath)) {
            filePath = getFilePath();
        }

        if (TextUtils.isEmpty(filePath)) {
            Log.d("FileService", "手机刚启动时，sd卡还未mounted，暂不可用，直接返回不写文件");
            return;
        }

        // 创建日志文件夹
        File dest = new File(filePath);
        if (!dest.exists()) {
            Log.d("FileService", "saveLogToFile, filePath=" + filePath);
            if (!dest.mkdirs()) {
                // modified by zhaguitao on 20140308
                // sd卡不可用或者没有权限等原因导致无法创建目录的情况，直接返回不写文件
                Log.d("FileService", "sd卡不可用或者没有权限等原因导致无法创建目录的情况，直接返回不写文件");
                return;
            }
        }
        dest = null;

		// 得到当前时间戳
        String date = DateUtil
                .getCurrentTimeSpecifyFormat(DateUtil.FORMAT_YYYYMMDD_HH_MM_SS_SSS);
		// 日志
        StringBuilder logCon = new StringBuilder();
        logCon.append("[").append(date).append("-").append(logLevel)
                .append("-").append(android.os.Process.myPid()).append("-")
                .append(android.os.Process.myTid()).append("-").append(tag)
                .append("-").append(method).append("-").append(sipId);
        if (!TextUtils.isEmpty(status)) {
            logCon.append("-").append(status);
        }
        logCon.append("]").append(content);

		// 取得YYYY-MM-DD形式的日期
        if (!TextUtils.isEmpty(date) && date.length() >= 8) {
			date = date.substring(0, 8);
		}

        if (TextUtils.isEmpty(curDate)) {
            curDate = date;
            curLogFile = new File(filePath, LOG_FILE_PREFIX + curDate
                    + LOG_FILE_EXTENSION);
        } else {
            if (!curDate.equals(date)) {
                curDate = date;
                curLogFile = new File(filePath, LOG_FILE_PREFIX + curDate
                        + LOG_FILE_EXTENSION);
            } else if (curLogFile == null) {
                curLogFile = new File(filePath, LOG_FILE_PREFIX + curDate
                        + LOG_FILE_EXTENSION);
            }
        }

		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(curLogFile, true)));
			out.newLine();
			out.write(logCon.toString());
		} catch (Exception e) {
            Log.e("FileService", "write log file error", e);
		} finally {
			try {
				if (null != out) {
					out.close();
				}
			} catch (IOException e) {
                Log.e("FileService", "close BufferedWriter error", e);
			}
		}
	}

    
    public synchronized void saveEventToFile(String content) {

    	if(TextUtils.isEmpty(content)){
    		return;
    	}
        // 创建日志文件夹
        File dest = new File(filePath);
        if (!dest.exists()) {
            if (!dest.mkdirs()) {
                // modified by zhaguitao on 20140308
                // sd卡不可用或者没有权限等原因导致无法创建目录的情况，直接返回不写文件
                Log.d("FileService", "sd卡不可用或者没有权限等原因导致无法创建目录的情况，直接返回不写文件");
                return;
            }
        }
        dest = null;
        dest = new File(filePath, "event.txt");
		if(dest!=null&&!dest.exists()){
			try {
				dest.createNewFile();
			} catch (IOException e) {
			    Log.e("FileService", "saveEventToFile error", e);
				dest = null;
			}
		}
				
		BufferedWriter out = null;
		try {
        	if (dest == null) {
        		return;
        	}
			out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(dest, true)));
			out.newLine();
			out.write(content);
		} catch (Exception e) {
            Log.e("FileService", "write event file error", e);
		} finally {
			try {
				if (null != out) {
					out.close();
				}
			} catch (IOException e) {
                Log.e("FileService", "close BufferedWriter error", e);
			}
		}
	}
    
	public synchronized String readEventFromFile() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		File file = new File(filePath + File.separator
				+ "event.txt");
		
		if(file==null||!file.exists()){
			return "";
		}
		
		FileInputStream inStream;
		try {
			inStream = new FileInputStream(file);

			byte[] buffer = new byte[1024];
			int len = -1;

			while ((len = inStream.read(buffer)) != -1) {
				stream.write(buffer, 0, len);
			}

			stream.close();
			inStream.close(); // 关闭输入流
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stream.toString();
	}
	
	public synchronized void emptyEventFile() {
		File file = new File(filePath + File.separator
				+ "event.txt");
		if(file!=null&&file.exists()){
			file.delete();
		}
	}

}
