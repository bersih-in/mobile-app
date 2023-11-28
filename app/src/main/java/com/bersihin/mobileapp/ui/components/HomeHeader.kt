package com.bersihin.mobileapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bersihin.mobileapp.ui.theme.BersihinTheme

data class HomeHeaderProps(
    val firstName: String,
    val lastName: String,
    val description: String,
)

@Composable
fun HomeHeader(
    props: HomeHeaderProps
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "Hello, ${props.firstName} ${props.lastName} 👋",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 24.sp,
                lineHeight = 32.sp,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = props.description,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun HomeHeaderPreview() {
    BersihinTheme {
        HomeHeader(
            props = HomeHeaderProps(
                firstName = "John",
                lastName = "Doe",
                description = "Welcome to Bersihin!"
            )
        )
    }
}