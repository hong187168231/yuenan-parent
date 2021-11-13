package com.indo.game.services;

//import com.caipiao.live.common.constant.Constants;
//import com.caipiao.live.common.enums.SysParameterEnum;
//import com.caipiao.live.common.exception.BusinessException;
//import com.caipiao.live.common.model.LoginUser;
//import com.caipiao.live.common.model.common.PageBounds;
//import com.caipiao.live.common.model.common.PageResult;
//import com.caipiao.live.common.model.vo.sys.SystemInfoSetVO;
//import com.caipiao.live.common.mybatis.entity.SysParameter;
//import com.caipiao.live.common.util.CollectionUtil;
//import com.caipiao.live.common.util.NumUtils;
//import com.caipiao.live.common.util.StringUtils;
import com.indo.common.enums.SysParameterEnum;
import com.indo.admin.pojo.entity.SysParameter;

public interface SysParamService {

    /**
     * 获取系统配置
     *
     * @param sysParameterEnum 系统配置码枚举
     * @return
     * @throws
     */
    SysParameter getByCode(SysParameterEnum sysParameterEnum) ;

    /**
     * 获取系统配置
     *
     * @param sysparamcode 系统配置码
     * @return
     * @throws
     */
    SysParameter getByCode(String sysparamcode);

//    /**
//     * 获取系统配置值
//     *
//     * @param sysparamcode 系统配置码
//     * @param enableStatus 是否只查询启用状态
//     * @return
//     * @throws BusinessException
//     */
//    default String getValueByCode(String sysparamcode, boolean enableStatus) throws BusinessException {
//        SysParameter sysParameter = getByCode(sysparamcode);
//        if (null == sysParameter) {
//            return "";
//        }
//        String value = sysParameter.getSysparamvalue();
//        value = null == value ? "" : value;
//        if (enableStatus) {
//            return null != sysParameter.getStatus() && sysParameter.getStatus().equals(0) ? value : "";
//        } else {
//            return value;
//        }
//    }
//
//    /**
//     * 分页获取系统配置
//     *
//     * @param req
//     * @param page
//     * @return
//     */
//    default PageResult getList(SysParameter req, PageBounds page) {
//        return new PageResult();
//    }
//
//    /**
//     * 根据id获取系统配置
//     *
//     * @param sysparamid
//     * @return
//     */
//    default SysParameter getDetails(Long sysparamid) {
//        return null;
//    }
//
//    /**
//     * 新增系统配置
//     *
//     * @param req
//     * @param loginUser
//     * @return
//     */
//    default Long save(SysParameter req, LoginUser loginUser) {
//        return 0L;
//    }
//
//    /**
//     * 修改配置
//     *
//     * @param sysParameter
//     * @return
//     */
//    default Integer edit(SysParameter sysParameter) {
//        return edit(sysParameter, new LoginUser());
//    }
//
//    /**
//     * 修改配置
//     *
//     * @param req
//     * @param loginUser
//     * @return
//     */
//    Integer edit(SysParameter req, LoginUser loginUser);
//
//    /**
//     * 修改系统配置值
//     *
//     * @param code
//     * @param value
//     * @param updateUser
//     * @return
//     */
//    Integer edit(String code, String value, String updateUser);
//
//    /**
//     * 删除配置
//     *
//     * @param sysparaid
//     * @param loginUser
//     * @return
//     */
//    default String doDel(Long sysparaid, LoginUser loginUser) {
//        return "";
//    }
//
//    /**
//     * 获取统一配置码的列表信息
//     *
//     * @param name 配置码
//     * @return
//     */
//    default List<String> getSameCodeParamList(String name) {
//        return getSameCodeParamList(name, "asc");
//    }
//
//    /**
//     * 获取同一配置码的列表信息
//     *
//     * @param code 配置码
//     * @param sort 排序方式：ASC，DESC 不区分大小写
//     * @return
//     */
//    default List<String> getSameCodeParamList(String code, String sort) {
//        return new ArrayList<>();
//    }
//
//    /**
//     * 获取同一配置码的值，以逗号分隔
//     *
//     * @param code 配置码
//     * @return
//     */
//    default String getSameCodeParamsValueString(String code) {
//        return getSameCodeParamsValueString(code, "asc");
//    }
//
//    /**
//     * 获取同一配置码的值，以逗号分隔
//     *
//     * @param code 配置码
//     * @param sort 排序方式：ASC，DESC 不区分大小写
//     * @return
//     */
//    default String getSameCodeParamsValueString(String code, String sort) {
//        List<String> valueList = getSameCodeParamList(code, sort);
//        return CollectionUtil.toAppendString(valueList);
//    }
//
//    List<SysParameter> queryByCodeNames(@Param("codeNames") List<String> codeNames);
//
//    /**
//     * 获取系统设置的信息
//     *
//     * @return
//     */
//    SystemInfoSetVO listSystemInfoSet();
//
//    /**
//     * 获取产品配置和活动设置的信息
//     *
//     * @return
//     */
//    Map<String, Object> listProductSet();
//
//    //List<SysParameter> reloadAll();
//
//    /**
//     * 查询注册型用户是否有评论等权限
//     *
//     * @return
//     */
//    String queryRecommendAuth();
//
////    /**
////     * 根据key获取配置信息
////     *
////     * @param key key值
////     * @return
////     */
////    SysParameter queryByKey(SysParameterEnum key);
//
//    /**
//     * 获取产品配置和活动设置的信息
//     *
//     * @param names 系统设置key
//     * @return
//     */
//    Map<String, SysParameter> getProductsByName(List<String> names);
//
//    List<SysParameter> queryList();
//
//    List<SysParameter> queryList(SysParameter systemInfo);
//
//    SysParameter queryByName(String name);
//
//    List<String> queryAllKey();
//
//    boolean deleteAllCaches();
//
//    /**
//     * 查询主播端平台对应配置信息
//     *
//     * @param platformCode
//     * @return
//     */
//    List<SysParameter> queryBasAnchorPlatformConfigs(String platformCode);
//
//    /**
//     * 查询主播端平台对应信息，对queryBasAnchorPlatformConfigs方法的再次处理
//     *
//     * @param platformCode
//     * @return
//     */
//    Map<String, Object> queryBasAnchorPlatformInfos(String platformCode);
//
//
//    Map<String, String> getkefuwechat();
//
//    void initSysParams();
//
//
//    /**
//     * 获取产品配置和活动设置的信息
//     *
//     * @param codes 系统设置key
//     * @return
//     */
//    Map<String, String> getParamValueByCode(List<String> codes);
//
//    /**
//     * 获取无效手机号配置
//     *
//     * @return
//     */
//    default String getInvalidPhonePrefixs() {
//        SysParameter sysParameter = getByCode(SysParameterEnum.INVALID_PHONE_PREFIX);
//        return validateAndGet(sysParameter);
//    }
//
//    /**
//     * 验证系统参数并获取值
//     *
//     * @return
//     */
//    default String validateAndGet(SysParameter sysParameter) {
//        if (null == sysParameter || Constants.STATUS_9.equals(sysParameter.getStatus())) {
//            return "";
//        }
//        return null == sysParameter.getSysparamvalue() ? "" : sysParameter.getSysparamvalue().trim();
//    }
//
//    default String validateAndGet(SysParameterEnum sysParameterEnum) {
//        SysParameter parameter = getByCode(sysParameterEnum);
//        return validateAndGet(parameter);
//    }
//
//    /**
//     * 判断指定号码是否为无效手机号
//     * 示例：165,170,171,167,162
//     *
//     * @param phone
//     * @return
//     */
//    default boolean isInvalidPhoneNumber(String phone) {
//        String invalidPhonePrefixs = getInvalidPhonePrefixs();
//        if (StringUtils.isEmpty(phone)) {
//            return true;
//        }
//
//        if ("".equals(invalidPhonePrefixs)) {
//            return false;
//        }
//
//        phone = phone.replace(" ", "");
//        if (phone.length() < 7) { //考虑国际号码
//            return true;
//        }
//
//        //处理大陆号码
//        if ("+86".equals(phone.substring(0, 3))) {
//            phone = phone.substring(3);
//        }
//
//        boolean isInvalidPhone = invalidPhonePrefixs.contains(phone.substring(0, 3));
//        System.out.println("isInvalidPhoneNumber phone:" + phone + ", isInvalidPhone:" + isInvalidPhone + ", invalidPhonePrefixs:" + invalidPhonePrefixs);
//
//        return isInvalidPhone;
//    }
//
//    /**
//     * 获取直播间最大彩种个数
//     *
//     * @return
//     */
//    default int getMaxLiveLotteryNums() {
//        SysParameter sysParameter = getByCode(SysParameterEnum.LIVE_LOTTERY_MAX_NUMS);
//        String value = validateAndGet(sysParameter);
//        if ("".equals(value)) {
//            return SysParameterEnum.LIVE_LOTTERY_MAX_NUMS.getIntegerValue();
//        }
//        Integer maxNums = NumUtils.parseInteger(value);
//        return null == maxNums ? SysParameterEnum.LIVE_LOTTERY_MAX_NUMS.getIntegerValue() : maxNums;
//    }
//
//    /**
//     * 直播间机器人没有配置时默认最小数量
//     *
//     * @return
//     */
//    default int getLeastLiveRobotNums() {
//        SysParameter sysParameter = getByCode(SysParameterEnum.LIVE_ROBOT_LEAST_NUMS);
//        String value = validateAndGet(sysParameter);
//        if ("".equals(value)) {
//            return Constants.LIVE_ROBOT_LEAST_NUMS;
//        }
//        Integer leastNums = NumUtils.parseInteger(value);
//        return null == leastNums ? Constants.LIVE_ROBOT_LEAST_NUMS : leastNums;
//    }
//
//    /**
//     * 直播间中奖机器人个数不达标时默认最小数量
//     *
//     * @return
//     */
//    default int getLeastLiveRobotWinningNums() {
//        SysParameter sysParameter = getByCode(SysParameterEnum.LIVE_ROBOT_WINNING_LEAST_NUMS);
//        String value = validateAndGet(sysParameter);
//        if ("".equals(value)) {
//            return Constants.LIVE_ROBOT_WINNING_LEAST_NUMS;
//        }
//        Integer leastNums = NumUtils.parseInteger(value);
//        return null == leastNums ? Constants.LIVE_ROBOT_WINNING_LEAST_NUMS : leastNums;
//    }


}