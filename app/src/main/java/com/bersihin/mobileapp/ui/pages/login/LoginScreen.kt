package com.bersihin.mobileapp.ui.pages.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.ui.components.FormField
import com.bersihin.mobileapp.ui.components.FormFieldProps
import com.bersihin.mobileapp.ui.theme.BersihinTheme
import com.bersihin.mobileapp.utils.FormFieldValidator

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToRegister: () -> Unit = {}
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    var isAllValid by rememberSaveable { mutableStateOf(false) }
    val validator = FormFieldValidator

    fun updateValid() {
        isAllValid = validator.validateEmail(email)
                && validator.validatePassword(password)

    }

    val props: List<FormFieldProps> = listOf(
        FormFieldProps(
            labelId = R.string.email,
            value = email,
            placeholderId = R.string.email_placeholder,
            validator = { validator.validateEmail(it as String) },
            onValueChanged = {
                email = it as String
                updateValid()
            },
            errorMessageId = R.string.email_invalid
        ),
        FormFieldProps(
            labelId = R.string.password,
            value = password,
            placeholderId = R.string.password_placeholder,
            validator = { validator.validatePassword(it as String) },
            onValueChanged = {
                password = it as String
                updateValid()
            },
            isPassword = true,
            isPasswordVisible = passwordVisible,
            onPasswordToggle = { passwordVisible = !passwordVisible },
            errorMessageId = R.string.password_invalid
        )
    )

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(vertical = 64.dp),
            text = stringResource(id = R.string.login),
            style = MaterialTheme.typography.displaySmall
        )

        props.forEach { prop ->
            FormField(props = prop)
        }

        Button(
            modifier = Modifier
                .width(320.dp)
                .height(80.dp)
                .padding(top = 32.dp),
            onClick = { /*TODO: login user*/ },
            enabled = isAllValid
        ) {
            Text(
                text = stringResource(id = R.string.login),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Row(
            modifier = Modifier.padding(vertical = 32.dp)
        ) {
            Text(
                text = stringResource(id = R.string.dont_have_account),
                style = MaterialTheme.typography.titleMedium
            )
            ClickableText(
                text = AnnotatedString(stringResource(id = R.string.register)),
                onClick = { navigateToRegister() },
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold,
                )
            )
        }

    }
}


@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    BersihinTheme {
        LoginScreen()
    }
}