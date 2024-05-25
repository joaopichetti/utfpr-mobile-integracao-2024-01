package br.edu.utfpr.serverpedidos.util.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class TelefoneValidator : ConstraintValidator<Telefone, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return value.isNullOrEmpty() ||
                value.matches(Regex("^(\\(\\d{2}\\)|\\d{2})\\s?9?\\d{4}\\-?\\d{4}\$"))
    }
}
