package com.example.afmobile;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.afmobile.LivroGeolocalizado;
import java.util.List;

public class LivroAdapter extends RecyclerView.Adapter<LivroAdapter.LivroViewHolder> {

    private final List<LivroGeolocalizado> livros;
    private final OnLivroClickListener clickListener;
    private final OnLivroLongClickListener longClickListener;

    // Interfaces para capturar os cliques na MainActivity
    public interface OnLivroClickListener {
        void onLivroClick(LivroGeolocalizado livro);
    }

    public interface OnLivroLongClickListener {
        void onLivroLongClick(LivroGeolocalizado livro);
    }

    public LivroAdapter(List<LivroGeolocalizado> livros,
                        OnLivroClickListener clickListener,
                        OnLivroLongClickListener longClickListener) {
        this.livros = livros;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public LivroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_livro, parent, false);
        return new LivroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LivroViewHolder holder, int position) {
        LivroGeolocalizado livro = livros.get(position);
        holder.bind(livro, clickListener, longClickListener);
    }

    @Override
    public int getItemCount() {
        return livros.size();
    }

    static class LivroViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitulo, tvAutor, tvStatus, tvSituacao, tvCoordenadas;

        public LivroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvItemTitulo);
            tvAutor = itemView.findViewById(R.id.tvItemAutor);
            tvStatus = itemView.findViewById(R.id.tvItemStatus);
            tvSituacao = itemView.findViewById(R.id.tvItemSituacao);
            tvCoordenadas = itemView.findViewById(R.id.tvItemCoordenadas);
        }

        public void bind(LivroGeolocalizado livro,
                         OnLivroClickListener clickListener,
                         OnLivroLongClickListener longClickListener) {

            tvTitulo.setText(livro.getTitulo());
            tvAutor.setText("Autor: " + livro.getAutores());
            tvStatus.setText("Status: " + livro.getStatusLeitura());
            tvSituacao.setText("Local: " + livro.getSituacao());
            tvCoordenadas.setText(String.format("Lat: %.4f | Lon: %.4f", livro.getLatitude(), livro.getLongitude()));

            // Configura o Clique Curto
            itemView.setOnClickListener(v -> clickListener.onLivroClick(livro));

            // Configura o Clique Longo
            itemView.setOnLongClickListener(v -> {
                longClickListener.onLivroLongClick(livro);
                return true; // Retorna true para consumir o evento e não disparar o clique curto junto
            });
        }
    }
}