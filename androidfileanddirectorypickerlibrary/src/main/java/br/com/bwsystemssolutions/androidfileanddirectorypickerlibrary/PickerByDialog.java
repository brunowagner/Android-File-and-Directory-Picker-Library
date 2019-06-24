package br.com.bwsystemssolutions.androidfileanddirectorypickerlibrary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import br.com.bwsystemssolutions.androidfileanddirectorypickerlibrary.adapter.MyListAdapter;

public class PickerByDialog implements DialogInterface.OnClickListener {

    private Context mContext;
    private int mthemeResId;

    public static final String TITLE_STRING_NAME = "title";

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
    private String m_subTilte = "";
    private int m_itemBackgroundColor;
    private int m_selectedItemBackgroundColor;

    public PickerByDialog(Context context, String root){
        mContext = context;
        m_root = root;
    }

    public PickerByDialog(Context context, String root, int themeResId){
        mContext = context;
        m_root = root;
        mthemeResId = themeResId;
    }

    public void setTitle(String title){
        m_title = title;
    }

    public void setSubTitle(String subTitle){
        m_subTilte = subTitle;
    }

    public void show(){

        AlertDialog.Builder dialogBuilder;

        dialogBuilder = mthemeResId > 0 ? new AlertDialog.Builder(mContext, mthemeResId) : new AlertDialog.Builder(mContext);

        dialogBuilder.setPositiveButton("Selecionar", this);
        dialogBuilder.setNegativeButton("Cancelar", this);
        dialogBuilder.setNeutralButton("Nova Pasta", this);

        if (m_title.length()>0) dialogBuilder.setTitle(m_title);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.list_row, null);

        if (m_subTilte.length()>0) {
            TextView textView = view.findViewById(R.id.tv_title);
            textView.setVisibility(View.VISIBLE);
            textView.setText(m_subTilte);
        }

        m_listAdapter=new MyListAdapter(mContext,m_item,m_path, m_isRoot);

        m_RootList = view.findViewById(R.id.lv_lvListRoot);

        getDirFromRoot(m_root);

        dialogBuilder.setView(view);

        AlertDialog dialog = dialogBuilder.create();

        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
                    m_files.add(file.getName());
                    m_filesPath.add(file.getPath());
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
        m_listAdapter=new MyListAdapter(mContext,m_item,m_path,m_isRoot);
        m_listAdapter.setItemBackgroundColor(m_itemBackgroundColor, m_selectedItemBackgroundColor);
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
                    Toast.makeText(mContext, "This is File", Toast.LENGTH_SHORT).show();
                    Log.d("bwvm", "onItemClick: position" + position);

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

    @Override
    public void onClick(DialogInterface dialog, int which) {

        int pressedButtom = which;

        switch (pressedButtom){
            case DialogInterface.BUTTON_POSITIVE:
                select();
                dialog.cancel();
                dialog.dismiss();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                cancel();
                dialog.cancel();
                dialog.dismiss();
                break;

            case DialogInterface.BUTTON_NEUTRAL:
                newFolder();
                dialog.cancel();
                dialog.dismiss();
        }
    }

    private void select(){
        Toast.makeText(mContext,"Botão de 'seleção' foi clicado!", Toast.LENGTH_SHORT).show();
    }

    private void cancel(){
        Toast.makeText(mContext,"Botão de 'cancelamento' foi clicado!", Toast.LENGTH_SHORT).show();
    }

    private void newFolder(){
        Toast.makeText(mContext,"Botão de 'Nova Pasta' foi clicado!", Toast.LENGTH_SHORT).show();
    }

    public void setItemBackgroundColor(int defaultColor, int selectedColor) {
        this.m_itemBackgroundColor = defaultColor;
        this.m_selectedItemBackgroundColor = selectedColor;
    }
}
