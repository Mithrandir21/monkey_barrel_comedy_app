package pm.bam.mbc.remote.datasources

import pm.bam.mbc.remote.models.RemotePodcast
import pm.bam.mbc.remote.models.RemotePodcastEpisode
import pm.bam.mbc.remote.models.RemotePodcastEpisodeExternalLink
import pm.bam.mbc.remote.models.RemotePodcastEpisodeExternalLinkType

class RemotePodcastDataSourceImpl : RemotePodcastDataSource {

    override fun getAllPodcasts(): List<RemotePodcast> =
        listOf(
            RemotePodcast(
                id = 1,
                name = "The Monkey Barrel Comedy Chat Show",
                description = "Welcome to The Monkey Barrel Comedy Chat Show, where conversations flow like Michael Parkinson meandering down a gentle river on a velvet canoe. Join our host, George Fox (@ComedyFox), regular MC at Monkey Barrel Comedy, “the best comedy club in the world” (Olga Koch), as he invites top comics to join him for a chat.\n" +
                        "On each episode of The Monkey Barrel Comedy Chat Show, George will be joined by Edinburgh Fringe favourites, top touring comedians, and local heroes for a lively chat about stand up, life, and everything in between. He’ll also be asking his guests questions set by other comedians and, even more importantly, by our listeners! So if you’ve ever been itching to cross-examine your favourite comic, be sure to follow our Instagram - @monkey.barrel.comedy – to get involved.\n" +
                        "Every episode is recorded in Monkey Barrel Comedy’s state of the art podcast recording studio in Edinburgh, shortly before George hosts The Big Show, Monkey Barrel’s flagship weekend comedy show, featuring the finest comedy talent in the country. The Big Show takes place every Friday (7PM, 9PM), Saturday (5PM, 7PM, 9PM), and Sunday (7PM, 9PM). Visit our show page to book tickets and follow us on social media (@monkey.barrel.comedy) to check out our line-ups ahead of time.",
                images = listOf("https://storage.buzzsprout.com/fe036jgl409idtpmf7otf0e64pb0?.jpg"),
                links = listOf(
                    RemotePodcastEpisodeExternalLink(
                        url = "https://example.com/podcast1",
                        type = RemotePodcastEpisodeExternalLinkType.SPOTIFY
                    )
                )
            ),
            RemotePodcast(
                id = 2,
                name = "The Other Show Podcast",
                description = "The Other Show Podcast is hosted by Giulia Galastro and Ross Foley. The live show takes place on the first and third Saturdays of the month at Monkey Barrel Comedy in Edinburgh. This accompanying podcast takes a deeper dive into the workings of live stand up and alternative comedy. Both Ross and Giulia bear their souls and share some tip top comedy secrets that most comics would love to learn. Join them in the Monkey Barrel Studio as they talk shop and interview some really interesting (but also funny) guests. Additional content by Cobin Millage. Produced by Monkey Barrel Comedy.",
                images = listOf("https://storage.buzzsprout.com/367h5whbvn87324wxbehdhe8oqqe?.jpg"),
                links = listOf(
                    RemotePodcastEpisodeExternalLink(
                        url = "https://example.com/podcast2",
                        type = RemotePodcastEpisodeExternalLinkType.SPOTIFY
                    )
                )
            )
        )

