package example.com.newsapp.jinritoutiao;

import java.util.Objects;

/**
 * Created by yzz on 2020/7/6.
 */

public class News {
    public String title;                                //标题
    public String digest;                               //内容
    public String imgsrc;                               //图片
    public String source;                               //发布者
    public String mtime;                                //发布时间


//    public boolean equals(Object o) {
//        if (this == o) return true;										//Object类的对象是同一个对象返回ture
//        if (o == null || getClass() != o.getClass()) return false;		//如果Object类的对象为空，或者Object类的对象不是本类的对象（即Student类的对象），那么返回false
//        News news = (News) o;									//将Object类的对象o 转化为Student类
//        return Objects.equals(digest,news.digest) && Objects.equals(title, news.title) && Objects.equals(imgsrc, news.imgsrc)
//                && Objects.equals(source, news.source) &&Objects.equals(mtime,news.mtime);//
//
//
//    }
//    public int hashCode() {
//        return Objects.hash(title, digest,imgsrc,source,mtime);									 //返回对象的name和age的hash值，看他们的位置是否已经存在
//    }
}

