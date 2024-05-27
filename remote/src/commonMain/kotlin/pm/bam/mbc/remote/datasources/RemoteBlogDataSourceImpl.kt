package pm.bam.mbc.remote.datasources

import pm.bam.mbc.remote.models.RemoteBlogPost

class RemoteBlogDataSourceImpl : RemoteBlogDataSource {

    override fun getAllBlogPosts(): List<RemoteBlogPost> =
        listOf(
            RemoteBlogPost(
                id = 1,
                title = "Vittorio Angelone - The Art of the Impossible",
                content = "Content 1",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/64ff19a560b4d9491b6329ac/scaled_768.jpg"),
                tags = listOf("tag1"),
                author = "Ross",
                releaseDate = "2024-01-01T19:00:00+0000"
            ),
            RemoteBlogPost(
                id = 2,
                title = "Sam Lake - There and back again",
                content = "Content 2",
                images = listOf("https://d3flpus5evl89n.cloudfront.net/5a57405cdf82fe5399b2ddb4/65ee4734e318f152be0264c7/scaled_768.jpg"),
                tags = listOf("tag2"),
                author = "Ross",
                releaseDate = "2024-02-01T19:00:00+0000"
            )
        )

}