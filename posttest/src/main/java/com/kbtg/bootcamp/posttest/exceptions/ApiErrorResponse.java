package com.kbtg.bootcamp.posttest.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ApiErrorResponse {
  private LocalDateTime timestamp;
  private int status;
  private String error;
  @JsonProperty("error_message")
  private String errorMessage;
  private String path;

  public ApiErrorResponse(
      LocalDateTime timestamp, int status, String error, String errorMessage, String path) {
    this.timestamp = timestamp;
    this.status = status;
    this.error = error;
    this.errorMessage = errorMessage;
    this.path = path;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
