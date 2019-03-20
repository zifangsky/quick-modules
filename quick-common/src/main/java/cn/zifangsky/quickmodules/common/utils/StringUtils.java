package cn.zifangsky.quickmodules.common.utils;

import java.util.Random;

/**
 * 常用的字符串相关方法
 *
 * @author zifangsky
 * @date 2017/11/12
 * @since 1.0.0
 */
public class StringUtils {
    private static final String VERIFY_CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String CHARS_1 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String CHARS_2 = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * 获取长度为num的随机数（0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz）
     * @param num 生成的字符串长度
     * */
    public static String getRandomStr1(final int num) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= num; i++) {
            stringBuilder.append(CHARS_1.charAt(new Random().nextInt(CHARS_1.length())));
        }
        return stringBuilder.toString();
    }

    /**
     * 获取长度为num的随机数（./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz）
     * @param num 生成的字符串长度
     * */
    public static String getRandomStr2(final int num) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= num; i++) {
            stringBuilder.append(CHARS_2.charAt(new Random().nextInt(CHARS_2.length())));
        }
        return stringBuilder.toString();
    }

    /**
     * 生成指定长度的验证码
     * @param num 验证码长度
     */
    public static String generateVerifyCode(final int num){
        return generateVerifyCode(num, VERIFY_CODES);
    }
    /**
     * 生成指定长度的验证码
     * @param num 验证码长度
     * @param sources 验证码字符源
     */
    public static String generateVerifyCode(final int num, String sources){
        if(sources == null || sources.length() == 0){
            sources = VERIFY_CODES;
        }
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <=num; i++) {
            stringBuilder.append(sources.charAt(new Random().nextInt(sources.length())));
        }
        return stringBuilder.toString();
    }

    /**
     * 将下划线变量转驼峰法命名
     * @author zifangsky
     * @date 2017/11/14 15:47
     * @since 1.0.0
     * @param str 原变量
     * @return java.lang.String
     */
    public static String underlineToHump(final String str){
        StringBuilder result=new StringBuilder();
        String[] array=str.split("_");
        for(String temp : array){
            if(result.length()==0){
                result.append(temp.toLowerCase());
            }else{
                result.append(temp.substring(0, 1).toUpperCase());
                result.append(temp.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 将驼峰法变量转下划线命名
     * @author zifangsky
     * @date 2017/11/14 15:47
     * @since 1.0.0
     * @param str 原变量
     * @return java.lang.String
     */
    public static String humpToUnderline(final String str){
        StringBuilder builder=new StringBuilder(str);
        int point=0;
        for(int i=0;i<str.length();i++){
            if(Character.isUpperCase(str.charAt(i))){
                builder.insert(i+point, "_");
                point+=1;
            }
        }
        return builder.toString().toLowerCase();
    }
    public static Boolean isEmpty(String str){
        return str==null||"".equals(str);
    }
    public static Boolean isNotEmpty(String str){
        return str!=null&&!"".equals(str);
    }

}
