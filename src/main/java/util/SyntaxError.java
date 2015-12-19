/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import util.Strings.RewindableIterator;


public class SyntaxError extends Error {
  public SyntaxError(String type, RewindableIterator obj) {
    super(String.format("Couldn't parse %s: \r\n######\r\n%s\r\n######\r\n", type, obj.toString()));
  }
}
