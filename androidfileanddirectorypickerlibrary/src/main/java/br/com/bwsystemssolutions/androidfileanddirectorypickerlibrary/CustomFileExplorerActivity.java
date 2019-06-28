package br.com.bwsystemssolutions.androidfileanddirectorypickerlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import br.com.bwsystemssolutions.androidfileanddirectorypickerlibrary.adapter.MyListAdapter;

public class CustomFileExplorerActivity extends AppCompatActivity {

    public static final String TITLE_STRING_NAME = "title";
    public static final String SUBTITLE_STRING_NAME = "subtitle";
    public static final String ITEM_DEFAULT_BACKGROUND_COLOR_STRING_NAME = "itemDefaultBackgroundColor";
    public static final String ITEM_SELECTED_BACKGROUND_COLOR_STRING_NAME = "itemSelectedBackgroundColor";
    public static final String SELECT_TYPE_STRING_NAME = "selectType";
    public static final String SELECT_BUTTON_TITLE_STRING_NAME = "selectButtonTitle";
    public static final String NEW_FOLDER_BUTTON_TITLE_STRING_NAME = "newFolderButtonTitle";
    public static final String CANCEL_BUTTON_TITLE_STRING_NAME = "cancelButtonTitle";
    public static final String INPUT_NEW_FOLDER_TITLE_STRING_NAME = "inputNewFolderTitle";
    public static final int SELECT_TYPE_FOLDER = 0;
    public static final int SELECT_TYPE_FILE = 1;
    public static final int SELECT_TYPE_ANY = 2;
    public static final String RESPONSE_STRING_NAME = "response";

    private ArrayList<String> m_item;
    private ArrayList<String> m_path;
    private ArrayList<String> m_files;
    private ArrayList<String> m_filesPath;
    private String m_curDir;
    private MyListAdapter m_listAdapter;
    private ListView m_RootList;
    private String m_root = "";
    private Boolean m_isRoot=true;

    private String m_title = "";
    private String m_subTitle = "";
    private int m_itemBackgroundColor;
    private int m_selectedItemBackgroundColor;
    private int m_selectType = SELECT_TYPE_FOLDER;
    private String m_selectButtonTitle = "Select";
    private String m_newFolderButtonTitle = "New Folder";
    private String m_cancelButtonTitle = "Cancel";
    private String m_inputNewFolderTitle = "New folder name:";
    private TextView m_curDirTextView;
    //private HorizontalScrollView titleScroll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_row);

        m_RootList = findViewById(R.id.lv_lvListRoot);
        m_curDirTextView = findViewById(R.id.tv_title);
        m_curDirTextView.setVisibility(View.VISIBLE);

