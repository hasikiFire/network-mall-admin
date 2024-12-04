package com.hasikiFire.networkmall.core.util;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {
  private static final SecureRandom random = new SecureRandom();
  private static final int SALT_LENGTH = 16;

  public static String generateSalt() {
    byte[] salt = new byte[SALT_LENGTH];
    random.nextBytes(salt);
    return Base64.getEncoder().encodeToString(salt);
  }
}
