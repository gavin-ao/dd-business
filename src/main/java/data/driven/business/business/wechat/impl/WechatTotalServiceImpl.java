package data.driven.business.business.wechat.impl;

import com.alibaba.fastjson.JSONObject;
import data.driven.business.business.wechat.WechatTotalService;
import data.driven.business.common.Constant;
import data.driven.business.dao.JDBCBaseDao;
import data.driven.business.util.DateFormatUtil;
import data.driven.business.util.JSONUtil;
import data.driven.business.vo.wechat.WechatTotalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 微信统计service
 * @author hejinkai
 * @date 2018/7/3
 */
@Service
public class WechatTotalServiceImpl implements WechatTotalService {

    /** 72小时的时间戳 **/
    private static final long three_day_time_long = 72 * 60 * 60 * 1000;


    /**
     * 72小时之外按照天统计，72小时之内按照小时统计
     * @param start
     * @param end
     * @return
     */
    private String getMysqlDateFormat(Date start, Date end){
        long tempTime = end.getTime() - start.getTime();
        if(tempTime > three_day_time_long){
            return Constant.mysql_day_format;
        }else{
            return Constant.mysql_hour_format;
        }
    }

    /**
     * 根据grouptime分组统计，统计之后返回有序的数据
     * @param list
     */
    private void coventWechatTotalList(List<WechatTotalVO> list) {
        //按照grouptime分组统计
        Map<String, Long> map = list.stream().collect(Collectors.groupingBy(WechatTotalVO::getGroupTime, Collectors.summingLong(WechatTotalVO::getCountNum)));
        TreeMap<String, Long> treeMap = new TreeMap<String, Long>(map);
        List<String> keyList = new ArrayList<String>(treeMap.keySet());
        //为前台展示根据grouptime做排序
        Collections.sort(keyList);
        list.clear();
        for(String key : keyList){
            list.add(new WechatTotalVO(treeMap.get(key), key));
        }
    }

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public JSONObject totalActivityNum(String appInfoId, String startDate, String endDate) {
        JSONObject result = new JSONObject();
        if(appInfoId == null){
            return JSONUtil.putMsg(false, "101", "参数appInfoId为空");
        }
        Date start = DateFormatUtil.getTime(startDate);
        Date end = DateFormatUtil.toEndDate(endDate);
        if(start == null || end == null){
            return JSONUtil.putMsg(false, "102", "时间获取失败，请检查时间格式");
        }
        String sql = "select count(log_id) from wechat_login_log where app_info_id = ? and login_at between ? and ?";
        Integer countNum = jdbcBaseDao.getCount(sql, appInfoId, start, end);
        result.put("success", true);
        result.put("countNum", countNum);
        return result;
    }

    @Override
    public JSONObject totalActivityNumView(String appInfoId, String startDate, String endDate) {
        JSONObject result = new JSONObject();
        if(appInfoId == null){
            return JSONUtil.putMsg(false, "101", "参数appInfoId为空");
        }
        Date start = DateFormatUtil.getTime(startDate);
        Date end = DateFormatUtil.toEndDate(endDate);
        if(start == null || end == null){
            return JSONUtil.putMsg(false, "102", "时间获取失败，请检查时间格式");
        }
        String format = getMysqlDateFormat(start, end);
        String sql = "select count(log_id) as count_num,DATE_FORMAT(login_at,'" + format + "') as group_time from wechat_login_log where app_info_id = ? and login_at between ? and ? group by DATE_FORMAT(login_at,'" + format + "')";
        List<WechatTotalVO> list = jdbcBaseDao.queryList(WechatTotalVO.class, sql, appInfoId, start, end);
        result.put("data", list);
        result.put("success", true);
        return result;
    }

    @Override
    public JSONObject totalSpreadRangeNum(String appInfoId, String startDate, String endDate) {
        JSONObject result = new JSONObject();
        if(appInfoId == null){
            return JSONUtil.putMsg(false, "101", "参数appInfoId为空");
        }
        Date start = DateFormatUtil.getTime(startDate);
        Date end = DateFormatUtil.toEndDate(endDate);
        if(start == null || end == null){
            return JSONUtil.putMsg(false, "102", "时间获取失败，请检查时间格式");
        }
        String sql = "select ifnull(count(1),0) from (select wechat_user_id from wechat_login_log where app_info_id = ? and login_at between ? and ? group by wechat_user_id) as t";
        Integer countNum = jdbcBaseDao.getCount(sql, appInfoId, start, end);
        result.put("success", true);
        result.put("countNum", countNum);
        return result;
    }

