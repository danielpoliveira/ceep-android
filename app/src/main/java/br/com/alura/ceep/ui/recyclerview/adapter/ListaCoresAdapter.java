package br.com.alura.ceep.ui.recyclerview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.model.Cor;

public class ListaCoresAdapter extends RecyclerView.Adapter<ListaCoresAdapter.CorViewHolder> {

    private final Context context;
    private final List<Cor> cores;
    private OnItemClickListener onItemClickListener;

    public ListaCoresAdapter(Context context, List<Cor> cores) {
        this.context = context;
        this.cores = cores;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater
                .from(context)
                .inflate(R.layout.item_cor, parent, false);

        return new CorViewHolder(viewCriada);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull CorViewHolder holder, int position) {
        Cor cor = cores.get(position);
        holder.vincula(cor);
    }

    @Override
    public int getItemCount() {
        return cores.size();
    }

    class CorViewHolder extends RecyclerView.ViewHolder {

        private final View corView;
        private Cor cor;

        public CorViewHolder(@NonNull View itemView) {
            super(itemView);
            corView = itemView.findViewById(R.id.item_cor_view);
            itemView.setOnClickListener(e -> {
                onItemClickListener.onItemClick(cor);
            });
        }

        public void vincula(Cor cor) {
            this.cor = cor;

            corView.getBackground()
                    .mutate()
                    .setColorFilter(
                            Color.parseColor(cor.getCorString()),
                            PorterDuff.Mode.SRC_ATOP
                    );
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Cor cor);
    }


}