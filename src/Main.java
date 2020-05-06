import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        News[] news;
        news = DataManager.provideData();
        IndexManager indexManager = new IndexManager();

        System.out.println("Config index...");
        indexManager.configIndex();
        System.out.println("Index news...");
        indexManager.indexNews(news);

        indexManager.closeConnection();

    }
}
