package br.com.woodi.mininl.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "nl_mobile.db";
    private static final int VERSAO_BANCO = 1;

    public static final String TB_PRODUTO = "PRODUTO";
    public static final String TB_GV_NOTAS = "GV_NOTAS";
    public static final String TB_GV_ITEM = "GV_NOTAS_ITEM";
    public static final String TB_VENDA = "venda";

    public DBHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlProduto = "CREATE TABLE " + TB_PRODUTO + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "descricao TEXT, "
                + "ean TEXT, "
                + "preco_venda REAL, "
                + "estoque REAL)";
        db.execSQL(sqlProduto);

        String sqlNotas = "CREATE TABLE " + TB_GV_NOTAS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "num_nota INTEGER, "
                + "cod_emp INTEGER, "
                + "data_hora TEXT, "
                + "tip_nota INTEGER, "
                + "tip_status INTEGER, "
                + "val_total REAL)";
        db.execSQL(sqlNotas);

        String sqlItem = "CREATE TABLE " + TB_GV_ITEM + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "id_nota INTEGER, "
                + "id_produto INTEGER, "
                + "qtd REAL, "
                + "val_unitario REAL, "
                + "val_total_item REAL, "
                + "FOREIGN KEY(id_nota) REFERENCES " + TB_GV_NOTAS + "(id))";
        db.execSQL(sqlItem);

        String sqlVenda = "CREATE TABLE " + TB_VENDA + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "data_hora DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + "total DOUBLE"
                + ")";
        db.execSQL(sqlVenda);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_GV_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TB_GV_NOTAS);
        db.execSQL("DROP TABLE IF EXISTS " + TB_PRODUTO);
        onCreate(db);
    }

}