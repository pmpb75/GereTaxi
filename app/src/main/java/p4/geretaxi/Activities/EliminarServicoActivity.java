package p4.geretaxi.Activities;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import p4.geretaxi.Constantes.Constants;
import p4.geretaxi.WebServiceClass.GereBD;
import p4.geretaxi.ClassesHelper.Helper;
import p4.geretaxi.R;
import p4.geretaxi.ClassesDados.Servico;
import p4.geretaxi.ClassesHelper.SharedPreference;
import p4.geretaxi.ClassesHelper.XMLHandler;

public class EliminarServicoActivity extends AppCompatActivity {

    EditText edtPesquisarServico;
    ListView listViewResultados;

    List<String> listItems;
    ArrayAdapter<String> adapter;
    private Servico servico;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_servico);

        edtPesquisarServico = (EditText) findViewById(R.id.edtPesquisarServico);
        listViewResultados = (ListView) findViewById(R.id.listViewResultados);
    }

    public void onClickEliminarServico(View v) {

        GereBD bd = new GereBD();
        if (Helper.isNetworkAvailable(getApplicationContext())) {
            boolean res = bd.excluirServico(servico.getProcesso(),
                    SharedPreference.getIdMotoristaSharedPreferences(getApplicationContext()));
            if (res) {
                File file = new File(Environment.getExternalStorageDirectory(), "servicos.xml");
                if(file.exists()) {
                    XMLHandler parser = new XMLHandler();
                    List<Servico> servicos;
                    servicos = parser.parseServico(Xml.newPullParser());
                    if (servicos.size() < 1) {
                        for (Servico s : servicos) {
                            if (s.getProcesso().equalsIgnoreCase(servico.getProcesso())) {
                                servicos.remove(s);
                            }
                        }

                        boolean deleted = file.delete();
                        if (deleted) {
                            for (Servico s : servicos) {

                                parser.writeServico(s);
                            }
                            Toast.makeText(getApplicationContext(), "Serviço apagado com Sucesso.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    listViewResultados.setAdapter(null);
                    edtPesquisarServico.setText("");
                    Toast.makeText(getApplicationContext(), "Serviço Apagado com Sucesso.", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Não foi possivel eliminar o Serviço que pretendia.", Toast.LENGTH_LONG).show();
            }
        }

    }
    public void onClickProcurarServico(View v) {
        if(!Helper.isEmpty(edtPesquisarServico)) {
            GereBD bd = new GereBD();
            listItems = new ArrayList<>();
            servico = bd.pesquisarServico(edtPesquisarServico.getText().toString(), SharedPreference.getIdMotoristaSharedPreferences(getApplicationContext()));
            if (servico.getProcesso().equals(edtPesquisarServico.getText().toString())) {
                populateListView();
                adapter = new ArrayAdapter<>(this, R.layout.item_list, listItems);
                listViewResultados.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "Não foi possivel encontrar o Serviço que procurava.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void populateListView() {
        listItems.add(Constants.PROCESSO_TOSTRING + servico.getProcesso());
        listItems.add(Constants.CLIENTE_TOSTRING + servico.getNomeCliente());
        listItems.add(Constants.TIPO_SERVICO_TOSTRING + servico.getTipo());
        listItems.add(Constants.HORA_DE_INICIO_TOSTRING + servico.getHoraDeInicio());
        listItems.add(Constants.DATA_TOSTRING + servico.getData());
        listItems.add(Constants.ORIGEM_TOSTRING + servico.getOrigem());
        listItems.add(Constants.DESTINO_TOSTRING + servico.getDestino());
        listItems.add(Constants.DISTANCIA_TOSTRING + servico.getDistancia());
        listItems.add(Constants.HORAS_DE_ESPERA_TOSTRING+ servico.getHorasDeEspera());
        listItems.add(Constants.NUM_PASSAGEIROS_TOSTRING+servico.getNumPassageiros());
        listItems.add(Constants.CUSTO_PORTAGENS_TOSTRING+servico.getCustoPortagens());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.logout_id:
                SharedPreference sharedPreference = new SharedPreference();
                sharedPreference.save(getApplicationContext(), " ", Constants.EMAIL);
                sharedPreference.save(getApplicationContext(), " ", Constants.PASS);
                sharedPreference.save(getApplicationContext(), -1, Constants.ID_MOTORISTA);
                sharedPreference.save(getApplicationContext(), Constants.FALSE, Constants.SESSION);
                sharedPreference.save(getApplicationContext(), Helper.getExpirationDate(), Constants.VALIDADE);
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;

            case R.id.settings_id:
                Intent in = new Intent(this, CoordenadasActivity.class);
                startActivity(in);

                break;

            case R.id.inicio_id:
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
