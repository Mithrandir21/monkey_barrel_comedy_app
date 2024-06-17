package pm.bam.mbc.feature.shows.ui.shows

import androidx.annotation.OpenForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import monkeybarrelcomey.feature.shows.generated.resources.Res
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_search_filter_categories_label
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_search_filter_categories_label_description
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_search_filter_exact_match_label
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_search_filter_price_range_label
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_search_filter_sort_by_label
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_search_filter_sort_by_label_description
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_search_filter_venues_label
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_search_filter_venues_label_description
import org.jetbrains.compose.resources.stringResource
import pm.bam.mbc.compose.theme.MonkeyCustomTheme
import pm.bam.mbc.domain.models.Categories
import pm.bam.mbc.domain.models.ShowSearchParameters
import pm.bam.mbc.domain.models.ShowSearchSortBy
import pm.bam.mbc.domain.models.ShowSearchSortByDirection
import pm.bam.mbc.domain.models.ShowVenues
import pm.bam.mbc.domain.models.SortDirection
import kotlin.math.roundToInt


internal data class ShowsSearchConfig(
    val existingSearchParameters: ShowSearchParameters,
    val showFilters: Boolean,
    val onShowFiltersChanged: (showFilters: Boolean) -> Unit,
    val onSortByChanged: (sortBy: ShowSearchSortByDirection) -> Unit,
    val onPriceChanged: (from: Int, to: Int) -> Unit,
    val onClearPrice: () -> Unit,
    val onVenuesChanged: (venue: ShowVenues, selected: Boolean) -> Unit,
    val onCategoriesChanged: (category: Categories, selected: Boolean) -> Unit,
    val onExactMatch: (exactMatch: Boolean) -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchFilters(searchConfig: ShowsSearchConfig) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (searchConfig.showFilters) {
        ModalBottomSheet(
            onDismissRequest = { searchConfig.onShowFiltersChanged(false) },
            sheetState = modalBottomSheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Filters(searchConfig)
        }
    }
}

