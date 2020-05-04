import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * Created by Pouya on 5/3/20.
 */
public class IndexManager {
    static String indexName = "asriran";

    ObjectMapper mapper;
    RestHighLevelClient client;

    IndexManager() {
        mapper = new ObjectMapper();
        client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")
        ));
    }

    void indexNews(News[] newsArray) {
        BulkRequest bulkRequest = new BulkRequest();

        for (News news: newsArray) {
            String json;
            try {
                json = mapper.writeValueAsString(news);
            } catch (JsonProcessingException e) {
                continue;
            }
            bulkRequest.add(new IndexRequest(IndexManager.indexName, News.typeName).source(json));
        }
    }

    void closeConnection() throws IOException {
        client.close();
    }
}
