package com.bersihin.mobileapp.ui.pages.general.register

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockPerson
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.bersihin.mobileapp.api.Response
import com.bersihin.mobileapp.ui.components.common.FormField
import com.bersihin.mobileapp.ui.components.common.FormFieldProps
import com.bersihin.mobileapp.ui.theme.BersihinTheme
import com.bersihin.mobileapp.utils.FormFieldValidator
import com.bersihin.mobileapp.utils.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = viewModel(
        factory = ViewModelFactory(LocalContext.current)
    ),
    navController: NavController? = null,
    snackbarHostState: SnackbarHostState? = null,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    val registerWorkerSuccessMessage = stringResource(id = R.string.register_success_worker)
    val registerUserSuccessMessage = stringResource(id = R.string.register_success_user)
    val registerFailedMessage = stringResource(id = R.string.register_failed)

    val publicReporter = stringResource(id = R.string.public_reporter)
    val environmentalWorker = stringResource(id = R.string.environmental_worker)

    val roles = arrayOf(
        publicReporter, environmentalWorker
    )
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var selectedRole by rememberSaveable { mutableStateOf(roles[0]) }

    var isAllValid by rememberSaveable { mutableStateOf(false) }

    val validator = FormFieldValidator

    val tileSize = with(LocalDensity.current) {
        2000.dp.toPx()
    }

    val isLoading = viewModel.isLoading.collectAsState()

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
            errorMessageId = R.string.first_name_invalid,
            icon = Icons.Outlined.Person
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
            errorMessageId = R.string.last_name_invalid,
            icon = Icons.Default.Person
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
            errorMessageId = R.string.confirm_password_invalid,
            icon = Icons.Outlined.LockPerson
        )
    )
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxHeight()
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(vertical = 64.dp),
                text = stringResource(id = R.string.register),
                style = MaterialTheme.typography.titleLarge
            )

            props.forEach { prop ->
                FormField(props = prop)
            }

            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.registering_as),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(end = 16.dp)
                )
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it }
                ) {
                    TextField(
                        value = selectedRole,
                        onValueChange = {},
                        readOnly = true,
                        textStyle = MaterialTheme.typography.labelMedium,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                        modifier = Modifier.menuAnchor(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false })
                    {
                        roles.forEach { role ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = role,
                                        style = MaterialTheme.typography.labelMedium,
                                    )
                                },
                                onClick = {
                                    selectedRole = role
                                    isExpanded = false
                                })
                        }
                    }
                }
            }

            Button(
                modifier = Modifier
                    .width(320.dp)
                    .height(80.dp)
                    .padding(top = 32.dp),
                onClick = {
                    scope.launch {
                        val response = viewModel.register(
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            password = password,
                            role = if (selectedRole == publicReporter) "USER" else "WORKER"
                        )

                        val isSuccess = response is Response.Success

                        scope.launch {
                            val message = if (isSuccess) {
                                if (selectedRole == publicReporter) {
                                    registerUserSuccessMessage
                                } else {
                                    registerWorkerSuccessMessage
                                }
                            } else {
                                registerFailedMessage + (response as Response.Error).errorMessage
                            }

                            snackbarHostState?.showSnackbar(message)
                        }

                        if (isSuccess) {
                            navController?.navigateUp()
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
                        text = stringResource(id = R.string.register),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Row(
                modifier = Modifier.padding(vertical = 32.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.already_have_account),
                    style = MaterialTheme.typography.labelLarge
                )
                ClickableText(
                    text = AnnotatedString(stringResource(id = R.string.login)),
                    onClick = { navController?.navigateUp() },
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.secondary,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
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
            RegisterScreen()
        }
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
fun PreviewDarkMode() {
    BersihinTheme {
        Surface {
            RegisterScreen()
        }
    }
}