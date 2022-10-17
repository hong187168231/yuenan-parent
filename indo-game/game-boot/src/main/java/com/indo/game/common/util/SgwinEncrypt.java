package com.indo.game.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;

/**
 * 信息加密类，用于加密用户密码。
 * 采用MessageDigest实现
 *
 * @author m3
 */
public class SgwinEncrypt {

    private static final SgwinEncrypt MD5 = new SgwinEncrypt("MD5");
    
    private String type;
    
    public SgwinEncrypt(String type) {
      setType(type);
  }
    
    public String getType() {
      return type;
  }

  public void setType(String type) {
      this.type = type;
  }

    public static String md5(String str) {
      return MD5.hash(str);
  }


  private String hash(String str) {
      if (str == null) {
          return null;
      }
      return hash(str.getBytes());
  }

  private String hash(byte[] data) {
      if (data == null) {
          return null;
      }
      return Hex.encodeHexString(digest(data));
  }

  private byte[] digest(byte[] data) {
      if (data == null) {
          return null;
      }

      try {
          MessageDigest digest = MessageDigest.getInstance(type);
          return digest.digest(data);
      } catch (NoSuchAlgorithmException e) {
          throw new RuntimeException(e);
      }
  }

}
