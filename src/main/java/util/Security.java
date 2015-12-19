/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class Security {
  public static SecureRandom getRandom() {
    try {
      return SecureRandom.getInstanceStrong();
    } catch (NoSuchAlgorithmException ex) {
      throw new Error(ex);
    }
  }
  public static String hashPassword(String in) {
    try {
      return Strings.toHex(
              MessageDigest.getInstance("SHA-256")
                      .digest(in.getBytes()));
    } catch (NoSuchAlgorithmException ex) {
      throw new Error(ex);
    }
  }
}
