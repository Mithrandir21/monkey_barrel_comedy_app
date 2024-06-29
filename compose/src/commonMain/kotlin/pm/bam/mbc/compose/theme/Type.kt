package pm.bam.mbc.compose.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import monkeybarrelcomey.compose.generated.resources.Res
import monkeybarrelcomey.compose.generated.resources.noto_sans_black
import monkeybarrelcomey.compose.generated.resources.noto_sans_black_italic
import monkeybarrelcomey.compose.generated.resources.noto_sans_bold
import monkeybarrelcomey.compose.generated.resources.noto_sans_bold_italic
import monkeybarrelcomey.compose.generated.resources.noto_sans_extra_bold
import monkeybarrelcomey.compose.generated.resources.noto_sans_extra_bold_italic
import monkeybarrelcomey.compose.generated.resources.noto_sans_extra_light
import monkeybarrelcomey.compose.generated.resources.noto_sans_extra_light_italic
import monkeybarrelcomey.compose.generated.resources.noto_sans_light
import monkeybarrelcomey.compose.generated.resources.noto_sans_light_italic
import monkeybarrelcomey.compose.generated.resources.noto_sans_medium
import monkeybarrelcomey.compose.generated.resources.noto_sans_medium_italic
import monkeybarrelcomey.compose.generated.resources.noto_sans_regular
import monkeybarrelcomey.compose.generated.resources.noto_sans_regular_italic
import monkeybarrelcomey.compose.generated.resources.noto_sans_semi_bold
import monkeybarrelcomey.compose.generated.resources.noto_sans_semi_bold_italic
import monkeybarrelcomey.compose.generated.resources.noto_sans_thin
import monkeybarrelcomey.compose.generated.resources.noto_sans_thin_italic
import org.jetbrains.compose.resources.Font


@Composable
fun NotoFontFamily() = FontFamily(
    Font(Res.font.noto_sans_thin, weight = FontWeight.Thin, style = FontStyle.Normal),
    Font(Res.font.noto_sans_thin_italic, weight = FontWeight.Thin, style = FontStyle.Italic),

    Font(Res.font.noto_sans_extra_light, weight = FontWeight.ExtraLight, style = FontStyle.Normal),
    Font(Res.font.noto_sans_extra_light_italic, weight = FontWeight.ExtraLight, style = FontStyle.Italic),

    Font(Res.font.noto_sans_light, weight = FontWeight.Light, style = FontStyle.Normal),
    Font(Res.font.noto_sans_light_italic, weight = FontWeight.Light, style = FontStyle.Italic),

    Font(Res.font.noto_sans_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(Res.font.noto_sans_regular_italic, weight = FontWeight.Normal, style = FontStyle.Italic),

    Font(Res.font.noto_sans_medium, weight = FontWeight.Medium, style = FontStyle.Normal),
    Font(Res.font.noto_sans_medium_italic, weight = FontWeight.Medium, style = FontStyle.Italic),

    Font(Res.font.noto_sans_semi_bold, weight = FontWeight.SemiBold, style = FontStyle.Normal),
    Font(Res.font.noto_sans_semi_bold_italic, weight = FontWeight.SemiBold, style = FontStyle.Italic),

    Font(Res.font.noto_sans_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
    Font(Res.font.noto_sans_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),

    Font(Res.font.noto_sans_extra_bold, weight = FontWeight.ExtraBold, style = FontStyle.Normal),
    Font(Res.font.noto_sans_extra_bold_italic, weight = FontWeight.ExtraBold, style = FontStyle.Italic),

    Font(Res.font.noto_sans_black, weight = FontWeight.Black, style = FontStyle.Normal),
    Font(Res.font.noto_sans_black_italic, weight = FontWeight.Black, style = FontStyle.Italic),
)

@Composable
fun NotoTypography(): Typography = Typography().run {
    val fontFamily = NotoFontFamily()

    return copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily)
    )
}