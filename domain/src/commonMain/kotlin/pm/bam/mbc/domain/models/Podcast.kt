package pm.bam.mbc.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Podcast(
    val id: Long,
    val name: String,
    val description: String,
    val images: List<String>,
    val links: List<Link>
)