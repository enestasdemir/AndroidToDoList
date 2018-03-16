package enes.com.a7_sqliteusing;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Detail extends AppCompatActivity {

    static ProList pr = null;

    DB db = new DB(this);

    Button btnDuzenle;
    EditText txtDBaslik, txtDAciklama;
    ToggleButton btnDDurum;
    TextView txtDTarih;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //back button in actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnDuzenle = findViewById(R.id.btnDuzenle);
        txtDBaslik = findViewById(R.id.txtDBaslik);
        txtDAciklama = findViewById(R.id.txtDAciklama);
        btnDDurum = findViewById(R.id.btnDDurum);
        txtDTarih = findViewById(R.id.txtDTarih);

        txtDBaslik.setText(pr.getBaslik());
        txtDAciklama.setText(pr.getAciklama());
        txtDTarih.setText(pr.getTarih());
        if(pr.getDurum() == 0){
            btnDDurum.setChecked(false);
            btnDDurum.setTextOff("Aktif");
        }else if(pr.getDurum() == 1){
            btnDDurum.setChecked(true);
            btnDDurum.setTextOn("Tamamlandı");
        }

        btnDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update sql query
                SQLiteDatabase duzenle = db.getWritableDatabase();
                ContentValues con = new ContentValues();
                con.put("baslik", txtDBaslik.getText().toString()); //textfielddan alınacak
                con.put("aciklama", txtDAciklama.getText().toString());
                if(btnDDurum.isChecked()){
                    con.put("durum", 1);
                }else{
                    con.put("durum", 0);
                }
                int dDurum = duzenle.update("liste", con, "lid = " + pr.getLid(), null);
                if(dDurum > 0){
                    finish();
                    Toast.makeText(Detail.this, "Düzenleme işlemi başarılı!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Detail.this, "Düzenleme hatası!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home: {
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
