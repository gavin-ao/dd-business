package data.driven.business.util;

import org.bson.types.ObjectId;

/**
 * @author 何晋凯
 * @date 2018/06/04
 */
public class UUIDUtil {

    public static String getUUID(){
        return new ObjectId().toString();
    }

    public static void main(String[] args) {
        System.out.println(getUUID());
    }
}
