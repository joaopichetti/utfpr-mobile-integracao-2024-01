package br.edu.utfpr.serverpedidosjava.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TelefoneValidator implements ConstraintValidator<Telefone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.isEmpty() || value.matches("^(\\(\\d{2}\\)|\\d{2})\\s?9?\\d{4}\\-?\\d{4}$");
    }

}
