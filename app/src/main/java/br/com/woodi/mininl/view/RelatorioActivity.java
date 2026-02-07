package br.com.woodi.mininl.view;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.woodi.mininl.R;
import br.com.woodi.mininl.database.DBHelper;
import br.com.woodi.mininl.model.Venda;

public class RelatorioActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private RecyclerView rvRelatorio;
    private TextView txtTotalDia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        dbHelper = new DBHelper(this);
        rvRelatorio = findViewById(R.id.rvRelatorio);
        txtTotalDia = findViewById(R.id.txtTotalDia);
        rvRelatorio.setLayoutManager(new LinearLayoutManager(this));

        carregarRelatorio();
    }

    private void carregarRelatorio() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Venda> vendas = new ArrayList<>();
        double totalGeral = 0;

        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TB_VENDA + " ORDER BY id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                int idxId = cursor.getColumnIndex("id");
                int idxTotal = cursor.getColumnIndex("total");
                int idxData = cursor.getColumnIndex("data_hora");

                if (idxId != -1 && idxTotal != -1) {
                    int id = cursor.getInt(idxId);
                    double total = cursor.getDouble(idxTotal);
                    String data = cursor.getString(idxData);

                    vendas.add(new Venda(id, total, data));
                    totalGeral += total;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        VendaAdapter adapter = new VendaAdapter(vendas);
        rvRelatorio.setAdapter(adapter);

        txtTotalDia.setText(String.format("R$ %.2f", totalGeral));
    }

}
