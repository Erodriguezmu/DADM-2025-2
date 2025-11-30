import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerFiltro;
    private EditText etValorFiltro;
    private Button btnBuscar;
    private ProgressBar progressBar;
    private TextView tvResultados;
    private RecyclerView recyclerView;
    private BeneficiarioAdapter adapter;
    private List<Beneficiario> listaBeneficiarios;

    private static final String BASE_URL = "https://www.datos.gov.co/resource/xfif-myr2.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarVistas();
        configurarSpinner();
        configurarRecyclerView();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarBusqueda();
            }
        });

        // Cargar datos iniciales
        new ObtenerDatosTask().execute(BASE_URL);
    }

    private void inicializarVistas() {
        spinnerFiltro = findViewById(R.id.spinnerFiltro);
        etValorFiltro = findViewById(R.id.etValorFiltro);
        btnBuscar = findViewById(R.id.btnBuscar);
        progressBar = findViewById(R.id.progressBar);
        tvResultados = findViewById(R.id.tvResultados);
        recyclerView = findViewById(R.id.recyclerView);
        listaBeneficiarios = new ArrayList<>();
    }

    private void configurarSpinner() {
        String[] filtros = {
            "Sin filtro",
            "Por Departamento",
            "Por Municipio",
            "Por Estado",
            "Por Tipo de Beneficio"
        };
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(
            this, android.R.layout.simple_spinner_item, filtros);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltro.setAdapter(adapterSpinner);
    }

    private void configurarRecyclerView() {
        adapter = new BeneficiarioAdapter(listaBeneficiarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void realizarBusqueda() {
        String filtroSeleccionado = spinnerFiltro.getSelectedItem().toString();
        String valorFiltro = etValorFiltro.getText().toString().trim();

        String urlConsulta = BASE_URL;

        if (!valorFiltro.isEmpty()) {
            switch (filtroSeleccionado) {
                case "Por Departamento":
                    urlConsulta += "?nombredepartamentoatencion=" + valorFiltro;
                    break;
                case "Por Municipio":
                    urlConsulta += "?nombremunicipioatencion=" + valorFiltro;
                    break;
                case "Por Estado":
                    urlConsulta += "?estadobeneficiario=" + valorFiltro;
                    break;
                case "Por Tipo de Beneficio":
                    urlConsulta += "?tipobeneficio=" + valorFiltro;
                    break;
            }
        }

        new ObtenerDatosTask().execute(urlConsulta);
    }

    private class ObtenerDatosTask extends AsyncTask<String, Void, List<Beneficiario>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        @Override
        protected List<Beneficiario> doInBackground(String... urls) {
            List<Beneficiario> resultado = new ArrayList<>();
            HttpURLConnection connection = null;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONArray jsonArray = new JSONArray(response.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Beneficiario beneficiario = new Beneficiario();

                        // Mapear todos los campos del JSON al objeto Beneficiario
                        if (jsonObject.has("bancarizado")) 
                            beneficiario.setBancarizado(jsonObject.getString("bancarizado"));
                        if (jsonObject.has("codigodepartamentoatencion")) 
                            beneficiario.setCodigoDepartamentoAtencion(jsonObject.getString("codigodepartamentoatencion"));
                        if (jsonObject.has("codigomunicipioatencion")) 
                            beneficiario.setCodigoMunicipioAtencion(jsonObject.getString("codigomunicipioatencion"));
                        if (jsonObject.has("discapacidad")) 
                            beneficiario.setDiscapacidad(jsonObject.getString("discapacidad"));
                        if (jsonObject.has("estadobeneficiario")) 
                            beneficiario.setEstadoBeneficiario(jsonObject.getString("estadobeneficiario"));
                        if (jsonObject.has("etnia")) 
                            beneficiario.setEtnia(jsonObject.getString("etnia"));
                        if (jsonObject.has("fechainscripcionbeneficiario")) 
                            beneficiario.setFechaInscripcionBeneficiario(jsonObject.getString("fechainscripcionbeneficiario"));
                        if (jsonObject.has("genero")) 
                            beneficiario.setGenero(jsonObject.getString("genero"));
                        if (jsonObject.has("nivelescolaridad")) 
                            beneficiario.setNivelEscolaridad(jsonObject.getString("nivelescolaridad"));
                        if (jsonObject.has("nombredepartamentoatencion")) 
                            beneficiario.setNombreDepartamentoAtencion(jsonObject.getString("nombredepartamentoatencion"));
                        if (jsonObject.has("nombremunicipioatencion")) 
                            beneficiario.setNombreMunicipioAtencion(jsonObject.getString("nombremunicipioatencion"));
                        if (jsonObject.has("pais")) 
                            beneficiario.setPais(jsonObject.getString("pais"));
                        if (jsonObject.has("tipoasignacionbeneficio")) 
                            beneficiario.setTipoAsignacionBeneficio(jsonObject.getString("tipoasignacionbeneficio"));
                        if (jsonObject.has("tipobeneficio")) 
                            beneficiario.setTipoBeneficio(jsonObject.getString("tipobeneficio"));
                        if (jsonObject.has("tipodocumento")) 
                            beneficiario.setTipoDocumento(jsonObject.getString("tipodocumento"));
                        if (jsonObject.has("tipopoblacion")) 
                            beneficiario.setTipoPoblacion(jsonObject.getString("tipopoblacion"));
                        if (jsonObject.has("rangobeneficioconsolidadoasignado")) 
                            beneficiario.setRangoBeneficioConsolidadoAsignado(jsonObject.getString("rangobeneficioconsolidadoasignado"));
                        if (jsonObject.has("rangoultimobeneficioasignado")) 
                            beneficiario.setRangoUltimoBeneficioAsignado(jsonObject.getString("rangoultimobeneficioasignado"));
                        if (jsonObject.has("fechaultimobeneficioasignado")) 
                            beneficiario.setFechaUltimoBeneficioAsignado(jsonObject.getString("fechaultimobeneficioasignado"));
                        if (jsonObject.has("rangoedad")) 
                            beneficiario.setRangoEdad(jsonObject.getString("rangoedad"));
                        if (jsonObject.has("titular")) 
                            beneficiario.setTitular(jsonObject.getString("titular"));
                        if (jsonObject.has("cantidaddebeneficiarios")) 
                            beneficiario.setCantidadBeneficiarios(jsonObject.getString("cantidaddebeneficiarios"));

                        resultado.add(beneficiario);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(List<Beneficiario> beneficiarios) {
            super.onPostExecute(beneficiarios);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            listaBeneficiarios.clear();
            listaBeneficiarios.addAll(beneficiarios);
            adapter.actualizarDatos(listaBeneficiarios);
            tvResultados.setText("Total de resultados: " + beneficiarios.size());
        }
    }
}