import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        News[] news;
        IndexManager indexManager = new IndexManager();

        try {
            news = DataManager.provideData();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        indexManager.indexNews(news);

    }
}
