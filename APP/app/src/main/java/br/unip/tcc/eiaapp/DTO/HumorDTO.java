package br.unip.tcc.eiaapp.DTO;

import java.util.Date;

public class HumorDTO {

    private long idUser;
    private int codHumor;
    private Date dtGravacao;
    private int codRetorno;

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public int getCodHumor() {
        return codHumor;
    }

    public void setCodHumor(int codHumor) {
        this.codHumor = codHumor;
    }

    public Date getDtGravacao() {
        return dtGravacao;
    }

    public void setDtGravacao(Date dtGravacao) {
        this.dtGravacao = dtGravacao;
    }

    public int getCodRetorno() {
        return codRetorno;
    }

    public void setCodRetorno(int codRetorno) {
        this.codRetorno = codRetorno;
    }
}
