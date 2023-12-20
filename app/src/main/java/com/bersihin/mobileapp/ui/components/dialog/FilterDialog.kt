package com.bersihin.mobileapp.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.ui.theme.BersihinTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    onApply: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    onSliderChange: (Int) -> Unit = {},
    onDropdownClick: (String) -> Unit = {}
) {
    val sortType = listOf("Location", "Date")
    var selectedSortType by rememberSaveable { mutableStateOf(sortType[0]) }
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var sortByDate by rememberSaveable { mutableStateOf(false) }
    var sliderPosition by rememberSaveable { mutableFloatStateOf(10f) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.sort_by),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    ExposedDropdownMenuBox(
                        expanded = isExpanded,
                        onExpandedChange = { isExpanded = it }
                    ) {
                        TextField(
                            value = selectedSortType,
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
                            sortType.forEach { role ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = role,
                                            style = MaterialTheme.typography.labelMedium,
                                        )
                                    },
                                    onClick = {
                                        selectedSortType = role
                                        onDropdownClick(role)
                                        sortByDate = selectedSortType == "Date"
                                        isExpanded = false
                                    })
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Radius: ", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.width(16.dp))
                    Slider(
                        modifier = Modifier.width(150.dp),
                        value = sliderPosition,
                        enabled = !sortByDate,
                        onValueChange = {
                            sliderPosition = it
                            onSliderChange(it.toInt())
                        },
                        valueRange = 5f..25f,
                        colors = SliderDefaults.colors(
                            inactiveTrackColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        "${sliderPosition.toInt()} km",
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Right
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                ElevatedButton(onClick = { onApply() }, modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.apply_changes),
                        style = MaterialTheme.typography.labelLarge
                    )
                }


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FilterDialogPreview() {
    BersihinTheme {
        FilterDialog()
    }
}
