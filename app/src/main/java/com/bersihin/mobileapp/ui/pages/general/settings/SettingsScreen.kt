package com.bersihin.mobileapp.ui.pages.general.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.preferences.settings.SettingsPreferences.Companion.EMAIL
import com.bersihin.mobileapp.preferences.settings.SettingsPreferences.Companion.FIRST_NAME
import com.bersihin.mobileapp.preferences.settings.SettingsPreferences.Companion.LAST_NAME
import com.bersihin.mobileapp.preferences.settings.SettingsPreferences.Companion.USER_ROLE
import com.bersihin.mobileapp.preferences.settings.SettingsViewModel
import com.bersihin.mobileapp.ui.components.common.DarkModeDropdown
import com.bersihin.mobileapp.ui.components.common.UserInfo
import com.bersihin.mobileapp.ui.components.common.UserInfoProps
import com.bersihin.mobileapp.ui.navigation.Screen
import com.bersihin.mobileapp.ui.theme.BersihinTheme
import com.bersihin.mobileapp.utils.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController = NavController(LocalContext.current),
    viewModel: SettingsViewModel = viewModel(
        factory = ViewModelFactory(context = LocalContext.current)
    ),
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val firstName = viewModel.getPrefValue(FIRST_NAME).collectAsState(initial = "John")
    val lastName = viewModel.getPrefValue(LAST_NAME).collectAsState(initial = "Doe")
    val email = viewModel.getPrefValue(EMAIL).collectAsState(initial = "johndoe@gmail.com")
    val userRole = viewModel.getPrefValue(USER_ROLE).collectAsState(initial = "WORKER")

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        UserInfo(
            props = UserInfoProps(
                firstName = firstName.value ?: "John",
                lastName = lastName.value ?: "Doe",
                email = email.value ?: "johndoe@gmail.com",
                userRole = userRole.value ?: "WORKER"
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.color_mode),
                style = MaterialTheme.typography.labelLarge
            )
            DarkModeDropdown()
        }

        Spacer(modifier = Modifier.height(16.dp))
        ElevatedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = {
                scope.launch {
                    viewModel.clearAuthInfo()
                    navController.navigate(Screen.Login.route)
                }
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onErrorContainer)
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.logout),
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.error
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    BersihinTheme {
        SettingsScreen()
    }
}