import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by Pouya on 5/3/20.
 */
public class IndexManager {
    static String indexName = "asriran-98443254";

    ObjectMapper mapper;
    RestHighLevelClient client;

    IndexManager() throws UnknownHostException {
        mapper = new ObjectMapper();
        client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")
        ));
    }

    private void createIndex() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    }

    private void deleteIndex() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
    }

    private boolean indexExsits() {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        try {
            return client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            return false;
        }
    }

    private void openIndex() throws IOException {
        OpenIndexRequest openIndexRequest = new OpenIndexRequest(indexName);
        client.indices().open(openIndexRequest, RequestOptions.DEFAULT);
    }

    void configIndex() throws IOException {
        if (indexExsits()) return;

        createIndex();
        openIndex();
    }

    void indexNews(News[] newsArray) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();

        for (News news: newsArray) {
            String json;
            try {
                json = mapper.writeValueAsString(news);
            } catch (JsonProcessingException e) {
                continue;
            }
            bulkRequest.add(new IndexRequest(News.typeName).source(json, XContentType.JSON));
        }

        client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    void closeConnection() throws IOException {
        client.close();
    }
}
