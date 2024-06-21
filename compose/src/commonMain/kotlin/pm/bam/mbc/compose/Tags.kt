package pm.bam.mbc.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import pm.bam.mbc.compose.theme.MonkeyCustomTheme
import pm.bam.mbc.domain.models.Categories
import pm.bam.mbc.domain.models.Categories.COMEDY
import pm.bam.mbc.domain.models.Categories.FRINGE
import pm.bam.mbc.domain.models.Categories.MUSICAL
import pm.bam.mbc.domain.models.Categories.STANDUP
import pm.bam.mbc.domain.models.Show

@Composable
fun ShowTags(
    modifier: Modifier = Modifier,
    show: Show
) {
    Row(modifier = modifier) {
        show.categories?.forEach { category ->
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(MonkeyCustomTheme.spacing.small)
                    .background(color = category.showTagColor(), shape = RoundedCornerShape(MonkeyCustomTheme.spacing.medium))
                    .padding(horizontal = MonkeyCustomTheme.spacing.small, vertical = MonkeyCustomTheme.spacing.extraSmall),
                textAlign = TextAlign.Start,
                text = category.name,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun Categories.showTagColor() = when (this) {
    COMEDY -> MonkeyCustomTheme.colors.showTagsComedy
    STANDUP -> MonkeyCustomTheme.colors.showTagsStandup
    MUSICAL -> MonkeyCustomTheme.colors.showTagsMusical
    FRINGE -> MonkeyCustomTheme.colors.showTagsFringe
}