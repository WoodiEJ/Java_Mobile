package br.com.woodi.mininl.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import br.com.woodi.mininl.R;
import br.com.woodi.mininl.model.VendaItem;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.ViewHolder> {

    private List<VendaItem> listaItens;
    private OnItemLongClickListener listener;

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public CarrinhoAdapter(List<VendaItem> listaItens, OnItemLongClickListener listener) {
        this.listaItens = listaItens;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrinho, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VendaItem item = listaItens.get(position);

        holder.txtDescricao.setText(item.getProduto().getDescricao());

        String detalhe = String.format("%.0f UN x R$ %.2f", item.getQuantidade(), item.getProduto().getPreco());
        holder.txtUnitario.setText(detalhe);

        String total = String.format("R$ %.2f", item.getTotalLinha());
        holder.txtTotalItem.setText(total);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int posicaoAtual = holder.getAdapterPosition();

                if (posicaoAtual != RecyclerView.NO_POSITION) {
                    listener.onItemLongClick(posicaoAtual);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaItens.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDescricao, txtUnitario, txtTotalItem;

        public ViewHolder(View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            txtUnitario = itemView.findViewById(R.id.txtUnitario);
            txtTotalItem = itemView.findViewById(R.id.txtTotalItem);
        }
    }
}