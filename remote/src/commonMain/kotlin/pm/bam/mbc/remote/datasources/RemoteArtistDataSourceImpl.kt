package pm.bam.mbc.remote.datasources

import pm.bam.mbc.remote.models.RemoteArtist
import pm.bam.mbc.remote.models.RemoteArtistExternalLink
import pm.bam.mbc.remote.models.RemoteArtistExternalLinkType

class RemoteArtistDataSourceImpl : RemoteArtistDataSource {

    override fun getAllArtists(): List<RemoteArtist> =
        listOf(
            RemoteArtist(
                id = 1,
                name = "Luisa Omielan",
                description = "Description",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/65394e5d37d1085efdb7f551/scaled_768.jpg"),
                genres = listOf("Comedy", "Standup"),
                showsIds = listOf(3),
                podcastsIds = emptyList(),
                externalLinks = listOf(
                    RemoteArtistExternalLink("https://www.instagram.com/luisaomielan/", RemoteArtistExternalLinkType.INSTAGRAM),
                    RemoteArtistExternalLink("https://www.facebook.com/luisaomielan", RemoteArtistExternalLinkType.FACEBOOK),
                    RemoteArtistExternalLink("https://twitter.com/luisaomielan", RemoteArtistExternalLinkType.TWITTER),
                    RemoteArtistExternalLink("https://www.youtube.com/user/luisaomielan", RemoteArtistExternalLinkType.YOUTUBE),
                    RemoteArtistExternalLink("https://open.spotify.com/artist/5vZ1Zz2v6v9Z3w1ZP8Zf2V", RemoteArtistExternalLinkType.SPOTIFY),
                    RemoteArtistExternalLink("https://www.luisaomielan.com/", RemoteArtistExternalLinkType.WEBSITE)
                )
            ),
            RemoteArtist(
                id = 2,
                name = "Marc Jennings",
                description = "Description",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/65f6e307cb58760cffce27d8/scaled_768.jpg"),
                genres = listOf("Comedy", "Standup"),
                showsIds = listOf(1, 2, 4),
                podcastsIds = emptyList(),
                externalLinks = listOf(
                    RemoteArtistExternalLink("https://www.instagram.com/marcjennings/", RemoteArtistExternalLinkType.INSTAGRAM),
                    RemoteArtistExternalLink("https://www.facebook.com/marcjennings", RemoteArtistExternalLinkType.FACEBOOK),
                    RemoteArtistExternalLink("https://twitter.com/marcjennings", RemoteArtistExternalLinkType.TWITTER),
                    RemoteArtistExternalLink("https://www.youtube.com/user/marcjennings", RemoteArtistExternalLinkType.YOUTUBE),
                    RemoteArtistExternalLink("https://open.spotify.com/artist/5vZ1Zz2v6v9Z3w1ZP8Zf2V", RemoteArtistExternalLinkType.SPOTIFY),
                    RemoteArtistExternalLink("https://www.marcjennings.com/", RemoteArtistExternalLinkType.WEBSITE)
                )
            ),
            RemoteArtist(
                id = 3,
                name = "Vittorio Angelone",
                description = "Description",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/64ff19a560b4d9491b6329ac/scaled_768.jpg"),
                genres = listOf("Comedy", "Standup"),
                showsIds = listOf(1, 5),
                podcastsIds = emptyList(),
                externalLinks = listOf(
                    RemoteArtistExternalLink("https://www.instagram.com/vittorioangelone/", RemoteArtistExternalLinkType.INSTAGRAM),
                    RemoteArtistExternalLink("https://www.facebook.com/vittorioangelone", RemoteArtistExternalLinkType.FACEBOOK),
                    RemoteArtistExternalLink("https://twitter.com/vittorioangelone", RemoteArtistExternalLinkType.TWITTER),
                    RemoteArtistExternalLink("https://www.youtube.com/user/vittorioangelone", RemoteArtistExternalLinkType.YOUTUBE),
                    RemoteArtistExternalLink("https://open.spotify.com/artist/5vZ1Zz2v6v9Z3w1ZP8Zf2V", RemoteArtistExternalLinkType.SPOTIFY),
                    RemoteArtistExternalLink("https://www.vittorioangelone.com/", RemoteArtistExternalLinkType.WEBSITE)
                )
            ),
            RemoteArtist(
                id = 4,
                name = "Sam Lake",
                description = "Description",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/65ee4734e318f152be0264c7/scaled_768.jpg"),
                genres = listOf("Comedy", "Standup"),
                showsIds = listOf(6),
                podcastsIds = emptyList(),
                externalLinks = listOf(
                    RemoteArtistExternalLink("https://www.instagram.com/samlake/", RemoteArtistExternalLinkType.INSTAGRAM),
                    RemoteArtistExternalLink("https://www.facebook.com/samlake", RemoteArtistExternalLinkType.FACEBOOK),
                    RemoteArtistExternalLink("https://twitter.com/samlake", RemoteArtistExternalLinkType.TWITTER),
                    RemoteArtistExternalLink("https://www.youtube.com/user/samlake", RemoteArtistExternalLinkType.YOUTUBE),
                    RemoteArtistExternalLink("https://open.spotify.com/artist/5vZ1Zz2v6v9Z3w1ZP8Zf2V", RemoteArtistExternalLinkType.SPOTIFY),
                    RemoteArtistExternalLink("https://www.samlake.com/", RemoteArtistExternalLinkType.WEBSITE)
                )
            ),
            RemoteArtist(
                id = 5,
                name = "Amy Matthews",
                description = "Description",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/65e60d82221ef91eed7e133f/scaled_768.jpg"),
                genres = listOf("Comedy", "Standup"),
                showsIds = listOf(1, 7),
                podcastsIds = emptyList(),
                externalLinks = listOf(
                    RemoteArtistExternalLink("https://www.instagram.com/amymatthews/", RemoteArtistExternalLinkType.INSTAGRAM),
                    RemoteArtistExternalLink("https://www.facebook.com/amymatthews", RemoteArtistExternalLinkType.FACEBOOK),
                    RemoteArtistExternalLink("https://twitter.com/amymatthews", RemoteArtistExternalLinkType.TWITTER),
                    RemoteArtistExternalLink("https://www.youtube.com/user/amymatthews", RemoteArtistExternalLinkType.YOUTUBE),
                    RemoteArtistExternalLink("https://open.spotify.com/artist/5vZ1Zz2v6v9Z3w1ZP8Zf2V", RemoteArtistExternalLinkType.SPOTIFY),
                    RemoteArtistExternalLink("https://www.amymatthews.com/", RemoteArtistExternalLinkType.WEBSITE)
                )
            ),
            RemoteArtist(
                id = 6,
                name = "Steve Bugeja",
                description = "Description",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/6514670741fad31755da03c7/scaled_768.jpg"),
                genres = listOf("Comedy", "Standup"),
                showsIds = listOf(2, 8),
                podcastsIds = listOf(1),
                externalLinks = listOf(
                    RemoteArtistExternalLink("https://www.instagram.com/stevebugeja/", RemoteArtistExternalLinkType.INSTAGRAM),
                    RemoteArtistExternalLink("https://www.facebook.com/stevebugeja", RemoteArtistExternalLinkType.FACEBOOK),
                    RemoteArtistExternalLink("https://twitter.com/stevebugeja", RemoteArtistExternalLinkType.TWITTER),
                    RemoteArtistExternalLink("https://www.youtube.com/user/stevebugeja", RemoteArtistExternalLinkType.YOUTUBE),
                    RemoteArtistExternalLink("https://open.spotify.com/artist/5vZ1Zz2v6v9Z3w1ZP8Zf2V", RemoteArtistExternalLinkType.SPOTIFY),
                    RemoteArtistExternalLink("https://www.stevebugeja.com/", RemoteArtistExternalLinkType.WEBSITE)
                )
            ),
            RemoteArtist(
                id = 7,
                name = "Laura Lexx",
                description = "Description",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/65bbb1219785342c8514328a/scaled_768.jpg"),
                genres = listOf("Comedy", "Standup"),
                showsIds = listOf(2, 9),
                podcastsIds = emptyList(),
                externalLinks = listOf(
                    RemoteArtistExternalLink("https://www.instagram.com/lauralexx/", RemoteArtistExternalLinkType.INSTAGRAM),
                    RemoteArtistExternalLink("https://www.facebook.com/lauralexx", RemoteArtistExternalLinkType.FACEBOOK),
                    RemoteArtistExternalLink("https://twitter.com/lauralexx", RemoteArtistExternalLinkType.TWITTER),
                    RemoteArtistExternalLink("https://www.youtube.com/user/lauralexx", RemoteArtistExternalLinkType.YOUTUBE),
                    RemoteArtistExternalLink("https://open.spotify.com/artist/5vZ1Zz2v6v9Z3w1ZP8Zf2V", RemoteArtistExternalLinkType.SPOTIFY),
                    RemoteArtistExternalLink("https://www.lauralexx.com/", RemoteArtistExternalLinkType.WEBSITE)
                )
            ),
            RemoteArtist(
                id = 8,
                name = "Harriet Kemsley",
                description = "Description",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/661f9d05dec44d21c932ab7f/scaled_768.jpg"),
                genres = listOf("Comedy", "Standup"),
                showsIds = listOf(10),
                podcastsIds = emptyList(),
                externalLinks = listOf(
                    RemoteArtistExternalLink("https://www.instagram.com/harrietkemsley/", RemoteArtistExternalLinkType.INSTAGRAM),
                    RemoteArtistExternalLink("https://www.facebook.com/harrietkemsley", RemoteArtistExternalLinkType.FACEBOOK),
                    RemoteArtistExternalLink("https://twitter.com/harrietkemsley", RemoteArtistExternalLinkType.TWITTER),
                    RemoteArtistExternalLink("https://www.youtube.com/user/harrietkemsley", RemoteArtistExternalLinkType.YOUTUBE),
                    RemoteArtistExternalLink("https://open.spotify.com/artist/5vZ1Zz2v6v9Z3w1ZP8Zf2V", RemoteArtistExternalLinkType.SPOTIFY),
                    RemoteArtistExternalLink("https://www.harrietkemsley.com/", RemoteArtistExternalLinkType.WEBSITE)
                )
            ),
            RemoteArtist(
                id = 9,
                name = "Pierre Novellie",
                description = "Description",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/656de2d5b5190741cb4a9a6a/scaled_768.jpg"),
                genres = listOf("Comedy", "Standup"),
                showsIds = listOf(1, 11),
                podcastsIds = emptyList(),
                externalLinks = listOf(
                    RemoteArtistExternalLink("https://www.instagram.com/pierrenovellie/", RemoteArtistExternalLinkType.INSTAGRAM),
                    RemoteArtistExternalLink("https://www.facebook.com/pierrenovellie", RemoteArtistExternalLinkType.FACEBOOK),
                    RemoteArtistExternalLink("https://twitter.com/pierrenovellie", RemoteArtistExternalLinkType.TWITTER),
                    RemoteArtistExternalLink("https://www.youtube.com/user/pierrenovellie", RemoteArtistExternalLinkType.YOUTUBE),
                    RemoteArtistExternalLink("https://open.spotify.com/artist/5vZ1Zz2v6v9Z3w1ZP8Zf2V", RemoteArtistExternalLinkType.SPOTIFY),
                    RemoteArtistExternalLink("https://www.pierrenovellie.com/", RemoteArtistExternalLinkType.WEBSITE)
                )
            ),
            RemoteArtist(
                id = 10,
                name = "Larry Dean",
                description = "Description",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/65e61b47221ef91eed7e2435/scaled_768.jpg"),
                genres = listOf("Comedy", "Standup"),
                showsIds = listOf(2, 12),
                podcastsIds = listOf(2),
                externalLinks = listOf(
                    RemoteArtistExternalLink("https://www.instagram.com/larrydean/", RemoteArtistExternalLinkType.INSTAGRAM),
                    RemoteArtistExternalLink("https://www.facebook.com/larrydean", RemoteArtistExternalLinkType.FACEBOOK),
                    RemoteArtistExternalLink("https://twitter.com/larrydean", RemoteArtistExternalLinkType.TWITTER),
                    RemoteArtistExternalLink("https://www.youtube.com/user/larrydean", RemoteArtistExternalLinkType.YOUTUBE),
                    RemoteArtistExternalLink("https://open.spotify.com/artist/5vZ1Zz2v6v9Z3w1ZP8Zf2V", RemoteArtistExternalLinkType.SPOTIFY),
                    RemoteArtistExternalLink("https://www.larrydean.com/", RemoteArtistExternalLinkType.WEBSITE)
                )
            )
        )

}