    @Override
    public JSONObject totalSpreadRangeNumView(String appInfoId, String startDate, String endDate) {
        JSONObject result = new JSONObject();
        if(appInfoId == null){
            return JSONUtil.putMsg(false, "101", "参数appInfoId为空");
        }
        Date start = DateFormatUtil.getTime(startDate);
        Date end = DateFormatUtil.toEndDate(endDate);
        if(start == null || end == null){
            return JSONUtil.putMsg(false, "102", "时间获取失败，请检查时间格式");
        }

        String format = getMysqlDateFormat(start, end);
        String sql = "select 1 as count_num,DATE_FORMAT(login_at,'" + format + "') as group_time from wechat_login_log where app_info_id = ? and login_at between ? and ? group by wechat_user_id,group_time";
        List<WechatTotalVO> list = jdbcBaseDao.queryList(WechatTotalVO.class, sql, appInfoId, start, end);

        coventWechatTotalList(list);

        result.put("data", list);
        result.put("success", true);
        return result;
    }

    @Override
    public JSONObject totalFissionEffectNewPeopleNum(String appInfoId, String startDate, String endDate) {
        JSONObject result = new JSONObject();
        if(appInfoId == null){
            return JSONUtil.putMsg(false, "101", "参数appInfoId为空");
        }
        Date start = DateFormatUtil.getTime(startDate);
        Date end = DateFormatUtil.toEndDate(endDate);
        if(start == null || end == null){
            return JSONUtil.putMsg(false, "102", "时间获取失败，请检查时间格式");
        }
        //统计分享数据
        String shareSql = "select count(wsd.to_id) from wechat_share_detail wsd left join wechat_app_user_mapping waum on waum.wechat_map_id = wsd.to_id where wsd.app_info_id = ? and wsd.share_at between ? and ? and waum.create_at between ? and ?";
        Integer countShareNum = jdbcBaseDao.getCount(shareSql, appInfoId, start, end, start, end);
        if(countShareNum == null){
            countShareNum = 0;
        }
        //统计助力数据
        String helpSql = "select count(whd.to_id) from wechat_help_detail whd left join wechat_app_user_mapping waum on waum.wechat_map_id = whd.to_id where whd.app_info_id = ? and whd.help_at between ? and ? and waum.create_at between ? and ?";
        Integer countHelpNum = jdbcBaseDao.getCount(helpSql, appInfoId, start, end, start, end);
        if(countHelpNum == null){
            countHelpNum = 0;
        }
        result.put("success", true);
        result.put("countNum", countShareNum + countHelpNum);
        return result;
    }

    @Override
    public JSONObject totalFissionEffectNewPeopleNumView(String appInfoId, String startDate, String endDate) {
        JSONObject result = new JSONObject();
        if(appInfoId == null){
            return JSONUtil.putMsg(false, "101", "参数appInfoId为空");
        }
        Date start = DateFormatUtil.getTime(startDate);
        Date end = DateFormatUtil.toEndDate(endDate);
        if(start == null || end == null){
            return JSONUtil.putMsg(false, "102", "时间获取失败，请检查时间格式");
        }

        String format = getMysqlDateFormat(start, end);
        String sql = "select count(wsd.to_id) as count_num,DATE_FORMAT(wsd.share_at,'" + format + "') as group_time from wechat_share_detail wsd left join wechat_app_user_mapping waum on waum.wechat_map_id = wsd.to_id where wsd.app_info_id = ? and wsd.share_at between ? and ? and waum.create_at between ? and ? group by group_time" +
                "   union all " +
                " select count(whd.to_id) as count_num,DATE_FORMAT(whd.help_at,'" + format + "') as group_time from wechat_help_detail whd left join wechat_app_user_mapping waum on waum.wechat_map_id = whd.to_id where whd.app_info_id = ? and whd.help_at between ? and ? and waum.create_at between ? and ? group by group_time";
        List<WechatTotalVO> list = jdbcBaseDao.queryList(WechatTotalVO.class, sql, appInfoId, start, end, start, end, appInfoId, start, end, start, end);
        result.put("data", list);
        result.put("success", true);
        return result;
    }

    @Override
    public JSONObject totalShareNum(String appInfoId, String startDate, String endDate) {
        JSONObject result = new JSONObject();
        if(appInfoId == null){
            return JSONUtil.putMsg(false, "101", "参数appInfoId为空");
        }
        Date start = DateFormatUtil.getTime(startDate);
        Date end = DateFormatUtil.toEndDate(endDate);
        if(start == null || end == null){
            return JSONUtil.putMsg(false, "102", "时间获取失败，请检查时间格式");
        }
        String sql = "select count(share_id) from wechat_share_info where app_info_id = ? and create_at between ? and ?";
        Integer countNum = jdbcBaseDao.getCount(sql, appInfoId, start, end);
        if(countNum == null){
            countNum = 0;
        }

        String helpSql = "select count(help_id) from wechat_help_info where app_info_id = ? and create_at between ? and ?";
        Integer countHelpNum = jdbcBaseDao.getCount(helpSql, appInfoId, start, end);
        if(countHelpNum == null){
            countHelpNum = 0;
        }

        result.put("success", true);
        result.put("countNum", countNum + countHelpNum);
        return result;
    }

