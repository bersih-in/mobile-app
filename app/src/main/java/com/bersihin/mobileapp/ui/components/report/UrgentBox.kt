package com.bersihin.mobileapp.ui.components.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.ui.theme.BersihinTheme


@Composable
fun UrgentBox(
    urgent: Boolean
) {
    val bgColor = if (urgent) {
        Color(0Xffdb0000)
    } else {
        Color(0xFF949494)
    }

    val text = stringResource(
        if (urgent) R.string.urgent else R.string.not_urgent
    )


    Row(
        modifier = Modifier
            .padding(4.dp)
            .clip(
                shape = RoundedCornerShape(18.dp)
            )
            .width(150.dp)
            .height(30.dp)
            .background(color = bgColor),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = text.uppercase(),
            style = MaterialTheme.typography.labelMedium.copy(
                color = Color.White,
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UrgentBoxPreview() {
    BersihinTheme {
        Column {
            UrgentBox(urgent = true)
            UrgentBox(urgent = false)
        }

    }
}