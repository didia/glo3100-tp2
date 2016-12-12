package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import app.ProtocoleTypes;
import app.User;

public class ClientUI extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 6945386908303590271L;
    private JTextField fieldUserID;
    private JTextField fieldNom;
    private JTextField fieldMotDePasse;
    private JTextField transactionField;
    private ClientController clientController;
    private JTextPane informationsPane;

    public ClientUI(ClientController controller) {
	this();
	this.clientController = controller;
    }

    /**
     * Create the panel.
     */
    public ClientUI() {
	setLayout(null);

	JLabel lblNewLabel = new JLabel("Client");
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

	addbtnE1();
	addbtnA1();
	addbtnT1();
	addbtnA3();
	addbtnT3();

	JButton btnTrousseCles = new JButton("trousse de clés");
	btnTrousseCles.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		afficheClients();
	    }
	});
	btnTrousseCles.setBounds(572, 191, 122, 29);
	add(btnTrousseCles);

	JLabel lblNewLabel_4 = new JLabel("Autres informations");
	lblNewLabel_4.setBounds(269, 272, 139, 16);
	add(lblNewLabel_4);

	JPanel panel = new JPanel();
	panel.setBounds(22, 121, 225, 139);
	add(panel);
	panel.setLayout(new FormLayout(
		new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
			FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
		new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
			FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
			FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));

	JLabel lblNewLabel_8 = new JLabel("Authentication");
	lblNewLabel_8.setHorizontalAlignment(SwingConstants.CENTER);
	panel.add(lblNewLabel_8, "2, 2, 3, 1");

	JLabel lblNewLabel_5 = new JLabel("UserID");
	panel.add(lblNewLabel_5, "2, 4, right, default");

	fieldUserID = new JTextField();
	panel.add(fieldUserID, "4, 4, fill, default");
	fieldUserID.setColumns(10);

	JLabel lblNewLabel_6 = new JLabel("Nom");
	panel.add(lblNewLabel_6, "2, 6, right, default");

	fieldNom = new JTextField();
	panel.add(fieldNom, "4, 6, fill, default");
	fieldNom.setColumns(10);

	JLabel lblNewLabel_7 = new JLabel("Mot de passe");
	panel.add(lblNewLabel_7, "2, 8, right, default");

	fieldMotDePasse = new JTextField();
	panel.add(fieldMotDePasse, "4, 8, fill, default");
	fieldMotDePasse.setColumns(10);

	JPanel panel_1 = new JPanel();
	panel_1.setBounds(259, 139, 309, 121);
	add(panel_1);
	panel_1.setLayout(null);
	buildProtocolChoiceButton(panel_1);

	JLabel lblNewLabel_9 = new JLabel("Protocole");
	lblNewLabel_9.setBounds(6, 6, 68, 16);
	panel_1.add(lblNewLabel_9);

	JLabel lblNewLabel_10 = new JLabel("Transaction");
	lblNewLabel_10.setBounds(190, 38, 79, 16);
	panel_1.add(lblNewLabel_10);

	transactionField = new JTextField();
	transactionField.setBounds(159, 55, 144, 28);
	panel_1.add(transactionField);
	transactionField.setColumns(10);

	informationsPane = new JTextPane();
	informationsPane.setContentType("text/html");
	informationsPane.setBounds(6, 300, 688, 57);
	add(informationsPane);
    }

    private void addbtnT3() {
	JButton btnT3 = new JButton("T3");
	btnT3.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		clientController.executeT3(fieldMotDePasse.getText());
	    }
	});
	btnT3.setBounds(316, 66, 61, 29);
	add(btnT3);
    }

    private void addbtnA3() {
	JButton btnA3 = new JButton("A3");
	btnA3.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		clientController.executeA3(fieldMotDePasse.getText());
	    }
	});
	btnA3.setBounds(208, 66, 61, 29);
	add(btnA3);

    }

    private void addbtnE1() {
	JButton btnE1 = new JButton("E1");
	btnE1.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (!(fieldUserID.getText().isEmpty() || fieldNom.getText().isEmpty()
			|| fieldMotDePasse.getText().isEmpty())) {
		    clientController.executeE1(fieldUserID.getText(), fieldNom.getText(), fieldMotDePasse.getText());
		}

	    }
	});
	btnE1.setBounds(100, 34, 61, 29);
	add(btnE1);
    }

    private void addbtnA1() {
	JButton btnA1 = new JButton("A1");
	btnA1.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		clientController.executeA1(fieldUserID.getText(), fieldMotDePasse.getText());
	    }
	});
	btnA1.setBounds(208, 34, 61, 29);
	add(btnA1);
    }

    private void addbtnT1() {
	JButton btnT1 = new JButton("T1");
	btnT1.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		clientController.executeT1(transactionField.getText());
	    }
	});
	btnT1.setBounds(313, 34, 61, 29);
	add(btnT1);
    }

    private void buildProtocolChoiceButton(JPanel panel_1) {
	ButtonGroup group = new ButtonGroup();

	ActionListener btnListener = new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		clientController.changeProtocole(ProtocoleTypes.valueOf(e.getActionCommand()));
	    }
	};

	JRadioButton btnUAF = new JRadioButton("UAF");
	btnUAF.setBounds(6, 34, 141, 23);
	btnUAF.setActionCommand(ProtocoleTypes.UAF.toString());
	btnUAF.addActionListener(btnListener);
	group.add(btnUAF);
	panel_1.add(btnUAF);

	JRadioButton btnNTLM = new JRadioButton("NTLM v2");
	btnNTLM.setBounds(6, 57, 141, 23);
	btnNTLM.setActionCommand(ProtocoleTypes.NTLM_V2.toString());
	btnNTLM.addActionListener(btnListener);
	group.add(btnNTLM);
	panel_1.add(btnNTLM);

	JRadioButton btnMotDePasse = new JRadioButton("Mot de passe");
	btnMotDePasse.setBounds(6, 81, 141, 23);
	btnMotDePasse.setActionCommand(ProtocoleTypes.MOT_DE_PASSE.toString());
	btnMotDePasse.addActionListener(btnListener);
	btnMotDePasse.setSelected(true);
	group.add(btnMotDePasse);
	panel_1.add(btnMotDePasse);

    }

    public void afficheClients() {
	List<User> users = this.clientController.getTrousseCles(fieldMotDePasse.getText());
	StringBuilder htmlContent = new StringBuilder();
	htmlContent.append(
		"<html><body><table><thead><tr><th> Serveur </th><th> UserID </th> <th> Private Key</th></tr></thead><tbody>");
	for (User user : users) {
	    htmlContent.append(
		    String.format("<tr><td>%s</td><td>%s</td><td>%s</td></tr>", "S", user.userId, user.passKey));
	}
	htmlContent.append("</tbody></table></body></html>");
	informationsPane.removeAll();
	informationsPane.setText(htmlContent.toString());
	informationsPane.revalidate();
    }
}
