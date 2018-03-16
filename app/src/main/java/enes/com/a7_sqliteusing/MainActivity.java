package enes.com.a7_sqliteusing;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button btnEkle;
    EditText txtBaslik, txtAciklama;
    ListView listData;

    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<ProList> prls = new ArrayList<ProList>();

    DB db = new DB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEkle = findViewById(R.id.btnEkle);
        txtBaslik = findViewById(R.id.txtBaslik);
        txtAciklama = findViewById(R.id.txtAciklama);
        listData = findViewById(R.id.listData);

        btnEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String b = txtBaslik.getText().toString().trim();
                String a = txtAciklama.getText().toString().trim();
                SQLiteDatabase yaz = db.getWritableDatabase();

                String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());

                //inster operation
                ContentValues con = new ContentValues();
                con.put("baslik", b);
                con.put("aciklama", a);
                con.put("tarih", timeStamp);
                con.put("durum", 0);

                long yazSonuc = yaz.insert("liste", null, con);
                if(yazSonuc > 0){
                    dataGetir();
                    Toast.makeText(MainActivity.this, txtBaslik.getText() + " öğesi eklendi", Toast.LENGTH_SHORT).show();
                    txtBaslik.setText("");
                    txtAciklama.setText("");
                    txtBaslik.requestFocus();

                }else{
                    Toast.makeText(MainActivity.this, "Yazma hatası!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dataGetir();
    }

    public void dataGetir(){
        SQLiteDatabase oku = db.getReadableDatabase();
        Cursor cr = oku.query("liste", null, null, null, null, null, null);
        titles.clear();
        prls.clear();
        while(cr.moveToNext()){
            String title = cr.getString(cr.getColumnIndex("baslik"));
            Log.d("title", title);
            titles.add(title);

            ProList pr = new ProList();
            pr.setAciklama(cr.getString(cr.getColumnIndex("aciklama")));
            pr.setBaslik(cr.getString(cr.getColumnIndex("baslik")));
            pr.setDurum(cr.getInt(cr.getColumnIndex("durum")));
            pr.setLid(cr.getInt(cr.getColumnIndex("lid")));
            pr.setTarih(cr.getString(cr.getColumnIndex("tarih")));
            prls.add(pr);
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, titles);
        listData.setAdapter(adp);

        oku.close();

        listData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Detail.pr = prls.get(i);
                Intent in = new Intent(MainActivity.this, Detail.class);
                startActivity(in);
            }
        });

        listData.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                AlertDialog.Builder uyari = new AlertDialog.Builder(MainActivity.this);
                uyari.setTitle("Veri Silme");
                uyari.setMessage("Veriyi silmek istediğinize emin misiniz?");
                uyari.setCancelable(false);
                uyari.setIcon(R.mipmap.warning);

                uyari.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        SQLiteDatabase sil = db.getWritableDatabase();
                        int sDurum = sil.delete("liste", "lid = " + prls.get(i).getLid(), null);
                        if(sDurum > 0){
                            dataGetir();
                            Toast.makeText(MainActivity.this, "Silme işlemi başarılı!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Silme işlemi başarısız!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                uyari.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Silme işlemi ipal edildi.", Toast.LENGTH_SHORT).show();
                    }
                });

                uyari.create().show();

                return false;
            }
        });
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        dataGetir();
    }

}
