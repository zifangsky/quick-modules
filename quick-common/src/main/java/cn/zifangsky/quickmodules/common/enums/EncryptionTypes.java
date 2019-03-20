package cn.zifangsky.quickmodules.common.enums;

/**
 * 加密方式
 *
 * @author zifangsky
 * @date 2017/11/2
 * @since 1.0.0
 */
public enum EncryptionTypes {
    Base64("Base64"),
    Md5Hex("Md5Hex"),
    Sha1Hex("Sha1Hex"),
    Sha256Hex("Sha256Hex"),
    Sha512Hex("Sha512Hex"),
    Md5Crypt("Md5Crypt"),
    Sha256Crypt("Sha256Crypt"),
    Sha512Crypt("Sha512Crypt")
    ;

    private String code;

    EncryptionTypes(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