@Composable
private fun Filters(searchConfig: ShowsSearchConfig) {
    val priceLowest = SearchFilterMinPrice
    val priceHighest = SearchFilterMaxPrice
    val priceRange = priceLowest..priceHighest

    val existingLowest = searchConfig.existingSearchParameters.priceRange?.first?.toFloat() ?: priceLowest
    val existingHighest = searchConfig.existingSearchParameters.priceRange?.second?.toFloat() ?: priceLowest
    val existingPriceRange = existingLowest..existingHighest

    var priceSliderValue by rememberSaveable(stateSaver = floatRangeSaver) { mutableStateOf(existingPriceRange) }
    var titleExactMatch by rememberSaveable { mutableStateOf(searchConfig.existingSearchParameters.titleExact ?: false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MonkeyCustomTheme.spacing.large)
            .navigationBarsPadding()
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(Res.string.show_screen_search_filter_price_range_label), Modifier.weight(1f))
            Text(text = rangeString(priceSliderValue.start, priceSliderValue.endInclusive, priceHighest))
        }
        RangeSlider(
            value = priceSliderValue,
            steps = SearchFilterPriceSteps,
            onValueChange = { range -> priceSliderValue = range },
            valueRange = priceRange,
            onValueChangeFinished = {
                when (priceSliderValue.start == priceSliderValue.endInclusive) {
                    true -> searchConfig.onClearPrice()
                    false -> searchConfig.onPriceChanged(priceSliderValue.start.roundToInt(), priceSliderValue.endInclusive.roundToInt())
                }
            },
        )

        HorizontalDivider()

        chipsSortBy(searchConfig)

        HorizontalDivider()

        chipsVenue(searchConfig)

        HorizontalDivider()

        chipsCategories(searchConfig)

        HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = MonkeyCustomTheme.spacing.large),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(Res.string.show_screen_search_filter_exact_match_label), Modifier.weight(1f))
            Switch(
                checked = titleExactMatch,
                onCheckedChange = {
                    titleExactMatch = it
                    searchConfig.onExactMatch(it)
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
@Suppress("kotlin:S3776")
private fun chipsSortBy(searchConfig: ShowsSearchConfig) {
    Text(
        modifier = Modifier.fillMaxWidth().padding(top = MonkeyCustomTheme.spacing.large),
        text = stringResource(Res.string.show_screen_search_filter_sort_by_label)
    )

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = MonkeyCustomTheme.spacing.small),
        horizontalArrangement = Arrangement.spacedBy(MonkeyCustomTheme.spacing.medium)
    ) {
        ShowSearchSortBy.entries.forEach { sortBy ->
            val selected = searchConfig.existingSearchParameters.sortBy.sortBy == sortBy
            val icon = if (!selected) Icons.Filled.ArrowDownward else when (searchConfig.existingSearchParameters.sortBy.direction) {
                SortDirection.TOP_TO_BOTTOM -> Icons.Filled.ArrowUpward
                SortDirection.BOTTOM_TO_TOP -> Icons.Filled.ArrowDownward
            }
            FilterChip(
                onClick = {
                    searchConfig.onSortByChanged(
                        if (selected) {
                            ShowSearchSortByDirection(
                                sortBy, when (searchConfig.existingSearchParameters.sortBy.direction) {
                                    SortDirection.TOP_TO_BOTTOM -> SortDirection.BOTTOM_TO_TOP
                                    SortDirection.BOTTOM_TO_TOP -> SortDirection.TOP_TO_BOTTOM
                                }
                            )
                        } else {
                            ShowSearchSortByDirection(sortBy, SortDirection.BOTTOM_TO_TOP)
                        }
                    )
                },
                label = { Text(text = sortBy.name) },
                selected = selected,
                leadingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(Res.string.show_screen_search_filter_sort_by_label_description),
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun chipsVenue(searchConfig: ShowsSearchConfig) {
    Text(
        modifier = Modifier.fillMaxWidth().padding(top = MonkeyCustomTheme.spacing.large),
        text = stringResource(Res.string.show_screen_search_filter_venues_label)
    )
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = MonkeyCustomTheme.spacing.small),
        verticalArrangement = Arrangement.spacedBy(MonkeyCustomTheme.spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(MonkeyCustomTheme.spacing.medium)
    ) {
        ShowVenues.entries.forEach { venue ->
            val selected = searchConfig.existingSearchParameters.venues.contains(venue)
            FilterChip(
                onClick = { searchConfig.onVenuesChanged(venue, !selected) },
                label = { Text(text = venue.name) },
                selected = selected,
                leadingIcon = {
                    if (selected) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = stringResource(Res.string.show_screen_search_filter_venues_label_description),
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun chipsCategories(searchConfig: ShowsSearchConfig) {
    Text(
        modifier = Modifier.fillMaxWidth().padding(top = MonkeyCustomTheme.spacing.large),
        text = stringResource(Res.string.show_screen_search_filter_categories_label)
    )
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = MonkeyCustomTheme.spacing.small),
        horizontalArrangement = Arrangement.spacedBy(MonkeyCustomTheme.spacing.medium)
    ) {
        Categories.entries.forEach { category ->
            val selected = searchConfig.existingSearchParameters.categories.contains(category)
            FilterChip(
                onClick = { searchConfig.onCategoriesChanged(category, !selected) },
                label = { Text(text = category.name) },
                selected = selected,
                leadingIcon = {
                    if (selected) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = stringResource(Res.string.show_screen_search_filter_categories_label_description),
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                },
            )
        }
    }
}


@OpenForTesting
internal fun rangeString(startValue: Float, endInclusiveValue: Float, highestValue: Float): String =
    "${startValue.roundToInt()} - ${endInclusiveValue.roundToInt()}"
        .plus(if (endInclusiveValue == highestValue) "+" else "")


/** Saving mechanism for [ShowSearchParameters] into [rememberSaveable]. */
internal val parametersSaver = run {
    mapSaver(
        save = { it.asMap() },
        restore = { ShowSearchParameters.from(it) }
    )
}

/** Saving mechanism for [ClosedFloatingPointRange<Float>] into [rememberSaveable]. */
private val floatRangeSaver = listSaver<ClosedFloatingPointRange<Float>, Any>(
    save = { listOf(it.start, it.endInclusive) },
    restore = { (it[0] as Float)..(it[1] as Float) }
)

internal const val SearchFilterMinPrice = 0f
internal const val SearchFilterMaxPrice = 40f
internal const val SearchFilterPriceSteps = SearchFilterMaxPrice.toInt()