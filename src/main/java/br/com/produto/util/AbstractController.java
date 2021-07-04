package br.com.produto.util;

import br.com.produto.domain.error.Error;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.*;

public abstract class AbstractController {

    protected ResponseEntity<Object> ok(Object resultado) {

        return ResponseEntity.ok().body(resultado);
    }

    protected ResponseEntity<Object> created(Object resultado) {

        return ResponseEntity.status(CREATED).body(resultado);
    }

    private ResponseEntity<Object> badRequest(Object resultado) {

        return ResponseEntity.status(BAD_REQUEST).body(resultado);
    }

    private ResponseEntity<Object> notFound(Object resultado) {

        return ResponseEntity.status(NOT_FOUND).body(resultado);
    }

    protected ResponseEntity<Object> error(Error error) {

        switch (error.getHttpStatus()) {
            case BAD_REQUEST:
                return badRequest(error);
            case NOT_FOUND:
                return notFound(error);
        }

        return null;
    }
}
