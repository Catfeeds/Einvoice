package com.aisino;

import com.google.common.io.Closer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static com.google.common.base.Strings.isNullOrEmpty;

public final class PKCS7 {
//    private final static Logger LOGGER = LoggerFactory.getLogger(PKCS7.class);

    //设置信任链
    public static native boolean setTrusts(String trusts);

    //设置解密证书
    public static native boolean setDecryptPfx(byte[] decPfx, String passwd);

    //设置签名证书
    public static native boolean setSignedPfx(byte[] sigPfx, String passwd);

    //验证证书，成功返回1
    public static native int validateCert(String base64Cert);

    //打包数字信封，传递加密证书(即接收者的证书)
    public synchronized static native byte[] signedAndEnveloped(String encBase64Cert, byte[] inData);

    //解包数字信封
    public synchronized static native PKCS7 unpack(byte[] inData);

    //获取错误码
    public static native int getLastError();

    /**
     * sigCert、serial、subject、data以下参数 请不要有任何幻想修改，包括你看不惯的命名！！！！！！！！！！！！！！
     */
    private String sigCert; //签名证书
    private String serial; //证书序列号
    private String subject; //证书主题
    private byte[] data; //原文

/*    static {
        System.load(CaConstant.getProperty("DLLADDRESS1"));
       // System.load("E:\\CA\\SOFJni_x64.dll");
    }*/


    public PKCS7() {
    }

    /**
     * @param trustsBytes     证书信任链
     * @param privatePFXBytes 加密/签名私钥
     * @param privatePFXKey   私钥密码
     * @throws Exception
     */
    public PKCS7(byte[] trustsBytes, byte[] privatePFXBytes, String privatePFXKey,String pathurl) throws Exception {
    	System.load(pathurl);
        if (!setTrusts(new String(trustsBytes))) {
            throw new Exception("" + getLastError());
        }

        if (!setDecryptPfx(privatePFXBytes, privatePFXKey)) {
            throw new Exception("setDecryptPfx " + getLastError());
        }

        if (!setSignedPfx(privatePFXBytes, privatePFXKey)) {
            throw new Exception("" + getLastError());
        }
    }

    /**
     * 依据文件绝对路径, 读取文件
     *
     * @param fileUri 文件绝对路径
     * @return byte[] 读取成功的文件字节流
     */
    private byte[] readFile(String fileUri) {
        final Closer closer = Closer.create();
        try {
            final BufferedInputStream bufferedInputStream = closer.register(new BufferedInputStream(new FileInputStream(fileUri)));
            final byte[] bufferedBytes = new byte[bufferedInputStream.available()];

            bufferedInputStream.read(bufferedBytes, 0, bufferedBytes.length);

            return bufferedBytes;
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
            try {
                closer.close();
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }

        return new byte[0];
    }

    /**
     * 签名加密
     *
     * @param plainContent     预加密的原文
     * @param publicPFXBytes 公钥加/解密证书的绝对路径
     * @return 加密后的密文数据
     */
    public byte[] pkcs7Encrypt(String plainContent, byte[] publicPFXBytes) {
        try {
            final byte[] certBytes = publicPFXBytes;

            if (certBytes == null) {
                throw new Exception("传入参数公钥为NULL,不可用");
            }

            final String encCert = new String(certBytes);
            if (1 != validateCert(encCert)) {//证书无效
                throw new Exception("" + getLastError());
            }

            if (isNullOrEmpty(plainContent)) {
                throw new Exception("传入参数原文为NULL,不可用");
            }

            return signedAndEnveloped(encCert, plainContent.getBytes("utf8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    /**
     * 解密验签
     *
     * @param decodeBase64EncryptTxtBytes 经过Base64解压后的密文字节流
     * @return byte[] 经过解密的明文字节流
     */
    public byte[] pkcs7Decrypt(byte[] decodeBase64EncryptTxtBytes) {
        byte[] decryptBytes = new byte[0];
        try {
            //解密
            if (decodeBase64EncryptTxtBytes == null) {
                throw new Exception("传入参数密文为NULL,不可用");
            }

            final PKCS7 pkcs7 = unpack(decodeBase64EncryptTxtBytes);

            if (pkcs7 == null) {
                throw new Exception("" + getLastError());
            }

            decryptBytes = pkcs7.data;
        } catch (Exception e) {
        	e.printStackTrace();
        }

        return decryptBytes;
    }





}
