package com.invoice.port.hnhangxin.invoice.util;

import java.io.*;
import com.google.common.io.Closer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Created by TT on 2018-03-16.
 */
public class PKCS7 {
    private final static Logger LOGGER = LoggerFactory.getLogger(PKCS7.class);

    // 设置信任链
    public static native boolean setTrusts(String trusts);

    // 设置解密证书
    public static native boolean setDecryptPfx(byte[] decPfx, String passwd);

    // 设置签名证书
    public static native boolean setSignedPfx(byte[] sigPfx, String passwd);

    // 验证证书，成功返回1
    // public static native int validateCert(String base64Cert);

    // 打包数字信封，传递加密证书(即接收者的证书)
    public synchronized static native byte[] signedAndEnveloped(String encBase64Cert, byte[] inData);

    // 解包数字信封
    public synchronized static native PKCS7 unpack(byte[] inData);

    // 获取错误码
    public static native int getLastError();

    private static String dllFile; //dll文件加载路径
    private static String path = PKCS7.class.getClassLoader().getResource("").getPath().substring(1); //公共路径
    private static String trustsBytes = CaConstant.getProperty("51fapiao.PUBLIC_TRUSTS"); //trusts
    private static String PLATFORM_DECRYPTCER = CaConstant.getProperty("51fapiao.PLATFORM_DECRYPTCER"); //服务端公钥

    /**
     * sigCert、serial、subject、data以下参数 请不要有任何幻想修改，包括你看不惯的命名！！！！！！！！！！！！！！
     */
    private String sigCert; // 签名证书
    private String serial; // 证书序列号
    private String subject; // 证书主题
    private byte[] data; // 原文

    static {
        dllFile = path + CaConstant.getProperty("51fapiao.DLLADDRESS2");
        System.load(dllFile);
    }

    public PKCS7() {
    }

    /**
     * @param trustsBytes 证书信任链
     * @param privatePFXBytes 加密/签名私钥
     * @param privatePFXKey 私钥密码
     * @throws Exception
     */
    public PKCS7(byte[] trustsBytes, byte[] privatePFXBytes, String privatePFXKey) throws Exception {
        if (!setTrusts(new String(trustsBytes))) {
            throw new Exception("" + getLastError());
        }

        if (!setDecryptPfx(privatePFXBytes, privatePFXKey)) {
            throw new Exception("" + getLastError());
        }

        if (!setSignedPfx(privatePFXBytes, privatePFXKey)) {
            throw new Exception("" + getLastError());
        }
    }

    /**
     * 依据文件绝对路径, 读取文件
     * @param fileUri 文件绝对路径
     * @return byte[] 读取成功的文件字节流
     */
    private static byte[] readFile(String fileUri) {
        final Closer closer = Closer.create();
        try {
            final BufferedInputStream bufferedInputStream = closer.register(new BufferedInputStream(new FileInputStream(fileUri)));
            final byte[] bufferedBytes = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(bufferedBytes, 0, bufferedBytes.length);
            return bufferedBytes;
        } catch (IOException e) {
            LOGGER.error("read file ioException:", e.fillInStackTrace());
        } finally {
            try {
                closer.close();
            } catch (IOException e) {
                LOGGER.error("close file ioException:", e.fillInStackTrace());
            }
        }
        return new byte[0];
    }

    /**
     * 签名加密
     * @param plainContent 预加密的原文
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
            if (isNullOrEmpty(plainContent)) {
                throw new Exception("传入参数原文为NULL,不可用");
            }
            return signedAndEnveloped(encCert, plainContent.getBytes(CaConstant.DEFAULT_CHARSET));
        } catch (Exception e) {
            LOGGER.error("pkcs7Encrypt Exception:", e.fillInStackTrace());
        }
        return new byte[0];
    }

    /**
     * 解密验签
     * @param decodeBase64EncryptTxtBytes 经过Base64解压后的密文字节流
     * @return byte[] 经过解密的明文字节流
     */
    public byte[] pkcs7Decrypt(byte[] decodeBase64EncryptTxtBytes) throws Exception {
        byte[] decryptBytes = new byte[0];
        try {
            // 解密
            if (decodeBase64EncryptTxtBytes == null) {
                throw new Exception("传入参数密文为NULL,不可用");
            }
            final PKCS7 pkcs7 = unpack(decodeBase64EncryptTxtBytes);
            if (pkcs7 == null) {
                throw new Exception("" + getLastError());
            }
            decryptBytes = pkcs7.data;
        } catch (Exception e) {
            LOGGER.error("pkcs7Decrypt Exception:", e.fillInStackTrace());
        }
        return decryptBytes;
    }

