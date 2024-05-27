package pm.bam.mbc.domain.db.transformations

import pm.bam.mbc.common.serializer.Serializer
import pm.bam.mbc.common.serializer.deserialize
import pm.bam.mbc.common.serializer.serialize
import pm.bam.mbc.domain.models.BlogPost
import pm.bam.mbc.remote.models.RemoteBlogPost
import pmbammbcdomain.DatabaseBlogPost

internal fun RemoteBlogPost.toDatabaseBlogPost(serializer: Serializer): DatabaseBlogPost = DatabaseBlogPost(
    id = id,
    title = title,
    content = content,
    images = serializer.serialize(images),
    tags = serializer.serialize(tags),
    author = author,
    releaseDate = releaseDate
)

internal fun DatabaseBlogPost.toBlogPost(serializer: Serializer): BlogPost = BlogPost(
    id = id,
    title = title,
    content = content,
    images = serializer.deserialize(images),
    tags = serializer.deserialize(tags),
    author = author,
    releaseDate = releaseDate
)