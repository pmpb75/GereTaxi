package p4.geretaxi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

public class TesteSoap extends AppCompatActivity {

    ArrayList<Servico> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_soap);


        Listar();
    }

    public void Listar(){

        new Thread(new Runnable() {
            public void run() {
                GereServico manager = new GereServico();
                 lista = manager.listarServico();
                Log.d("Listagem:", lista.toString());
            }
        }).start();
    }
}
