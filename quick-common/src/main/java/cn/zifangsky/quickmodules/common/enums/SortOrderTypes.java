package cn.zifangsky.quickmodules.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * SQL查询的排序方式
 *
 * @author zifangsky
 * @date 2017/11/15
 * @since 1.0.0
 */
public enum SortOrderTypes {
    //正序
    ASC("ASC"),
    //倒序
    DESC("DESC");

    private String code;

    SortOrderTypes(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static SortOrderTypes fromCode(String code){
        if(StringUtils.isNoneBlank(code)){
            for(SortOrderTypes type : values()){
                if(type.getCode().equals(code.toUpperCase())){
                    return type;
                }
            }
        }

        return null;
    }
}
