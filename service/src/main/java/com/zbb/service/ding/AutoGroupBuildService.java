package com.zbb.service.ding;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbb.bean.Result;
import com.zbb.bo.CodeManagementBo;
import com.zbb.common.util.ConvertUtils;
import com.zbb.common.util.qrcode.QrCode;
import com.zbb.common.util.string.StringUtil;
import com.zbb.dao.mapper.BhGroupMapper;
import com.zbb.dao.mapper.CodeManagementMapper;
import com.zbb.dto.GroupInfoDto;
import com.zbb.entity.BhGroup;
import com.zbb.entity.BhUserActivity;
import com.zbb.entity.CodeManagement;
import com.zbb.vo.CodeManagementVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author sunflower
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AutoGroupBuildService {

    @Resource
    private BhUserActivityService bhuserActivityService;

    /**
     * 二维码群管理（规则配置）
     */
    @Resource
    private CodeManagementMapper codeManagementMapper;

    /**
     * 群信息
     */
    @Resource
    private BhGroupMapper bhGroupMapper;

    /**
     * 群人数负载因子
     */
    static final float DEFAULT_LOAD_FACTOR = 0.90f;


    public String checkGroupName(String groupName) {
        char[] charArray = groupName.toCharArray();
        StringBuilder num = new StringBuilder();
        for (int i = charArray.length - 1; i >= 0; i--) {
            char c = charArray[i];
            String s = String.valueOf(c);
            try {
                Integer integer = Integer.valueOf(s);
                System.out.println(integer);
                num.append(s);
            } catch (Exception e) {
                e.printStackTrace();
                return num.reverse().toString();
            }
        }
        return null;
    }

    /**
     * 自动建群规则配置
     */
    public String edit(CodeManagementBo codeManagementBo) throws Exception {
        CodeManagement codeManagement = ConvertUtils.convertBean(codeManagementBo, CodeManagement.class);
        int groupNoNum = 0;
        if (codeManagementBo.getAutoGroupBuild() == 0){
            String groupNo = checkGroupName(codeManagementBo.getGroupName());

            if (StringUtils.isBlank(groupNo)) {
                groupNoNum = 1;
            } else {
                groupNoNum = Integer.parseInt(groupNo);
            }
            if (CollectionUtils.isEmpty(codeManagementBo.getGroupInfoDto())) {
                return Result.failResult("请选择关联的群聊");
            }
        }
        codeManagement.setCreateBy("");
        codeManagement.setCreateTime(new Date());
        codeManagement.setUpdateBy("");
        codeManagement.setUpdateTime(new Date());
        String url;
        if (codeManagement.getId() == null) {
            Example example = new Example(CodeManagement.class);
            example.createCriteria().andEqualTo("codeName", codeManagementBo.getCodeName());
            if (CollectionUtils.isNotEmpty(codeManagementMapper.selectByExample(example))) {
                return Result.failResult("活码名称已存在！");
            }
            String codeUrl = codeManagement.getCodeUrl();
            codeManagement.setGroupNo(groupNoNum);
            codeManagementMapper.insertSelective(codeManagement);
            url = codeUrl + "/ding/group/scanCodeJump?id=" + codeManagement.getId();
            // 生成二维码
            CodeManagement management = new CodeManagement();
            String img = QrCode.generateImg(url, codeUrl, StringUtil.getUuid());
            management.setCodeUrl(img);
            management.setId(codeManagement.getId());
            codeManagementMapper.updateByPrimaryKeySelective(management);
            codeManagementBo.setCodeUrl(img);
        } else {
            if (codeManagementBo.getCodeState() == null){
                // 判断是不是在修改的接口时候有没有修改名称，如果没有
                CodeManagement management = codeManagementMapper.selectByPrimaryKey(codeManagementBo.getId());
                if (!management.getCodeName().equals(codeManagementBo.getCodeName())){
                    Example example = new Example(CodeManagement.class);
                    example.createCriteria().andEqualTo("codeName", codeManagementBo.getCodeName());
                    if (CollectionUtils.isNotEmpty(codeManagementMapper.selectByExample(example))) {
                        return Result.failResult("活码名称已存在！");
                    }
                }
                codeManagementMapper.updateByPrimaryKeySelective(codeManagement);
            }

        }
        if (codeManagementBo.getCodeState() == null){
            // 批量添加
            batchInsertGroup(codeManagementBo, codeManagement);
        }
        return Result.succResult(codeManagementBo);
    }

    private void batchInsertGroup(CodeManagementBo codeManagementBo, CodeManagement codeManagement) {
        //如果有数据就先删除
        Example example = new Example(BhGroup.class);
        example.createCriteria().andEqualTo("bhGroupId", codeManagement.getId());
        List<BhGroup> bhGroups = bhGroupMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(bhGroups)) {
            bhGroupMapper.deleteByExample(example);
        }
        // 创建批量操作集合
        List<BhGroup> list = Lists.newArrayList();
        // 保存关联的子群
        for (GroupInfoDto infoDto : codeManagementBo.getGroupInfoDto()) {
            BhGroup bhGroup = new BhGroup();
            bhGroup.setGroupId(infoDto.getGroupId());
            bhGroup.setGroupName(infoDto.getGroupName());
            bhGroup.setCreateTime(new Date());
            bhGroup.setCreateBy("");
            bhGroup.setGroupMainName(infoDto.getGroupMainName());
            bhGroup.setGroupMainId(infoDto.getGroupMainId());
            bhGroup.setGroupNum(0);
            bhGroup.setBhGroupId(codeManagement.getId());
            list.add(bhGroup);
        }
        if (CollectionUtils.isNotEmpty(list)){
            bhGroupMapper.insertList(list);
        }
    }

    /**
     * 根据用户ID获取活码配置
     *
     * @param userId userId
     * @return List<CodeManagement>
     */
    public PageInfo<CodeManagementVo> getCodeManagementList(String userId, String codeName,Integer currentPage, Integer showCount) {

        Example example = new Example(CodeManagement.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("createBy", userId);
        if (StringUtils.isNotBlank(codeName)){
            criteria.andLike("codeName","%" + codeName + "%");
        }
        PageHelper.startPage(currentPage, showCount);
        List<CodeManagement> list = codeManagementMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            list = Lists.newArrayList();
        }
        List<CodeManagementVo> cvs = Lists.newArrayList();
        for (CodeManagement codeManagement : list) {
            Example example1 = new Example(BhGroup.class);
            example1.createCriteria().andEqualTo("bhGroupId",codeManagement.getId());
            List<BhGroup> bhGroups = bhGroupMapper.selectByExample(example1);
            CodeManagementVo code = ConvertUtils.convertBean(codeManagement, CodeManagementVo.class);
            code.setBhGroups(bhGroups);
            cvs.add(code);
        }
        PageInfo<CodeManagementVo> userPageInfo = new PageInfo<>(cvs);

        int total = getTotal(userId,codeName);
        int pages = (int) Math.ceil(total * 1.00 / showCount);
        userPageInfo.setTotal(total);
        userPageInfo.setPageSize(showCount);
        userPageInfo.setPageNum(currentPage);
        userPageInfo.setPages(pages);
        return userPageInfo;
    }
    private int getTotal(String userId, String codeName) {
        Example example = new Example(CodeManagement.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("createBy", userId);
        if (StringUtils.isNotBlank(codeName)){
            criteria.andLike("codeName","%" + codeName + "%");
        }
        return codeManagementMapper.selectCountByExample(example);
    }

    /**
     * 批量删除配置（无法删除已启用状态）
     *
     * @param ids id列表
     */
    public void deleteBatch(String[] ids) {
        for (String id : ids) {
            CodeManagement codeManagement = codeManagementMapper.selectByPrimaryKey(id);
            codeManagementMapper.deleteByPrimaryKey(id);
            // 删除图片文件
            try {
                QrCode.deleteImg(codeManagement.getCodeUrl());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 加入群聊(自动建群)
     *
     * @param id 群聊ID
     */
    public String joinGroupAuto(String id, Long userId) {
        CodeManagement codeManagement = codeManagementMapper.selectByPrimaryKey(id);
        if (codeManagement.getCodeState() == 1) {
            return "对不起！此二维码已经禁用。";
        }
        int i1 = 2;
        if (codeManagement.getCodeState() == i1) {
            return "对不起！此二维码已经失效。";
        }
        // 此二维码关联的群聊信息
        Example example = new Example(BhGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("bhGroupId", id);
        List<BhGroup> bhGroups = bhGroupMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(bhGroups)) {
            for (BhGroup bhGroup : bhGroups) {
                // 是否大于阈值   如果当前群聊的人数小于于总人数的百分之九十才能加入群聊
                boolean load = bhGroup.getGroupNum() <= (bhGroup.getGroupMaxNum() * DEFAULT_LOAD_FACTOR);
                // 不需要自动建群
                if (codeManagement.getAutoGroupBuild() == 1 && load) {
                    // todo 加入群聊逻辑（钉钉接口）

                    // 更新时间排序
                    updateByTimeAndBhUserActivity(userId);
                    return "加入群聊成功！";
                }
                // 加入群聊
                if (codeManagement.getAutoGroupBuild() == 0 && load) {
                    // todo 加入群聊逻辑（钉钉接口）

                    // 更新时间排序
                    updateByTimeAndBhUserActivity(userId);
                    return "加入群聊成功！";
                } else {
                    //已满的话就需要自动建群
                    // todo 创建群聊逻辑（钉钉接口）并加入群聊

                    // 更新时间排序
                    updateByTimeAndBhUserActivity(userId);
                    return "创建群聊并加入成功！";
                }
            }
        }
        return "服务器异常";
    }

    /**
     * 修改进行中活动的排序方式
     *
     * @param userId 用户id
     */
    private void updateByTimeAndBhUserActivity(Long userId) {
        List<BhUserActivity> list = bhuserActivityService.getBhUserActivityListByUserId(userId, "活码管理");
        if (CollectionUtils.isNotEmpty(list)) {
            BhUserActivity bhUserActivity = list.get(0);
            bhUserActivity.setLastTime(new Date());
            bhuserActivityService.updateBhUserActivity(bhUserActivity);
        } else {
            Date activityTime = new Date();
            BhUserActivity bhUserActivity = new BhUserActivity();
            bhUserActivity.setActivityName("活码管理");
            bhUserActivity.setActivityUrl("。。。");
            bhUserActivity.setActivityDescribe("活码描述(预留)");
            bhUserActivity.setActivityLogo("活码logo");
            bhUserActivity.setActivityTime(activityTime);
            bhUserActivity.setCreateTime(activityTime);
            bhUserActivity.setUserId(userId);
            bhUserActivity.setActivityType(0);
            bhUserActivity.setLastTime(activityTime);
            bhuserActivityService.insertBhUserActivity(bhUserActivity);
        }
    }
}
