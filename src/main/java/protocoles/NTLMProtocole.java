package protocoles;

import java.util.Optional;

import app.Crypto;
import app.EtapeProtocole;
import app.ProtocoleTypes;
import app.User;
import client.ClientController;
import pirate.PirateController;
import serveur.ServeurController;

public class NTLMProtocole extends BaseProtocole {

    private User currentClient = null;

    public NTLMProtocole(ServeurController serveur, ClientController client, PirateController pirate, Crypto crypto) {
	super(serveur, client, pirate, crypto);
    }

    @Override
    public void executeEtape(EtapeProtocole etape, String message) {
	String[] messageParts = message.split(" ");

	switch (etape) {
	case A1:
	    message = genereSessionID(messageParts);
	    break;
	case A3:
	    message = generateNc(message);
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

    private String getTransactionMessage(String transaction) {
	if (transaction.isEmpty()) {
	    throw new ProtocolError("Le champ transaction doit être rempli ");
	}
	return String.format("%s %s Cookie:session=%s", client.getCookie("SESSION_ID"), transaction,
		client.getCookie("NS_PRIME"));
    }

    private String generateNc(String password) {
	String sessionID = client.getCookie("SESSION_ID");
	String nsHash = getNxHash(sessionID, client.getCookie("NS"), password);
	String nc = crypto.random5Digits();
	client.setCookie("NC", nc);
	client.setCookie("EXPECTED-NC-HASH", getNxHash(sessionID, nc, password));
	return String.format("%s %s %s", client.getCookie("SESSION_ID"), nsHash, nc);
    }

    private String genereSessionID(String[] messageParts) {
	if (messageParts.length == 0) {
	    throw new ProtocolError("Le userId est réquis pour l'authentication");
	}
	return String.format("%s %s", crypto.random5Digits(), messageParts[0]);
    }

    @Override
    public void completeEtape(EtapeProtocole etape, String message) {
	String[] messageParts = message.split(" ");
	switch (etape) {
	case A1:
	    message = genereNSPourSession(messageParts);
	    serveur.recoisMessage(message);
	    break;
	case A2:
	    enregistrerSessionIDEtNS(messageParts);
	    break;
	case A3:
	    message = checkNsHash(messageParts);
	    serveur.recoisMessage(message);
	    break;
	case A4:
	    completeAuthentication(messageParts);
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

    private String verifyAuthentication(String[] messageParts) {
	String sessionID = messageParts[0];
	String nS = messageParts[2].split("=")[1];
	String code = "401";
	if (sessionMap.get(sessionID).equals(nS)) {
	    code = "200";
	}
	return String.format("%s %s", sessionID, code);
    }

    private void completeAuthentication(String[] messageParts) {
	if (messageParts[1].equals("200")) {
	    if (messageParts[2].equals(client.getCookie("EXPECTED-NC-HASH"))) {
		client.setCookie("NS_PRIME", messageParts[3].split("=")[1]);
	    }
	}
    }

    private String checkNsHash(String[] messageParts) {
	if (messageParts.length != 3) {
	    throw new ProtocolError("Le message réçu pour l'étape A3 est incorrect");
	}
	if (messageParts[1]
		.equals(getNxHash(messageParts[0], sessionMap.get(messageParts[0]), currentClient.passKey))) {
	    String ncHash = getNxHash(messageParts[0], messageParts[2], currentClient.passKey);
	    String nsPrime = crypto.random5Digits();
	    sessionMap.put(messageParts[0], nsPrime);
	    return String.format("%s 200 %s SetCookie:Session=%s", messageParts[0], ncHash, nsPrime);
	} else {
	    return "401";
	}
    }

    private void enregistrerSessionIDEtNS(String[] messageParts) {
	if (messageParts.length != 2) {
	    throw new ProtocolError(String.format("Le message devrait contenir l'ID et le NS", messageParts.length));
	}
	client.setCookie("SESSION_ID", messageParts[0]);
	client.setCookie("NS", messageParts[1]);
    }

    private String genereNSPourSession(String[] messageParts) {
	Optional<User> user = serveur.reqClientParID(reqProtocoleType(), messageParts[1]);
	if (!user.isPresent()) {
	    return "401";
	}
	currentClient = user.get();
	sessionMap.put(messageParts[0], crypto.random5Digits());
	return String.format("%s %s", messageParts[0], sessionMap.get(messageParts[0]));
    }

    @Override
    public ProtocoleTypes reqProtocoleType() {
	return ProtocoleTypes.NTLM_V2;
    }

    private String executeEnregistrement(String[] messageParts) {
	if (messageParts.length != 3) {
	    throw new ProtocolError(String.format(
		    "Le message attendu à l'étape d'enregistrement devrait avoir 3 parties, seulement %ds réçu",
		    messageParts.length));
	}
	serveur.enregistreUtilisateur(messageParts[0], messageParts[1], messageParts[2], reqProtocoleType());
	return "200";
    }

}
