import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        News[] newsArray = DataManager.provideData();
        RestHighLevelClient client = ClientManager.shared.client;
        IndexManager indexManager = new IndexManager(client);
        SearchManager searchManager = new SearchManager(client);

        System.out.println("Index news...");
        indexManager.startIndex(newsArray);

        SearchResponse response = searchManager.searchTerm("افغانستان");
        List<News> newsList = searchManager.fetchResults(response);

        for (News news: newsList) {
            System.out.println(news.title);
        }

        indexManager.closeConnection();
    }
}
