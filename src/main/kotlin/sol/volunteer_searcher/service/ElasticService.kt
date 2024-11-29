package sol.volunteer_searcher.service

import org.opensearch.action.admin.indices.alias.get.GetAliasesRequest
import org.opensearch.client.GetAliasesResponse
import org.opensearch.client.RequestOptions
import org.opensearch.client.RestHighLevelClient
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val searchClient: RestHighLevelClient,
) {
    fun getAliases(): GetAliasesResponse {
        return searchClient.indices().getAlias(GetAliasesRequest(), RequestOptions.DEFAULT)
    }
}
