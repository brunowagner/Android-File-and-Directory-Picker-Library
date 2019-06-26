package br.com.bwsystemssolutions.androidfileanddirectorypickerlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button pickerByActivityButtom;
    static final int PICK_RESPONSE_CODE = 1;

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
        intent.putExtra(CustomFileExplorerActivity.SUBTITLE_STRING_NAME, "Navegue pelas pastas abaixo:");
        intent.putExtra(CustomFileExplorerActivity.ITEM_DEFAULT_BACKGROUND_COLOR_STRING_NAME, getResources().getColor(R.color.defaulBackgroudColor));
        intent.putExtra(CustomFileExplorerActivity.ITEM_SELECTED_BACKGROUND_COLOR_STRING_NAME, getResources().getColor(R.color.selectedBackgroudColor));
        intent.putExtra(CustomFileExplorerActivity.SELECT_TYPE_STRING_NAME, CustomFileExplorerActivity.SELECT_TYPE_ANY);
        intent.putExtra(CustomFileExplorerActivity.SELECT_BUTTON_TITLE_STRING_NAME, getResources().getString(R.string.select_button_title));
        intent.putExtra(CustomFileExplorerActivity.NEW_FOLDER_BUTTON_TITLE_STRING_NAME, getResources().getString(R.string.new_folder_button_title));
        intent.putExtra(CustomFileExplorerActivity.CANCEL_BUTTON_TITLE_STRING_NAME, getResources().getString(R.string.cancel_button_title));
        intent.putExtra(CustomFileExplorerActivity.INPUT_NEW_FOLDER_TITLE_STRING_NAME, "Nome da nova pasta:");
        startActivityForResult(intent, PICK_RESPONSE_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_RESPONSE_CODE) {
            if(resultCode == RESULT_OK){
                String path = data.getStringExtra(CustomFileExplorerActivity.RESPONSE_STRING_NAME);
                Toast.makeText(MainActivity.this, path, Toast.LENGTH_LONG).show();
            } else if(resultCode == RESULT_CANCELED){
                Toast.makeText(MainActivity.this, "Foi cancelado!", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    

    public void showPickerByDialog(){

        String root = getExternalFilesDir(null).getAbsolutePath();
        PickerByDialog pickerByDialog = new PickerByDialog(this, root);
        pickerByDialog.setTitle("Selecione o Arquivo:");
        pickerByDialog.setSubTitle("Navegue pelas pastas abaixo:");
        pickerByDialog.setItemBackgroundColor(getResources().getColor(R.color.defaulBackgroudColor),getResources().getColor(R.color.selectedBackgroudColor));
        pickerByDialog.setSelectType(PickerByDialog.SELECT_TYPE_ANY);
        pickerByDialog.setSelectButtonTitle(getResources().getString(R.string.select_button_title));
        pickerByDialog.setNewFolderButtonTitle(getResources().getString(R.string.new_folder_button_title));
        pickerByDialog.setCancelButtonTitle(getResources().getString(R.string.cancel_button_title));
        pickerByDialog.setInputNewFolderTitle("Nome da nova pasta:");
        pickerByDialog.setOnResponseListener(new PickerByDialog.OnResponseListener() {
            @Override
            public void onResponse(boolean canceled, String response) {
                if (canceled){
                    Toast.makeText(MainActivity.this, "Foi cancelado!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                }
            }
        });

        pickerByDialog.show();
    }



}
