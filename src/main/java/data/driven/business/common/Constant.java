package data.driven.business.common;

/**
 * @author 何晋凯
 * @date 2018/06/05
 */
public class Constant {
    /** 文件上传路径 **/
    public static String FILE_UPLOAD_PATH;
    /** 静态资源访问路径 **/
    public static String STATIC_FILE_PATH = "/static/file/";


    public void setFileUploadPath(String fileUploadPath) {
        FILE_UPLOAD_PATH = fileUploadPath;
    }
}
