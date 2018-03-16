package enes.com.a7_sqliteusing;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper{

    final static String name = "proje";
    final static int version = 1;

    public DB(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE `liste` (\n" +
                "\t`lid`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`baslik`\tTEXT,\n" +
                "\t`aciklama`\tTEXT,\n" +
                "\t`tarih`\tTEXT,\n" +
                "\t`durum`\tINTEGER\n" +
                ");");
        sqLiteDatabase.execSQL("insert into liste values(null, 'Havuç', 'Turuncu olanlar - 5 TL', '03.05.2018', 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists liste");
        onCreate(sqLiteDatabase);
    }
}
