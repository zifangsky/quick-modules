package cn.zifangsky.quickmodules.log.aop;

import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析表达式的公共组件
 *
 * @author zifangsky
 * @date 2017/12/5
 * @since 1.0.0
 */
public class ParseExpressionUtils {
    private static final Pattern PATTERN = Pattern.compile("#\\{.+?\\}");

    /**
     * 将参数设置到{@link StandardEvaluationContext}
     * @author zifangsky
     * @date 2017/12/5 16:58
     * @since 1.0.0
     * @param paramNames 方法的参数名数组
     * @param params 方法的参数值数组
     * @return org.springframework.expression.spel.support.StandardEvaluationContext
     */
    public static StandardEvaluationContext setContextVariables(String[] paramNames, Object[] params){
        StandardEvaluationContext context = new StandardEvaluationContext();

        if(params != null && params.length > 0 && paramNames != null && paramNames.length > 0){
            for(int i=0;i<params.length;i++){
                context.setVariable(paramNames[i], params[i]);
            }
        }

        return context;
    }

    /**
     * 去除符号关键字
     */
    public static String removeSymbol(String value){
        return "#" + value.replace("#","").replace("{","").replace("}","");
    }

    /**
     * 将形如 #{userInfo.userId} 的字符串解析成真正的数据
     * @author zifangsky
     * @date 2017/12/5 17:48
     * @since 1.0.0
     * @param value 待解析字符串
     * @return java.lang.String
     */
    public static String parseValue(String value, StandardEvaluationContext context){
        if(StringUtils.isBlank(value)){
            return value;
        }

        StringBuffer stringBuffer = new StringBuffer();

        Matcher matcher = PATTERN.matcher(value);
        while (matcher.find()){
            //取出真正的SPEL表达式
            String temp = removeSymbol(matcher.group());
            matcher.appendReplacement(stringBuffer, parseSpelValue(temp, context, Object.class).toString());
        }

        //将剩下未匹配到的部分添加到StringBuffer
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    /**
     * 解析指定的SPEL表达式
     * @author zifangsky
     * @date 2017/12/5 16:59
     * @since 1.0.0
     * @param value 待解析的表达式
     * @param clazz 返回类型
     * @return T
     */
    public static  <T> T parseSpelValue(String value, StandardEvaluationContext context, Class<T> clazz){
        ExpressionParser parser = new SpelExpressionParser();

        //返回解析后的内容
        return parser.parseExpression(value).getValue(context, clazz);
    }

}
