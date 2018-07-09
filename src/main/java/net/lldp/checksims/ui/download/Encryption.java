package net.lldp.checksims.ui.download;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.lambdaworks.crypto.SCryptUtil;

public class Encryption {
	private static SecretKeySpec generateKeySpec(String key) {
		MessageDigest sha = null;
        try {
            byte[] bytes = key.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-512");
            bytes = sha.digest(bytes);
            bytes = Arrays.copyOf(bytes, 16);
            return new SecretKeySpec(bytes, "AES");
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public static String encrypt(String data, String key) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec sks = generateKeySpec(key);
			if(sks == null) {
				return null;
			}
			cipher.init(Cipher.ENCRYPT_MODE, sks);
			return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes("UTF-8")));
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String decrypt(String data, String key) throws Exception {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec sks = generateKeySpec(key);
			if(sks == null) {
				return null;
			}
			cipher.init(Cipher.DECRYPT_MODE, sks);
		} catch(Exception e) {
			throw e;
		}
		try {
			return new String(cipher.doFinal(Base64.getDecoder().decode(data)));
		} catch(Exception e) {
			return null;
		}
	}
	
	public static String scrypt(String data) {
		return SCryptUtil.scrypt(data, 16384, 8, 1);
	}
	
	public static boolean scryptCheck(String data, String hashed) {
		return SCryptUtil.check(data, hashed);
	}
}

