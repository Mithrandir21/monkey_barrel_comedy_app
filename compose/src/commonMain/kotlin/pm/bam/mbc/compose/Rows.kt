package pm.bam.mbc.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import monkeybarrelcomey.common.generated.resources.image_placeholder
import monkeybarrelcomey.compose.generated.resources.Res
import monkeybarrelcomey.compose.generated.resources.artist_image_content_description
import monkeybarrelcomey.compose.generated.resources.show_image_content_description
import monkeybarrelcomey.compose.generated.resources.show_venue_label
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pm.bam.mbc.compose.theme.MonkeyCustomTheme
import pm.bam.mbc.domain.models.Artist
import pm.bam.mbc.domain.models.Show


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
            contentDescription = stringResource(Res.string.artist_image_content_description, artist.name),
            contentScale = ContentScale.Fit,
            error = painterResource(monkeybarrelcomey.common.generated.resources.Res.drawable.image_placeholder),
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
                text = artist.name,
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
            contentDescription = stringResource(Res.string.show_image_content_description, show.name),
            contentScale = ContentScale.Fit,
            error = painterResource(monkeybarrelcomey.common.generated.resources.Res.drawable.image_placeholder),
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
                text = show.startDate,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MonkeyCustomTheme.spacing.small),
                textAlign = TextAlign.Start,
                text = stringResource(Res.string.show_venue_label, show.venue)
            )
        }
    }
}
