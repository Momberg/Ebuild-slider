package tcc.ebuild_slider;

import android.content.Context;
import java.io.IOException;
import java.util.List;

public class ObraService {

    public List<Obra> getObrabyID(Context context, int ID){
        List<Obra> obras = getObrasFromBancoByID(context, ID);
        return obras;
    }

    public List<Obra> getObrasAll(Context context) throws IOException {
        List<Obra> obras = getObrasFromBancoAll(context);
        return obras;
    }

    public List<Obra> getObrasLatLng(Context context, double lat, double lng){
        List<Obra> obras = getObrasFromBancoLatLng(context, lat, lng);
        return obras;
    }

    public List<Obra> searchObrasName(Context context, String name){
        List<Obra> obras = searchObrasFromBancoByName(context, name);
        return obras;
    }

    private static List<Obra> getObrasFromBancoByID(Context context, int ID){
        ObrasDB db = new ObrasDB(context);
        try{
            List<Obra> obras = db.findbyID(ID);
            return obras;
        } finally {
            db.close();
        }
    }

    private static List<Obra> getObrasFromBancoAll(Context context) throws IOException{
        ObrasDB db = new ObrasDB(context);
        try{
            List<Obra> obras = db.findAll();
            return obras;
        } finally {
            db.close();
        }
    }

    private static List<Obra> getObrasFromBancoLatLng(Context context, double lat, double lng){
        ObrasDB db = new ObrasDB(context);
        String latitude, longitude;
        latitude = String.valueOf(lat);
        longitude = String.valueOf(lng);
        try{
            List<Obra> obras = db.findLatLng(latitude, longitude);
            return obras;
        } finally {
            db.close();
        }
    }

    private static List<Obra> searchObrasFromBancoByName(Context context, String name){
        ObrasDB db = new ObrasDB(context);
        try{
            List<Obra> obras = db.searchObraByName(name);
            return  obras;
        } finally {
            db.close();
        }
    }
}
