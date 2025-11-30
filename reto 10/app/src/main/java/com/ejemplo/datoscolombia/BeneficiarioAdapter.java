import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BeneficiarioAdapter extends RecyclerView.Adapter<BeneficiarioAdapter.BeneficiarioViewHolder> {

    private List<Beneficiario> beneficiarios;

    public BeneficiarioAdapter(List<Beneficiario> beneficiarios) {
        this.beneficiarios = beneficiarios;
    }

    @NonNull
    @Override
    public BeneficiarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_beneficiario, parent, false);
        return new BeneficiarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BeneficiarioViewHolder holder, int position) {
        Beneficiario beneficiario = beneficiarios.get(position);
        holder.bind(beneficiario);
    }

    @Override
    public int getItemCount() {
        return beneficiarios.size();
    }

    public void actualizarDatos(List<Beneficiario> nuevosBeneficiarios) {
        this.beneficiarios = nuevosBeneficiarios;
        notifyDataSetChanged();
    }

    static class BeneficiarioViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDepartamento;
        private TextView tvMunicipio;
        private TextView tvEstado;
        private TextView tvTipoBeneficio;

        public BeneficiarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDepartamento = itemView.findViewById(R.id.tvDepartamento);
            tvMunicipio = itemView.findViewById(R.id.tvMunicipio);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvTipoBeneficio = itemView.findViewById(R.id.tvTipoBeneficio);
        }

        public void bind(Beneficiario beneficiario) {
            tvDepartamento.setText(beneficiario.getNombreDepartamentoAtencion() != null ? 
                beneficiario.getNombreDepartamentoAtencion() : "N/A");
            tvMunicipio.setText(beneficiario.getNombreMunicipioAtencion() != null ? 
                beneficiario.getNombreMunicipioAtencion() : "N/A");
            tvEstado.setText(beneficiario.getEstadoBeneficiario() != null ? 
                beneficiario.getEstadoBeneficiario() : "N/A");
            tvTipoBeneficio.setText(beneficiario.getTipoBeneficio() != null ? 
                beneficiario.getTipoBeneficio() : "N/A");
        }
    }
}