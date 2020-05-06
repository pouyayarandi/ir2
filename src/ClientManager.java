import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * Created by Pouya on 5/6/20.
 */
public class ClientManager {
    RestHighLevelClient client;
    static ClientManager shared = new ClientManager();

    ClientManager() {
        client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")
        ));
    }
}