    @Override
    public JSONObject totalShareNumView(String appInfoId, String startDate, String endDate) {
        JSONObject result = new JSONObject();
        if(appInfoId == null){
            return JSONUtil.putMsg(false, "101", "参数appInfoId为空");
        }
        Date start = DateFormatUtil.getTime(startDate);
        Date end = DateFormatUtil.toEndDate(endDate);
        if(start == null || end == null){
            return JSONUtil.putMsg(false, "102", "时间获取失败，请检查时间格式");
        }

        String format = getMysqlDateFormat(start, end);
        String sql = "select count(share_id) as count_num,DATE_FORMAT(create_at,'" + format + "') as group_time from wechat_share_info where app_info_id = ? and create_at between ? and ? group by DATE_FORMAT(create_at,'" + format + "')" +
                " union all " +
                " select count(help_id) as count_num,DATE_FORMAT(create_at,'" + format + "') as group_time from wechat_help_info where app_info_id = ? and create_at between ? and ? group by DATE_FORMAT(create_at,'" + format + "')";
        List<WechatTotalVO> list = jdbcBaseDao.queryList(WechatTotalVO.class, sql, appInfoId, start, end, appInfoId, start, end);
        result.put("data", list);
        result.put("success", true);
        return result;
    }

    @Override
    public JSONObject totalSharePeopleNum(String appInfoId, String startDate, String endDate) {
        JSONObject result = new JSONObject();
        if(appInfoId == null){
            return JSONUtil.putMsg(false, "101", "参数appInfoId为空");
        }
        Date start = DateFormatUtil.getTime(startDate);
        Date end = DateFormatUtil.toEndDate(endDate);
        if(start == null || end == null){
            return JSONUtil.putMsg(false, "102", "时间获取失败，请检查时间格式");
        }
        //利用union的特性，相同的只取一个，保证分享和助力数据统计人数时不重复
        String sql = "select wechat_user_id from wechat_share_info where app_info_id = ? and create_at between ? and ? group by wechat_user_id" +
                " union " +
                " select wechat_user_id from wechat_help_info where app_info_id = ? and create_at between ? and ? group by wechat_user_id ";
        List<String> userIdList = jdbcBaseDao.getColumns(String.class, sql, appInfoId, start, end, appInfoId, start, end);
        Integer countNum = 0;
        if(userIdList != null){
            countNum = userIdList.size();
        }
        result.put("success", true);
        result.put("countNum", countNum);
        return result;
    }

    @Override
    public JSONObject totalSharePeopleNumView(String appInfoId, String startDate, String endDate) {
        JSONObject result = new JSONObject();
        if(appInfoId == null){
            return JSONUtil.putMsg(false, "101", "参数appInfoId为空");
        }
        Date start = DateFormatUtil.getTime(startDate);
        Date end = DateFormatUtil.toEndDate(endDate);
        if(start == null || end == null){
            return JSONUtil.putMsg(false, "102", "时间获取失败，请检查时间格式");
        }
        //利用union的特性，相同的只取一个，保证分享和助力数据统计人数时不重复
        String format = getMysqlDateFormat(start, end);
        String sql = "select 1 as count_num,DATE_FORMAT(create_at,'" + format + "') as group_time,wechat_user_id from wechat_share_info where app_info_id = ? and create_at between ? and ? group by wechat_user_id,group_time" +
                " union " +
                " select 1 as count_num,DATE_FORMAT(create_at,'" + format + "') as group_time,wechat_user_id from wechat_help_info where app_info_id = ? and create_at between ? and ? group by wechat_user_id,group_time";
        List<WechatTotalVO> list = jdbcBaseDao.queryList(WechatTotalVO.class, sql, appInfoId, start, end, appInfoId, start, end);
        coventWechatTotalList(list);
        result.put("data", list);
        result.put("success", true);
        return result;
    }

    @Override
    public JSONObject totalOldAndNewUser(String appInfoId, String startDate, String endDate) {
        JSONObject result = new JSONObject();
        if(appInfoId == null){
            return JSONUtil.putMsg(false, "101", "参数appInfoId为空");
        }
        Date start = DateFormatUtil.getTime(startDate);
        Date end = DateFormatUtil.toEndDate(endDate);
        if(start == null || end == null){
            return JSONUtil.putMsg(false, "102", "时间获取失败，请检查时间格式");
        }
        //查询开始时间之前的用户，即为老用户
        String oldSql = "select count(wechat_map_id) from wechat_app_user_mapping where app_info_id = ? and create_at < ?";
        Integer oldUserNum = jdbcBaseDao.getCount(oldSql, appInfoId, start);
        //查询开始时间之后，结束之前的用户，即为新用户
        String newUserSql = "select count(wechat_map_id) from wechat_app_user_mapping where app_info_id = ? and create_at between ? and ?";
        Integer newUserNum = jdbcBaseDao.getCount(newUserSql, appInfoId, start, end);

        result.put("success", true);
        result.put("oldUserNum", oldUserNum);
        result.put("newUserNum", newUserNum);
        return result;
    }

    @Override
    public JSONObject totalSpreadTrajectory(String appInfoId, String startDate, String endDate) {
        return null;
    }
}
