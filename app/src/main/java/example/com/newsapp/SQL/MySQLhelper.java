package example.com.newsapp.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by yzz on 2020/7/17.
 */

public class MySQLhelper extends SQLiteOpenHelper {
    public static final String CREATE_SHOUCANG="create table shoucang(" +               //创建收藏的数据表
            "id integer primary key autoincrement," +
            "titleNews text unique," +
            "contentNews text unique," +
            "pictureNews text unique," +
            "shuocang_zhuangtai integer)";
    private Context mContext;

    public MySQLhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {                       //创建数据库
        db.execSQL(CREATE_SHOUCANG);
        Toast.makeText(mContext,"数据库创建成功",Toast.LENGTH_SHORT).show();;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
