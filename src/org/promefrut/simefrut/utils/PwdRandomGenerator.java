package org.promefrut.simefrut.utils;

import java.security.SecureRandom;
import java.math.BigInteger;

public final class PwdRandomGenerator{

  private static SecureRandom random = new SecureRandom();

  public static String generatePwd(){
    return new BigInteger(130, random).toString(32);
  }
}
