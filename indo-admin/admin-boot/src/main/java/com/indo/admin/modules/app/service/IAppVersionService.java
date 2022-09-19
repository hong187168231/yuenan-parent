package com.indo.admin.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.ActivityDTO;
import com.indo.admin.pojo.dto.ActivityQueryDTO;
import com.indo.admin.pojo.req.app.AppVersionReq;
import com.indo.common.result.Result;
import com.indo.core.pojo.entity.Activity;
import com.indo.core.pojo.entity.AppVersion;
import com.indo.user.pojo.vo.act.ActivityVo;
import com.indo.user.pojo.vo.app.AppVersionVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 活动记录表 服务类
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
public interface IAppVersionService extends IService<AppVersion> {


    /**
     * 分页查询
     *
     * @return
     */
   List<AppVersionVo> queryList();

    /**
     * 新增appversion
     *
     * @param req
     * @return
     */
    boolean add(AppVersionReq req);


    /**
     * 编辑appversion
     *
     * @param req
     * @return
     */
    boolean edit(AppVersionReq req, HttpServletRequest request);


    /**
     * 删除appversion
     *
     * @param actId
     * @return
     */
    boolean del(Integer actId);



}
