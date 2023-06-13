package br.edu.ifsp.dmo.dmoagendafirebase_atividade5.presenter;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;

import br.edu.ifsp.dmo.dmoagendafirebase_atividade5.constant.Constants;
import br.edu.ifsp.dmo.dmoagendafirebase_atividade5.model.Contato;
import br.edu.ifsp.dmo.dmoagendafirebase_atividade5.mvp.MainMVP;
import br.edu.ifsp.dmo.dmoagendafirebase_atividade5.view.Adapter.ContatoAdapter;
import br.edu.ifsp.dmo.dmoagendafirebase_atividade5.view.DetalhesActivity;
import br.edu.ifsp.dmo.dmoagendafirebase_atividade5.view.ItemCliclListener;


public class MainPresenter implements MainMVP.Presenter {
    private MainMVP.View view;
    private FirebaseFirestore database;
    private ContatoAdapter adapter;

    public MainPresenter(MainMVP.View view) {
        this.view = view;
        database = FirebaseFirestore.getInstance();
    }

    @Override
    public void detach() {
        this.view = null;
    }


    public void populate(RecyclerView recyclerView, String filter) {
        Query query = database.collection(Constants.CONTACTS_COLLECTION).orderBy(Constants.ATTR_NAME, Query.Direction.ASCENDING);
        if (filter!= null){
            filter = filter.toLowerCase(Locale.getDefault());
            if (filter.length() == 0){
                populate(recyclerView, null);
            } else{
                query = database.collection(Constants.CONTACTS_COLLECTION).orderBy(Constants.ATTR_NAME).startAt(filter).endAt(filter + '\uf8ff');
            }
        }

        FirestoreRecyclerOptions<Contato> options = new FirestoreRecyclerOptions.Builder<Contato>().setQuery(query, Contato.class).build();
        adapter = new ContatoAdapter(options);
        adapter.setClickListener(new ItemCliclListener() {
            @Override
            public void onClick(String referenceId) {
                abrirDetalhes(referenceId);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void startListener() {
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    public void stopListener() {
        if (adapter != null)
            adapter.stopListening();
    }

    private void abrirDetalhes(String documento) {
        Intent intent = new Intent(view.getContext(), DetalhesActivity.class);
        intent.putExtra(Constants.FIRESOTRE_DOCUMENT_KEY, documento);
        view.getContext().startActivity(intent);
    }
}
