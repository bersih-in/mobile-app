package com.bersihin.mobileapp.ui.pages.register

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.ui.components.FormField
import com.bersihin.mobileapp.ui.components.FormFieldProps
import com.bersihin.mobileapp.ui.theme.BersihinTheme
import com.bersihin.mobileapp.utils.FormFieldValidator

@Composable
fun RegisterScreen() {
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    var isAllValid by rememberSaveable { mutableStateOf(false) }
    val validator = FormFieldValidator

    fun updateValid() {
        isAllValid = validator.validateName(firstName)
                && validator.validateName(lastName)
                && validator.validateEmail(email)
                && validator.validatePassword(password)
                && validator.validateConfirmPassword(confirmPassword, password)
    }


    val props: List<FormFieldProps> = listOf(
        FormFieldProps(
            labelId = R.string.first_name,
            value = firstName,
            placeholderId = R.string.first_name_placeholder,
            validator = { validator.validateName(it as String) },
            onValueChanged = {
                firstName = it as String
                updateValid()
            },
            errorMessageId = R.string.first_name_invalid
        ),
        FormFieldProps(
            labelId = R.string.last_name,
            value = lastName,
            placeholderId = R.string.last_name_placeholder,
            validator = { validator.validateName(it as String) },
            onValueChanged = {
                lastName = it as String
                updateValid()
            },
            errorMessageId = R.string.last_name_invalid
        ),
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
        ),
        FormFieldProps(
            labelId = R.string.confirm_password,
            value = confirmPassword,
            placeholderId = R.string.confirm_password_placeholder,
            validator = { validator.validateConfirmPassword(it as String, password) },
            onValueChanged = {
                confirmPassword = it as String
                Log.i("RegisterPage", "createProps: $confirmPassword")
                updateValid()
            },
            isPassword = true,
            isPasswordVisible = confirmPasswordVisible,
            onPasswordToggle = { confirmPasswordVisible = !confirmPasswordVisible },
            errorMessageId = R.string.confirm_password_invalid
        )
    )

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(bottom = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(vertical = 64.dp),
            text = stringResource(id = R.string.register),
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
            onClick = { /*TODO: register user*/ },
            enabled = isAllValid
        ) {
            Text(
                text = stringResource(id = R.string.register),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RegisterScreenPreview() {
    BersihinTheme {
        RegisterScreen()
    }
}