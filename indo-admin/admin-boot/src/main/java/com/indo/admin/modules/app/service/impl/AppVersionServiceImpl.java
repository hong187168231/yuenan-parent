package com.indo.admin.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.common.constant.SystemConstants;
import com.indo.admin.common.util.AdminBusinessRedisUtils;
import com.indo.admin.modules.act.mapper.ActivityMapper;
import com.indo.admin.modules.act.service.IActivityService;
import com.indo.admin.modules.app.mapper.AppVersionMapper;
import com.indo.admin.modules.app.service.IAppVersionService;
import com.indo.admin.pojo.dto.ActivityDTO;
import com.indo.admin.pojo.dto.ActivityQueryDTO;
import com.indo.admin.pojo.req.app.AppVersionReq;
import com.indo.common.constant.RedisConstants;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.common.web.util.JwtUtils;
import com.indo.core.pojo.entity.Activity;
import com.indo.core.pojo.entity.AppVersion;
import com.indo.user.pojo.vo.act.ActivityVo;
import com.indo.user.pojo.vo.app.AppVersionVo;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 活动记录表 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
@Service
public class AppVersionServiceImpl extends ServiceImpl<AppVersionMapper, AppVersion> implements IAppVersionService {

    @Override
    public List<AppVersionVo> queryList() {
        LambdaQueryWrapper<AppVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(AppVersion::getUpdateTime);
        List<AppVersion> list = this.baseMapper.selectList(wrapper);
        List<AppVersionVo> result = DozerUtil.convert(list, AppVersionVo.class);
        return result;
    }

    @Override
    @Transactional
    public boolean add(AppVersionReq req) {
        AppVersion appVersion = new AppVersion();
        DozerUtil.map(req, appVersion);
        if (baseMapper.insert(appVersion) > 0) {
            AdminBusinessRedisUtils.hset(RedisConstants.APP_VERSION_KEY, appVersion.getVersionId() + "", appVersion);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean edit(AppVersionReq req, HttpServletRequest request) {
        AppVersion appVersion = findAppVersionById(req.getVersionId());
        if (null == appVersion) {
            String countryCode = request.getHeader("countryCode");
            throw new BizException(MessageUtils.get(ResultCode.DATA_NONENTITY.getCode(),countryCode));
        }
        DozerUtil.map(req, appVersion);
        if (baseMapper.updateById(appVersion) > 0) {
            AdminBusinessRedisUtils.hset(RedisConstants.APP_VERSION_KEY, appVersion.getVersionId() + "", appVersion);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean del(Integer versionId) {
        if (this.baseMapper.deleteById(versionId) > 0) {
            AdminBusinessRedisUtils.hdel(RedisConstants.APP_VERSION_KEY, versionId + "");
            return true;
        }
        return false;
    }


    private AppVersion findAppVersionById(Integer versionId) {
        return this.baseMapper.selectById(versionId);
    }


}
