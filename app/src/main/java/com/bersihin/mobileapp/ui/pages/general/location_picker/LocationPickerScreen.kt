package com.bersihin.mobileapp.ui.pages.general.location_picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bersihin.mobileapp.ui.pages.user.report_upload.ReportUploadViewModel
import com.bersihin.mobileapp.utils.ViewModelFactory
import com.bersihin.mobileapp.utils.rememberMapViewWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Composable
fun LocationPickerScreen(
    navController: NavHostController,
    viewModel: ReportUploadViewModel = viewModel(
        factory = ViewModelFactory(LocalContext.current)
    )
) {
    var position by rememberSaveable { mutableStateOf<LatLng?>(null) }
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle()

    val address = viewModel.address.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        AndroidView({ mapView }, modifier = Modifier.fillMaxHeight(0.8f)) { mapView ->
            mapView.getMapAsync { map ->

                map.uiSettings.isZoomControlsEnabled = true
                map.uiSettings.isCompassEnabled = true

                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            viewModel.latitude,
                            viewModel.longitude
                        ), 15f
                    )
                )
                map.setOnMapClickListener { latLng ->
                    position = latLng
                    viewModel.updateLatLng(position!!, context)
                    map.clear()
                    map.addMarker(MarkerOptions().position(latLng))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Location: ${address.value}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 24.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            )

            ElevatedButton(
                onClick = {
                    viewModel.updateLatLng(position!!, context)
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(60.dp)
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Confirm Location",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }


    }
}