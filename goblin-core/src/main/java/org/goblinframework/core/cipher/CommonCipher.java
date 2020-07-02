package org.goblinframework.core.cipher;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Objects;

public class CommonCipher {

    private String cipherMode;
    private String keyMode;

    public CommonCipher(String cipherMode, String keyMode) {
        this.cipherMode = cipherMode;
        this.keyMode = keyMode;
        CipherManager.INSTANCE.getCipherPool(cipherMode);// init object pool
    }

    /**
     * 使用指定的key加密data，并返回base64字符串 (UTF-8)
     *
     * @param key  the key string
     * @param data the data string to be encrypted
     * @return encrypted base64 string
     */
    public String encryptBase64String(String key, String data) {
        return encryptBase64String(key, data, StandardCharsets.UTF_8);
    }

    /**
     * 使用指定的key加密data，并返回base64字符串
     *
     * @param key     the key string
     * @param data    the data string to be encrypted
     * @param charset charset to encode key and data string, use UTF-8 in case of not specified
     * @return encrypted base64 string
     */
    public String encryptBase64String(String key, String data, Charset charset) {
        return encryptBase64String(key, data, charset, null);
    }

    /**
     * 使用指定的key加密data，并返回base64字符串
     *
     * @param key     the key string
     * @param data    the data string to be encrypted
     * @param charset charset to encode key and data string, use UTF-8 in case of not specified
     * @param iv      the initialization vector
     * @return
     */
    public String encryptBase64String(String key, String data, Charset charset, String iv) {
        byte[] encrypted = encrypt(key, data, charset, iv);
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * 使用指定的key加密data，并返回hex字符串 (UTF-8)
     *
     * @param key  the key string
     * @param data the data string to be encrypted
     * @return encrypted hex string
     */
    public String encryptHexString(String key, String data) {
        return encryptHexString(key, data, StandardCharsets.UTF_8);
    }

    /**
     * 使用指定的key加密data，并返回hex字符串
     *
     * @param key     the key string
     * @param data    the data string to be encrypted
     * @param charset the charset to encode key and data strings
     * @return encrypted hex string
     */
    public String encryptHexString(String key, String data, Charset charset) {
        return encryptHexString(key, data, charset, null);
    }

    /**
     * 使用指定的key加密data，并返回hex字符串
     *
     * @param key     the key string
     * @param data    the data string to be encrypted
     * @param charset the charset to encode key and data strings
     * @param iv      the initialization vector
     * @return
     */
    public String encryptHexString(String key, String data, Charset charset, String iv) {
        byte[] encrypted = encrypt(key, data, charset, iv);
        return Hex.encodeHexString(encrypted);
    }

    /**
     * 使用指定的key加密data，编码使用UTF-8
     * 如果不指定charset，使用UTF-8
     *
     * @param key  the key string
     * @param data the data string to be encrypted
     * @return encrypted bytes
     */
    public byte[] encrypt(String key, String data) {
        return encrypt(key, data, StandardCharsets.UTF_8);
    }

    /**
     * 使用指定的key加密data
     * 如果不指定charset，使用UTF-8
     *
     * @param key     the key string
     * @param data    the data string to be encrypted
     * @param charset the charset to encode key and data strings
     * @return encrypted bytes
     */
    public byte[] encrypt(String key, String data, Charset charset) {
        return encrypt(key, data, charset, null);
    }

    /**
     * 使用指定的key加密data
     * 如果不指定charset，使用UTF-8
     * 如果不指定iv，默认null
     *
     * @param key     the key string
     * @param data    the data string to be encrypted
     * @param charset the charset to encode key and data strings
     * @param iv      the initialization vector
     * @return
     */
    public byte[] encrypt(String key, String data, Charset charset, String iv) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(data);
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        byte[] k = key.getBytes(charset);
        byte[] d = data.getBytes(charset);
        byte[] v = null;
        if (iv != null) {
            v = iv.getBytes(charset);
        }
        return encrypt(k, d, v);
    }

