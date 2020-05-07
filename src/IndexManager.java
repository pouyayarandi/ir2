import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CloseIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pouya on 5/3/20.
 */
public class IndexManager {
    private ObjectMapper mapper;
    private RestHighLevelClient client;

    IndexManager(RestHighLevelClient client) throws UnknownHostException {
        mapper = new ObjectMapper();
        this.client = client;
    }

    void startIndex(News[] newsArray) throws IOException {
        boolean isExist = client.indices().exists(new GetIndexRequest(News.typeName), RequestOptions.DEFAULT);
        if (!isExist) indexNews(newsArray);
        configIndex();
    }

    private void configIndex() throws IOException {
        Map<String, String> settings = new HashMap<>();
        UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest(News.typeName);
        CloseIndexRequest closeIndexRequest = new CloseIndexRequest(News.typeName);
        OpenIndexRequest openIndexRequest = new OpenIndexRequest(News.typeName);

        settings.put("index.analysis.analyzer.default.type", "parsi");
        updateSettingsRequest.settings(settings);

        client.indices().close(closeIndexRequest, RequestOptions.DEFAULT);
        client.indices().putSettings(updateSettingsRequest, RequestOptions.DEFAULT);
        client.indices().open(openIndexRequest, RequestOptions.DEFAULT);
    }

    private void indexNews(News[] newsArray) throws IOException {
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
