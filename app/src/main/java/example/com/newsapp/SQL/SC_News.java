package example.com.newsapp.SQL;

/**
 * Created by yzz on 2020/7/17.
 */

public class SC_News {                                                                  //收藏的数据的类
    private String titleNews;
    private String contentNews;
    private String pictureNews;

    public String getTitleNews() {
        return titleNews;
    }

    public void setTitleNews(String titleNews) {
        this.titleNews = titleNews;
    }

    public String getContentNews() {
        return contentNews;
    }

    public void setContentNews(String contentNews) {
        this.contentNews = contentNews;
    }

    public String getPictureNews() {
        return pictureNews;
    }

    public void setPictureNews(String pictureNews) {
        this.pictureNews = pictureNews;
    }

    public SC_News(String titleNews, String contentNews, String pictureNews) {
        this.titleNews = titleNews;
        this.contentNews = contentNews;
        this.pictureNews = pictureNews;
    }
}
