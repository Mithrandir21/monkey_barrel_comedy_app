package pm.bam.mbc.remote.datasources

import pm.bam.mbc.remote.models.RemoteEventStatus
import pm.bam.mbc.remote.models.RemoteShow

class RemoteShowsDataSourceImpl : RemoteShowsDataSource {

    override fun getAllShows(): List<RemoteShow> =
        listOf(
            RemoteShow(
                id = 1,
                name = "THE BIG SHOW: Friday - 7pm",
                url = "https://event.liveit.io/43997/the-big-show-friday-4",
                venue = "MB3",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/6234b7d30a7e076f31526fda/scaled_768.jpg"),
                eventStatus = RemoteEventStatus.ACTIVE,
                description = "Top stand-up Comedy at Scotland's Best Comedy Venue (Chortle Awards 2023), Edinburgh's #1 rated Comedy Club and home to the Best Comedy Show at this year's Edinburgh Fringe.",
                artistIds = listOf(5, 2, 9, 5),
                startDate = "2024-05-01T19:00:00+0000",
                endDate = "2024-05-01T20:00:00+0000"
            ),
            RemoteShow(
                id = 2,
                name = "BEST OF THE FRINGE: WIP! - 9pm",
                url = "https://event.liveit.io/34239/sneak-peek-2",
                venue = "MB3",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/650db2597c7aab41e7c49ac0/scaled_768.jpg"),
                eventStatus = RemoteEventStatus.ACTIVE,
                description = "Described by the Metro as 'Fringe Heroes' and as the Fringe's 'Beating Heart' by the Telegraph, Monkey Barrel Comedy believes there should be a taste of the Edinburgh Festival Fringe ALL YEAR ROUND.",
                artistIds = listOf(2, 7, 6, 10),
                startDate = "2024-05-02T19:00:00+0000",
                endDate = "2024-05-02T20:00:00+0000"
            ),
            RemoteShow(
                id = 3,
                name = "Bitter (Special Live Recording)",
                url = "https://event.bookitbee.com/46488/luisa-omielan-bitter",
                venue = "MB3",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/65394e5d37d1085efdb7f551/scaled_768.jpg"),
                eventStatus = RemoteEventStatus.ACTIVE,
                description = "So you know how like the world doesn’t make any fucking sense. And working hard and doing the right thing no longer seems to cut it.",
                artistIds = listOf(1),
                startDate = "2024-05-03T19:00:00+0000",
                endDate = "2024-05-03T20:00:00+0000"
            ),
            RemoteShow(
                id = 4,
                name = "Marc Jennings: Work-in-Progress",
                url = "https://event.bookitbee.com/48086/marc-jennings-work-in-progress-4",
                venue = "MB1",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/65f6e307cb58760cffce27d8/scaled_768.jpg"),
                eventStatus = RemoteEventStatus.ACTIVE,
                description = "Scottish Comedian of the Year winner and host of the Some Laugh Podcast Marc Jennings returns to Edinburgh with a work-in-progress of his new show following sold-out Fringe runs in 2022 & 2023.",
                artistIds = listOf(2),
                startDate = "2024-05-04T19:00:00+0000",
                endDate = "2024-05-04T20:00:00+0000"
            ),
            RemoteShow(
                id = 5,
                name = "Vittorio Angelone: Work In Progress",
                url = "https://event.bookitbee.com/46011/vittorio-angelone-work-in-progress-2",
                venue = "MB3",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/64ff19a560b4d9491b6329ac/scaled_768.jpg"),
                eventStatus = RemoteEventStatus.ACTIVE,
                description = "Vittorio Angelone previews a work in progress of his brand new show ahead of the Fringe.",
                artistIds = listOf(3),
                startDate = "2024-05-05T19:00:00+0000",
                endDate = "2024-05-05T20:00:00+0000"
            ),
            RemoteShow(
                id = 6,
                name = "Esméralda (Work in Progress)",
                url = "https://event.bookitbee.com/48010/sam-lake-esmeralda-work-in-progress",
                venue = "MB1",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/65ee4734e318f152be0264c7/scaled_768.jpg"),
                eventStatus = RemoteEventStatus.ACTIVE,
                description = "An uplifting new show about coming out as Spanish, grief and the Ice Age movie franchise.",
                artistIds = listOf(4),
                startDate = "2024-05-06T19:00:00+0000",
                endDate = "2024-05-06T20:00:00+0000"
            ),
            RemoteShow(
                id = 7,
                name = "Amy Matthews: Work in Progress",
                url = "https://event.bookitbee.com/47956/amy-matthews-work-in-progress",
                venue = "MB1",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/65e60d82221ef91eed7e133f/scaled_768.jpg"),
                eventStatus = RemoteEventStatus.ACTIVE,
                description = "Matthews brings a work in progress version of her latest show to the Monkey Barrel stage.",
                artistIds = listOf(5),
                startDate = "2024-05-07T19:00:00+0000",
                endDate = "2024-05-07T20:00:00+0000"
            ),
            RemoteShow(
                id = 8,
                name = "Self Doubt (I Think)",
                url = "https://event.bookitbee.com/46222/steve-bugeja-self-doubt-i-think",
                venue = "MB3",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/6514670741fad31755da03c7/scaled_768.jpg"),
                eventStatus = RemoteEventStatus.ACTIVE,
                description = "The creator and star of ITV2's hit sitcom, Buffering, is back on the road following a sell-out 2022 tour and a critically acclaimed run at the 2023 Edinburgh fringe festival.",
                artistIds = listOf(6),
                startDate = "2024-05-08T19:00:00+0000",
                endDate = "2024-05-08T20:00:00+0000"
            ),
            RemoteShow(
                id = 9,
                name = "Slinky",
                url = "https://event.bookitbee.com/47701/laura-lexx-slinky",
                venue = "MB1",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/65bbb1219785342c8514328a/scaled_768.jpg"),
                eventStatus = RemoteEventStatus.ACTIVE,
                description = "Join multi award winning stand up star and viral sensation Laura Lexx (Live at the Apollo, Mock The Week, Hypothetical, The Now Show) with her highly anticipated UK tour: Slinky.",
                artistIds = listOf(7),
                startDate = "2024-05-09T19:00:00+0000",
                endDate = "2024-05-09T20:00:00+0000"
            ),
            RemoteShow(
                id = 10,
                name = "Everything Always Works Out For Me",
                url = "https://event.bookitbee.com/48421/harriet-kemsley-everything-always-works-out-for-me",
                venue = "MB3",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/661f9d05dec44d21c932ab7f/scaled_768.jpg"),
                eventStatus = RemoteEventStatus.ACTIVE,
                description = "Ridiculous things always happen to Harriet, but this year it’s out of control.",
                artistIds = listOf(8),
                startDate = "2024-05-10T19:00:00+0000",
                endDate = "2024-05-10T20:00:00+0000"
            ),
            RemoteShow(
                id = 11,
                name = "Why Are You Laughing?",
                url = "https://event.bookitbee.com/46853/pierre-novellie-why-are-you-laughing",
                venue = "MB3",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/656de2d5b5190741cb4a9a6a/scaled_768.jpg"),
                eventStatus = RemoteEventStatus.CANCELLED,
                description = "He can’t stress enough that he thinks these thoughts are normal and reasonable – it’s you people that don’t make sense. He is the normal one, definitely, but that does raise an important question: Why are you laughing?",
                artistIds = listOf(9),
                startDate = "2024-05-11T19:00:00+0000",
                endDate = "2024-05-11T20:00:00+0000"
            ),
            RemoteShow(
                id = 12,
                name = "Dodger",
                url = "https://event.bookitbee.com/47958/larry-dean-dodger",
                venue = "MB3",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/65e61b47221ef91eed7e2435/scaled_768.jpg"),
                eventStatus = RemoteEventStatus.ACTIVE,
                description = "Larry’s been spending a lot of time with his granny lately. He wants to tell you about it.",
                artistIds = listOf(10),
                startDate = "2024-05-12T19:00:00+0000",
                endDate = "2024-05-12T20:00:00+0000"
            )
        )
}