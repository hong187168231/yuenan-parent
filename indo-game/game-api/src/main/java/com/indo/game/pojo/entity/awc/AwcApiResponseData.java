package com.indo.game.pojo.entity.awc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AwcApiResponseData{
    private String status;
    private String url;
    private String extension;
    private String desc;
    private String txStatus;//交易类型    0:transfer fail 转帐失败    1:transfer success转帐成功
    private String balance;//交易完成后余额
    private String transferAmount;//转帐金额
    private String transferType;//deposit /withdraw 存款 /取款
    private String txCode;//交易识别码
    private List<?> results;//余额结果集
    private String count;
    private String querytime;
    private String amount;// 取款总额
    private String lastModified;//当前余额上次修改时间(ISO 8601格式)
    private String method;//"WITHDRAW" 显示 "取款"
    private String currentBalance;//取款后余额
    private List<?> transactions;//拉取注单结果集
    private String logoutUsers;//所有被强制登出的玩家ID

    private static Map<Integer, String> MSG_EN_MAP;

    private static Map<Integer, String> MSG_CN_MAP;

    static {
        MSG_EN_MAP = new HashMap<>();
        MSG_CN_MAP = new HashMap<>();
        MSG_EN_MAP.put(9998,"System Busy");
        MSG_EN_MAP.put(9999,"Fail");
        MSG_EN_MAP.put(0000,"Success");
        MSG_EN_MAP.put(10,"please input all data!");
        MSG_EN_MAP.put(1000,"Invalid user Id");
        MSG_EN_MAP.put(1001,"Account existed");
        MSG_EN_MAP.put(1002,"Account is not exists");
        MSG_EN_MAP.put(1004,"Invalid Currency");
        MSG_EN_MAP.put(1005,"language is not exists");
        MSG_EN_MAP.put(1006,"PT Setting is empty!");
        MSG_EN_MAP.put(1007,"Invalid PT setting with parent!");
        MSG_EN_MAP.put(1008,"Invalid token!");
        MSG_EN_MAP.put(1009,"Invalid timeZone");
        MSG_EN_MAP.put(1010,"Invalid amount");
        MSG_EN_MAP.put(1011,"Invalid txCode");
        MSG_EN_MAP.put(1012,"Has Pending Transfer");
        MSG_EN_MAP.put(1013,"Account is Lock");
        MSG_EN_MAP.put(1014,"Account is Suspend");
        MSG_EN_MAP.put(1016,"TxCode already operation!");
        MSG_EN_MAP.put(1017,"TxCode is not exist");
        MSG_EN_MAP.put(1018,"Not Enough Balance");
        MSG_EN_MAP.put(1019,"No Data");
        MSG_EN_MAP.put(1024,"Invalid date time format");
        MSG_EN_MAP.put(1025,"Invalid transaction status");
        MSG_EN_MAP.put(1026,"Invalid bet limit setting");
        MSG_EN_MAP.put(1027,"Invalid Certificate");
        MSG_EN_MAP.put(1028,"Unable to proceed. please try again later.");
        MSG_EN_MAP.put(1029,"invalid IP address.It might be IP address did not whitelist yet, or AWC cannot identify your AgentID");
        MSG_EN_MAP.put(1030,"invalid Device to call API.(Ex.IE)");
        MSG_EN_MAP.put(1031,"System is under maintenance.");
        MSG_EN_MAP.put(1032,"Duplicate login.");
        MSG_EN_MAP.put(1033,"Invalid gameCode");
        MSG_EN_MAP.put(1034,"Time does not meet.");
        MSG_EN_MAP.put(1035,"Invalid Agent Id.");
        MSG_EN_MAP.put(1036,"Invalid parameters.");
        MSG_EN_MAP.put(1038,"Duplicate transaction.");
        MSG_EN_MAP.put(1039,"Transaction not found.");
        MSG_EN_MAP.put(1040,"Request timeout.");
        MSG_EN_MAP.put(1041,"HTTP Status error.");
        MSG_EN_MAP.put(1042,"HTTP Response is empty.");
        MSG_EN_MAP.put(1043,"Bet has canceled.");
        MSG_EN_MAP.put(1044,"Invalid bet.");
        MSG_EN_MAP.put(1045,"Add account statement failed.");
        MSG_EN_MAP.put(1046,"Transfer Failed! Please contact customer support immediately. Sorry for any inconvenience caused.");
        MSG_EN_MAP.put(1047,"Game is under maintenance.");
        MSG_EN_MAP.put(1054,"Invalid Platform");


        MSG_CN_MAP.put(9998,"系统繁忙");
        MSG_CN_MAP.put(9999,"失败");
        MSG_CN_MAP.put(0000,"成功");
        MSG_CN_MAP.put(10,"请输入所有数据");
        MSG_CN_MAP.put(1000,"无效的使用者账号");
        MSG_CN_MAP.put(1001,"帐号已存在");
        MSG_CN_MAP.put(1002,"帐号不存在");
        MSG_CN_MAP.put(1004,"无效的货币");
        MSG_CN_MAP.put(1005,"语言不存在");
        MSG_CN_MAP.put(1006,"PT 设定为空");
        MSG_CN_MAP.put(1007,"PT 设定与上线冲突");
        MSG_CN_MAP.put(1008,"无效的 token");
        MSG_CN_MAP.put(1009,"无效时区");
        MSG_CN_MAP.put(1010,"无效的数量");
        MSG_CN_MAP.put(1011,"无效的交易代码");
        MSG_CN_MAP.put(1012,"有待处理的转帐");
        MSG_CN_MAP.put(1013,"帐号已锁");
        MSG_CN_MAP.put(1014,"帐号暂停");
        MSG_CN_MAP.put(1016,"交易代码已被执行过");
        MSG_CN_MAP.put(1017,"交易代码不存在");
        MSG_CN_MAP.put(1018,"余额不足");
        MSG_CN_MAP.put(1019,"没有资料");
        MSG_CN_MAP.put(1024,"无效的日期 (时间) 格式");
        MSG_CN_MAP.put(1025,"无效的交易状态");
        MSG_CN_MAP.put(1026,"无效的投注限制设定");
        MSG_CN_MAP.put(1027,"无效的认证码");
        MSG_CN_MAP.put(1028,"无法执行指定的行为,请稍后再试");
        MSG_CN_MAP.put(1029,"无效的 IP。通常发生于您的 IP 尚未加白,或是 AWC 辨识不到您的 AgentID");
        MSG_CN_MAP.put(1030,"使用无效的装置呼叫 (例如：IE)");
        MSG_CN_MAP.put(1031,"系统维护中");
        MSG_CN_MAP.put(1032,"重复登入");
        MSG_CN_MAP.put(1033,"无效的游戏代码");
        MSG_CN_MAP.put(1034,"您使用的时间参数不符合此 API 规定格式");
        MSG_CN_MAP.put(1035,"无效的 Agent Id");
        MSG_CN_MAP.put(1036,"无效的参数");
        MSG_CN_MAP.put(1038,"重复的交易");
        MSG_CN_MAP.put(1039,"无此交易");
        MSG_CN_MAP.put(1040,"请求逾时");
        MSG_CN_MAP.put(1041,"HTTP 状态错误");
        MSG_CN_MAP.put(1042,"HTTP 请求空白");
        MSG_CN_MAP.put(1043,"下注已被取消");
        MSG_CN_MAP.put(1044,"无效的下注");
        MSG_CN_MAP.put(1045,"帐便记录新增失败");
        MSG_CN_MAP.put(1046,"转帐失败！请立即联系客服,非常抱歉给您造成的任何不便");
        MSG_CN_MAP.put(1047,"游戏维护中");
        MSG_CN_MAP.put(1054,"无效的平台商");
    }

    public String getCodeEnMsg() {
        if (null == this.status) {
            return null;
        }
        return MSG_EN_MAP.get(this.status);
    }
    public String getCodeCnMsg() {
        if (null == this.status) {
            return null;
        }
        return MSG_CN_MAP.get(this.status);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(String txStatus) {
        this.txStatus = txStatus;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTxCode() {
        return txCode;
    }

    public void setTxCode(String txCode) {
        this.txCode = txCode;
    }

    public List<?> getResults() {
        return results;
    }

    public void setResults(List<?> results) {
        this.results = results;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getQuerytime() {
        return querytime;
    }

    public void setQuerytime(String querytime) {
        this.querytime = querytime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public List<?> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<?> transactions) {
        this.transactions = transactions;
    }

    public String getLogoutUsers() {
        return logoutUsers;
    }

    public void setLogoutUsers(String logoutUsers) {
        this.logoutUsers = logoutUsers;
    }
}
