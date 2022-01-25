package com.indo.game.service.app.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.game.mapper.frontend.GameDownloadMapper;
import com.indo.game.pojo.entity.manage.GameDownload;
import com.indo.game.service.app.IGameDownloadService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class GameDownloadServiceImpl extends ServiceImpl<GameDownloadMapper, GameDownload> implements IGameDownloadService {


    public List<GameDownload> queryAllGameDownload() {
        LambdaQueryWrapper<GameDownload> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GameDownload::getIsStart,"1");
        List<GameDownload> categoryList = this.baseMapper.selectList(wrapper);
        return categoryList;
    }

}
