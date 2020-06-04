package in.ekstep.am.builder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public abstract class ResponseBuilder<T> {
  boolean success;
  String errMsg;
  String err;
  private HttpStatus httpStatus = HttpStatus.OK;

  public ResponseEntity<T> response() {
    return ResponseEntity.status(httpStatus).body(build());
  }

  public ResponseEntity<T> errorResponse(String error, String errMsg) {
    markFailure(error, errMsg);
    return ResponseEntity.status(500).body(build());
  }

  public ResponseEntity<T> badRequest(BindingResult bindingResult) {
    String error = "BAD_REQUEST";
    String errorMessage = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining(", "));
    markFailure(error, errorMessage);
    return ResponseEntity
        .badRequest()
        .body(build());
  }

  protected abstract T build();

  public boolean successful() {
    return success;
  }

  public void markFailure(String error, String errmsg) {
    this.success = false;
    this.err = error;
    this.errMsg = errmsg;
    this.httpStatus = HttpStatus.BAD_REQUEST;
  }

  public void markSuccess() {
    this.success = true;
    this.httpStatus = HttpStatus.OK;
  }

}
