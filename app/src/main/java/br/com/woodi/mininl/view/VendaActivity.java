package br.com.woodi.mininl.view;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager; // <--- FALTAVA ESSE IMPORT
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.woodi.mininl.R;
import br.com.woodi.mininl.database.DBHelper;
import br.com.woodi.mininl.model.Produto;
import br.com.woodi.mininl.model.VendaItem;

public class VendaActivity extends AppCompatActivity {

    private EditText edtEan;
    private TextView txtTotalVenda;
    private RecyclerView rvCarrinho;
    private CarrinhoAdapter adapter;
    private List<VendaItem> carrinho = new ArrayList<>();
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda);
        dbHelper = new DBHelper(this);

        edtEan = findViewById(R.id.edtEan);
        txtTotalVenda = findViewById(R.id.txtTotalVenda);
        rvCarrinho = findViewById(R.id.rvCarrinho);
        Button btnAdicionar = findViewById(R.id.btnAdicionar);
        Button btnFinalizar = findViewById(R.id.btnFinalizar);

        adapter = new CarrinhoAdapter(carrinho, new CarrinhoAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                VendaItem removido = carrinho.remove(position);
                adapter.notifyItemRemoved(position);
                atualizarTotal();
                Toast.makeText(VendaActivity.this, "Item removido: " + removido.getProduto().getDescricao(), Toast.LENGTH_SHORT).show();
            }
        });
        rvCarrinho.setLayoutManager(new LinearLayoutManager(this));
        rvCarrinho.setAdapter(adapter);

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ean = edtEan.getText().toString();
                buscarEAdicionar(ean);
            }
        });

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carrinho.isEmpty()) {
                    Toast.makeText(VendaActivity.this, "O carrinho está vazio!", Toast.LENGTH_SHORT).show();
                } else {
                    double totalFinal = 0;
                    for(VendaItem item : carrinho) {
                        totalFinal += item.getTotalLinha();
                    }

                    salvarVendaNoBanco(totalFinal);
                    Toast.makeText(VendaActivity.this, "VENDA REGISTRADA: R$ " + totalFinal, Toast.LENGTH_SHORT).show();
                    carrinho.clear();
                    adapter.notifyDataSetChanged();
                    atualizarTotal();
                    edtEan.requestFocus();
                }
            }
        });
    }

    private void buscarEAdicionar(String ean) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, descricao, preco_venda FROM " + DBHelper.TB_PRODUTO + " WHERE ean = ?", new String[]{ean});

        if (cursor.moveToFirst()) {
            int indexId = cursor.getColumnIndex("id");
            int indexDesc = cursor.getColumnIndex("descricao");
            int indexPreco = cursor.getColumnIndex("preco_venda");

            if(indexDesc != -1 && indexPreco != -1) {
                int id = cursor.getInt(indexId);
                String desc = cursor.getString(indexDesc);
                double preco = cursor.getDouble(indexPreco);

                boolean achou = false;
                for (VendaItem itemExistente : carrinho) {
                    if (itemExistente.getProduto().getEan().equals(ean)) {
                        itemExistente.somarQuantidade(1.0);
                        achou = true;
                        break;
                    }
                }

                if (!achou) {
                    Produto p = new Produto(id, desc, ean, preco);
                    VendaItem novoItem = new VendaItem(p, 1.0);
                    carrinho.add(novoItem);
                }

                adapter.notifyDataSetChanged();
                atualizarTotal();
                edtEan.setText("");
            } else {
                System.out.println("ERRO: Colunas não encontradas.");
            }
        } else {
            Toast.makeText(this, "Produto não encontrado!", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    private void salvarVendaNoBanco(double totalVenda) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        android.content.ContentValues values = new android.content.ContentValues();
        values.put("total", totalVenda);
        long idVenda = db.insert(DBHelper.TB_VENDA, null, values);

        if (idVenda != -1) {
            System.out.println("Venda salva com sucesso! ID: " + idVenda);
        } else {
            System.out.println("Erro ao salvar venda");
        }
    }

    private void atualizarTotal() {
        if (txtTotalVenda == null) {
            System.out.println("ERRO CRÍTICO: txtTotalVenda é NULL! Verifique o onCreate.");
            return;
        }

        double total = 0;
        for (VendaItem item : carrinho) {
            total += item.getTotalLinha();
        }

        String textoValor = String.format(java.util.Locale.getDefault(), "R$ %.2f", total);
        txtTotalVenda.setText(textoValor);
    }
}