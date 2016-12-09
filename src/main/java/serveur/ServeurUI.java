package serveur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class ServeurUI extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 2232635670608578297L;

    private JTextPane informationsPane;
    private ServeurController serveurController;

    public ServeurUI(ServeurController controller) {
	this();
	this.serveurController = controller;
    }

    /**
     * Create the panel.
     */
    public ServeurUI() {
	setLayout(null);

	JLabel lblNewLabel = new JLabel("Serveur");
	lblNewLabel.setBounds(6, 6, 46, 16);
	add(lblNewLabel);

	JLabel lblNewLabel_1 = new JLabel("Enregistrement");
	lblNewLabel_1.setBounds(80, 6, 95, 16);
	add(lblNewLabel_1);

	JLabel lblNewLabel_2 = new JLabel("Authentification");
	lblNewLabel_2.setBounds(187, 6, 102, 16);
	add(lblNewLabel_2);

	JLabel lblNewLabel_3 = new JLabel("Transaction");
	lblNewLabel_3.setBounds(303, 6, 74, 16);
	add(lblNewLabel_3);

	addE2Button();
	addA2Button();
	addT2Button();
	addA4Button();

	JButton t4Button = new JButton("T4");
	t4Button.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
	    }
	});
	t4Button.setBounds(316, 66, 61, 29);
	add(t4Button);

	addClientsButton();

	JLabel lblNewLabel_4 = new JLabel("Autres informations");
	lblNewLabel_4.setBounds(187, 107, 139, 16);
	add(lblNewLabel_4);

	informationsPane = new JTextPane();
	informationsPane.setContentType("text/html");
	informationsPane.setBounds(6, 129, 490, 63);
	add(informationsPane);

    }

    private void addA4Button() {
	JButton a4Button = new JButton("A4");
	a4Button.setBounds(208, 66, 61, 29);
	a4Button.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		serveurController.executeA4();
	    }
	});
	add(a4Button);

    }

    private void addClientsButton() {
	JButton clientsButton = new JButton("clients");
	clientsButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		serveurController.afficheClients();
	    }
	});
	clientsButton.setBounds(389, 66, 86, 29);
	add(clientsButton);

    }

    private void addE2Button() {
	JButton e2Button = new JButton("E2");
	e2Button.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		serveurController.executeE2();
	    }
	});
	e2Button.setBounds(100, 34, 61, 29);
	add(e2Button);
    }

    private void addA2Button() {
	JButton a2Button = new JButton("A2");
	a2Button.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		serveurController.executeA2();
	    }
	});
	a2Button.setBounds(208, 34, 61, 29);
	add(a2Button);
    }

    private void addT2Button() {
	JButton t2Button = new JButton("T2");
	t2Button.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		serveurController.executeT2();
	    }
	});
	t2Button.setBounds(313, 34, 61, 29);
	add(t2Button);
    }

    public void afficheClients(List<Client> clients) {
	StringBuilder htmlContent = new StringBuilder();
	htmlContent.append(
		"<html><body><table><thead><tr><th> UserID </th><th> Nom </th> <th> Passkey </th></tr></thead><tbody>");
	for (Client client : clients) {
	    htmlContent.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td></tr>", client.userId, client.name,
		    client.passKey));
	}
	htmlContent.append("</tbody></table></body></html>");
	informationsPane.removeAll();
	informationsPane.setText(htmlContent.toString());
	informationsPane.revalidate();
    }
}
