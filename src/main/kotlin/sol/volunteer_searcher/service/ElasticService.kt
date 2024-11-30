package sol.volunteer_searcher.service

import org.opensearch.action.admin.indices.alias.Alias
import org.opensearch.action.admin.indices.alias.get.GetAliasesRequest
import org.opensearch.action.admin.indices.delete.DeleteIndexRequest
import org.opensearch.client.GetAliasesResponse
import org.opensearch.client.RequestOptions
import org.opensearch.client.RestHighLevelClient
import org.opensearch.client.indices.CreateIndexRequest
import org.opensearch.client.indices.GetIndexRequest
import org.opensearch.common.compress.CompressedXContent
import org.opensearch.common.settings.Settings
import org.opensearch.core.xcontent.MediaTypeRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import sol.volunteer_searcher.config.IndexProps
import sol.volunteer_searcher.util.readResource

@Service
class SearchService(
    private val searchClient: RestHighLevelClient,
    @Value("\${my.search.index-prefix:test}") private val indexPrefix: String,
    private val env: Environment,
) {
    fun convertName(indexName: String) = "$indexPrefix-$indexName"
    fun getAliases(): GetAliasesResponse {
        return searchClient.indices().getAlias(GetAliasesRequest(), RequestOptions.DEFAULT)
    }

    fun existsIndex(indexName: String): Boolean {
        return searchClient.indices().exists(GetIndexRequest(convertName(indexName)), RequestOptions.DEFAULT)
    }

    fun deleteIndex(indexName: String): Boolean {
        return searchClient.indices()
            .delete(DeleteIndexRequest(convertName(indexName)), RequestOptions.DEFAULT)
            .isAcknowledged
    }

    fun createIndex(
        indexProps: IndexProps,
    ): Boolean? {
        var settingsBuilder = Settings.builder()
        if (indexProps.settings == null) {
            // TODO: 이미 템플릿이 있을경우 넘어가고
            // 없으면 에러
            return null
        }

        indexProps.settings.forEach { setting ->
            settingsBuilder = settingsBuilder.loadFromSource(readResource("/search/$setting"), MediaTypeRegistry.JSON)
        }

        val request = CreateIndexRequest(convertName(indexProps.indexName))
            .settings(settingsBuilder.build())
            .mapping(CompressedXContent(readResource("/search/${indexProps.mapping}")).uncompressed(), MediaTypeRegistry.JSON)
            .alias(Alias(convertName(indexProps.alias)).writeIndex(indexProps.aliasWritable))

        return searchClient.indices()
            .create(request, RequestOptions.DEFAULT)
            .isAcknowledged
    }
}

