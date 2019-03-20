package cn.zifangsky.quickmodules.common.plugins;

import cn.zifangsky.quickmodules.common.common.JDBCConstants;
import cn.zifangsky.quickmodules.common.common.PageInfo;
import cn.zifangsky.quickmodules.common.utils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import javax.xml.ws.Holder;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DbUtils基本操作的模板
 *
 * @author zifangsky
 * @date 2017/11/13
 * @since 1.0.0
 */
public class DbUtilsTemplate {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String REGEX_RULE = "[\\u4e00-\\u9fa5\\w-]+";

    private DataSource dataSource;

    private RowProcessor rowProcessor;

    public DbUtilsTemplate(DataSource dataSource, RowProcessor rowProcessor) {
        this.dataSource = dataSource;
        this.rowProcessor = rowProcessor;
    }

    /**
     * 物理删除
     * @author zifangsky
     * @date 2017/11/13 19:43
     * @since 1.0.0
     * @param tableName 表名
     * @param fieldName 字段名
     * @param value 判断条件
     * @return java.lang.Integer
     */
    public Integer delete(String tableName, String fieldName, Object value){
        QueryRunner queryRunner = new QueryRunner(dataSource);

        try {
            String sql = MessageFormat.format(JDBCConstants.BASE_DELETE, tableName, fieldName);
            return queryRunner.update(sql, value);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 逻辑删除（假删除）
     * @author zifangsky
     * @date 2017/11/13 19:43
     * @since 1.0.0
     * @param tableName 表名
     * @param fieldName 字段名
     * @param value 判断条件
     * @return java.lang.Integer
     */
    public Integer deleteLogically(String tableName, String fieldName, Object value){
        QueryRunner queryRunner = new QueryRunner(dataSource);

        try {
            String sql = MessageFormat.format(JDBCConstants.BASE_DELETE_LOGICALLY, tableName, fieldName);
            return queryRunner.update(sql, value);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 根据指定参数条件查询数据条数
     * @author zifangsky
     * @date 2017/11/14 10:58
     * @since 1.0.0
     * @param tableName 表名
     * @param params 参数Map
     * @return java.lang.Long
     */
    public Long count(String tableName, Map<String, Object> params){
        QueryRunner queryRunner = new QueryRunner(dataSource);

        try {
            StringBuilder builder = new StringBuilder(MessageFormat.format(JDBCConstants.BASE_COUNT, tableName));

            List<Object> valuesList = new ArrayList<>(params != null ? params.size() : 1);

            if(params != null && params.size() > 0){
                //拼接查询条件
                for(Map.Entry<String, Object> entry : params.entrySet()){
                    builder.append(" and ");
                    builder.append(entry.getKey());
                    builder.append(" = ?");

                    valuesList.add(entry.getValue());
                }
            }

            return queryRunner.query(builder.toString(), new ScalarHandler<>(), valuesList.toArray());
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 根据指定参数条件查询数据条数（可传递自定义的SQL参数）
     * @author zifangsky
     * @date 2017/11/14 10:58
     * @since 1.0.0
     * @param tableName 表名
     * @param querySql 查询SQL
     * @param valuesList 参数List
     * @return java.lang.Long
     */
    public Long count(String tableName, String querySql, List<Object> valuesList){
        if(valuesList == null){
            valuesList = new ArrayList<>(4);
        }

        QueryRunner queryRunner = new QueryRunner(dataSource);

        try {
            String sql = MessageFormat.format(JDBCConstants.BASE_COUNT, tableName);
            if(StringUtils.isNoneBlank(querySql)){
                sql = sql + " and " + querySql.trim();
            }else{
                valuesList.clear();
            }

            return queryRunner.query(sql, new ScalarHandler<>(), valuesList.toArray());
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 单条件查询
     * @author zifangsky
     * @date 2017/11/13 20:14
     * @since 1.0.0
     * @param tableName 表名
     * @param fieldName 判断字段名
     * @param value 判断条件
     * @param clazz 返回的Bean的类型
     * @return K
     */
    public <K> K select(String tableName, String fieldName, Object value, Class<K> clazz){
        QueryRunner queryRunner = new QueryRunner(dataSource);

        try {
            String sql = MessageFormat.format(JDBCConstants.BASE_SELECT, tableName, fieldName);
            return queryRunner.query(sql, new BeanHandler<>(clazz, rowProcessor), value);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 多条件查询
     * @author zifangsky
     * @date 2017/11/14 11:28
     * @since 1.0.0
     * @param tableName 表名
     * @param params 参数Map
     * @param clazz 返回的Bean的类型
     * @return java.lang.Integer
     */
    public <K> K select(String tableName, Map<String, Object> params, Class<K> clazz){
        QueryRunner queryRunner = new QueryRunner(dataSource);

        try {
            StringBuilder builder = new StringBuilder(MessageFormat.format(JDBCConstants.BASE_PARAMS_SELECT, tableName));

            List<Object> valuesList = new ArrayList<>(params != null ? params.size() : 1);

            if(params != null && params.size() > 0){
                //拼接查询条件
                for(Map.Entry<String, Object> entry : params.entrySet()){
                    builder.append(" and ");
                    builder.append(entry.getKey());
                    builder.append(" = ?");

                    valuesList.add(entry.getValue());
                }
            }

            return queryRunner.query(builder.toString(), new BeanHandler<>(clazz, rowProcessor), valuesList.toArray());
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 单条件查询列表
     * @author zifangsky
     * @date 2017/11/13 20:14
     * @since 1.0.0
     * @param tableName 表名
     * @param fieldName 判断字段名
     * @param value 判断条件
     * @param clazz 返回的Bean的类型
     * @return K
     */
    public <K> List<K> selectForList(String tableName, String fieldName, Object value, Class<K> clazz){
        QueryRunner queryRunner = new QueryRunner(dataSource);

        try {
            String sql = MessageFormat.format(JDBCConstants.BASE_SELECT, tableName, fieldName);
            return queryRunner.query(sql, new BeanListHandler<>(clazz, rowProcessor), value);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 多条件查询列表
     * @author zifangsky
     * @date 2017/11/14 11:28
     * @since 1.0.0
     * @param tableName 表名
     * @param params 参数Map
     * @param clazz 返回的Bean的类型
     * @return java.lang.Integer
     */
    public <K> List<K> selectForList(String tableName, Map<String, Object> params, Class<K> clazz){
        QueryRunner queryRunner = new QueryRunner(dataSource);

        try {
            StringBuilder builder = new StringBuilder(MessageFormat.format(JDBCConstants.BASE_PARAMS_SELECT, tableName));

            List<Object> valuesList = new ArrayList<>(params != null ? params.size() : 1);

            if(params != null && params.size() > 0){
                //拼接查询条件
                for(Map.Entry<String, Object> entry : params.entrySet()){
                    builder.append(" and ");
                    builder.append(entry.getKey());
                    builder.append(" = ?");

                    valuesList.add(entry.getValue());
                }
            }

            return queryRunner.query(builder.toString(), new BeanListHandler<>(clazz, rowProcessor), valuesList.toArray());
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 将Java Bean中的所有参数（包括NULL参数）插入到数据库
     * <p>Note: 这个方法需要Bean中的参数（驼峰法）与数据库的参数（下划线）一一对应</p>
     * @author zifangsky
     * @date 2017/11/14 14:11
     * @since 1.0.0
     * @param tableName 表名
     * @param k 待插入的Java Bean
     * @return java.lang.Integer
     */
    public <K> Integer insert(String tableName, K k){
        QueryRunner queryRunner = new QueryRunner(dataSource);

        try {
            Map<String, Object> params = BeanUtils.objectToMap(k);
            //参数
            List<String> keysList = new ArrayList<>();
            //问号
            List<String> markList = new ArrayList<>(params != null ? params.size() : 1);
            //参数值
            List<Object> valuesList = new ArrayList<>();

            if(params != null && params.size() > 0){
                //拼接插入参数
                for(Map.Entry<String, Object> entry : params.entrySet()){
                    keysList.add(cn.zifangsky.quickmodules.common.utils.StringUtils.humpToUnderline(entry.getKey()));
                    valuesList.add(entry.getValue());
                    markList.add("?");
                }
            }

            //组装SQL
            String sql = MessageFormat.format(JDBCConstants.BASE_INSERT, tableName
                    , StringUtils.join(keysList, ","), StringUtils.join(markList, ","));
            return queryRunner.update(sql, valuesList.toArray());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 将Java Bean中有值的参数插入到数据库
     * <p>Note: 这个方法需要Bean中的参数（驼峰法）与数据库的参数（下划线）一一对应</p>
     * @author zifangsky
     * @date 2017/11/14 14:11
     * @since 1.0.0
     * @param tableName 表名
     * @param k 待插入的Java Bean
     * @return java.lang.Integer
     */
    public <K> Integer insertSelective(String tableName, K k){
        QueryRunner queryRunner = new QueryRunner(dataSource);

        try {
            Map<String, Object> params = BeanUtils.objectToMap(k);
            //参数
            List<String> keysList = new ArrayList<>();
            //问号
            List<String> markList = new ArrayList<>(params != null ? params.size() : 1);
            //参数值
            List<Object> valuesList = new ArrayList<>();

            if(params != null && params.size() > 0){
                //拼接插入参数
                for(Map.Entry<String, Object> entry : params.entrySet()){
                    if(entry.getValue() != null){
                        keysList.add(cn.zifangsky.quickmodules.common.utils.StringUtils.humpToUnderline(entry.getKey()));
                        valuesList.add(entry.getValue());
                        markList.add("?");
                    }
                }
            }

            //组装SQL
            String sql = MessageFormat.format(JDBCConstants.BASE_INSERT, tableName
                    , StringUtils.join(keysList, ","), StringUtils.join(markList, ","));
            return queryRunner.update(sql, valuesList.toArray());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 将Map参数插入到数据库
     * @author zifangsky
     * @date 2017/11/14 15:36
     * @since 1.0.0
     * @param tableName 表名
     * @param params 待插入的所有参数（跟数据库的表的字段一一对应）
     * @return java.lang.Integer
     */
    public Integer insertFromMap(String tableName, Map<String, Object> params){
        QueryRunner queryRunner = new QueryRunner(dataSource);

        try {
            //参数
            List<String> keysList = new ArrayList<>();
            //问号
            List<String> markList = new ArrayList<>(params != null ? params.size() : 1);
            //参数值
            List<Object> valuesList = new ArrayList<>();

            if(params != null && params.size() > 0){
                //拼接插入参数
                for(Map.Entry<String, Object> entry : params.entrySet()){
                    keysList.add(entry.getKey());
                    valuesList.add(entry.getValue());
                    markList.add("?");
                }
            }

            //组装SQL
            String sql = MessageFormat.format(JDBCConstants.BASE_INSERT, tableName
                    , StringUtils.join(keysList, ","), StringUtils.join(markList, ","));
            return queryRunner.update(sql, valuesList.toArray());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 将Java Bean中有值的参数插入到数据库（批量插入）
     * @author zifangsky
     * @date 2017/12/13 15:54
     * @since 1.0.0
     * @param tableName 表名
     * @param list 待插入的Java Bean的集合
     * @return int[]
     */
    public <K> int[] insertBatchSelective(String tableName, List<K> list){
        QueryRunner queryRunner = new QueryRunner(dataSource);

        try {
            if(list != null && list.size() > 0){
                //所有参数值
                Object[][] params = new Object[list.size()][];
                //参数
                List<String> keysList = new ArrayList<>();
                //问号
                List<String> markList = new ArrayList<>();

                for(int i = 0; i < list.size(); i++){
                    K k = list.get(i);
                    Map<String, Object> elementParams = BeanUtils.objectToMap(k);
                    //参数值
                    List<Object> valuesList = new ArrayList<>();

                    if(elementParams != null && elementParams.size() > 0){
                        //拼接插入参数
                        for(Map.Entry<String, Object> entry : elementParams.entrySet()){
                            if(entry.getValue() != null){
                                //只有第一次才设置参数
                                if(i == 0){
                                    keysList.add(cn.zifangsky.quickmodules.common.utils.StringUtils.humpToUnderline(entry.getKey()));
                                    markList.add("?");
                                }
                                valuesList.add(entry.getValue());
                            }
                        }

                        if(valuesList.size() > 0){
                            params[i] = valuesList.toArray();
                        }
                    }
                }

                //组装SQL
                String sql = MessageFormat.format(JDBCConstants.BASE_INSERT, tableName
                        , StringUtils.join(keysList, ","), StringUtils.join(markList, ","));
                return queryRunner.batch(sql, params);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new int[]{0};
    }

    /**
     * 更新数据（包括NULL参数）
     * <p>Note: 这个方法需要Bean中的参数（驼峰法）与数据库的参数（下划线）一一对应</p>
     * @author zifangsky
     * @date 2017/11/14 15:36
     * @since 1.0.0
     * @param tableName 表名
     * @param k 待更新的Java Bean
     * @param fieldName 判断字段名
     * @param value 判断条件
     * @return java.lang.Integer
     */
    public <K> Integer update(String tableName, K k, String fieldName, Object value){
        QueryRunner queryRunner = new QueryRunner(dataSource);

        try {
            Map<String, Object> params = BeanUtils.objectToMap(k);
            //参数
            List<String> keysList = new ArrayList<>();
            //参数值
            List<Object> valuesList = new ArrayList<>();

            if(params != null && params.size() > 0){
                //拼接更新参数
                for(Map.Entry<String, Object> entry : params.entrySet()){
                    keysList.add(cn.zifangsky.quickmodules.common.utils.StringUtils.humpToUnderline(entry.getKey()) + " = ?");
                    valuesList.add(entry.getValue());
                }
            }

            //组装SQL
            String sql = MessageFormat.format(JDBCConstants.BASE_UPDATE, tableName, StringUtils.join(keysList, ","))
                    + " and " + fieldName + " = ?";
            valuesList.add(value);

            return queryRunner.update(sql, valuesList.toArray());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 更新数据（有值的参数才更新）
     * <p>Note: 这个方法需要Bean中的参数（驼峰法）与数据库的参数（下划线）一一对应</p>
     * @author zifangsky
     * @date 2017/11/14 15:36
     * @since 1.0.0
     * @param tableName 表名
     * @param k 待更新的Java Bean
     * @param fieldName 判断字段名
     * @param value 判断条件
     * @return java.lang.Integer
     */
    public <K> Integer updateSelective(String tableName, K k, String fieldName, Object value){
        QueryRunner queryRunner = new QueryRunner(dataSource);

        try {
            Map<String, Object> params = BeanUtils.objectToMap(k);
            //参数
            List<String> keysList = new ArrayList<>();
            //参数值
            List<Object> valuesList = new ArrayList<>();

            if(params != null && params.size() > 0){
                //拼接更新参数
                for(Map.Entry<String, Object> entry : params.entrySet()){
                    if(entry.getValue() != null){
                        keysList.add(cn.zifangsky.quickmodules.common.utils.StringUtils.humpToUnderline(entry.getKey()) + " = ?");
                        valuesList.add(entry.getValue());
                    }
                }
            }

            //组装SQL
            String sql = MessageFormat.format(JDBCConstants.BASE_UPDATE, tableName, StringUtils.join(keysList, ","))
                    + " and " + fieldName + " = ?";
            valuesList.add(value);

            return queryRunner.update(sql, valuesList.toArray());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 使用Map参数更新数据库
     * @author zifangsky
     * @date 2017/11/14 16:48
     * @since 1.0.0
     * @param tableName 表名
     * @param params 待更新的所有参数（跟数据库的表的字段一一对应）
     * @param fieldName 判断字段名
     * @param value 判断条件
     * @return java.lang.Integer
     */
    public Integer updateFromMap(String tableName, Map<String, Object> params, String fieldName, Object value){
        QueryRunner queryRunner = new QueryRunner(dataSource);

        try {
            //参数
            List<String> keysList = new ArrayList<>();
            //参数值
            List<Object> valuesList = new ArrayList<>();

            if(params != null && params.size() > 0){
                //拼接插入参数
                for(Map.Entry<String, Object> entry : params.entrySet()){
                    keysList.add(entry.getKey() + " = ?");
                    valuesList.add(entry.getValue());
                }
            }

            //组装SQL
            String sql = MessageFormat.format(JDBCConstants.BASE_UPDATE, tableName, StringUtils.join(keysList, ","))
                    + " and " + fieldName + " = ?";
            valuesList.add(value);

            return queryRunner.update(sql, valuesList.toArray());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 分页查询
     * <p>Note: 这个方法需要传递确定的参数值</p>
     * @author zifangsky
     * @date 2017/11/14 17:57
     * @since 1.0.0
     * @param tableName 表名
     * @param params 参数Map
     * @param pageInfoHolder 分页参数
     * @param clazz 返回的Bean的类型
     * @return java.lang.Integer
     */
    public <K> List<K> findAll(String tableName, Map<String, Object> params, Holder<PageInfo> pageInfoHolder, Class<K> clazz){
        QueryRunner queryRunner = new QueryRunner(dataSource);

        try {
            StringBuilder builder = new StringBuilder(MessageFormat.format(JDBCConstants.BASE_PARAMS_SELECT, tableName));
            //参数值
            List<Object> valuesList = new ArrayList<>(params != null ? params.size() : 1);

            if(params != null && params.size() > 0){
                //拼接查询条件
                for(Map.Entry<String, Object> entry : params.entrySet()){
                    builder.append(" and ");
                    builder.append(entry.getKey());
                    builder.append(" = ?");

                    valuesList.add(entry.getValue());
                }
            }

            if(pageInfoHolder != null){
                PageInfo pageInfo = pageInfoHolder.value;

                //查询总条数
                Long count = this.count(tableName, params);
                if(count == null){
                    return null;
                }

                pageInfo.setCount(count);
                //计算页数
                Long pageCount = count / pageInfo.getPageSize() + ((count % pageInfo.getPageSize()) == 0 ? 0 : 1);
                pageInfo.setPageCount(pageCount);

                if(pageInfo.getCurrentPage() != null){
                    if(pageInfo.getCurrentPage() > pageCount){
                        pageInfo.setCurrentPage(pageCount.intValue());
                    }else if(pageInfo.getCurrentPage() < 1){
                        pageInfo.setCurrentPage(1);
                    }
                }

                //排序
                String sortName = this.filterCharacter(cn.zifangsky.quickmodules.common.utils.StringUtils.humpToUnderline(pageInfo.getSortName()));
                if(sortName != null){
                    builder.append(" ORDER BY ");
                    builder.append(sortName);
                }

                //ASC（正序）、DESC（倒序）
                if(StringUtils.isNoneBlank(pageInfo.getSortOrder())){
                    builder.append(" ");
                    builder.append(pageInfo.getSortOrder());
                }

                //LIMIT
                if(pageInfo.getCurrentPage() != null && pageInfo.getPageSize() != null){
                    Integer start = (pageInfo.getCurrentPage() - 1) * pageInfo.getPageSize();
                    builder.append(" LIMIT ?,?");
                    valuesList.add(start);
                    valuesList.add(pageInfo.getPageSize());
                }
            }

            return queryRunner.query(builder.toString(), new BeanListHandler<>(clazz, rowProcessor), valuesList.toArray());
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 分页查询
     * <p>Note: 这个方法通过传递自定义SQL查询条件查询，可以做模糊查询</p>
     * @author zifangsky
     * @date 2017/11/14 17:57
     * @since 1.0.0
     * @param tableName 表名
     * @param querySql 查询SQL
     * @param valuesList 参数List
     * @param pageInfoHolder 分页参数
     * @param clazz 返回的Bean的类型
     * @return java.lang.Integer
     */
    public <K> List<K> findAll(String tableName, String querySql, List<Object> valuesList, Holder<PageInfo> pageInfoHolder, Class<K> clazz){
        if(valuesList == null){
            valuesList = new ArrayList<>(4);
        }

        QueryRunner queryRunner = new QueryRunner(dataSource);

        try {
            StringBuilder builder = new StringBuilder(MessageFormat.format(JDBCConstants.BASE_PARAMS_SELECT, tableName));

            if(StringUtils.isNoneBlank(querySql)){
                builder.append(" and ");
                builder.append(querySql.trim());
            }else{
                valuesList.clear();
            }

            if(pageInfoHolder != null){
                PageInfo pageInfo = pageInfoHolder.value;

                //查询总条数
                Long count = this.count(tableName, querySql, valuesList);
                if(count == null){
                    return null;
                }

                pageInfo.setCount(count);
                //计算页数
                Long pageCount = count / pageInfo.getPageSize() + ((count % pageInfo.getPageSize()) == 0 ? 0 : 1);
                pageInfo.setPageCount(pageCount);

                if(pageInfo.getCurrentPage() != null){
                    if(pageInfo.getCurrentPage() > pageCount){
                        pageInfo.setCurrentPage(pageCount.intValue());
                    }else if(pageInfo.getCurrentPage() < 1){
                        pageInfo.setCurrentPage(1);
                    }
                }

                //排序
                String sortName = this.filterCharacter(cn.zifangsky.quickmodules.common.utils.StringUtils.humpToUnderline(pageInfo.getSortName()));
                if(sortName != null){
                    builder.append(" ORDER BY ");
                    builder.append(sortName);
                }

                //ASC（正序）、DESC（倒序）
                if(StringUtils.isNoneBlank(pageInfo.getSortOrder())){
                    builder.append(" ");
                    builder.append(pageInfo.getSortOrder());
                }

                //LIMIT
                if(pageInfo.getCurrentPage() != null && pageInfo.getPageSize() != null){
                    Integer start = (pageInfo.getCurrentPage() - 1) * pageInfo.getPageSize();
                    builder.append(" LIMIT ?,?");
                    valuesList.add(start);
                    valuesList.add(pageInfo.getPageSize());
                }
            }

            return queryRunner.query(builder.toString(), new BeanListHandler<>(clazz, rowProcessor), valuesList.toArray());
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 过滤排序变量，防止危险字符注入
     * @author zifangsky
     * @date 2017/11/15 18:36
     * @since 1.0.0
     * @param sortName 排序变量
     * @return java.lang.String
     */
    private String filterCharacter(String sortName){
        if(StringUtils.isNoneBlank(sortName)){
            Pattern pattern = Pattern.compile(REGEX_RULE);
            Matcher matcher = pattern.matcher(sortName);
            if(matcher.find()){
                return matcher.group();
            }
        }

        return null;
    }
}