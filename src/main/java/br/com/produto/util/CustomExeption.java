package br.com.produto.util;

import br.com.produto.domain.error.Error;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomExeption extends RuntimeException {

    private Error error;
}
