package protocoles;

import java.util.Optional;

import app.Crypto;
import app.EtapeProtocole;
import app.ProtocoleTypes;
import client.ClientController;
import pirate.PirateController;
import serveur.Client;
import serveur.ServeurController;

public class MotDePasseProtocole extends BaseProtocole {

    public MotDePasseProtocole(ServeurController serveur, ClientController client, PirateController pirate,
	    Crypto crypto) {
	super(serveur, client, pirate, crypto);
    }

    public void executeEtape(EtapeProtocole etape, String message) {
	String[] messageParts = message.split(" ");

	switch (etape) {
	case A1:
	    message = genereSessionID(messageParts);
	    break;
	case T1:
	    message = getTransactionMessage(message);
	    break;
	case T2:
	    break;
	case T3:
	    break;
	case T4:
	    break;
	default:
	    break;
	}

	pirate.intercepteMessage(etape, message);

    }

    @Override
    public void completeEtape(EtapeProtocole etape, String message) {
	String[] messageParts = message.split(" ");
	switch (etape) {
	case A1:
	    message = executeAuthentification(messageParts);
	    serveur.recoisMessage(message);
	    break;
	case A2:
	    setClientCookies(messageParts);
	    break;
	case E1:
	    message = executeEnregistrement(messageParts);
	    serveur.recoisMessage(message);
	    break;
	case T1:
	    message = verifyAuthentication(messageParts);
	    serveur.recoisMessage(message);
	    break;
	case T2:
	    break;
	case T3:
	    break;
	case T4:
	    break;
	default:
	    break;
	}
    }

    private String executeAuthentification(String[] messageParts) {
	Optional<Client> client = serveur.reqClientParID(reqProtocoleType(), messageParts[1]);
	if (!(client.isPresent() && crypto.h1(messageParts[2]).equals(client.get().passKey))) {
	    return "401";
	}
	sessionMap.put(messageParts[0], crypto.random5Digits());
	return String.format("%s 200 SetCookie:Session=%s", messageParts[0], sessionMap.get(messageParts[0]));
    }

    private String genereSessionID(String[] messageParts) {
	if (messageParts.length != 2) {
	    throw new ProtocolError("Le mot de passe et le le userId sont réquis pour l'authentication");
	}
	return String.format("%s %s %s", crypto.random5Digits(), messageParts[0], messageParts[1]);
    }

    private String executeEnregistrement(String[] messageParts) {
	if (messageParts.length != 3) {
	    throw new ProtocolError(String.format(
		    "Le message attendu à l'étape d'enregistrement devrait avoir 3 parties, seulement %ds réçu",
		    messageParts.length));
	}
	serveur.enregistreUtilisateur(messageParts[0], messageParts[1], crypto.h1(messageParts[2]), reqProtocoleType());
	return "200";
    }

    private void setClientCookies(String[] messageParts) {
	if (messageParts.length != 3) {
	    throw new ProtocolError(String.format(
		    "La reponse attendu du serveur devrait avoir 3 parties, seulement %ds réçu", messageParts.length));
	}
	if (messageParts[1].equals("200")) {
	    client.setCookie("SESSION_ID", messageParts[0]);
	    client.setCookie("NS", messageParts[2].split("=")[1]);
	}
    }

    private String getTransactionMessage(String transaction) {
	if (transaction.isEmpty()) {
	    throw new ProtocolError("Le champ transaction doit être rempli ");
	}
	return String.format("%s %s Cookie:session=%s", client.getCookie("SESSION_ID"), transaction,
		client.getCookie("NS"));
    }

    private String verifyAuthentication(String[] messageParts) {
	String sessionID = messageParts[0];
	String nS = messageParts[2].split("=")[1];
	String code = "401";
	if (sessionMap.get(sessionID).equals(nS)) {
	    code = "200";
	}
	return String.format("%s %s", sessionID, code);
    }

    @Override
    public ProtocoleTypes reqProtocoleType() {
	return ProtocoleTypes.MOT_DE_PASSE;
    }
}
