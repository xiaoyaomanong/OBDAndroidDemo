package com.example.obdandroid.config;

/**
 * 作者：Jealous
 * 日期：2021/1/5 0005
 * 描述：接口
 */
public class APIConfig {
    /**
     * APP服务器地址
     */
    //public static final String SERVER_URL = "http://192.168.0.113:1021";//本地测试
    //public static final String SERVER_URL = "http://y342r9.natappfree.cc";//本地测试
    public static final String SERVER_URL = "http://8.136.125.33:1021";//生产
    public static final String UserAgreement_URL = "http://8.136.125.33/userAgreement.html";//隐私政策
    public static final String ServiceAgreement_URL = "http://8.136.125.33/serviceAgreement.html";//用户协议
    /**
     * 注册接口
     */
    public static final String REGISTER_URL = "/api/register";
    /**
     * 用户登录
     */
    public static final String LOGIN_URL = "/api/login";
    /**
     * 发送短信验证码
     */
    public static final String sendSMSVerificationCode_URL = "/api/sendSMSVerificationCode";
    /**
     * 验证验证码
     */
    public static final String verifySMSVerificationCode_URL = "/api/verifySMSVerificationCode";
    /**
     * 退出登录
     */
    public static final String LOGOUT_URL = "/api/logout";
    /**
     * 获取用户信息
     */
    public static final String USER_INFO_URL = "/api/getUserInformation";
    /**
     * 修改密码
     */
    public static final String UPDATE_PASSWORD_URL = "/api/updatePwd";
    /**
     * 充值套餐
     */
    public static final String CHARGE_URL = "/api/getRechargeSetMealSettingsPageList";
    /**
     * 获取用户车辆列表
     */
    public static final String Vehicle_URL = "/api/getVehiclePageList";
    /**
     * 查询故障码
     */
    public static final String FAULT_CODE_URL = "/api/getFaultCodeDetails";
    /**
     * 添加车辆
     */
    public static final String ADD_VEHICLE_URL = "/api/addVehicle";
    /**
     * 获取车辆品牌
     */
    public static final String getAutomobileBrandList_URL = "/api/getAutomobileBrandList";
    /**
     * 获取品牌型号
     */
    public static final String getCarModelList_URL = "/api/getCarModelList";
    /**
     * 获取用户车辆详情
     */
    public static final String getVehicleInfoById_URL = "/api/getVehicleInfoById";
    /**
     * 删除车辆
     */
    public static final String deleteVehicle_URL = "/api/deleteVehicle";
    /**
     * 绑定蓝牙
     */
    public static final String bindingVehicle_URL = "/api/bindingVehicle";
    /**
     * 修改车辆信息
     */
    public static final String modifyVehicle_URL = "/api/modifyVehicle";
    /**
     * 业务字典值
     */
    public static final String getVocationalDictDataListByType_URL = "/api/getVocationalDictDataListByType";
    /**
     * 获取用户检测记录列表
     */
    public static final String getTestRecordPageList_URL = "/api/getTestRecordPageList";
    /**
     * 获取用户检测记录详情
     */
    public static final String getTestRecordById_URL = "/api/getTestRecordById";
    /**
     * 添加检测信息
     */
    public static final String addTestRecord_URL = "/api/addTestRecord";
    /**
     * 获取常用车辆
     */
    public static final String getCommonBrandList_URL = "/api/getCommonBrandList";
    /**
     * 添加购买套餐记录
     */
    public static final String updateRechargeRecord_URL = "/api/updateRechargeRecord";
    /**
     * 获取用户购买套餐记录列表
     */
    public static final String getRechargeRecordPageList_URL = "/api/getRechargeRecordPageList";
    /**
     * 添加意见反馈
     */
    public static final String addFeedback_URL = "/api/addFeedback";
    /**
     * 添加消息
     */
    public static final String addRemind_URL = "/api/addRemind";
    /**
     * 获取用户消息列表
     */
    public static final String getRemindPageList_URL = "/api/getRemindPageList";
    /**
     * 用户读取消息
     */
    public static final String addReadRemind_URL = "/api/addReadRemind";
    /**
     * 添加购买套餐校验
     */
    public static final String addRechargeRecordCheck_URL = "/api/addRechargeRecordCheck";
    /**
     * 减少使用次数和累计使用次数
     */
    public static final String reduceAndCumulativeFrequency_URL = "/api/reduceAndCumulativeFrequency";
    /**
     * APP用户购买套餐下单接口
     */
    public static final String placeAnOrder_URL = "/api/placeAnOrder";
    /**
     * 检测故障码
     */
    public static final String getInquireAboutFaultCodeDetails_URL = "/api/getInquireAboutFaultCodeDetails";
    /**
     * 获取当前账号充值状态
     */
    public static final String getTheUserCurrentRecharge_URL = "/api/getTheUserCurrentRecharge";
}