package org.example.handler;

import org.hibernate.PessimisticLockException;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            PessimisticLockException.class,
            CannotAcquireLockException.class,
            OptimisticLockingFailureException.class
    })
    public ResponseEntity<String> handleConcurrencyExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("O sistema está com alta concorrência neste recurso e esgotou as tentativas de reexecução. Por favor, tente novamente mais tarde.");
    }
}
