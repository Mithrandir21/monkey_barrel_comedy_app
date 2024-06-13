package pm.bam.mbc.domain.models

import kotlinx.serialization.Serializable


@Serializable
data class Link(
    val url: String,
    val type: LinkType
)


enum class LinkType {
    INSTAGRAM,
    FACEBOOK,
    TWITTER,
    YOUTUBE,
    SPOTIFY,
    WEBSITE,
    SOUNDCLOUD
}


@Serializable
enum class Categories {
    COMEDY,
    STANDUP,
    MUSICAL
}