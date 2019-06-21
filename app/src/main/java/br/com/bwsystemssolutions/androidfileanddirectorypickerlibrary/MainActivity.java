package br.com.bwsystemssolutions.androidfileanddirectorypickerlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button pickerByActivityButtom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickerByActivityButtom = (Button) findViewById(R.id.btn_picker_by_activity);
        pickerByActivityButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerByActivity();
            }
        });
    }

    public void showPickerByActivity(){
        Intent intent = new Intent(MainActivity.this, CustomFileExplorerActivity.class);
        intent.putExtra(CustomFileExplorerActivity.TITLE_STRING_NAME, "Selecione o arquivo");
        startActivity(intent);

    }

    public void showPickerByDialog(){
        //
    }



}
