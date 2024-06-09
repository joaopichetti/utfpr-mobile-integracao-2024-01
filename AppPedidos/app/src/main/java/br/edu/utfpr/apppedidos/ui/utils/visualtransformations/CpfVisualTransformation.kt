package br.edu.utfpr.apppedidos.ui.utils.visualtransformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import br.edu.utfpr.apppedidos.extensions.toFormattedCpf

class CpfVisualTransformation : VisualTransformation {
    //999.999.999-99
    override fun filter(text: AnnotatedString): TransformedText {
        val formattedCpf = text.text.toFormattedCpf()

        return TransformedText(
            AnnotatedString(formattedCpf),
            CpfOffsetMapping
        )
    }

    object CpfOffsetMapping : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return when {
                offset > 9 -> offset + 3
                offset > 6 -> offset + 2
                offset > 3 -> offset + 1
                else -> offset
            }
        }

        override fun transformedToOriginal(offset: Int): Int {
            return when {
                offset > 9 -> offset - 3
                offset > 6 -> offset - 2
                offset > 3 -> offset - 1
                else -> offset
            }
        }
    }
}