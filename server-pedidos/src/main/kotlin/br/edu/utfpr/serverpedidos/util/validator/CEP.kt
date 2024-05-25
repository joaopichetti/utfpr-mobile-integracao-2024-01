package br.edu.utfpr.serverpedidos.util.validator

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [CEPValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CEP(
    val message: String = "{cep.invalido}",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)
