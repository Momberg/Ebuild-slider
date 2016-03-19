package tcc.ebuild_slider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by momberg on 19/03/16.
 */
public class ObraAdapter extends BaseAdapter {

    private Context context;
    private List<Obra> obras;

    public ObraAdapter(Context context, List<Obra> obras){
        this.context = context;
        this.obras = obras;
    }

    @Override
    public int getCount() {
        return obras != null ? obras.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return obras.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_obra, parent, false);
        TextView t_nome = (TextView) view.findViewById(R.id.t_nome);
        TextView t_endereco = (TextView) view.findViewById(R.id.t_endereco);
        TextView t_data = (TextView) view.findViewById(R.id.t_data);
        TextView t_tipo_fase = (TextView) view.findViewById(R.id.t_tipo_fase);
        TextView t_fase = (TextView) view.findViewById(R.id.t_fase);
        Obra obra = obras.get(position);
        String endereco;
        t_nome.setText(obra.getNome());
        endereco = obra.getRua() + ", " + obra.getBairro() + " - " + obra.getCidade();
        t_endereco.setText(endereco);
        t_data.setText(obra.getData());
        t_tipo_fase.setText(obra.getTipoFase());
        t_fase.setText(obra.getFase());
        return view;
    }
}
