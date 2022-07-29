package com.indo.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.core.pojo.dto.SignInRecordDTO;
import com.indo.core.pojo.entity.SignInRecord;

/**
 * <p>
 * 签到记录表 服务类
 * </p>
 *
 * @author xxx
 * @since 2022-07-29
 */
public interface ISignInRecordService extends IService<SignInRecord> {
    /**
     * 查询用户签到记录分页
     * @param signInRecordDTO
     * @param loginInfo
     * @return
     */
     Page<SignInRecord> findMemSignInRecordPage(SignInRecordDTO signInRecordDTO,LoginInfo loginInfo);

    /**
     * 查询用户签到次数
     * @param loginInfo
     * @return
     */
     Integer findUserSignInNum(LoginInfo loginInfo);

    /**
     * 签到
     */
    void signIn(LoginInfo loginInfo);
}
