package pirate;

import app.EtapeProtocole;
import protocoles.CurrentProtocole;

public class PirateController {

    private PirateUI ui;
    private EtapeProtocole etape;
    private CurrentProtocole protocole;

    public void setUI(PirateUI ui) {
	this.ui = ui;
    }

    public void intercepteMessage(EtapeProtocole etape, String message) {
	this.etape = etape;
	if (this.ui != null) {
	    this.ui.afficheMessage(message);
	}
    }

    public void achemineMessage(String message) {
	this.protocole.completeEtape(etape, message);
	ui.resetMessage();
    }

    public void setProtocole(CurrentProtocole currentProtocole) {
	this.protocole = currentProtocole;

    }
}
