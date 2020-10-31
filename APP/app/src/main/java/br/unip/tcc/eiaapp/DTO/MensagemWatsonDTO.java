package br.unip.tcc.eiaapp.DTO;

import android.util.Log;

import java.util.List;

public class MensagemWatsonDTO {
	private long idUser;
	private String mensagemEntrada;
	private List<String> mensagemRetorno;
	private int codRetorno;

	public MensagemWatsonDTO(Long idUser, String mensagemEntrada) {
		this.idUser = idUser;
		this.mensagemEntrada = mensagemEntrada;
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
	public List<String> getMensagemRetorno() {
		return mensagemRetorno;
	}
	public void setMensagemRetorno(List<String> mensagemRetorno) {
		this.mensagemRetorno = mensagemRetorno;
	}
	public int getCodRetorno() {
		return codRetorno;
	}
	public void setCodRetorno(int codRetorno) {
		this.codRetorno = codRetorno;
	}
}
