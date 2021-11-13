package com.indo.game.pojo.dto;



import com.indo.game.pojo.entity.db.DbBetOrder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class DbBetOrderDTO extends DbBetOrder {
    private static final long serialVersionUID = 1L;
    private String mobileno;
    private String nickname;
    private String startTime;
    private String endTime;
    private Integer pageNo = 1;
    private String uniqueId;
    /**
     * 分页大小
     */
    private Integer pageSize = 10;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public DbBetOrderDTO() {
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}

