package com.bersihin.mobileapp.ui.components.common

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.preferences.settings.SettingsPreferences.Companion.COLOR_MODE
import com.bersihin.mobileapp.preferences.settings.SettingsViewModel
import com.bersihin.mobileapp.utils.ColorMode
import com.bersihin.mobileapp.utils.ViewModelFactory

//import com.bersihin.mobileapp.preferences.settings.AppTheme
//import com.bersihin.mobileapp.preferences.settings.SettingsPreferences


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DarkModeDropdown(
    settingsViewModel: SettingsViewModel = viewModel(
        factory = ViewModelFactory(context = LocalContext.current)
    )
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val modes = listOf(
        stringResource(id = R.string.use_system_default),
        stringResource(id = R.string.dark_mode),
        stringResource(id = R.string.light_mode),
    )

    val values = listOf(
        ColorMode.AUTO,
        ColorMode.DARK,
        ColorMode.LIGHT,
    )

    var selectedMode by rememberSaveable { mutableStateOf(modes[0]) }
    val colorMode =
        settingsViewModel.getPrefValue(COLOR_MODE).collectAsState(initial = ColorMode.AUTO.mode)

    LaunchedEffect(Unit) {
        Log.i("DarkModeDropdown", "colorMode: ${colorMode.value}")
        selectedMode = modes[
            values.indexOf(ColorMode.valueOf(colorMode.value ?: ColorMode.AUTO.mode))
        ]
    }

    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(0.8f),
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it }
    ) {
        TextField(
            value = selectedMode,
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
            modes.forEach { mode ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = mode,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    },
                    onClick = {
                        selectedMode = mode
                        settingsViewModel.setColorMode(
                            values[modes.indexOf(mode)]
                        )
                        isExpanded = false
                    })
            }
        }
    }
}
