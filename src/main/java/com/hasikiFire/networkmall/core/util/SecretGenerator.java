package com.hasikiFire.networkmall.core.util;

import java.security.SecureRandom;
import java.math.BigInteger;

public class SecretGenerator {
  public static void main(String[] args) {
    SecureRandom random = new SecureRandom();
    String secret = new BigInteger(130, random).toString(32);
    System.out.println("Secret: " + secret);
  }
}