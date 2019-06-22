package br.com.bwsystemssolutions.androidfileanddirectorypickerlibrary;

import android.content.Intent;
import android.os.Environment;
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

        pickerByActivityButtom = (Button) findViewById(R.id.btn_picker_by_dialog);
        pickerByActivityButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerByDialog();
            }
        });
    }

    public void showPickerByActivity(){
        Intent intent = new Intent(MainActivity.this, CustomFileExplorerActivity.class);
        intent.putExtra(CustomFileExplorerActivity.TITLE_STRING_NAME, "Selecione o arquivo");
        startActivity(intent);

    }

    public void showPickerByDialog(){

        String root = getExternalFilesDir(null).getAbsolutePath();
        PickerByDialog pickerByDialog = new PickerByDialog(this, root);
        pickerByDialog.setTitle("Selecione o Arquivo:");
        pickerByDialog.setSubTitle("Navegue pelas pastas abaixo:");
        pickerByDialog.show();
    }



}
