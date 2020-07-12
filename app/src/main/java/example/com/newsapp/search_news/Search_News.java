package example.com.newsapp.search_news;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yzz on 2020/7/8.
 */

public class Search_News {
    public String title;                    //标题

    @SerializedName("pdate_src")
    public String time;                     //时间
    public String content;
    public String src;                      //发布人
    public String img;                      //图片
}
