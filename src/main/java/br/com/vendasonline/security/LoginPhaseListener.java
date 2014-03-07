package br.com.vendasonline.security;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class LoginPhaseListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	private static final String LOGIN_PAGE = "/index.xhtml";

	@Override
	public void afterPhase(PhaseEvent event) {

		FacesContext context = event.getFacesContext();
		String viewId = context.getViewRoot().getViewId();

		Credentials credential = context.getApplication().evaluateExpressionGet(context, "#{credential}", Credentials.class);
		
		if (!viewId.equals(LOGIN_PAGE)) {
			if (!credential.isLogado()) {
				NavigationHandler handler = context.getApplication().getNavigationHandler();
				handler.handleNavigation(context, null, LOGIN_PAGE+"?faces-redirect=true");
				context.renderResponse();
			}
		} else {
			credential.setEmpresa(null);
			credential.clear();
			credential.setLogado(false);
		}

	}

	@Override
	public void beforePhase(PhaseEvent event) {

	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
