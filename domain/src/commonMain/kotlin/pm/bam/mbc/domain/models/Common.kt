package pm.bam.mbc.domain.models

import kotlinx.serialization.Serializable
import pm.bam.mbc.remote.models.RemoteCategories
import pm.bam.mbc.remote.models.RemoteLink
import pm.bam.mbc.remote.models.RemoteLinkType


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
    MUSICAL,
    FRINGE
}

internal fun RemoteCategories.toCategory(): Categories = when (this) {
    RemoteCategories.COMEDY -> Categories.COMEDY
    RemoteCategories.STANDUP -> Categories.STANDUP
    RemoteCategories.MUSICAL -> Categories.MUSICAL
    RemoteCategories.FRINGE -> Categories.FRINGE
}

internal fun RemoteLinkType.toLinkType(): LinkType = when (this) {
    RemoteLinkType.INSTAGRAM -> LinkType.INSTAGRAM
    RemoteLinkType.FACEBOOK -> LinkType.FACEBOOK
    RemoteLinkType.TWITTER -> LinkType.TWITTER
    RemoteLinkType.YOUTUBE -> LinkType.YOUTUBE
    RemoteLinkType.SPOTIFY -> LinkType.SPOTIFY
    RemoteLinkType.WEBSITE -> LinkType.WEBSITE
    RemoteLinkType.SOUNDCLOUD -> LinkType.SOUNDCLOUD
}

internal fun RemoteLink.toLink(): Link = Link(
    url = url,
    type = type.toLinkType()
)