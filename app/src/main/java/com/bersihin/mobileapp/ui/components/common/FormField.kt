package com.bersihin.mobileapp.ui.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.ui.theme.BersihinTheme

data class FormFieldProps(
    val labelId: Int,
    val value: Any,
    val placeholderId: Int,
    val validator: (Any) -> Boolean,
    val onValueChanged: (Any) -> Unit,
    val errorMessageId: Int,
    val isPassword: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val onPasswordToggle: () -> Unit = {},
    val singleLine: Boolean = true,
    val textFieldHeight: Int = 60,
    val icon: ImageVector? = null,
    val maxLength: Int? = null
)

@Composable
fun FormField(
    props: FormFieldProps
) {
    var isError by rememberSaveable { mutableStateOf(false) }

    val visualTransformation = if (props.isPassword && !props.isPasswordVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    Column(
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        Text(
            text = stringResource(id = props.labelId),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.W900
            ),
            modifier = Modifier.padding(start = 12.dp)
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(props.textFieldHeight.dp),
            value = props.value.toString(),
            onValueChange = {
                props.onValueChanged(it)
                isError = !props.validator(it)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
            ),
            textStyle = MaterialTheme.typography.labelMedium,
            isError = isError,
            placeholder = {
                Text(
                    text = stringResource(id = props.placeholderId),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            },
            supportingText = {
                if (props.maxLength != null) {
                    Text(
                        text = "${props.value.toString().length} / ${props.maxLength}",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                    )
                }
            },
            singleLine = props.singleLine,
            shape = RoundedCornerShape(12.dp),
            visualTransformation = visualTransformation,
            leadingIcon = if (props.icon != null) {
                {
                    Icon(
                        imageVector = props.icon,
                        contentDescription = null
                    )
                }
            } else null,
            trailingIcon = {
                if (props.isPassword) {
                    val icon =
                        if (props.isPasswordVisible) {
                            Icons.Filled.VisibilityOff
                        } else {
                            Icons.Filled.Visibility
                        }

                    IconButton(onClick = props.onPasswordToggle) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null
                        )
                    }
                }
            }
        )

        if (isError) {
            Text(
                text = stringResource(id = props.errorMessageId),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FormFieldPreview() {
    BersihinTheme {
        FormField(
            props = FormFieldProps(
                labelId = R.string.first_name,
                value = "",
                placeholderId = R.string.first_name_placeholder,
                validator = { it is String },
                onValueChanged = { },
                errorMessageId = R.string.first_name_invalid
            )
        )
    }
}