package com.indo.admin.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class MsgPushRecordVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 推送终端: 0 全部 1 ios  2 android
     */
    private Integer deviceType;

    /**
     * 是否删除 0 未删除 1 删除
     */
    private Boolean isDel;

    private String createUser;

    /**
     * 备注
     */
    private String remark;
}
