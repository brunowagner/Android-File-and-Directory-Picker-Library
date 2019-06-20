package br.com.bwsystemssolutions.androidfileanddirectorypickerlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button fileExplorerButtom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileExplorerButtom = (Button) findViewById(R.id.btn_fileExplorer);
        fileExplorerButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileExplorer();
            }
        });

    }

    public void showFileExplorer(){
        CustomFileExplorerActivity fileExplorer = new CustomFileExplorerActivity();

        Intent intent = new Intent(MainActivity.this, CustomFileExplorerActivity.class);
        startActivity(intent);

    }
}
