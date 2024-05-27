package pm.bam.mbc.remote.datasources

import pm.bam.mbc.remote.models.RemotePodcastEpisode
import pm.bam.mbc.remote.models.RemotePodcastEpisodeExternalLink
import pm.bam.mbc.remote.models.RemotePodcastEpisodeExternalLinkType

class RemotePodcastDataSourceImpl : RemotePodcastDataSource {

    override fun getAllPodcastEpisodes(): List<RemotePodcastEpisode> =
        listOf(
            RemotePodcastEpisode(
                id = 1,
                name = "Steve Bugeja",
                description = "Description 1",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/6514670741fad31755da03c7/scaled_768.jpg"),
                links = listOf(
                    RemotePodcastEpisodeExternalLink(
                        url = "https://example.com/episode1",
                        type = RemotePodcastEpisodeExternalLinkType.SPOTIFY
                    )
                ),
                duration = "1:00:00",
                releaseDate = "2024-03-01T19:00:00+0000"
            ),
            RemotePodcastEpisode(
                id = 2,
                name = "Larry Dean",
                description = "Description 2",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/65e61b47221ef91eed7e2435/scaled_768.jpg"),
                links = listOf(
                    RemotePodcastEpisodeExternalLink(
                        url = "https://example.com/episode2",
                        type = RemotePodcastEpisodeExternalLinkType.YOUTUBE
                    )
                ),
                duration = "1:30:00",
                releaseDate = "2024-04-01T19:00:00+0000"
            )
        )

}