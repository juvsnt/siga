package br.ita.webquest.util;

import br.ita.webquest.bean.UserBean;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

public class LoggedInCheck implements PhaseListener {

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    @Override
    public void beforePhase(PhaseEvent event) {
    }

    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext fc = event.getFacesContext();

        // Check to see if they are on the login page.
        boolean loginPage = fc.getViewRoot().getViewId().lastIndexOf("login") > -1;
        boolean cadastrarUsuarioPage = fc.getViewRoot().getViewId().lastIndexOf("register-user") > -1;
        boolean solicitarConvite = fc.getViewRoot().getViewId().lastIndexOf("request-invitation") > -1;
        if (!loginPage && !solicitarConvite && !cadastrarUsuarioPage && !loggedIn(fc)) {
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            nh.handleNavigation(fc, null, "login?faces-redirect=true");
        }
    }

    private boolean loggedIn(FacesContext cont) {
        UserBean b = (UserBean) ((HttpServletRequest) (cont.getExternalContext()
                .getRequest())).getSession().getAttribute(
                "userBean");
        if (b != null && b.isLoggedIn()) {
            return true;
        } else {
            return false;
        }
    }
}