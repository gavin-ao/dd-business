package data.driven.business.util;

import com.alibaba.fastjson.JSONObject;
import data.driven.business.common.Constant;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * 文件工具类
 * @author 何晋凯
 * @date 2018/06/05
 */
public class FileUtil {

    public static JSONObject uploadFile(byte[] file, String fileName) throws Exception {
        JSONObject result = new JSONObject();
        String yearStr = DateFormatUtil.getLocal("yyyyMM").format(new Date());
        Path path = Paths.get(Constant.FILE_UPLOAD_PATH + yearStr);
        File f = path.toFile();
        if(!f.exists()){
            f.mkdirs();
        }
        fileName = yearStr + "/" + fileName;
        String newFilePath = Constant.FILE_UPLOAD_PATH + fileName;
        FileOutputStream out = new FileOutputStream(newFilePath);
        out.write(file);
        out.flush();
        out.close();
        result.put("relativePath", fileName);
        result.put("newFilePath", newFilePath);
        result.put("success", true);
        return result;
    }
    public static String getFileHeader( MultipartFile file) {
        InputStream is = null;
        String value = null;
        try {
            is = file.getInputStream();
            byte[] b = new byte[4];
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return value;
    }
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        System.out.println(builder.toString());
        return builder.toString();
    }
}