    /**
     * 使用指定的key加密data，并返回base64字符串
     *
     * @param key  the key bytes
     * @param data the data bytes to be encrypted
     * @return encrypted base64 string
     */
    public String encryptBase64String(byte[] key, byte[] data) {
        byte[] encrypted = encrypt(key, data);
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * 使用指定的key加密data，并返回hex字符串
     *
     * @param key  the key bytes
     * @param data the data bytes to be encrypted
     * @return encrypted hex string
     */
    public String encryptHexString(byte[] key, byte[] data) {
        byte[] encrypted = encrypt(key, data);
        return Hex.encodeHexString(encrypted);
    }

    /**
     * 使用指定的key加密data
     *
     * @param key  the key bytes
     * @param data the data bytes to be encrypted
     * @return encrypted bytes
     */
    public byte[] encrypt(byte[] key, byte[] data) {
        return encrypt(key, data, null);
    }

    /**
     * 使用指定的key加密data
     *
     * @param key  the key bytes
     * @param data the data bytes to be encrypted
     * @param iv   the initialization vector
     * @return
     */
    public byte[] encrypt(byte[] key, byte[] data, byte[] iv) {
        Objects.requireNonNull(data);
        Cipher cipher = null;
        try {
            Key secretKey = new SecretKeySpec(key, keyMode);
            cipher = acquireCipher();
            if (iv == null) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            } else {
                IvParameterSpec ips = new IvParameterSpec(iv);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ips);
            }
            return cipher.doFinal(data);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to encrypt (" + cipherMode + ")", ex);
        } finally {
            releaseCipher(cipher);
        }
    }

    /**
     * 使用指定的key解密base64字符串，使用UTF-8编码
     *
     * @param key    the key string
     * @param base64 the encrypted base64 string
     * @return decrypted string (UTF-8)
     */
    public String decryptBase64String(String key, String base64) {
        return decryptBase64String(key, base64, Charset.defaultCharset());
    }

    /**
     * 使用指定的key解密base64字符串
     * 如果不指定编码，使用UTF-8
     *
     * @param key     the key string
     * @param base64  the encrypted base64 string
     * @param charset the charset to decode decrypted bytes
     * @return decrypted string
     */
    public String decryptBase64String(String key, String base64, Charset charset) {
        return decryptBase64String(key, base64, charset, null);
    }

    /**
     * 使用指定的key解密base64字符串
     * 如果不指定编码，使用UTF-8
     * 如果不指定iv，默认null
     *
     * @param key     the key string
     * @param base64  the encrypted base64 string
     * @param charset the charset to decode decrypted bytes
     * @param iv      the initialization vector
     * @return
     */
    public String decryptBase64String(String key, String base64, Charset charset, String iv) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(base64);
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        byte[] k = key.getBytes(charset);
        byte[] e;
        try {
            e = Base64.decodeBase64(base64);
            if (e.length == 0) {
                throw new Exception();
            }
        } catch (Exception ex) {
            throw new IllegalStateException("No valid BASE64 string");
        }
        byte[] v = null;
        if (iv != null) {
            v = iv.getBytes(charset);
        }
        byte[] decrypted = decrypt(k, e, v);
        return new String(decrypted, charset);
    }

    /**
     * 使用指定的key解密hex字符串，使用UTF-8编码
     *
     * @param key the key string
     * @param hex the encrypted hex string
     * @return decrypted string (UTF-8)
     */
    public String decryptHexString(String key, String hex) {
        return decryptHexString(key, hex, StandardCharsets.UTF_8);
    }

    /**
     * 使用指定的key解密hex字符串
     * 如果不指定编码，使用UTF-8
     *
     * @param key     the key string
     * @param hex     the encrypted hex string
     * @param charset the charset to decode decrypted bytes
     * @return decrypted string
     */
    public String decryptHexString(String key, String hex, Charset charset) {
        return decryptHexString(key, hex, charset, null);
    }

    /**
     * 使用指定的key解密hex字符串
     * 如果不指定编码，使用UTF-8
     * 如果不指定iv，默认null
     *
     * @param key     the key string
     * @param hex     the encrypted hex string
     * @param charset the charset to decode decrypted bytes
     * @param iv      the initialization vector
     * @return
     */
    public String decryptHexString(String key, String hex, Charset charset, String iv) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(hex);
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        byte[] k = key.getBytes(charset);
        byte[] e;
        try {
            e = Hex.decodeHex(hex.toCharArray());
        } catch (DecoderException ex) {
            throw new IllegalStateException("No valid HEX string");
        }
        byte[] v = null;
        if (iv != null) {
            v = iv.getBytes(charset);
        }
        byte[] decrypted = decrypt(k, e, v);
        return new String(decrypted, charset);
    }

    /**
     * 使用指定的key解密data
     *
     * @param key     the key bytes
     * @param encoded the encrypted bytes
     * @return decrypted bytes
     */
    public byte[] decrypt(byte[] key, byte[] encoded) {
        return decrypt(key, encoded, null);
    }

    /**
     * 使用指定的key解密data
     *
     * @param key     the key bytes
     * @param encoded the encrypted bytes
     * @param iv      the initialization vector
     * @return
     */
    public byte[] decrypt(byte[] key, byte[] encoded, byte[] iv) {
        Cipher cipher = null;
        try {
            Key secretKey = new SecretKeySpec(key, keyMode);
            cipher = acquireCipher();
            if (iv == null) {
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
            } else {
                IvParameterSpec ips = new IvParameterSpec(iv);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ips);
            }
            return cipher.doFinal(encoded);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to decrypt (" + cipherMode + ")", ex);
        } finally {
            releaseCipher(cipher);
        }
    }

    protected Cipher acquireCipher() {
        return CipherManager.INSTANCE.acquireCipher(this.cipherMode);
    }

    protected void releaseCipher(Cipher cipher) {
        CipherManager.INSTANCE.releaseCipher(cipher, this.cipherMode);
    }
}
