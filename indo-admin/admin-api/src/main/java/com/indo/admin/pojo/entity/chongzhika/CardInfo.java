package com.indo.admin.pojo.entity.chongzhika;


/**
 * 卡信息
 */
public class CardInfo {
    private Long id;
    private Long userId;//登录ID
    private String cardNoPrefix;//前缀
    private String cardNoSerial;//序号
    private String cardNo;//卡号=前缀+序号
    private String cardPwd;
    private Double cardAmount;//卡面金额
    private Double additionalAmount;//增送金额
    private String isActivation;//0否   1激活
    private String isExp;//0否   1导出
    private String isHandle;//0否   1处理
    private String activationAcct;//激活账号
    private String is_delete;//0否   1删除
    private String create_time;
    private String update_time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardPwd() {
        return cardPwd;
    }

    public void setCardPwd(String cardPwd) {
        this.cardPwd = cardPwd;
    }

    public String getIsExp() {
        return isExp;
    }

    public void setIsExp(String isExp) {
        this.isExp = isExp;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getIsActivation() {
        return isActivation;
    }

    public void setIsActivation(String isActivation) {
        this.isActivation = isActivation;
    }

    public String getActivationAcct() {
        return activationAcct;
    }

    public void setActivationAcct(String activationAcct) {
        this.activationAcct = activationAcct;
    }

    public Double getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(Double cardAmount) {
        this.cardAmount = cardAmount;
    }

    public Double getAdditionalAmount() {
        return additionalAmount;
    }

    public void setAdditionalAmount(Double additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    public String getIsHandle() {
        return isHandle;
    }

    public void setIsHandle(String isHandle) {
        this.isHandle = isHandle;
    }

    public String getCardNoPrefix() {
        return cardNoPrefix;
    }

    public void setCardNoPrefix(String cardNoPrefix) {
        this.cardNoPrefix = cardNoPrefix;
    }

    public String getCardNoSerial() {
        return cardNoSerial;
    }

    public void setCardNoSerial(String cardNoSerial) {
        this.cardNoSerial = cardNoSerial;
    }
}
