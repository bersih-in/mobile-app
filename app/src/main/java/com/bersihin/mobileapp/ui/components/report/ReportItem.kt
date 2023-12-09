package com.bersihin.mobileapp.ui.components.report

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bersihin.mobileapp.ui.theme.BersihinTheme
import com.bersihin.mobileapp.utils.ReportStatus

data class ReportItemProps(
    val id: String,
    val title: String,
    val description: String,
    val status: ReportStatus
)

@Composable
fun ReportItem(
    modifier: Modifier = Modifier,
    props: ReportItemProps,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(8.dp))
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = MaterialTheme.colorScheme.secondary,
                spotColor = MaterialTheme.colorScheme.secondary
            )
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.height(50.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = props.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                    ),
                    maxLines = 2
                )
            }

            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                StatusBox(status = props.status)
            }
        }

        Spacer(modifier = modifier.height(8.dp))

        Text(
            text = props.description,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            modifier = Modifier.heightIn(min = 24.dp, max = 48.dp)
        )
    }

    Spacer(modifier = modifier.height(16.dp))
}


@Preview(showBackground = true)
@Composable
fun LightModePreview() {
    BersihinTheme {
        ReportItem(
            props = ReportItemProps(
                id = "1",
                title = "Banjir",
                description = "Banjir di daerah Cipinang",
                status = ReportStatus.IN_PROGRESS
            )
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DarkModePreview() {
    BersihinTheme {
        ReportItem(
            props = ReportItemProps(
                id = "1",
                title = "Banjir Banjir Banjir Banjir Banjir",
                description = "Banjir di daerah Cipinang",
                status = ReportStatus.IN_PROGRESS
            )
        )
    }
}