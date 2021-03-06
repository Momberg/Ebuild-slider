package tcc.ebuild_slider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class ObrasDB extends SQLiteOpenHelper {

    private static final int VERSAO_BANCO = 3;
    public static final String NOME_BANCO = "obras.sqlite";

    private static final String[] typ_str = new String[] {"avenida", "av.", "av", "rua", "alameda", "al.", "al", "estrada", "estr.", "estr"};

    public ObrasDB(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists obras (_id integer primary key autoincrement, nome text, data text, rua text, bairro text, cidade text, tipo_fase text, fase text, latitude text, longitude text);");
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
            values.put("rua", obra.getRua());
            values.put("bairro", obra.getBairro());
            values.put("cidade", obra.getCidade());
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

    public List<Obra> findAll(){
        SQLiteDatabase db = getWritableDatabase();
        try{
            Cursor c = db.query("obras", null, null, null, null, null, null);
            return toList(c);
        } finally {
            db.close();
        }
    }

    public List<Obra> searchObraByName(String name){
        SQLiteDatabase db = getReadableDatabase();
        try{
            Cursor c = db.query("obras", null, "nome LIKE '" + name + "%' " +
                    "or rua LIKE '" + name + "%' " +
                    "or bairro LIKE '" + name + "%' " +
                    "or cidade LIKE '" + name + "%' "
                    , null, null, null, null);
            return  toList(c);
        } finally {
            db.close();
        }
    }

    public List<Obra> findbyID(int ID){
        SQLiteDatabase db = getWritableDatabase();
        try{
            Cursor c = db.query("obras", null, "_id = " + ID, null, null, null, null);
            return toList(c);
        } finally {
            db.close();
        }
    }

    public List<Obra> findLatLng(String lat, String lng){
        SQLiteDatabase db = getWritableDatabase();
        try{
            Cursor c = db.query("obras", null, "latitude = '" + lat + "' AND longitude = '" + lng + "'", null, null, null, null);
            return toList(c);
        } finally {
            db.close();
        }
    }

    private List<Obra> toList(Cursor c) {
        List<Obra> obras = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                Obra obra = new Obra();
                obras.add(obra);
                obra.id = c.getInt(c.getColumnIndex("_id"));
                obra.nome = c.getString(c.getColumnIndex("nome"));
                obra.data = c.getString(c.getColumnIndex("data"));
                obra.rua = c.getString(c.getColumnIndex("rua"));
                obra.bairro = c.getString(c.getColumnIndex("bairro"));
                obra.cidade = c.getString(c.getColumnIndex("cidade"));
                obra.TipoFase = c.getString(c.getColumnIndex("tipo_fase"));
                obra.fase = c.getString(c.getColumnIndex("fase"));
                obra.lat = c.getString(c.getColumnIndex("latitude"));
                obra.lng = c.getString(c.getColumnIndex("longitude"));
            } while (c.moveToNext());
        }
        return obras;
    }

    public void delete(int ID){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("obras","_id=" + ID, null);
    }

}
