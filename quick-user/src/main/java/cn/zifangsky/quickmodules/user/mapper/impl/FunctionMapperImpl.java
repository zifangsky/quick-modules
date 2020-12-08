package cn.zifangsky.quickmodules.user.mapper.impl;

import cn.zifangsky.quickmodules.common.common.Holder;
import cn.zifangsky.quickmodules.common.common.JDBCConstants;
import cn.zifangsky.quickmodules.common.common.PageInfo;
import cn.zifangsky.quickmodules.user.common.Constants;
import cn.zifangsky.quickmodules.user.mapper.FunctionMapper;
import cn.zifangsky.quickmodules.user.model.SysFunction;
import cn.zifangsky.quickmodules.user.plugins.DbUtilsTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限表相关的数据库操作
 * @author zifangsky
 * @date 2017/11/5
 * @since 1.0.0
 */
@Repository
public class FunctionMapperImpl implements FunctionMapper {

    @Resource(name = "userDbUtilsTemplate")
    private DbUtilsTemplate template;

    @Override
    public int deleteLogically(Integer id) {
        return template.deleteLogically(Constants.TABLE_FUNC, "id", id);
    }

    @Override
    public int delete(Long id) {
        return template.delete(Constants.TABLE_FUNC, "id", id);
    }

    @Override
    public int insert(SysFunction permission) {
        return template.insert(Constants.TABLE_FUNC, permission);
    }

    @Override
    public int insertSelective(SysFunction permission) {
        return template.insertSelective(Constants.TABLE_FUNC, permission);
    }

    @Override
    public SysFunction selectByPrimaryKey(Integer id) {
        return template.select(Constants.TABLE_FUNC, "id", id, SysFunction.class);
    }

    @Override
    public int updateByPrimaryKeySelective(SysFunction permission) {
        return template.updateSelective(Constants.TABLE_FUNC, permission, "id", permission.getId());
    }

    @Override
    public int updateByPrimaryKey(SysFunction permission) {
        return template.update(Constants.TABLE_FUNC, permission, "id", permission.getId());
    }

    @Override
    public SysFunction selectByName(String name, String parentId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("parent_id", parentId);
        params.put("name", name);

        return template.select(Constants.TABLE_FUNC, params, SysFunction.class);
    }

    @Override
    public SysFunction selectParentByParentId(String parentId) {
        return template.select(Constants.TABLE_FUNC, "myself_id", parentId, SysFunction.class);
    }

    @Override
    public List<SysFunction> selectSiblingByParentId(String parentId) {
        return template.selectForList(Constants.TABLE_FUNC, "parent_id", parentId, SysFunction.class);
    }

    @Override
    public List<SysFunction> findAll(String queryParam, Holder<PageInfo> pageInfoHolder) {
        List<Object> valuesList = new ArrayList<>();
        if(queryParam != null){
            queryParam = queryParam.replace("%", "");
        }else{
            queryParam = "";
        }
        valuesList.add("%" + queryParam + "%");

        return template.findAll(Constants.TABLE_FUNC, JDBCConstants.FUNC_FIND_ALL, valuesList, pageInfoHolder, SysFunction.class);
    }

    @Override
    public List<SysFunction> findAll() {
        return template.selectForList(Constants.TABLE_FUNC, null, SysFunction.class);
    }
}
