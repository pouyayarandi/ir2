import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        News[] news;
        news = DataManager.provideData();
        IndexManager indexManager = new IndexManager();

        List<News> list = new ArrayList<>();
        list.addAll(Arrays.asList(news).subList(0, 1));


        indexManager.indexNews(news);

        indexManager.closeConnection();

    }
}
