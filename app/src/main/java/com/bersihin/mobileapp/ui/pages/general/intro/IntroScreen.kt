package com.bersihin.mobileapp.ui.pages.general.intro

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.ui.navigation.Screen
import com.bersihin.mobileapp.ui.theme.BersihinTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class IntroScreenCardProps(
    val title: String,
    val description: String,
//    val emoji: String
    val imageId: Int
)

@Composable
fun IntroScreenCard(
    props: IntroScreenCardProps
) {
    Surface(
        shape = RoundedCornerShape(32.dp),
        color = Color(0x44FFFFFF),
        contentColor = Color.White,
        modifier = Modifier
            .padding(32.dp)
            .fillMaxHeight(0.75f)
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
    ) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = props.imageId),
                    contentDescription = null,
                    modifier = Modifier.height(150.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = props.title, style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = props.description,
                    style = TextStyle(
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }

        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun IntroScreen(
    navController: NavController? = null
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        scope.launch {
            while (true) {
                delay(3000L)
                pagerState.animateScrollToPage((pagerState.currentPage + 1) % pagerState.pageCount)
            }
        }
    }


    val props = listOf(
        IntroScreenCardProps(
            title = "Welcome to bersih.in!",
            description = "Start helping your community by reporting trash around you!",
//            emoji = "👋"
            imageId = R.drawable.slide1
        ),
        IntroScreenCardProps(
            title = "Start reporting!",
            description = "Whenever you see a trash pile, report it and we'll take care of it!",
            imageId = R.drawable.slide2
        ),
        IntroScreenCardProps(
            title = "More efficient cleaning!",
            description = "Find nearest pile reports and help clean them up!",
            imageId = R.drawable.slide3
        )
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.secondary
                    ),
                    tileMode = TileMode.Clamp
                )
            )
    ) {
        HorizontalPager(
            count = 3,
            state = pagerState,
        ) { page ->
            IntroScreenCard(props[page])
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
        )

        Spacer(modifier = Modifier.height(32.dp))


        ElevatedButton(
            onClick = {
                navController?.navigate(Screen.Login.route)
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(
                text = stringResource(id = R.string.get_started),
                style = MaterialTheme.typography.labelLarge
            )
        }

    }
}

@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun IntroScreenCardPreview() {
    BersihinTheme {
        IntroScreen()
    }
}

