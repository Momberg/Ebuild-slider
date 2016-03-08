package tcc.ebuild_slider;

import android.content.Context;

import java.io.IOException;
import java.util.List;

/**
 * Created by momberg on 05/03/16.
 */
public class ObraService {
    public List<Obra> getObras(Context context) throws IOException {
        List<Obra> obras = getObrasFromBanco(context);
        return obras;
    }

    public static List<Obra> getObrasFromBanco(Context context) throws IOException{
        ObrasDB db = new ObrasDB(context);
        try{
            List<Obra> obras = db.findAll();
            return obras;
        } finally {
            db.close();
        }
    }
}
