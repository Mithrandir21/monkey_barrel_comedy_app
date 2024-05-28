package pm.bam.mbc.common.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Internal set so that only CustomSpaces can be accessed via MonkeyTheme.spaces

internal val extraSmallSpace: Dp = 2.dp
internal val smallSpace: Dp = 4.dp
internal val mediumSpace: Dp = 8.dp
internal val largeSpace: Dp = 16.dp
internal val extraLargeSpace: Dp = 40.dp



@Immutable
data class CustomSpaces(
    val extraSmall: Dp = extraSmallSpace,
    val small: Dp = smallSpace,
    val medium: Dp = mediumSpace,
    val large: Dp = largeSpace,
    val extraLarge: Dp = extraLargeSpace,
)