package pm.bam.mbc.remote.datasources

import pm.bam.mbc.remote.models.RemoteBlogPost

interface RemoteBlogDataSource {

    fun getAllBlogPosts(): List<RemoteBlogPost>

}