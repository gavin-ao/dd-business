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
//        String sql = "INSERT INTO `reward_act_command` (`command_id`, `command`, `command_type`, `act_id`, `user_id`, `app_info_id`, `used`, `create_at`) VALUES ";
//        StringBuilder sb = new StringBuilder(sql);
//        for (int i = 0; i < 500; i++){
//            String b = "('" + getUUID() + "', '€k4aNbbc8TEr€', '2', '8', 'fkxg', '5b3dd7c91d76102dd8a2d0d4', '0', '2018-07-29 17:35:07')";
//            sb.append(b).append(",");
//        }
//        System.out.println(sb.delete(sb.length()-1,sb.length()));

//        System.out.println(getUUID());
    }
}
