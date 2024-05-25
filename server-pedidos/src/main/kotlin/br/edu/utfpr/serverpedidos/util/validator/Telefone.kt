package br.edu.utfpr.serverpedidos.util.validator

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [TelefoneValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Telefone(
    val message: String = "{telefone.invalido}",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)
