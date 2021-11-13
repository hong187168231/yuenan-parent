package com.indo.game.pojo.dto;

import java.math.BigDecimal;
import java.util.List;

public class KYResultInfoDTO {
    /**
     * 子操作类型
     */
    private Integer s;
    /**
     * 主操作类型
     */
    private String m;
    /**
     * 数据结果
     */
    private DataInfo d;

    /**
     * 成功
     *
     * @return
     */
    public boolean isSuccess() {
        if (null == d || null == d.getCode()) {
            return false;
        }

        if (!d.getCode().equals(0)) {
            return false;
        }

        if (null == d.getStatus() || !d.getStatus().equals(0)) {
            return false;
        }
        return true;
    }

    /**
     * 订单不存在
     *
     * @return
     */
    public boolean isOrderNotExist() {
        if (null == d || null == d.getCode()) {
            return false;
        }

        if (!d.getCode().equals(0)) {
            return false;
        }

        if (null == d.getStatus() || !d.getStatus().equals(-1)) {
            return false;
        }
        return true;
    }

    /**
     * 订单处理失败
     *
     * @return
     */
    public boolean isFailure() {
        if (null == d || null == d.getCode()) {
            return false;
        }

        if (!d.getCode().equals(0)) {
            return false;
        }

        if (null == d.getStatus() || !d.getStatus().equals(2)) {
            return false;
        }
        return true;
    }

    /**
     * 订单处理中
     *
     * @return
     */
    public boolean isOrderHanding() {
        if (null == d || null == d.getCode()) {
            return false;
        }

        if (!d.getCode().equals(0)) {
            return false;
        }

        if (null == d.getStatus() || !d.getStatus().equals(3)) {
            return false;
        }
        return true;
    }

    public Integer getS() {
        return s;
    }

    public void setS(Integer s) {
        this.s = s;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public DataInfo getD() {
        return d;
    }

    public void setD(DataInfo d) {
        this.d = d;
    }

    public class DataInfo {
        /**
         * 游戏地址
         */
        private String url;
        /**
         * 错误码（-1:不存在、0:成功、2:失败、3:处理中）
         */
        private Integer code;
        /**
         * 余额
         */
        private BigDecimal money;
        /**
         * 总余额
         */
        private BigDecimal totalMoney;
        /**
         * 可下分余额
         */
        private BigDecimal freeMoney;
        /**
         * 玩家账号
         */
        private String account;

        /**
         * 状态码（-1、不存在，0、不在线,1、在线，2、封停）
         */
        private Integer status;
        /**
         * 0、不在游戏中，1 在游戏中
         */
        private Integer gameStatus;
        /**
         * 返回列表行数
         */
        private Integer count;
        /**
         * 数据拉取开始时间
         */
        private Long start;
        /**
         * 数据拉取结束时间
         */
        private Long end;
        /**
         * 数据列表
         */
        private List<KYNoteList> list;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public BigDecimal getMoney() {
            return money;
        }

        public void setMoney(BigDecimal money) {
            this.money = money;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public BigDecimal getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(BigDecimal totalMoney) {
            this.totalMoney = totalMoney;
        }

        public BigDecimal getFreeMoney() {
            return freeMoney;
        }

        public void setFreeMoney(BigDecimal freeMoney) {
            this.freeMoney = freeMoney;
        }

        public Integer getGameStatus() {
            return gameStatus;
        }

        public void setGameStatus(Integer gameStatus) {
            this.gameStatus = gameStatus;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Long getStart() {
            return start;
        }

        public void setStart(Long start) {
            this.start = start;
        }

        public Long getEnd() {
            return end;
        }

        public void setEnd(Long end) {
            this.end = end;
        }

        public List<KYNoteList> getList() {
            return list;
        }

        public void setList(List<KYNoteList> list) {
            this.list = list;
        }
    }
}
