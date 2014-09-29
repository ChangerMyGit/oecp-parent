package oecp.framework.util;
import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * 
 * @author wl
 * 另一种des加密，与php的des加密一致，不会出现两方语言加密后密文不一致的情况
 * 
 * 
 * 
 * 
 * 
 <code>
 <?php
 	//对应的php的des代码
	$key='doctorpda_passport';
	function desdecrypt($encrypted,$key) {
		$encrypted = base64_decode($encrypted);
	    $td = mcrypt_module_open('des','','ecb','');	//使用MCRYPT_DES算法,cbc模式
	    $iv = @mcrypt_create_iv(mcrypt_enc_get_iv_size($td), MCRYPT_RAND);
	    $ks = mcrypt_enc_get_key_size($td);
	    @mcrypt_generic_init($td, $key, $iv);			//初始处理
	    $decrypted = mdecrypt_generic($td, $encrypted); //解密
	    mcrypt_generic_deinit($td);       				//结束
	    mcrypt_module_close($td);
	    $y = pkcs5_unpad($decrypted);
	    return $y;
	}
	function pkcs5_unpad($text) {
		$pad = ord($text{strlen($text)-1});
		if ($pad > strlen($text))
			return false;
		if (strspn($text, chr($pad), strlen($text) - $pad) != $pad)
			return false;
	    return substr($text, 0, -1 * $pad);
	}
	echo desdecrypt("6frLKWtbOD2Vo9HoZByGcJny4W2wQk9o",$key);
?>
</code>
*/
@SuppressWarnings("restriction")
public class DES4PHP {
	private byte[] desKey;  
	  
    public DES4PHP(String desKey) {  
        this.desKey = desKey.getBytes();  
    }  
  
    public byte[] desEncrypt(byte[] plainText) throws Exception {  
        SecureRandom sr = new SecureRandom();  
        byte rawKeyData[] = desKey;  
        DESKeySpec dks = new DESKeySpec(rawKeyData);  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
        SecretKey key = keyFactory.generateSecret(dks);  
        Cipher cipher = Cipher.getInstance("DES");  
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);  
        byte data[] = plainText;  
        byte encryptedData[] = cipher.doFinal(data);  
        return encryptedData;  
    }  
  
    public byte[] desDecrypt(byte[] encryptText) throws Exception {  
        SecureRandom sr = new SecureRandom();  
        byte rawKeyData[] = desKey;  
        DESKeySpec dks = new DESKeySpec(rawKeyData);  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
        SecretKey key = keyFactory.generateSecret(dks);  
        Cipher cipher = Cipher.getInstance("DES");  
        cipher.init(Cipher.DECRYPT_MODE, key, sr);  
        byte encryptedData[] = encryptText;  
        byte decryptedData[] = cipher.doFinal(encryptedData);  
        return decryptedData;  
    }  
  
    public String encrypt(String input) {
    	String str = "";
    	try{
    		str = base64Encode(desEncrypt(input.getBytes()));
    	}catch(Exception e){
    		throw new RuntimeException("Error encrypt DES4PHP class. Cause: " + e);
    	}
    	return str;
    }  
  
    public String decrypt(String input) {
    	String str = "";
    	try{
    		byte[] result = base64Decode(input);  
    		str = new String(desDecrypt(result));
    	}catch(Exception e){
    		throw new RuntimeException(  "Error decrypt DES4PHP class. Cause: " + e); 
    	}
        return str;  
    }  
  
	public static String base64Encode(byte[] s) {  
        if (s == null)  
            return null;  
        BASE64Encoder b = new sun.misc.BASE64Encoder();  
        return b.encode(s);  
    }  
  
	public static byte[] base64Decode(String s) throws IOException {  
        if (s == null)  
            return null;  
        BASE64Decoder decoder = new BASE64Decoder();  
        byte[] b = decoder.decodeBuffer(s);  
        return b;  
    }  
  
    public static void main(String[] args) throws Exception {  
        String key = "doctorpda_passport";  
        String input = "zj0715@gmail.com";  
        DES4PHP crypt = new DES4PHP(key);  
        System.out.println("Encode:" + crypt.encrypt(input));  
        System.out.println("Decode:" + crypt.decrypt(crypt.encrypt(input)));  
    }  

}
