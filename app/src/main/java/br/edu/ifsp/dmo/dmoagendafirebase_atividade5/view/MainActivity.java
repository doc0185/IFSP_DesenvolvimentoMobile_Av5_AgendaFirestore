package br.edu.ifsp.dmo.dmoagendafirebase_atividade5.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import br.edu.ifsp.dmo.dmoagendafirebase_atividade5.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.edu.ifsp.dmo.dmoagendafirebase_atividade5.mvp.MainMVP;
import br.edu.ifsp.dmo.dmoagendafirebase_atividade5.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity
        implements MainMVP.View, View.OnClickListener, SearchView.OnQueryTextListener{

    private MainMVP.Presenter presenter;
    private FloatingActionButton mActionButton;
    private RecyclerView mRecyclerView;
    private SearchView editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findById();
        setListener();
        presenter = new MainPresenter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.populate(mRecyclerView, null);
        presenter.startListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stopListener();
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v == mActionButton){
            Intent intent = new Intent(this, DetalhesActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void findById(){
        mActionButton = findViewById(R.id.fab_new_contact);
        mRecyclerView = findViewById(R.id.recyler_view);
        editSearch = findViewById(R.id.search);
        editSearch.setOnQueryTextListener(this);
    }

    private void setListener(){
        mActionButton.setOnClickListener(this);
    }


    @Override
    public boolean onQueryTextSubmit(String s) {

        presenter.populate(mRecyclerView, s);
        presenter.startListener();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        presenter.populate(mRecyclerView, s);
        presenter.startListener();
        return false;
    }
}