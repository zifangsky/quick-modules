package cn.zifangsky.quickmodules.user.mapper;

import cn.zifangsky.quickmodules.common.common.PageInfo;
import cn.zifangsky.quickmodules.user.model.SysFunction;

import javax.xml.ws.Holder;
import java.util.List;

public interface FunctionMapper {
    int deleteLogically(Integer id);

    int delete(Long id);

    int insert(SysFunction permission);

    int insertSelective(SysFunction permission);

    SysFunction selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysFunction permission);

    int updateByPrimaryKey(SysFunction permission);

    /**
     * 通过资源名称和parentId查询
     * @author zifangsky
     * @date 2017/11/19 16:36
     * @since 1.0.0
     * @param name 资源名称
     * @param parentId parentId
     * @return cn.zifangsky.quickmodules.user.model.SysFunction
     */
    SysFunction selectByName(String name, String parentId);

    /**
     * 通过parentId查询父级节点
     * @author zifangsky
     * @date 2017/11/19 16:36
     * @since 1.0.0
     * @param parentId parentId
     * @return cn.zifangsky.quickmodules.user.model.SysFunction
     */
    SysFunction selectParentByParentId(String parentId);

    /**
     * 通过parentId查询同级节点
     * @author zifangsky
     * @date 2017/11/19 16:56
     * @since 1.0.0
     * @param parentId parentId
     * @return cn.zifangsky.quickmodules.user.model.SysFunction
     */
    List<SysFunction> selectSiblingByParentId(String parentId);

    /**
     * 分页查询
     * @author zifangsky
     * @date 2017/11/19 13:38
     * @since 1.0.0
     * @param queryParam 查询参数
     * @param pageInfoHolder 分页参数的包装类
     * @return java.util.List<cn.zifangsky.quickmodules.user.model.SysFunction>
     */
    List<SysFunction> findAll(String queryParam, Holder<PageInfo> pageInfoHolder);

    /**
     * 查询所有
     * @author zifangsky
     * @date 2017/11/19 16:37
     * @since 1.0.0
     * @return java.util.List<cn.zifangsky.quickmodules.user.model.SysFunction>
     */
    List<SysFunction> findAll();

}