package br.com.woodi.mininl.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import br.com.woodi.mininl.R;
import br.com.woodi.mininl.model.Venda;

public class VendaAdapter extends RecyclerView.Adapter<VendaAdapter.ViewHolder> {

    private List<Venda> listaVendas;

    public VendaAdapter(List<Venda> listaVendas) {
        this.listaVendas = listaVendas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venda, parent, false);
        return new ViewHolder(view); // Verifique o "i" aqui
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { // Verifique o "n" aqui
        Venda venda = listaVendas.get(position);
        holder.txtId.setText("VENDA #" + venda.getId());
        holder.txtData.setText(venda.getDataHora());
        String valor = String.format("R$ %.2f", venda.getTotal());
        holder.txtValor.setText(valor);
    }

    @Override
    public int getItemCount() { return listaVendas.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtData, txtValor;
        public ViewHolder(View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txtIdVenda);
            txtData = itemView.findViewById(R.id.txtDataHora);
            txtValor = itemView.findViewById(R.id.txtValorVenda);
        }
    }
}