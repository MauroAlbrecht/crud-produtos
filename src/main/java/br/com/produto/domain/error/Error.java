package br.com.produto.domain.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class Error {

    @JsonProperty("status_code")
    private Integer statusCode;

    private String message;

    @JsonIgnore
    private HttpStatus httpStatus;

    public Error(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.statusCode = httpStatus.value();
    }
}