        // this force scrollbar go to full right
        m_curDirTextView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                ((HorizontalScrollView) v.getParent()).fullScroll(View.FOCUS_RIGHT);
            }
        });

        m_root = getExternalFilesDir(null).getAbsolutePath();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }


    private void init(){
        //   set title
        if (getIntent().hasExtra(TITLE_STRING_NAME)){
            m_title = getIntent().getStringExtra(TITLE_STRING_NAME);
        }
        if (m_title.length() > 0) {
            setTitle(m_title);
        }
        
        //  set subTitle
         if (getIntent().hasExtra(SUBTITLE_STRING_NAME)){
            m_subTitle = getIntent().getStringExtra(SUBTITLE_STRING_NAME);
        }
        if (m_subTitle.length() > 0) {
            setSubTitle(m_subTitle);
        }
        
        //  set Color
        if (getIntent().hasExtra(ITEM_DEFAULT_BACKGROUND_COLOR_STRING_NAME)){
            m_itemBackgroundColor = getIntent().getIntExtra(ITEM_DEFAULT_BACKGROUND_COLOR_STRING_NAME, 0);
        }
        if (getIntent().hasExtra(ITEM_DEFAULT_BACKGROUND_COLOR_STRING_NAME)){
            m_selectedItemBackgroundColor = getIntent().getIntExtra(ITEM_SELECTED_BACKGROUND_COLOR_STRING_NAME, 0);
        }
        
        //  set selectType
        if (getIntent().hasExtra(SELECT_TYPE_STRING_NAME)){
            m_selectType = getIntent().getIntExtra(SELECT_TYPE_STRING_NAME,0);
        }

        //  set select button title
        if (getIntent().hasExtra(SELECT_BUTTON_TITLE_STRING_NAME)){
            m_selectButtonTitle = getIntent().getStringExtra(SELECT_BUTTON_TITLE_STRING_NAME);
        }

        //  set new folder button title
        if (getIntent().hasExtra(NEW_FOLDER_BUTTON_TITLE_STRING_NAME)){
            m_newFolderButtonTitle = getIntent().getStringExtra(NEW_FOLDER_BUTTON_TITLE_STRING_NAME);
        }

        //  set input new folder title
        if (getIntent().hasExtra(INPUT_NEW_FOLDER_TITLE_STRING_NAME)){
            m_inputNewFolderTitle = getIntent().getStringExtra(INPUT_NEW_FOLDER_TITLE_STRING_NAME);
        }

        getDirFromRoot(m_root);
    }

    public void setSubTitle(String subTitle){
        m_subTitle = subTitle;
    }


    //get directories and files from selected path
    public void getDirFromRoot(String p_rootPath)
    {
        m_item = new ArrayList<String>();

        m_path = new ArrayList<String>();
        m_files = new ArrayList<String>();
        m_filesPath=new ArrayList<String>();
        File m_file = new File(p_rootPath);
        File[] m_filesArray = m_file.listFiles();
        if(!p_rootPath.equals(m_root))
        {
            m_item.add("../");
            m_path.add(m_file.getParent());
            m_isRoot=false;

        }
        m_curDirTextView.setText(m_file.getAbsolutePath());
        m_curDir=p_rootPath;

        //sorting file list in alphabetical order
        if (m_filesArray != null) {
            Arrays.sort(m_filesArray);

            for (int i = 0; i < m_filesArray.length; i++) {
                File file = m_filesArray[i];
                if (file.isDirectory()) {
                    m_item.add(file.getName());
                    m_path.add(file.getPath());
                } else {
                    if (m_selectType == SELECT_TYPE_ANY || m_selectType == SELECT_TYPE_FILE) {
                        m_files.add(file.getName());
                        m_filesPath.add(file.getPath());
                    }
                }
            }
        }
        for(String m_AddFile:m_files)
        {
            m_item.add(m_AddFile);
        }
        for(String m_AddPath:m_filesPath)
        {
            m_path.add(m_AddPath);
        }
        configureAdapter();

    }

    private void configureAdapter (){

        m_listAdapter=new MyListAdapter(this,m_item,m_path,m_isRoot);
        if (m_itemBackgroundColor != 0 && m_selectedItemBackgroundColor != 0) m_listAdapter.setItemBackgroundColor(m_itemBackgroundColor, m_selectedItemBackgroundColor);
        m_RootList.setAdapter(m_listAdapter);

        m_RootList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                File m_isFile=new File(m_path.get(position));
                if(m_isFile.isDirectory())
                {
                    getDirFromRoot(m_isFile.toString());
                }
                else
                {
                    Toast.makeText(CustomFileExplorerActivity.this, "This is File", Toast.LENGTH_SHORT).show();
                    if (m_listAdapter.m_selectedItem.contains(position) ) {
                        m_listAdapter.m_selectedItem.clear();
                    } else {
                        m_listAdapter.m_selectedItem.clear();
                        m_listAdapter.m_selectedItem.add(0, position);
                    }
                    m_listAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    void createNewFile (){
        create(0);
    }

    void createNewFolder (){
        create(1);
    }

    private void create( final int p_opt)
    {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(m_inputNewFolderTitle);

        // Set up the input
        final EditText m_edtinput = new EditText(this);
        // Specify the type of input expected;
        m_edtinput.setInputType(InputType.TYPE_CLASS_TEXT);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_text = m_edtinput.getText().toString();
                if(p_opt == 1)
                {
                    File m_newPath=new File(m_curDir,m_text);
                    if(!m_newPath.exists()) {
                        m_newPath.mkdirs();
                    }
                }
                else
                {
                    try {
                        FileOutputStream m_Output = new FileOutputStream((m_curDir+File.separator+m_text), false);
                        m_Output.close();
                        //  <!--<intent-filter>
                        //  <action android:name="android.intent.action.SEARCH" />
                        //  </intent-filter>
                        //  <meta-data android:name="android.app.searchable"
                        //  android:resource="@xml/searchable"/>-->

                    } catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                getDirFromRoot(m_curDir);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setView(m_edtinput);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,1,1, m_selectButtonTitle);
        menu.add(Menu.NONE,2,2, m_newFolderButtonTitle);
        menu.add(Menu.NONE,3,3,m_cancelButtonTitle);
        MenuItem item = menu.getItem(0);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == 1){
            String resultado;

            resultado = m_listAdapter.m_selectedItem.size() == 0 ? m_curDir :
                    m_path.get(m_listAdapter.m_selectedItem.get(0));

            Intent returnIntent = new Intent();
            returnIntent.putExtra(RESPONSE_STRING_NAME,resultado);
            setResult(RESULT_OK,returnIntent);
            finish();
        }

        if (id == 2){
            createNewFolder();
        }

        if (id == 3){
            Intent returnIntent = new Intent();
            returnIntent.putExtra(RESPONSE_STRING_NAME,"");
            setResult(RESULT_CANCELED,returnIntent);
            finish();
        }

        if (id == android.R.id.home){
            Intent returnIntent = new Intent();
            returnIntent.putExtra(RESPONSE_STRING_NAME,"");
            setResult(RESULT_CANCELED,returnIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
