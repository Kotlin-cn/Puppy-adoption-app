/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.model.MALE
import com.example.androiddevchallenge.model.Pet
import com.example.androiddevchallenge.model.PetRepo
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.utils.Navigator
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlin.math.max
import kotlin.math.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp(onBackPressedDispatcher)
            }
        }
    }
}

// Start building your app here !
@Composable
fun MyApp(backDispatcher: OnBackPressedDispatcher) {
    val navigator: Navigator<Destination> = rememberSaveable(
        saver = Navigator.saver(backDispatcher)
    ) {
        Navigator(Destination.Home, backDispatcher)
    }
    val actions = remember(navigator) { Actions(navigator) }

    Crossfade(navigator.current) { destination ->
        when (destination) {
            Destination.Home -> PetHome(actions.openPet)
            is Destination.PetDetail -> PetDetail(
                petId = destination.id,
                upPress = actions.upPress
            )
        }
    }
}

@Composable
fun PetHome(openPet: (Long) -> Unit) {
    val pets = remember { PetRepo.getPets() }
    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.primary)
        ) {
            Text(
                textAlign = TextAlign.Start,
                text = "Lovely Dogs!",
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onPrimary),
                fontSize = 18.sp,
            )
        }

        LazyColumn(modifier = Modifier) {
            itemsIndexed(pets) { index, pet ->
                PetItem(
                    item = pet,
                    onItemClick = openPet,
                )
            }
        }
    }
}

@Composable
fun PetItem(
    item: Pet,
    onItemClick: (Long) -> Unit
) {
    Card(
        elevation = 0.dp,
        modifier = Modifier
            .clickable(onClick = { onItemClick(item.id) })
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .height(56.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .heightIn(min = 56.dp)
                .padding(end = 16.dp)
        ) {
            CoilImage(
                fadeIn = true,
                data = item.imageUrl,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(56.dp)
                    .width(56.dp)
                    .clip(shape = RoundedCornerShape(4.dp)),
            )
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(0.dp)
                )
                Text(
                    text = formatAge(item.age),
                    style = MaterialTheme.typography.caption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(0.dp)
                )
            }
        }
    }
}

// -------- for details page --------

private val TitleHeight = 128.dp
private val GradientScroll = 180.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 300.dp
private val CollapsedImageSize = 150.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)

@Composable
fun PetDetail(petId: Long, upPress: () -> Unit) {
    val item = remember(petId) { PetRepo.getPet(petId) }
    Box(Modifier.fillMaxSize()) {
        val scroll = rememberScrollState(0)
        Header()
        Body(item, scroll)
        Title(item, scroll.value)
        Image(item, scroll.value)
        Up(upPress)
    }
}

@Composable
private fun Header() {
    Spacer(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    arrayListOf(
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.secondary
                    )
                )
            )
    )
}

@Composable
private fun Up(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .size(36.dp)
            .background(
                color = MaterialTheme.colors.primary,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.ArrowBack,
            tint = MaterialTheme.colors.onPrimary,
            contentDescription = "Back"
        )
    }
}

@Composable
private fun Body(
    item: Pet,
    scroll: ScrollState
) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(MinTitleOffset)
        )
        Column(
            modifier = Modifier.verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(GradientScroll))
            Surface(Modifier.fillMaxWidth()) {
                Column {
                    Spacer(Modifier.height(ImageOverlap))
                    Spacer(Modifier.height(TitleHeight))

                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.details),
                        style = MaterialTheme.typography.overline,
                        color = MaterialTheme.colors.primaryVariant,
                        modifier = HzPadding
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.height, item.height),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onBackground,
                        modifier = HzPadding
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.weight, item.weight),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onBackground,
                        modifier = HzPadding
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.breed, item.breed),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onBackground,
                        modifier = HzPadding
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = item.desc,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onBackground,
                        modifier = HzPadding
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.skills),
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.primaryVariant,
                        modifier = HzPadding
                    )

                    LazyRow(modifier = HzPadding) {
                        itemsIndexed(item.skills.toList()) { index, skill ->
                            Text(
                                text = if (index == item.skills.size - 1) skill else "$skill,",
                                style = MaterialTheme.typography.body2,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(end = 6.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.dogs_introduction),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSecondary,
                        modifier = HzPadding
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun Title(item: Pet, scroll: Int) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }
    val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .heightIn(min = TitleHeight)
            .graphicsLayer { translationY = offset }
            .background(color = MaterialTheme.colors.background)
    ) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = item.name,
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.secondary,
            modifier = HzPadding
        )
        Text(
            text = "Sex: ${formatGender(item.gender)}",
            style = MaterialTheme.typography.caption,
            fontSize = 16.sp,
            color = MaterialTheme.colors.onBackground,
            modifier = HzPadding
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Age: ${formatAge(item.age)}",
            style = MaterialTheme.typography.caption,
            fontSize = 16.sp,
            color = MaterialTheme.colors.onBackground,
            modifier = HzPadding
        )

        Spacer(Modifier.height(8.dp))
        Divider()
    }
}

fun formatGender(gender: Int): String {
    return if (gender == MALE) {
        "Male"
    } else {
        "Female"
    }
}

@Composable
private fun Image(
    item: Pet,
    scroll: Int
) {
    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFraction = (scroll / collapseRange).coerceIn(0f, 1f)

    CollapsingImageLayout(
        collapseFraction = collapseFraction,
        modifier = HzPadding
    ) {
        CoilImage(
            fadeIn = true,
            data = item.imageUrl,
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(4.dp)),
        )
    }
}

@Composable
private fun CollapsingImageLayout(
    collapseFraction: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 1)

        val imageMaxSize = min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = max(CollapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth =
            lerp(imageMaxSize.toDp(), imageMinSize.toDp(), collapseFraction).roundToPx()
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

        val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction).roundToPx()
        val imageX = lerp(
            ((constraints.maxWidth - imageWidth) / 2).toDp(), // centered when expanded
            (constraints.maxWidth - imageWidth).toDp(), // right aligned when collapsed
            collapseFraction
        ).roundToPx()
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ) {
            imagePlaceable.place(imageX, imageY)
        }
    }
}

fun formatAge(age: Int): String {
    val year = age / 12
    val month = age % 12
    if (year == 0) {
        if (month == 1) {
            return "1 Month"
        } else {
            return "$month Months"
        }
    } else if (year == 1) {
        if (month == 0) {
            return "1 Year"
        } else if (month == 1) {
            return "1 Year 1 Month"
        } else {
            return "1 Year $month Months"
        }
    } else {
        if (month == 0) {
            return "$year Years"
        } else if (month == 1) {
            return "$year Years 1 Month"
        } else {
            return "$year Years $month Months"
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        PetHome(openPet = { })
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        PetHome(openPet = { })
    }
}

@Preview("Detail")
@Composable
private fun PetDetailPreview() {
    MyTheme {
        PetDetail(
            petId = 1L,
            upPress = { }
        )
    }
}

@Preview("Detail • Dark")
@Composable
private fun PetDetailDarkPreview() {
    MyTheme(darkTheme = true) {
        PetDetail(
            petId = 1L,
            upPress = { }
        )
    }
}
