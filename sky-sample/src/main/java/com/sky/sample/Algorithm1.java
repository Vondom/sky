package com.sky.sample;

/**
 * Created by jcooky on 2014. 6. 19..
 */
public class Algorithm1 {
  private static int count(int n) {
    int i;
    for (i = 0; n != 0; i += n % 10 == 1 ? 1 : 0, n /= 10);
    return i;
  }

  private static int f(int n, int prev) {
    return n == 1 ? 1 : count(n) + prev;
  }

  public static void main(String[] args) {
    int i, n;
    for (i = 2, n = f(1, 0); (n = f(i, n)) != i; ++i);

    System.out.printf("f(%d) = %d\n", i, n);
  }


}