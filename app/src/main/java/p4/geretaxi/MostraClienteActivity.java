package p4.geretaxi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MostraClienteActivity extends AppCompatActivity implements DialogAtualizaClienteFragment.CommunicatorCliente{

    ListView listView;
    private Cliente cliente;
    ArrayList<String> listitems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_cliente);

        listView = (ListView) findViewById(R.id.listView);

        cliente = (Cliente) getIntent().getSerializableExtra("cliente");
        populateList();
        adapter = new ArrayAdapter<>(this, R.layout.item_list, listitems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        android.support.v4.app.DialogFragment newFragment = DialogAtualizaClienteFragment.newInstance(position);
                        newFragment.show(fragmentTransaction, "dialog");

                    }
                }
        );
    }

    private void populateList() {
        listitems.add(cliente.getNome());
        listitems.add(cliente.getMorada());
        listitems.add(cliente.getCodigoPostal());
        listitems.add(cliente.getEmail());
        listitems.add(cliente.getTipo());
        listitems.add(String.valueOf(cliente.getNif()));
        listitems.add(String.valueOf(cliente.getContacto()));
    }

    @Override
    public void onDialogMessage(String dados, int num) {
        switch (num) {
            case 0:
                cliente.setNome(dados);
                //TODO: MUDAR O VALOR DA TAG NOMSE CORRESPONDENTE AO CLIENT NO FICHEIRO CLIENTES.XML
                break;
            case 1:
                cliente.setMorada(dados);
                break;
            case 2:
                cliente.setCodigoPostal(dados);
                break;
            case 3:
                cliente.setEmail(dados);
                break;
            case 4:
                cliente.setTipo(dados);
                break;
            case 5:
                cliente.setNif(Integer.parseInt(dados));
                break;
            case 6:
                cliente.setContacto(Integer.parseInt(dados));
                break;
            default:
                break;
        }
        //TODO: METODO ATUALIZAR CLIENT
        listitems.clear();
        adapter.notifyDataSetChanged();
        populateList();
    }
}
