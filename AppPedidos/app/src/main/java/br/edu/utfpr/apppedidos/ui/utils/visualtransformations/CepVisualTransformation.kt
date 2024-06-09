package br.edu.utfpr.apppedidos.ui.utils.visualtransformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import br.edu.utfpr.apppedidos.extensions.toFormattedCep

class CepVisualTransformation : VisualTransformation {
    //99999-999
    override fun filter(text: AnnotatedString): TransformedText {
        val formattedCep = text.text.toFormattedCep()

        return TransformedText(
            AnnotatedString(formattedCep),
            CepOffsetMapping
        )
    }

    object CepOffsetMapping : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return if (offset > 5) {
                offset + 1
            } else {
                offset
            }
        }

        override fun transformedToOriginal(offset: Int): Int {
            return if (offset > 5) {
                offset - 1
            } else {
                offset
            }
        }
    }
}