    override fun getAllPodcastEpisodes(): List<RemotePodcastEpisode> =
        listOf(
            RemotePodcastEpisode(
                id = 1,
                name = "Steve Bugeja",
                description = "Description 1",
                images = listOf("https://storage.buzzsprout.com/fe036jgl409idtpmf7otf0e64pb0?.jpg"),
                links = listOf(
                    RemotePodcastEpisodeExternalLink(
                        url = "https://example.com/episode1",
                        type = RemotePodcastEpisodeExternalLinkType.SPOTIFY
                    )
                ),
                duration = "1:00:00",
                releaseDate = "2024-03-01T19:00:00+0000",
                podcastId = 1,
                showId = 8,
                artistId = 6,
            ),
            RemotePodcastEpisode(
                id = 2,
                name = "Larry Dean",
                description = "Description 2",
                images = listOf("https://storage.buzzsprout.com/fe036jgl409idtpmf7otf0e64pb0?.jpg"),
                links = listOf(
                    RemotePodcastEpisodeExternalLink(
                        url = "https://example.com/episode2",
                        type = RemotePodcastEpisodeExternalLinkType.YOUTUBE
                    )
                ),
                duration = "1:30:00",
                releaseDate = "2024-04-01T19:00:00+0000",
                podcastId = 1,
                showId = 12,
                artistId = 10,
            ),
            RemotePodcastEpisode(
                id = 3,
                name = "Marc Jennings",
                description = "Description 3",
                images = listOf("https://storage.buzzsprout.com/fe036jgl409idtpmf7otf0e64pb0?.jpg"),
                links = listOf(
                    RemotePodcastEpisodeExternalLink(
                        url = "https://example.com/episode3",
                        type = RemotePodcastEpisodeExternalLinkType.YOUTUBE
                    )
                ),
                duration = "1:30:00",
                releaseDate = "2024-04-02T19:00:00+0000",
                podcastId = 1,
                showId = 4,
                artistId = 2,
            ),
            RemotePodcastEpisode(
                id = 4,
                name = "Sam Lake",
                description = "Description 4",
                images = listOf("https://storage.buzzsprout.com/fe036jgl409idtpmf7otf0e64pb0?.jpg"),
                links = listOf(
                    RemotePodcastEpisodeExternalLink(
                        url = "https://example.com/episode4",
                        type = RemotePodcastEpisodeExternalLinkType.YOUTUBE
                    )
                ),
                duration = "1:30:00",
                releaseDate = "2024-04-03T19:00:00+0000",
                podcastId = 1,
                showId = 6,
                artistId = 4,
            ),
            RemotePodcastEpisode(
                id = 5,
                name = "Amy Matthews",
                description = "Description 5",
                images = listOf("https://storage.buzzsprout.com/fe036jgl409idtpmf7otf0e64pb0?.jpg"),
                links = listOf(
                    RemotePodcastEpisodeExternalLink(
                        url = "https://example.com/episode5",
                        type = RemotePodcastEpisodeExternalLinkType.YOUTUBE
                    )
                ),
                duration = "1:30:00",
                releaseDate = "2024-04-04T19:00:00+0000",
                podcastId = 1,
                showId = 7,
                artistId = 5,
            ),
            RemotePodcastEpisode(
                id = 6,
                name = "Pierre Novellie",
                description = "Description 6",
                images = listOf("https://storage.buzzsprout.com/fe036jgl409idtpmf7otf0e64pb0?.jpg"),
                links = listOf(
                    RemotePodcastEpisodeExternalLink(
                        url = "https://example.com/episode6",
                        type = RemotePodcastEpisodeExternalLinkType.YOUTUBE
                    )
                ),
                duration = "1:30:00",
                releaseDate = "2024-04-05T19:00:00+0000",
                podcastId = 1,
                showId = 11,
                artistId = 9,
            ),
            RemotePodcastEpisode(
                id = 7,
                name = "Amelia Bayler",
                description = "Description 7",
                images = listOf("https://storage.buzzsprout.com/367h5whbvn87324wxbehdhe8oqqe?.jpg"),
                links = listOf(
                    RemotePodcastEpisodeExternalLink(
                        url = "https://example.com/episode7",
                        type = RemotePodcastEpisodeExternalLinkType.YOUTUBE
                    )
                ),
                duration = "1:23:00",
                releaseDate = "2024-04-01T19:00:00+0000",
                podcastId = 2
            ),
            RemotePodcastEpisode(
                id = 8,
                name = "Emer McGinnity",
                description = "Description 8",
                images = listOf("https://storage.buzzsprout.com/367h5whbvn87324wxbehdhe8oqqe?.jpg"),
                links = listOf(
                    RemotePodcastEpisodeExternalLink(
                        url = "https://example.com/episode8",
                        type = RemotePodcastEpisodeExternalLinkType.YOUTUBE
                    )
                ),
                duration = "1:30:00",
                releaseDate = "2024-04-02T19:00:00+0000",
                podcastId = 2
            ),
            RemotePodcastEpisode(
                id = 9,
                name = "Phil O'Shea",
                description = "Description 9",
                images = listOf("https://storage.buzzsprout.com/367h5whbvn87324wxbehdhe8oqqe?.jpg"),
                links = listOf(
                    RemotePodcastEpisodeExternalLink(
                        url = "https://example.com/episode9",
                        type = RemotePodcastEpisodeExternalLinkType.YOUTUBE
                    )
                ),
                duration = "1:12:00",
                releaseDate = "2024-04-03T19:00:00+0000",
                podcastId = 2
            )
        )
}