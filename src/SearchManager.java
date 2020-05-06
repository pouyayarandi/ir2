import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
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

    SearchResponse searchTerm(String term) throws IOException {
        SearchRequest searchRequest = new SearchRequest(News.typeName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        sourceBuilder.query(QueryBuilders.fuzzyQuery("body", term));
        searchRequest.source(sourceBuilder);

        return client.search(searchRequest, RequestOptions.DEFAULT);
    }

    List<News> fetchResults(SearchResponse response) throws IOException {
        SearchHits searchHits = response.getHits();
        List<News> newsList = new ArrayList<>();

        for (SearchHit hit: searchHits)
            newsList.add(mapper.readValue(hit.getSourceAsString(), News.class));

        return newsList;
    }
}
