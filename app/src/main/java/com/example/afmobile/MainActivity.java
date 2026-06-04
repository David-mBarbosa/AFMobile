package com.example.afmobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.afmobile.LivroGeolocalizado;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LivroAdapter adapter;
    private List<LivroGeolocalizado> listaLivros;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        listaLivros = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewLivros);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Configura o adapter passando os eventos de clique exigidos pelo enunciado
        adapter = new LivroAdapter(listaLivros,
                this::abrirDialogEditar,  // Clique Curto (Editar)
                this::abrirDialogExcluir  // Clique Longo (Excluir)
        );
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAdicionar);
        fab.setOnClickListener(v -> startActivity(new Intent(this, CadastroLivroActivity.class)));

        carregarLivrosDoFirebase();
    }

    private void carregarLivrosDoFirebase() {
        db.collection("livros").addSnapshotListener((value, error) -> {
            if (error != null || value == null) return;

            listaLivros.clear();
            for (DocumentSnapshot doc : value.getDocuments()) {
                LivroGeolocalizado livro = doc.toObject(LivroGeolocalizado.class);
                if (livro != null) {
                    livro.setId(doc.getId()); // Guarda o ID gerado pelo Firebase para atualizar/deletar depois
                    listaLivros.add(livro);
                }
            }
            adapter.notifyDataSetChanged();
        });
    }

    // Funcionalidade 4: Clique Curto para Visualização e Edição
    private void abrirDialogEditar(LivroGeolocalizado livro) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Status do Livro");

        // Layout inflado dinamicamente ou via XML para edição rápida
        // Aqui estamos alterando apenas o status de leitura e observação como solicitado
        final Spinner spinnerStatus = new Spinner(this);
        String[] opcoes = {"Quero ler", "Lendo", "Concluído"};
        spinnerStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, opcoes));

        builder.setView(spinnerStatus);

        builder.setPositiveButton("Salvar", (dialog, which) -> {
            String novoStatus = spinnerStatus.getSelectedItem().toString();
            db.collection("livros").document(livro.getId())
                    .update("statusLeitura", novoStatus)
                    .addOnSuccessListener(aVoid -> carregarLivrosDoFirebase());
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    // Funcionalidade 4: Clique Longo para Exclusão
    private void abrirDialogExcluir(LivroGeolocalizado livro) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Livro")
                .setMessage("Deseja realmente remover " + livro.getTitulo() + " da sua lista?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    db.collection("livros").document(livro.getId())
                            .delete()
                            .addOnSuccessListener(aVoid -> carregarLivrosDoFirebase());
                })
                .setNegativeButton("Não", null)
                .show();
    }
}