    /**
     * 公共信任链OMIT
     * 加密过程 :客户端私钥(pfx)、pwd + 平台公钥(cer)
     * @param source
     * @throws Exception
     */
    public String clientEncrypt(String source,PKCS7 pkcs7Client) throws Exception{
        //此处加载路径需根据数据库重新配置
        byte[] encodeData = pkcs7Client.pkcs7Encrypt(source, FileUtils.readFileToByteArray(new File(path + PLATFORM_DECRYPTCER)));
        byte[] base64Data = Base64Helper.encode(encodeData);
        String encodeText = new String(base64Data,"UTF-8");
        LOGGER.info("客户端加密:{}", encodeText);
        return encodeText;
    }

    /**
     * 客户端加密
     * @param PLATFORM_DECRYPTCER_URL 平台公钥
     * @param source 待加密内容
     * @param pkcs7Client
     * @return
     * @throws Exception
     */
    public String clientEncrypt(String PLATFORM_DECRYPTCER_URL,String source,PKCS7 pkcs7Client) throws Exception{
        //此处加载路径需根据数据库重新配置
        byte[] encodeData = pkcs7Client.pkcs7Encrypt(source, readFileFromUrlToByteArray(PLATFORM_DECRYPTCER_URL));
        byte[] base64Data = Base64Helper.encode(encodeData);
        String encodeText = new String(base64Data,"UTF-8");
        LOGGER.info("客户端加密:{}", encodeText);
        return encodeText;
    }

    /**
     * 客户端解密过程 :客户端私钥(pfx)、pwd
     * @param base64Data
     * @return
     * @throws Exception
     */
    public String clientDecrypt(String base64Data,PKCS7 pkcs7Client) throws Exception{
        //路径需根据数据库进行配置
        byte[] decodeData = pkcs7Client.pkcs7Decrypt(base64Data.getBytes());
        String decodeText = new String(decodeData,"UTF-8");
        LOGGER.info("客户端解密:{}", decodeText);
        return decodeText;
    }

    /**
     * 获得对象
     * @param client_decryptpfx 客户端私钥
     * @param client_decryptpfx_key 客户端私钥密码
     * @return
     */
    public static PKCS7 getPkcs7Client(String client_decryptpfx,String client_decryptpfx_key) {
        try {
            PKCS7 pkcs7Client = new PKCS7(FileUtils.readFileToByteArray(new File(path + trustsBytes)), FileUtils.readFileToByteArray(new File(path + client_decryptpfx)), client_decryptpfx_key);
            return pkcs7Client;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过网络地址获得对象
     * @param trustsUrl trust文件
     * @param client_decryptpfx 客户端私钥
     * @param client_decryptpfx_key 客户端私钥密码
     * @return
     */
    public static PKCS7 getPkcs7Client(String trustsUrl,String client_decryptpfx,String client_decryptpfx_key) {
        try {
            PKCS7 pkcs7Client = new PKCS7(readFileFromUrlToByteArray(trustsUrl), readFileFromUrlToByteArray(client_decryptpfx), client_decryptpfx_key);
            return pkcs7Client;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从网络读取文件
     * @param url
     * @return
     */
    public static byte[] readFileFromUrlToByteArray(String url)  {
        Resource resource = null;
        InputStream inputStream =null;
        byte[] var2 = null;
        try {
            resource = new UrlResource(url);
            inputStream = resource.getInputStream();
            var2 = IOUtils.toByteArray(inputStream);
            return var2;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * 从本地读取文件
     * @param
     * @param
     * @return
     */
    public static byte[] readFileFromFileToByteArray() {
        Resource resource = null;
        InputStream inputStream = null;
        byte[] var2 = null;
        try {
            resource = new FileSystemResource(new File("C:\\Users\\TT\\Desktop\\证书\\914300007170539007.pfx"));
            inputStream = resource.getInputStream();
            var2 =  IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return var2;
    }

    public static PKCS7 getPkcs7Server(String server_decryptpfx, String server_decryptpfx_key) {
        try {
            PKCS7 pkcs7Client = new PKCS7(FileUtils.readFileToByteArray(new File(path + trustsBytes)), FileUtils.readFileToByteArray(new File(path + server_decryptpfx)), server_decryptpfx_key);
            return pkcs7Client;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
