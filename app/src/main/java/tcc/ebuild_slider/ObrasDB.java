package tcc.ebuild_slider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by momberg on 02/03/16.
 */
public class ObrasDB extends SQLiteOpenHelper {

    private static final int VERSAO_BANCO = 1;
    public static final String NOME_BANCO = "obras.sqlite";

    public ObrasDB(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists obras (_id integer primary key autoincrement, nome text, data text, tipo_fase text, fase text, latitude double, longitude double);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists obras");
        onCreate(db);
    }

    public long save(Obra obra){
        long id = obra.id;
        SQLiteDatabase db = getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("nome", obra.getNome());
            values.put("data", obra.getData());
            values.put("tipo_fase", obra.getTipoFase());
            values.put("fase", obra.getFase());
            values.put("latitude", obra.getLat());
            values.put("longitude", obra.getLng());
            if(id != 0){
                String _id = String.valueOf(obra.id);
                String[] whereArgs = new String[] {_id};
                int count = db.update("obras", values, "_id=?", whereArgs);
                return count;
            } else {
                id = db.insert("obras", "", values);
                return id;
            }
        } finally {
            db.close();
        }
    }

    public void limpa_db(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from obras;");
    }
}
