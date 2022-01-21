package com.indo.game.pojo.dto.ug;

import com.alibaba.fastjson.annotation.JSONField;

public class UgTasksJsonDTO extends UgParentRequJsonDTO {

    private long SortNo;// long 是 排序编号 返回大于该排序编号的注单
    private int Rows;// int 否 要获取的数据条数 最大为 1000，默认 1000

    @JSONField(name="SortNo")
    public long getSortNo() {
        return SortNo;
    }
    @JSONField(name="SortNo")
    public void setSortNo(long sortNo) {
        SortNo = sortNo;
    }
    @JSONField(name="Rows")
    public int getRows() {
        return Rows;
    }
    @JSONField(name="Rows")
    public void setRows(int rows) {
        Rows = rows;
    }
}
