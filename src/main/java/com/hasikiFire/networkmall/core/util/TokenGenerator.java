package com.hasikiFire.networkmall.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class TokenGenerator {

  /**
   * 生成类似 9cxxxxxxx823xxxxxf412139 的 token
   *
   * @return 生成的 token 字符串
   */
  public static String generateToken() {
    // 生成随机 UUID
    String uuid = UUID.randomUUID().toString().replace("-", "");

    // 使用 MD5 对 UUID 进行哈希处理
    String md5Token = md5(uuid);

    return md5Token;
  }

  /**
   * 使用 MD5 算法生成哈希值
   *
   * @param input 输入字符串
   * @return 哈希值
   */
  private static String md5(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] digest = md.digest(input.getBytes());

      // 将字节数组转换为十六进制字符串
      StringBuilder hexString = new StringBuilder();
      for (byte b : digest) {
        hexString.append(String.format("%02x", b));
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("MD5 algorithm not available", e);
    }
  }

  public static void main(String[] args) {
    // 生成并打印一个 token
    String token = generateToken();
    System.out.println("Generated token: " + token);
  }
}
