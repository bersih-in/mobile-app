package com.bersihin.mobileapp.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.ui.theme.BersihinTheme
import com.bersihin.mobileapp.utils.getColorFromString

data class UserInfoProps(
    val firstName: String,
    val lastName: String,
    val email: String,
    val userRole: String
)

fun getInitials(firstName: String, lastName: String): String {
    var initial = ""

    if (firstName.isNotEmpty()) {
        initial += firstName.first().toString().uppercase()
    }

    if (lastName.isNotEmpty()) {
        initial += lastName.first().toString().uppercase()
    }

    return initial
}

@Composable
fun UserInfo(
    props: UserInfoProps
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(shape = CircleShape)
                .background(
                    Color(getColorFromString(props.firstName + " " + props.lastName))
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = getInitials(props.firstName, props.lastName),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        Column {
            Text(
                text = "${props.firstName} ${props.lastName}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = props.email,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal
                ),
            )
            Text(
                text = stringResource(
                    id = if (props.userRole == "WORKER") {
                        R.string.environmental_worker
                    } else {
                        R.string.public_reporter
                    }
                ),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserInfoPreview() {
    BersihinTheme {
        UserInfo(
            props = UserInfoProps(
                firstName = "Owen",
                lastName = "Wijaya",
                email = "johndoe@gmail.com",
                userRole = "WORKER"
            )
        )
    }
}
