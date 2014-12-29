package com.sky.worker.exception;

/**
 * Created by JCooky on 14. 12. 29..
 */
public class WorkingException extends Exception {
  public WorkingException() {
    super();
  }

  public WorkingException(String message) {
    super(message);
  }

  public WorkingException(String message, Throwable cause) {
    super(message, cause);
  }

  public WorkingException(Throwable cause) {
    super(cause);
  }

  protected WorkingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
