package pm.bam.mbc.remote.datasources

import pm.bam.mbc.remote.models.RemoteNewsExternalLink
import pm.bam.mbc.remote.models.RemoteNewsExternalLinkType
import pm.bam.mbc.remote.models.RemoteNews
import pm.bam.mbc.remote.models.RemoteNewsType

internal class RemoteNewsDataSourceImpl : RemoteNewsDataSource {

    override fun getAllNews(): List<RemoteNews> =
        listOf(
            RemoteNews(
                id = 1,
                title = "New App for Monkey Barrel Comedy!",
                description = "Monkey Barrel Comedy has a new app! Download it now to get the latest news, shows, podcasts and more!",
                images = listOf("https://static.wixstatic.com/media/d75e7b_78c48bdf7d674cd6877bf207ae2a11c7~mv2.png"),
                types = listOf(RemoteNewsType.BUSINESS),
                externalLinks = listOf(
                    RemoteNewsExternalLink("https://www.monkeybarrelcomedy.com/", RemoteNewsExternalLinkType.WEBSITE)
                )
            ),
            RemoteNews(
                id = 2,
                title = "New Monkey Barrel Venue - MB4!",
                description = "Monkey Barrel Comedy has a new venue! MB4 is now open and has a full schedule of shows and events!",
                images = listOf("https://static.wixstatic.com/media/d75e7b_78c48bdf7d674cd6877bf207ae2a11c7~mv2.png"),
                types = listOf(RemoteNewsType.BUSINESS, RemoteNewsType.COMEDY),
                externalLinks = listOf(
                    RemoteNewsExternalLink("https://www.monkeybarrelcomedy.com/", RemoteNewsExternalLinkType.WEBSITE)
                )
            ),
            RemoteNews(
                id = 3,
                title = "Vittorio Angelone - New Show!",
                description = "Vittoio Angelone has a new show! Get tickets now to see him live at Monkey Barrel Comedy!",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/64ff19a560b4d9491b6329ac/scaled_768.jpg"),
                types = listOf(RemoteNewsType.COMEDY, RemoteNewsType.STANDUP),
                showsIds = listOf(1, 5),
                podcastsIds = emptyList(),
                externalLinks = listOf(
                    RemoteNewsExternalLink("https://www.instagram.com/vittorioangelone/", RemoteNewsExternalLinkType.INSTAGRAM),
                    RemoteNewsExternalLink("https://www.facebook.com/vittorioangelone", RemoteNewsExternalLinkType.FACEBOOK),
                    RemoteNewsExternalLink("https://twitter.com/vittorioangelone", RemoteNewsExternalLinkType.TWITTER),
                    RemoteNewsExternalLink("https://www.youtube.com/user/vittorioangelone", RemoteNewsExternalLinkType.YOUTUBE),
                    RemoteNewsExternalLink("https://open.spotify.com/artist/5vZ1Zz2v6v9Z3w1ZP8Zf2V", RemoteNewsExternalLinkType.SPOTIFY),
                    RemoteNewsExternalLink("https://www.vittorioangelone.com/", RemoteNewsExternalLinkType.WEBSITE)
                )
            )
        )

}