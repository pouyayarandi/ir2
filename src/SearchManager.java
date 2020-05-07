import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pouya on 5/6/20.
 */
public class SearchManager {
    private RestHighLevelClient client;
    private ObjectMapper mapper;

    SearchManager(RestHighLevelClient client) {
        this.client = client;
        mapper = new ObjectMapper();
    }

    List<News> search(String query) throws IOException {
        SearchRequest searchRequest = makeSearchRequest(QueryBuilders.queryStringQuery(query).fuzziness(Fuzziness.AUTO));
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        return fetchResults(response);
    }

    List<News> suggest(String query) throws IOException {
        SearchRequest searchRequest = makeSearchRequest(QueryBuilders.prefixQuery("title", query));
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        return fetchResults(response);
    }

    List<News> searchInDate(String query) throws IOException {
        SearchRequest searchRequest = makeSearchRequest(QueryBuilders.termQuery("publishDate", query));
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        return fetchResults(response);
    }

    private SearchRequest makeSearchRequest(QueryBuilder queryBuilder) {
        SearchRequest searchRequest = new SearchRequest(News.typeName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        sourceBuilder.query(queryBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(1000);
        searchRequest.source(sourceBuilder);

        return searchRequest;
    }

    private List<News> fetchResults(SearchResponse response) throws IOException {
        SearchHits searchHits = response.getHits();
        List<News> newsList = new ArrayList<>();

        for (SearchHit hit: searchHits)
            newsList.add(mapper.readValue(hit.getSourceAsString(), News.class));

        return newsList;
    }
}
