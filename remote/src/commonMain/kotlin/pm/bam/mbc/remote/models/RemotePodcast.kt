package pm.bam.mbc.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class RemotePodcast(
    val id: Long,
    val name: String,
    val description: String,
    val images: List<String>,
    val links: List<RemoteLink>
)