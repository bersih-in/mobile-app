package com.bersihin.mobileapp.ui.pages.general.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class SplashScreenCardProps(
    val title: String,
    val description: String,
    val emoji: String
)

@Composable
fun SplashScreenCard(
    props: SplashScreenCardProps
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0x44FFFFFF),
        contentColor = Color.White,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
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
                Text(
                    text = props.title, style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
                Text(
                    text = props.description,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
            }

        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SplashScreen() {
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
        SplashScreenCardProps(
            title = "Welcome to bersih.in!",
            description = "Start helping your community by reporting trash around you!",
            emoji = "👋"
        ),
        SplashScreenCardProps(
            title = "Title 2",
            description = "Description 2",
            emoji = "Emoji 2"
        ),
        SplashScreenCardProps(
            title = "Title 3",
            description = "Description 3",
            emoji = "Emoji 3"
        )
    )

    Column {
        HorizontalPager(
            count = 3,
            state = pagerState, modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.secondary
                        ),
//                        endX = with(LocalDensity.current) {
//                            500.dp.toPx()
//                        },
                        tileMode = TileMode.Clamp
                    )
                )
        ) { page ->
            SplashScreenCard(props[page])
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier.align(Alignment.End)
        )
    }

}

