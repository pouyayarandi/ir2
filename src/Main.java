import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        News[] newsArray = DataManager.provideData();
        RestHighLevelClient client = ClientManager.shared.client;
        IndexManager indexManager = new IndexManager(client);
        SearchManager searchManager = new SearchManager(client);

        int functionType;
        String query;

        System.out.println("Index news...");
        indexManager.startIndex(newsArray);

        System.out.println("1. Search in news \n2. Search in date of news");
        functionType = scanner.nextInt();

        switch (functionType) {
            case 1:
                System.out.println("Enter query");
                query = scanner.next();

                List<News> newsList = searchManager.search(query);
                List<News> suggests = searchManager.suggest(query);

                System.out.println("\nresults (" + newsList.size() + ")");
                for (News news: newsList) {
                    System.out.println(news.title);
                    System.out.println(news.url);
                    System.out.println();
                }

                System.out.println("\nsuggestions (" + suggests.size() + ")");
                for (News news: suggests) {
                    System.out.println(news.title);
                }
                break;
            case 2:
                System.out.println("Enter query");
                query = scanner.next();

                List<News> newsList1 = searchManager.searchInDate(query);

                System.out.println("\nresults (" + + newsList1.size() + ")");
                for (News news: newsList1) {
                    System.out.println(news.title);
                    System.out.println(news.url);
                    System.out.println();
                }
                break;
            default:
                System.out.println("Wrong input");
                break;
        }

        indexManager.closeConnection();
    }
}
