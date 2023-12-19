package com.bersihin.mobileapp.ui.components.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.bersihin.mobileapp.models.Report
import com.bersihin.mobileapp.utils.rememberMapViewWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

@Composable
fun ListMapView(
    modifier: Modifier = Modifier,
    reports: List<Report>
) {
    val mapView = rememberMapViewWithLifecycle()
    AndroidView(
        { mapView },
        modifier = modifier
    ) { mapView ->
        mapView.getMapAsync { map ->
            val builder = LatLngBounds.builder()

            reports.forEach {
                val latLng = LatLng(
                    it.latitude,
                    it.longitude
                )


                val marker = map.addMarker(
                    MarkerOptions().position(
                        latLng
                    ).title(it.title)
                )

                if (reports.size == 1) {
                    marker?.showInfoWindow()
                }

                builder.include(latLng)
            }

            val bounds = builder.build()
            val padding = 50
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(
                bounds,
                padding
            )

            map.moveCamera(cameraUpdate)
        }
    }
}