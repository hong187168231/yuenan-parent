package com.indo.admin.pojo.req.chongzhika;

import com.indo.admin.pojo.entity.chongzhika.MemberInfo;

public class MemberInfoReq extends MemberInfo {

    private String startingTime;//开始时间
    private String endTime;//结束时间

    public String getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
