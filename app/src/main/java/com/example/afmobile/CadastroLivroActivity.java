package com.example.afmobile;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.afmobile.LivroGeolocalizado;
import com.example.afmobile.LocationHelper;
import com.google.firebase.firestore.FirebaseFirestore;

public class CadastroLivroActivity extends AppCompatActivity {

    private EditText etTitulo, etAutores, etObservacao;
    private Spinner spinnerSituacao, spinnerStatus;
    private Button btnSalvar;
    private LocationHelper locationHelper;
    private FirebaseFirestore db;

    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_livro);

        db = FirebaseFirestore.getInstance();
        locationHelper = new LocationHelper(this);

        // Inicializar Views (Substitua pelos IDs do seu layout XML)
        etTitulo = findViewById(R.id.etTitulo);
        etAutores = findViewById(R.id.etAutores);
        etObservacao = findViewById(R.id.etObservacao);
        spinnerSituacao = findViewById(R.id.spinnerSituacao);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnSalvar = findViewById(R.id.btnSalvar);

        // Captura a localização assim que abre a tela de cadastro
        capturarLocalizacao();

        btnSalvar.setOnClickListener(v -> salvarLivroNoFirebase());
    }

    private void capturarLocalizacao() {
        locationHelper.getDeviceLocation(new LocationHelper.LocationCallback() {
            @Override
            public void onLocationResult(double latitude, double longitude) {
                currentLatitude = latitude;
                currentLongitude = longitude;
                Toast.makeText(CadastroLivroActivity.this, "Localização capturada com sucesso!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied() {
                Toast.makeText(CadastroLivroActivity.this, "Permissão de GPS negada. O livro será salvo sem coordenadas exatas.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void salvarLivroNoFirebase() {
        String titulo = etTitulo.getText().toString();
        String autores = etAutores.getText().toString();
        String situacao = spinnerSituacao.getSelectedItem().toString();
        String status = spinnerStatus.getSelectedItem().toString();
        String obs = etObservacao.getText().toString();

        if (titulo.isEmpty()) {
            etTitulo.setError("O título é obrigatório");
            return;
        }

        LivroGeolocalizado novoLivro = new LivroGeolocalizado(
                titulo, autores, "2026", "Editora Exemplo",
                currentLatitude, currentLongitude, situacao, status, obs
        );

        db.collection("livros")
                .add(novoLivro)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Livro salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    finish(); // Fecha a tela e volta para a listagem
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Erro ao salvar.", Toast.LENGTH_SHORT).show());
    }

    // Trata a resposta da permissão do GPS
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LocationHelper.LOCATION_REQUEST_CODE) {
            capturarLocalizacao(); // Tenta capturar novamente após a resposta da permissão
        }
    }
}