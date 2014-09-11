package com.sky.server.exception;

/**
 * Created by jcooky on 2014. 8. 27..
 */
public class WebNotFoundException extends Exception {
  public WebNotFoundException() {
    super();
  }

  public WebNotFoundException(String message) {
    super(message);
  }

  public WebNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public WebNotFoundException(Throwable cause) {
    super(cause);
  }

  protected WebNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
