package my.edu.tarc.user.smartplat;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="id8184997_smartplatdb";
    private static final String TABLE_LOGIN="Login";

    private static final String KEY_ID="id", KEY_USERNAME="username",KEY_PASSWORD="password";

    public SQLiteHandler(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_USERNAME + "TEXT, "
                + KEY_USERNAME + "TEXT" + ")";
            db.execSQL(CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        //Create again
        onCreate(db);
    }

    //Storing data into database
    public void addUser(String username, String password, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD,password);

        //Inserting Row
        db.insert(TABLE_LOGIN,null,values);
        db.close();
    }
}
