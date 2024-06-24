package pm.bam.mbc.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import monkeybarrelcomey.common.generated.resources.artist_image_content_description
import monkeybarrelcomey.common.generated.resources.image_placeholder
import monkeybarrelcomey.common.generated.resources.news_image_content_description
import monkeybarrelcomey.common.generated.resources.show_image_content_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pm.bam.mbc.common.datetime.formatting.DateTimeFormatter
import pm.bam.mbc.compose.theme.MonkeyCustomTheme
import pm.bam.mbc.domain.models.Artist
import pm.bam.mbc.domain.models.EventStatus
import pm.bam.mbc.domain.models.Merch
import pm.bam.mbc.domain.models.News
import pm.bam.mbc.domain.models.PodcastEpisode
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.models.ShowSchedule
import monkeybarrelcomey.common.generated.resources.Res as CommonRes


@Composable
fun NewsRow(
    modifier: Modifier = Modifier,
    news: News,
    onViewNewsItem: (newsId: Long) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onViewNewsItem(news.id) }
            .padding(vertical = MonkeyCustomTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = news.images.firstOrNull(),
            contentDescription = stringResource(CommonRes.string.news_image_content_description, news.title),
            contentScale = ContentScale.Fit,
            error = painterResource(CommonRes.drawable.image_placeholder),
            modifier = Modifier
                .padding(MonkeyCustomTheme.spacing.small)
                .height(60.dp)
                .width(100.dp)
                .clip(RoundedCornerShape(MonkeyCustomTheme.spacing.extraSmall))
        )
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MonkeyCustomTheme.spacing.small),
                textAlign = TextAlign.Start,
                text = news.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun PodcastEpisodeRow(
    modifier: Modifier = Modifier,
    podcastEpisode: PodcastEpisode,
    onViewPodcastEpisode: (podcastEpisodeId: Long) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onViewPodcastEpisode(podcastEpisode.id) }
            .padding(vertical = MonkeyCustomTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = podcastEpisode.images.firstOrNull(),
            contentDescription = stringResource(CommonRes.string.artist_image_content_description, podcastEpisode.name),
            contentScale = ContentScale.Fit,
            error = painterResource(CommonRes.drawable.image_placeholder),
            modifier = Modifier
                .padding(MonkeyCustomTheme.spacing.small)
                .height(60.dp)
                .width(100.dp)
                .clip(RoundedCornerShape(MonkeyCustomTheme.spacing.extraSmall))
        )
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MonkeyCustomTheme.spacing.small),
                textAlign = TextAlign.Start,
                text = podcastEpisode.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun ArtistRow(
    modifier: Modifier = Modifier,
    artist: Artist,
    onViewArtist: (artistId: Long) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onViewArtist(artist.id) }
            .padding(vertical = MonkeyCustomTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = artist.images.firstOrNull(),
            contentDescription = stringResource(CommonRes.string.artist_image_content_description, artist.getFullName()),
            contentScale = ContentScale.Fit,
            error = painterResource(CommonRes.drawable.image_placeholder),
            modifier = Modifier
                .padding(MonkeyCustomTheme.spacing.small)
                .height(60.dp)
                .width(100.dp)
                .clip(RoundedCornerShape(MonkeyCustomTheme.spacing.extraSmall))
        )
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MonkeyCustomTheme.spacing.small),
                textAlign = TextAlign.Start,
                text = artist.getFullName(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun ShowRow(
    modifier: Modifier = Modifier,
    show: Show,
    onShowSelected: (showId: Long) -> Unit,
    dateTimeFormatter: DateTimeFormatter
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onShowSelected(show.id) }
            .padding(vertical = MonkeyCustomTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = show.images.firstOrNull(),
            contentDescription = stringResource(CommonRes.string.show_image_content_description, show.name),
            contentScale = ContentScale.Fit,
            error = painterResource(CommonRes.drawable.image_placeholder),
            modifier = Modifier
                .padding(MonkeyCustomTheme.spacing.small)
                .height(60.dp)
                .width(100.dp)
                .clip(RoundedCornerShape(MonkeyCustomTheme.spacing.extraSmall))
        )
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MonkeyCustomTheme.spacing.small),
                textAlign = TextAlign.Start,
                text = show.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MonkeyCustomTheme.spacing.small),
                textAlign = TextAlign.Start,
                text = dateTimeFormatter.formatDateTimesFromTo(show.schedule.first().start, show.schedule.last().end),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            ShowTags(show = show)
        }
    }
}

@Composable
fun ShowScheduleRow(
    modifier: Modifier = Modifier,
    show: Show,
    showSchedule: ShowSchedule,
    onShowSelected: (showId: Long) -> Unit,
) {
    val cancelled = showSchedule.status == EventStatus.CANCELLED

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onShowSelected(show.id) }
            .padding(vertical = MonkeyCustomTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .background(Color.Black)
                .padding(MonkeyCustomTheme.spacing.small)
                .width(80.dp)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.wrapContentSize(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Thin,
                text = showSchedule.start.date.dayOfMonth.toString(),
            )
            Text(
                modifier = Modifier.wrapContentSize(Alignment.TopCenter),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                text = showSchedule.start.month.name.take(3)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f).padding(horizontal = MonkeyCustomTheme.spacing.small),
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = MonkeyCustomTheme.spacing.small),
                    textAlign = TextAlign.Start,
                    text = show.name,
                    style = LocalTextStyle.current.let {
                        if (cancelled) it.copy(textDecoration = TextDecoration.LineThrough) else it
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.padding(horizontal = MonkeyCustomTheme.spacing.small),
                    textAlign = TextAlign.Start,
                    text = showSchedule.venue.name,
                    style = MaterialTheme.typography.labelSmall.let {
                        if (cancelled) it.copy(textDecoration = TextDecoration.LineThrough) else it
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (showSchedule.status != EventStatus.ACTIVE) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = MonkeyCustomTheme.spacing.large)
                        .border(2.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(4.dp))
                        .padding(MonkeyCustomTheme.spacing.medium),
                ) {
                    Text(
                        modifier = Modifier.wrapContentSize(),
                        textAlign = TextAlign.Start,
                        text = showSchedule.status.name,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        modifier = Modifier.wrapContentSize(),
                        textAlign = TextAlign.Start,
                        text = showSchedule.statusNote ?: "",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun MerchRow(
    modifier: Modifier = Modifier,
    merch: Merch,
    onMerchSelected: (merchId: Long) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onMerchSelected(merch.id) }
            .padding(vertical = MonkeyCustomTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = merch.images.firstOrNull(),
            contentDescription = stringResource(CommonRes.string.artist_image_content_description, merch.name),
            contentScale = ContentScale.Fit,
            error = painterResource(CommonRes.drawable.image_placeholder),
            modifier = Modifier
                .padding(MonkeyCustomTheme.spacing.small)
                .height(60.dp)
                .width(100.dp)
                .clip(RoundedCornerShape(MonkeyCustomTheme.spacing.extraSmall))
        )
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MonkeyCustomTheme.spacing.small),
                textAlign = TextAlign.Start,
                text = merch.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
