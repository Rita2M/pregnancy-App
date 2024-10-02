/////*
//// * Copyright 2020 Google LLC
//// *
//// * Licensed under the Apache License, Version 2.0 (the "License");
//// * you may not use this file except in compliance with the License.
//// * You may obtain a copy of the License at
//// *
//// *     https://www.apache.org/licenses/LICENSE-2.0
//// *
//// * Unless required by applicable law or agreed to in writing, software
//// * distributed under the License is distributed on an "AS IS" BASIS,
//// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//// * See the License for the specific language governing permissions and
//// * limitations under the License.
//// */
////
//package com.nickmorus.pregnancyapp.screens.article
//
//import android.graphics.drawable.Drawable
//import android.widget.TextView
//import androidx.annotation.VisibleForTesting
//import androidx.compose.animation.core.Spring
//import androidx.compose.animation.core.animateFloat
//import androidx.compose.animation.core.spring
//import androidx.compose.animation.core.updateTransition
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.ScrollState
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.sizeIn
//import androidx.compose.foundation.layout.statusBarsPadding
//import androidx.compose.foundation.layout.systemBarsPadding
//import androidx.compose.foundation.layout.wrapContentSize
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Share
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.layout.LayoutModifier
//import androidx.compose.ui.layout.Measurable
//import androidx.compose.ui.layout.MeasureResult
//import androidx.compose.ui.layout.MeasureScope
//import androidx.compose.ui.layout.onGloballyPositioned
//import androidx.compose.ui.layout.positionInWindow
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.pluralStringResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.semantics.contentDescription
//import androidx.compose.ui.semantics.semantics
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.Constraints
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.text.HtmlCompat
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
//import com.bumptech.glide.integration.compose.GlideImage
//import com.bumptech.glide.load.DataSource
//import com.bumptech.glide.load.engine.GlideException
//import com.bumptech.glide.request.RequestListener
//import com.bumptech.glide.request.target.Target
//import com.nickmorus.pregnancyapp.R
//import com.nickmorus.pregnancyapp.data.entities.Article
//import com.nickmorus.pregnancyapp.data.entities.ArticleContent
//import com.nickmorus.pregnancyapp.data.entities.ArticleMeta
//import com.nickmorus.pregnancyapp.screens.article.ArticleDetailsScroller
//import com.nickmorus.pregnancyapp.screens.article.ArticleViewModel
//import com.nickmorus.pregnancyapp.screens.article.Dimens
//import com.nickmorus.pregnancyapp.screens.article.ToolbarState
//import com.nickmorus.pregnancyapp.screens.article.isShown
//
///**
// * As these callbacks are passed in through multiple Composables, to avoid having to name
// * parameters to not mix them up, they're aggregated in this class.
// */
//@Composable
//fun ArticleScreen(
//    plantDetailsViewModel: ArticleViewModel = hiltViewModel(),
//    onBackClick: () -> Unit,
//    onShareClick: () -> Unit,
//) {
//    val article by plantDetailsViewModel.article.collectAsState(null)
//    if (article != null) {
//        ArticleDetails(
//            article = article!!,
//            onBackClick = onBackClick,
//            onShareClick = onShareClick,
//        )
//    }
//}
//
//@VisibleForTesting
//@Composable
//fun ArticleDetails(
//    modifier: Modifier = Modifier,
//    articleMeta: ArticleMeta,
//    articleContent: ArticleContent,
//    onBackClick: () -> Unit,
//    onShareClick: () -> Unit
//
//) {
//    // articleDetails owns the scrollerPosition to simulate CollapsingToolbarLayout's behavior
//    val scrollState = rememberScrollState()
//    var plantScroller by remember { mutableStateOf(ArticleDetailsScroller(scrollState, Float.MIN_VALUE)) }
//    val transitionState = remember(plantScroller) { plantScroller.toolbarTransitionState }
//    val toolbarState = plantScroller.getToolbarState(LocalDensity.current)
//
//    // Transition that fades in/out the header with the image and the Toolbar
//    val transition = updateTransition(transitionState, label = "")
//    val toolbarAlpha = transition.animateFloat(
//        transitionSpec = { spring(stiffness = Spring.StiffnessLow) }, label = "")
//    { toolbarTransitionState ->
//        if (toolbarTransitionState == ToolbarState.HIDDEN) 0f else 1f
//    }
//    val contentAlpha = transition.animateFloat(
//        transitionSpec = { spring(stiffness = Spring.StiffnessLow) }, label = ""
//    ) { toolbarTransitionState ->
//        if (toolbarTransitionState == ToolbarState.HIDDEN) 1f else 0f
//    }
//
//    Box(modifier.fillMaxSize()) {
//        ArticleDetailsContent(
//            scrollState = scrollState,
//            toolbarState = toolbarState,
//            onNamePosition = { newNamePosition ->
//                // Comparing to Float.MIN_VALUE as we are just interested on the original
//                // position of name on the screen
//                if (plantScroller.namePosition == Float.MIN_VALUE) {
//                    plantScroller =
//                        plantScroller.copy(namePosition = newNamePosition)
//                }
//            },
//            article = article,
//            imageHeight = with(LocalDensity.current) {
//                val candidateHeight =
//                    Dimens.PlantDetailAppBarHeight
//                // FIXME: Remove this workaround when https://github.com/bumptech/glide/issues/4952
//                // is released
//                maxOf(candidateHeight, 1.dp)
//            },
//            contentAlpha = { contentAlpha.value }
//        )
//        ArticleToolbar(
//            toolbarState = toolbarState,
//            articleTitle = articleMeta.title,
//            onShareClick = onShareClick,
//            onBackClick = onBackClick,
//            toolbarAlpha = { toolbarAlpha.value },
//            contentAlpha = { contentAlpha.value }
//        )
//    }
//}
//
//@Composable
//private fun ArticleDetailsContent(
//    scrollState: ScrollState,
//    toolbarState: ToolbarState,
//    article: Article,
//    imageHeight: Dp,
//    onNamePosition: (Float) -> Unit,
//    contentAlpha: () -> Float,
//) {
//    Column(
//        Modifier.verticalScroll(scrollState))
//    {
//        Column {
//            ArticleImage(
//                imageUrl = article.image,
//                imageHeight = imageHeight,
//                modifier = Modifier.alpha(contentAlpha())
//            )
//
//            ArticleInformation(
//                name = article.title,
//                description = article.content,
//                onNamePosition = { onNamePosition(it) },
//                toolbarState = toolbarState
//            )
//        }
//    }
//}
//
//@OptIn(ExperimentalGlideComposeApi::class)
//@Composable
//private fun ArticleImage(
//    imageUrl: String,
//    imageHeight: Dp,
//    modifier: Modifier = Modifier,
//    placeholderColor: Color = MaterialTheme.colorScheme.onSurface.copy(0.2f)
//) {
//    var isLoading by remember { mutableStateOf(true) }
//    Box(
//        modifier
//            .fillMaxWidth()
//            .height(imageHeight)
//    ) {
//        if (isLoading) {
//            // TODO: Update this implementation once Glide releases a version
//            // that contains this feature: https://github.com/bumptech/glide/pull/4934
//            Box(
//                Modifier
//                    .fillMaxSize()
//                    .background(placeholderColor)
//            )
//        }
//        GlideImage(
//            model = imageUrl,
//            contentDescription = null,
//            modifier = Modifier
//                .fillMaxSize(),
//            contentScale = ContentScale.Crop,
//        ) {
//            it.addListener(object : RequestListener<Drawable> {
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Drawable>,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    isLoading = false
//                    return false
//                }
//
//                override fun onResourceReady(
//                    resource: Drawable,
//                    model: Any,
//                    target: Target<Drawable>?,
//                    dataSource: DataSource,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    isLoading = false
//                    return false
//                }
//            })
//        }
//    }
//}
//
//
//@Composable
//private fun ArticleToolbar(
//    toolbarState: ToolbarState,
//    articleTitle: String,
//    onShareClick: () -> Unit,
//    onBackClick: () -> Unit,
//    toolbarAlpha: () -> Float,
//    contentAlpha: () -> Float
//) {
//    if (toolbarState.isShown) {
//        ArticleDetailsToolbar(
//            articleTitle = articleTitle,
//            onBackClick = onBackClick,
//            onShareClick = onShareClick,
//            modifier = Modifier.alpha(toolbarAlpha())
//        )
//    } else {
//        ArticleHeaderActions(
//            onBackClick = onBackClick,
//            onShareClick = onShareClick,
//            modifier = Modifier.alpha(contentAlpha())
//        )
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun ArticleDetailsToolbar(
//    articleTitle: String,
//    onBackClick: () -> Unit,
//    onShareClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Surface {
//        TopAppBar(
//            modifier = modifier
//                .statusBarsPadding()
//                .background(color = MaterialTheme.colorScheme.surface),
//            title = {
//                Row {
//                    IconButton(
//                        onBackClick,
//                        Modifier.align(Alignment.CenterVertically)
//                    ) {
//                        Icon(
//                            Icons.Filled.ArrowBack,
//                            contentDescription = "back"
//                        )
//                    }
//                    Text(
//                        text = articleTitle,
//                        style = MaterialTheme.typography.titleLarge,
//                        // As title in TopAppBar has extra inset on the left, need to do this: b/158829169
//                        modifier = Modifier
//                            .weight(1f)
//                            .fillMaxSize()
//                            .wrapContentSize(Alignment.Center)
//                    )
//                    val shareContentDescription = "share"
//                    IconButton(
//                        onShareClick,
//                        Modifier
//                            .align(Alignment.CenterVertically)
//                            // Semantics in parent due to https://issuetracker.google.com/184825850
//                            .semantics { contentDescription = shareContentDescription }
//                    ) {
//                        Icon(
//                            Icons.Filled.Share,
//                            contentDescription = null
//                        )
//                    }
//                }
//            }
//        )
//    }
//}
//
//@Composable
//private fun ArticleHeaderActions(
//    onBackClick: () -> Unit,
//    onShareClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Row(
//        modifier = modifier
//            .fillMaxSize()
//            .systemBarsPadding()
//            .padding(top = Dimens.ToolbarIconPadding),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        val iconModifier = Modifier
//            .sizeIn(
//                maxWidth = Dimens.ToolbarIconSize,
//                maxHeight = Dimens.ToolbarIconSize
//            )
//            .background(
//                color = MaterialTheme.colorScheme.surface,
//                shape = CircleShape
//            )
//
//        IconButton(
//            onClick = onBackClick,
//            modifier = Modifier
//                .padding(start = Dimens.ToolbarIconPadding)
//                .then(iconModifier)
//        ) {
//            Icon(
//                Icons.Filled.ArrowBack,
//                contentDescription = "back"
//            )
//        }
//        val shareContentDescription = "content"
//        IconButton(
//            onClick = onShareClick,
//            modifier = Modifier
//                .padding(end = Dimens.ToolbarIconPadding)
//                .then(iconModifier)
//                // Semantics in parent due to https://issuetracker.google.com/184825850
//                .semantics {
//                    contentDescription = shareContentDescription
//                }
//        ) {
//            Icon(
//                Icons.Filled.Share,
//                contentDescription = null
//            )
//        }
//    }
//}
//
//@Composable
//private fun ArticleInformation(
//    name: String,
//    description: String,
//    onNamePosition: (Float) -> Unit,
//    toolbarState: ToolbarState,
//    modifier: Modifier = Modifier
//) {
//    Column(modifier = modifier.padding(Dimens.PaddingLarge)) {
//        Text(
//            text = name,
//            style = MaterialTheme.typography.displaySmall,
//            modifier = Modifier
//                .padding(
//                    start = Dimens.PaddingSmall,
//                    end = Dimens.PaddingSmall,
//                    bottom = Dimens.PaddingNormal
//                )
//                .align(Alignment.CenterHorizontally)
//                .onGloballyPositioned { onNamePosition(it.positionInWindow().y) }
//                .visible { toolbarState == ToolbarState.HIDDEN }
//        )
//        PlantDescription(description)
//    }
//}
//
//@Composable
//private fun PlantDescription(description: String) {
//    AndroidView(
//        modifier = Modifier.fillMaxSize(),
//        factory = { TextView(it) },
//        update = { it.text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT) }
//    )
//}
//
//fun Modifier.visible(isVisible: () -> Boolean) = this.then(VisibleModifier(isVisible))
//private data class VisibleModifier(
//    private val isVisible: () -> Boolean
//) : LayoutModifier {
//    override fun MeasureScope.measure(
//        measurable: Measurable,
//        constraints: Constraints
//    ): MeasureResult {
//        val placeable = measurable.measure(constraints)
//        return layout(placeable.width, placeable.height) {
//            if (isVisible()) {
//                placeable.place(0, 0)
//            }
//        }
//    }
//}
