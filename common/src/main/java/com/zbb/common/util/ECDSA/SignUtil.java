package com.zbb.common.util.ECDSA;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.zip.GZIPOutputStream;

/**
 * @author sunflower
 * @className SignUtil
 * @description RSA256用于上链接口签名
 * @date 2020/6/25
 */
public class SignUtil {

    public static final String SHA256_RSA = "SHA256withRSA";

    public static final String SHA1_RSA = "SHA1WithRSA";

    public static final String SHA1_ECDSA = "SHA1withECDSA";

    public static final String SHA256_ECDSA = "SHA256withECDSA";

    public static final String SHA512_ECDSA = "SHA512withECDSA";

    public static final String EC = "EC";

    public static KeyPair KP;

    public static final String TEST_PRIVATE_KEY = "-----BEGIN EC PRIVATE KEY-----\n" +
            "MHcCAQEEICrtjX0TwDdX6I8/tY2pDSRSbfgsT854UYhKxeY74uxDoAoGCCqGSM49\n" +
            "AwEHoUQDQgAE9se8MTnBIoMzXIt0g1iifhEk8WZMB1KQp5NVdsTuBrgQOUlausT7\n" +
            "cu2/9Ouop848XlDBQi5+nl3xGc9rXsSNwg==\n" +
            "-----END ECC PRIVATE KEY-----";

    public static final String TEST_PUBLIC_KEY = "-----BEGIN EC PUBLICK KEY-----\n" +
            "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE9se8MTnBIoMzXIt0g1iifhEk8WZM\n" +
            "B1KQp5NVdsTuBrgQOUlausT7cu2/9Ouop848XlDBQi5+nl3xGc9rXsSNwg==\n" +
            "-----END ECC PUBLICK KEY-----\n";

    /**
     * 初始化密钥对
     *
     * @throws Exception 异常
     */
    public static KeyPair initialize() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(571);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 获取公钥
     *
     * @param keyPair 密钥对
     * @return 公钥字符串
     */
    public static byte[] getPublic(KeyPair keyPair) {
        ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();
        return ecPublicKey.getEncoded();
    }

    /**
     * 获取私钥
     *
     * @param keyPair 密钥对
     * @return 私钥字符串
     */
    public static byte[] getPrivate(KeyPair keyPair) {
        ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();
        return ecPrivateKey.getEncoded();
    }

    /**
     * 私钥签名
     *
     * @param str      待签名字符串
     * @param filePath 私钥文件路径
     * @return 签名String
     */
    public static String privateKeySign(String str, String filePath) throws Exception {

        PEMParser pemParser = new PEMParser(new FileReader(new File(filePath)));
        Object object = pemParser.readObject();
        Security.addProvider(new BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        PEMKeyPair ukp = (PEMKeyPair) object;
        KP = converter.getKeyPair(ukp);
        BCECPrivateKey privateKey = (BCECPrivateKey) KP.getPrivate();
        ECDSASigner ecdsaSigner = new ECDSASigner();

        System.out.println(privateKey.getEncoded().toString());

        ECParameterSpec ecSpec = privateKey.getParameters();
        // G
        ECDomainParameters params = new ECDomainParameters(ecSpec.getCurve(), ecSpec.getG(),
                ecSpec.getN()); // n

        // d
        ECPrivateKeyParameters priKey = new ECPrivateKeyParameters(privateKey.getD(),
                params);
        ecdsaSigner.init(true, priKey);

        // Create digest message (hash) for the actual message.
        SHA256Digest keyDigest = new SHA256Digest();
        keyDigest.update(str.getBytes(), 0, str.getBytes().length);
        byte[] digestMessage = new byte[keyDigest.getDigestSize()];
        keyDigest.doFinal(digestMessage, 0);
        BigInteger[] sig = ecdsaSigner.generateSignature(digestMessage);

        // Convert signature to base64
        String rText = sig[0].toString(10);
        System.out.println("r:" + rText);
        String sText = sig[1].toString(10);
        System.out.println("s:" + sText);

        String signature = rText + "+" + sText;
        // signature =compress(signature,"UTF-8");
      /*  System.out.println(signature);
        signature = Hex.encodeHexString(signature.getBytes());
        System.out.println(signature);*/
        return signature;
    }

    public static String compress(String str, String inEncoding) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes(inEncoding));
        gzip.close();
        return out.toString("ISO-8859-1");

    }

    /**
     * 公钥验证
     *
     * @param str  待验证字符串
     * @param sign 待验证签名
     * @param kp   密钥对
     * @return 验证结果
     * @throws Exception 异常
     */
    public static boolean publicKeyVerify(String str, byte[] sign, KeyPair kp) throws Exception {
        // 验证签名
        BCECPublicKey publicKey = (BCECPublicKey) kp.getPublic();
        System.out.println(publicKey);
        Signature signature = Signature.getInstance(SHA256_ECDSA);
        signature.initVerify(publicKey);
        signature.update(str.getBytes());
        return signature.verify(sign);
    }

    public static void test() throws Exception {
        String str = "ABC";
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(571);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();
        ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();

        // 2.执行签名
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(ecPrivateKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("EC");

        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance(SHA512_ECDSA);
        signature.initSign(privateKey);

        signature.update(str.getBytes());
        byte[] sign = signature.sign();

        // 验证签名
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(ecPublicKey.getEncoded());
        keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        signature = Signature.getInstance(SHA512_ECDSA);
        signature.initVerify(publicKey);
        signature.update(str.getBytes());

        boolean bool = signature.verify(sign);
        System.out.println(bool);
    }

    /**
     * 将byte转为16进制
     *
     * @param bytes
     * @return
     */
    public static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xff);
            if (temp.length() == 1) {
                // 1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }


}
