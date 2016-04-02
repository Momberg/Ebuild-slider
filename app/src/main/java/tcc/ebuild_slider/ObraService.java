package tcc.ebuild_slider;

import android.content.Context;
import java.io.IOException;
import java.util.List;

public class ObraService {
    public List<Obra> getObrasAll(Context context) throws IOException {
        List<Obra> obras = getObrasFromBancoAll(context);
        return obras;
    }

    public List<Obra> getObrasLatLng(Context context, double lat, double lng){
        List<Obra> obras = getObrasFromBancoLatLng(context, lat, lng);
        return obras;
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
        try{
            List<Obra> obras = db.findLatLng(lat, lng);
            return obras;
        } finally {
            db.close();
        }
    }
}
