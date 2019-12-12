package org.goblinframework.core.cipher;

import java.nio.charset.Charset;

public class AesCipherUtils  {

    private static CommonCipher cipher = new CommonCipher("AES", "AES");

    public static String encryptBase64String(String key, String data) {
        return cipher.encryptBase64String(key, data);
    }

    public static String encryptBase64String(String key, String data, Charset charset) {
        return cipher.encryptBase64String(key, data, charset);
    }

    public static String encryptBase64String(String key, String data, Charset charset, String iv) {
        return cipher.encryptBase64String(key, data, charset, iv);
    }

    public static String encryptHexString(String key, String data) {
        return cipher.encryptHexString(key, data);
    }

    public static String encryptHexString(String key, String data, Charset charset) {
        return cipher.encryptHexString(key, data, charset);
    }

    public static String encryptHexString(String key, String data, Charset charset, String iv) {
        return cipher.encryptHexString(key, data, charset, iv);
    }

    public static byte[] encrypt(String key, String data) {
        return cipher.encrypt(key, data);
    }

    public static byte[] encrypt(String key, String data, Charset charset) {
        return cipher.encrypt(key, data, charset);
    }

    public static byte[] encrypt(String key, String data, Charset charset, String iv) {
        return cipher.encrypt(key, data, charset, iv);
    }

    public static String encryptBase64String(byte[] key, byte[] data) {
        return cipher.encryptBase64String(key, data);
    }

    public static String encryptHexString(byte[] key, byte[] data) {
        return cipher.encryptHexString(key, data);
    }

    public static byte[] encrypt(byte[] key, byte[] data) {
        return cipher.encrypt(key, data);
    }

    public static byte[] encrypt(byte[] key, byte[] data, byte[] iv) {
        return cipher.encrypt(key, data, iv);
    }

    public static String decryptBase64String(String key, String base64) {
        return cipher.decryptBase64String(key, base64);
    }

    public static String decryptBase64String(String key, String base64, Charset charset) {
        return cipher.decryptBase64String(key, base64, charset);
    }

    public static String decryptBase64String(String key, String base64, Charset charset, String iv) {
        return cipher.decryptBase64String(key, base64, charset, iv);
    }

    public static String decryptHexString(String key, String hex) {
        return cipher.decryptHexString(key, hex);
    }

    public static String decryptHexString(String key, String hex, Charset charset) {
        return cipher.decryptHexString(key, hex, charset);
    }

    public static String decryptHexString(String key, String hex, Charset charset, String iv) {
        return cipher.decryptHexString(key, hex, charset, iv);
    }

    public static byte[] decrypt(byte[] key, byte[] encoded) {
        return cipher.decrypt(key, encoded);
    }

    public static byte[] decrypt(byte[] key, byte[] encoded, byte[] iv) {
        return cipher.decrypt(key, encoded, iv);
    }
}
