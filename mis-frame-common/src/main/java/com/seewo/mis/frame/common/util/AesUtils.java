package com.seewo.mis.frame.common.util;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

import static com.seewo.mis.frame.common.constant.BaseConstant.DEFAULT_ENCODING;

/**
 * AES加解密工具类
 *
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-11-30
 * @version 1.0
 */
@Slf4j
public class AesUtils {

    private AesUtils() {
        throw new InstantiationError("can not instantiate utils class");
    }

    private static final String ENCODE_RULES = "SEEWO-MIS";
    private static final String AES          = "AES";
    private static final String SHA1PRNG     = "SHA1PRNG";

    /**
     * AES加密
     * 1.构造密钥生成器
     * 2.根据 encode Rules规则初始化密钥生成器
     * 3.产生密钥
     * 4.创建和初始化密码器
     * 5.内容加密
     * 6.返回字符串
     *
     * @param content 明文,不可为空
     * @return 密文, 异常情况下返回空值
     */
    @Nullable
    public static String aesEncode(@NotNull String content) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            SecureRandom random = SecureRandom.getInstance(SHA1PRNG);
            random.setSeed(ENCODE_RULES.getBytes());
            keyGenerator.init(128, random);
            SecretKey originalKey = keyGenerator.generateKey();
            byte[] raw = originalKey.getEncoded();
            SecretKey key = new SecretKeySpec(raw, AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] byteEncode = content.getBytes(DEFAULT_ENCODING);
            byte[] byteAES = cipher.doFinal(byteEncode);
            return new BASE64Encoder().encode(byteAES);
        } catch (Exception e) {
            log.error("AES 加密异常:输入明文:{} ,异常信息:{}", content, e);
        }
        return null;
    }

    /**
     * * 解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     *
     * @param content 密文,不可为空
     * @return 明文, 异常情况下返回空
     */
    @Nullable
    public static String aesDecode(@NotNull String content) {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance(AES);
            SecureRandom random = SecureRandom.getInstance(SHA1PRNG);
            random.setSeed(ENCODE_RULES.getBytes());
            keygen.init(128, random);
            SecretKey originalKey = keygen.generateKey();
            byte[] raw = originalKey.getEncoded();
            SecretKey key = new SecretKeySpec(raw, AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] byteContent = new BASE64Decoder().decodeBuffer(content);
            byte[] byteDecode = cipher.doFinal(byteContent);
            return new String(byteDecode, DEFAULT_ENCODING);
        } catch (Exception e) {
            log.error("AES 解密异常:输入密文:{} ,异常信息:{}", content, e);
        }
        return null;
    }

}
