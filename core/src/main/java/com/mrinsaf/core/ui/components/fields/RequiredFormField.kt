package com.mrinsaf.core.ui.components.fields

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.mrinsaf.core.R

@Composable
fun RequiredFormField(
    value: String,
    AdditionalContent: (@Composable () -> Unit)? = null,
    label: String,
    onValueChange: (String) -> Unit,
) {
    val annotatedLabel = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Red)) {
            append("* ")
        }
        append(label)
    }
        BasicFormField(
            value = value,
            AdditionalContent = AdditionalContent,
            label = annotatedLabel,
        ) {
            onValueChange(it)
        }
}

@Preview
@Composable
fun RequiredFormFieldPreview() {
    RequiredFormField(
        value = "",
        label =  "Электронная почта",
        AdditionalContent = {
            Text(
                text = stringResource(R.string.passwordRestrictions),
                modifier = Modifier
            )
        }
    ) {

    }
}