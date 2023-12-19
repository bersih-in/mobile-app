package com.bersihin.mobileapp.ui.pages.general.login

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.api.ApiConfig
import com.bersihin.mobileapp.ui.components.common.FormField
import com.bersihin.mobileapp.ui.components.common.FormFieldProps
import com.bersihin.mobileapp.ui.navigation.Screen
import com.bersihin.mobileapp.ui.theme.BersihinTheme
import com.bersihin.mobileapp.utils.FormFieldValidator
import com.bersihin.mobileapp.utils.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(
        factory = ViewModelFactory(LocalContext.current)
    ),
    navController: NavController? = null,
    snackbarHostState: SnackbarHostState? = null,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    var isAllValid by rememberSaveable { mutableStateOf(false) }

    val validator = FormFieldValidator

    val loginSuccessMessage = stringResource(id = R.string.login_success)
    val loginFailedMessage = stringResource(id = R.string.login_failed)

    val tileSize = with(LocalDensity.current) {
        1500.dp.toPx()
    }

    val isLoading = viewModel.isLoading.collectAsState()

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
            errorMessageId = R.string.email_invalid,
            icon = Icons.Outlined.Email
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
            errorMessageId = R.string.password_invalid,
            icon = Icons.Outlined.Lock
        )
    )

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            Color(0xFF016b64)
                        ),
                        endY = tileSize,
                        tileMode = TileMode.Clamp
                    )
                )
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(vertical = 64.dp),
                text = stringResource(id = R.string.login),
                style = MaterialTheme.typography.titleLarge
            )

            props.forEach { prop ->
                FormField(props = prop)
            }

            Button(
                modifier = Modifier
                    .width(320.dp)
                    .height(80.dp)
                    .padding(top = 32.dp),
                onClick = {
                    scope.launch {
                        val isSuccess = viewModel.login(email, password)

                        scope.launch {
                            val message =
                                if (isSuccess) loginSuccessMessage else loginFailedMessage + viewModel.errorMessage
                            snackbarHostState?.showSnackbar(message)
                        }

                        if (isSuccess) {
                            navController?.navigate(
                                if (viewModel.userRole == "WORKER") Screen.WorkerHome.route
                                else Screen.UserHome.route
                            ) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }

                            ApiConfig.setAuthToken(viewModel.authToken)

                            Log.i("LoginScreen", "authToken: ${viewModel.authToken}")
                        }
                    }
                },
                enabled = isAllValid && !isLoading.value
            ) {
                if (isLoading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(4.dp)
                            .height(24.dp)
                            .width(24.dp)
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.login),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

            }

            Row(
                modifier = Modifier.padding(vertical = 32.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.dont_have_account),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
                ClickableText(
                    text = AnnotatedString(stringResource(id = R.string.register)),
                    onClick = { navController?.navigate(Screen.Register.route) },
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.secondary,
                        textDecoration = TextDecoration.Underline,
                    )
                )
            }
        }
    }

}


@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true)
fun PreviewLightMode() {
    BersihinTheme {
        Surface {
            LoginScreen()
        }
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
fun PreviewDarkMode() {
    BersihinTheme {
        Surface {
            LoginScreen()
        }
    }
}