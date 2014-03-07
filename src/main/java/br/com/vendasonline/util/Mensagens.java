package br.com.vendasonline.util;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class Mensagens {

	public static void exibeMensagemGrowlSemDetalhe(Severity severidade, String mensagem) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severidade, mensagem, null));
	}
	
}
