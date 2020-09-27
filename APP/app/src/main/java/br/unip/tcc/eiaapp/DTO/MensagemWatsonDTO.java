package br.unip.tcc.eiaapp.DTO;

import android.util.Log;

public class MensagemWatsonDTO {
	private Long idUser;
	private String mensagemEntrada;
	private String mensagemRetorno;

	public MensagemWatsonDTO(Long idUser, String mensagemEntrada, String mensagemRetorno) {
		this.idUser = idUser;
		this.mensagemEntrada = mensagemEntrada;
		this.mensagemRetorno = mensagemRetorno;
	}

	public long getIdUser() {
		return idUser;
	}
	public void setIdUser(long idUsuario) {
		this.idUser = idUsuario;
	}
	public String  getMensagemEntrada() {
		return mensagemEntrada;
	}
	public void setMensagemEntrada(String mensagemEntrada) {
		this.mensagemEntrada = mensagemEntrada;
	}
	public String getMensagemRetorno() {
		return mensagemRetorno;
	}
	public void setMensagemRetorno(String mensagemRetorno) {
		this.mensagemRetorno = mensagemRetorno;
	}
}
