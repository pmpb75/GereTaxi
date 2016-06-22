package p4.geretaxi;

import java.io.Serializable;

public class AssistenciaEmViagem extends Servico implements Serializable {

    private String NumProcesso;
    private int IdCompanhia;
    private String Companhia;

    public String getCompanhia() {
        return Companhia;
    }

    public void setCompanhia(String companhia) {
        Companhia = companhia;
    }

    public int getIdCompanhia() {
        return IdCompanhia;
    }

    public String getNumProcesso() {
        return NumProcesso;
    }

    public void setIdCompanhia(int idCompanhia) {
        IdCompanhia = idCompanhia;
    }

    public void setNumProcesso(String numProcesso) {
        NumProcesso = numProcesso;
    }

    @Override
    public void setData(String data ) {

        super.setData(data);
    }

    @Override
    public void setHoraDeInicio(String horaDeInicio) {
        super.setHoraDeInicio(horaDeInicio);
    }

    @Override
    public void setDistancia(Double distancia) {
        super.setDistancia(distancia);
    }

    @Override
    public void setNumPassageiros(int numPassageiros) {
        super.setNumPassageiros(numPassageiros);
    }

    @Override
    public int getNumPassageiros() {
        return super.getNumPassageiros();
    }
}