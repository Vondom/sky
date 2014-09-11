package com.sky.sample;

/**
 * Created by jcooky on 2014. 6. 19..
 */
public class Algorithm1 {

  private static int gcd(int a, int b) {
    if (a == 0) return a;
    return gcd(a, a%b);
  }

  public static void main(String[] args) {
    int a = 56, b = 24;
    System.out.printf("gcd(%d, %d) = %d\n", a, b, gcd(a, b));
  }


}