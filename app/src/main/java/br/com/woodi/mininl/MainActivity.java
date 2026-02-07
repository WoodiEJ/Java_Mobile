package br.com.woodi.mininl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


import br.com.woodi.mininl.database.DBHelper;
import br.com.woodi.mininl.view.VendaActivity;
import br.com.woodi.mininl.view.RelatorioActivity;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsuario, edtSenha;
    private Button btnEntrar, btnSincronizar, btnRelatorio;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUsuario = findViewById(R.id.edtUsuario);
        edtSenha = findViewById(R.id.edtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnSincronizar = findViewById(R.id.btnSincronizar);
        btnRelatorio = findViewById(R.id.btnRelatorio);

        dbHelper = new DBHelper(this);

        btnSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarCargaInicial();
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = edtUsuario.getText().toString();
                if (usuario.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Informe o usuário!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Bem vindo ao PDV Mobile!", Toast.LENGTH_SHORT).show();
                    startActivity(new android.content.Intent(MainActivity.this, VendaActivity.class));
                }
            }
        });

        btnRelatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new android.content.Intent(MainActivity.this, br.com.woodi.mininl.view.RelatorioActivity.class));
            }
        });
    }

    private void realizarCargaInicial() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + DBHelper.TB_PRODUTO);

        inserirProduto(db, "COCA COLA 2L", "7894900011517", 14.50, 100);
        inserirProduto(db, "ARROZ TIO JOAO 1KG", "7891000053508", 7.99, 50);
        inserirProduto(db, "CHOCOLATE BARRA", "7891000100103", 5.49, 200);

        Toast.makeText(this, "CARGA REALIZADA! Dados atualizados.", Toast.LENGTH_SHORT).show();
    }

    private void inserirProduto(SQLiteDatabase db, String nome, String ean, double preco, double estoque) {
        ContentValues values = new ContentValues();
        values.put("descricao", nome);
        values.put("ean", ean);
        values.put("preco_venda", preco);
        values.put("estoque", estoque);
        db.insert(DBHelper.TB_PRODUTO, null, values);
    }

    private void contarProdutos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM " + DBHelper.TB_PRODUTO, null);
        if (cursor.moveToFirst()) {
            int qtd = cursor.getInt(0);
            System.out.println("MINI_NL: Total de produtos no banco: " + qtd);
        }
        cursor.close();
    }
}