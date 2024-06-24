package pm.bam.mbc.feature.shows.ui.shows

import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import monkeybarrelcomey.feature.shows.generated.resources.Res
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_search_filter_dates_range_dialog_negative_label
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_search_filter_dates_range_dialog_positive_label
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun datePicker(
    searchConfig: ShowsSearchConfig
) {
    val dateState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = searchConfig.existingSearchParameters.dateTimeRange?.first,
        initialSelectedEndDateMillis = searchConfig.existingSearchParameters.dateTimeRange?.second,
        selectableDates = FutureOnlySelectableDates
    )

    DatePickerDialog(
        onDismissRequest = { searchConfig.onShowDatePickerChanged(false) },
        confirmButton = {
            Button(onClick = {
                dateState.selectedStartDateMillis?.let { searchConfig.onStartDateTimeChanged(it) }
                dateState.selectedEndDateMillis?.let { searchConfig.onEndDateTimeChanged(it) }

                searchConfig.onShowDatePickerChanged(false)
            }) {
                Text(text = stringResource(Res.string.show_screen_search_filter_dates_range_dialog_positive_label))
            }
        },
        dismissButton = {
            Button(onClick = { searchConfig.onShowDatePickerChanged(false) }) {
                Text(text = stringResource(Res.string.show_screen_search_filter_dates_range_dialog_negative_label))
            }
        },
        content = {
            DateRangePicker(
                modifier = Modifier.weight(1f), // https://issuetracker.google.com/issues/325107799
                state = dateState
            )
        }
    )
}

@ExperimentalMaterial3Api
object FutureOnlySelectableDates : SelectableDates {
    private val now = Clock.System.now()
    override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis >= now.toEpochMilliseconds()
    override fun isSelectableYear(year: Int): Boolean = year >= now.toLocalDateTime(TimeZone.UTC).year
}