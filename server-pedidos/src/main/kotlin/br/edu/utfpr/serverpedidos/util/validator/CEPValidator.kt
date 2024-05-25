package br.edu.utfpr.serverpedidos.util.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class CEPValidator : ConstraintValidator<CEP, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return value.isNullOrEmpty() || value.matches(Regex("^\\d{5}\\-?\\d{3}\$"))
    }
}
