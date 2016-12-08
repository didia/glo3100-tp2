package pirate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PirateUI extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 4861060740787368019L;
    private JTextField textField;
    private PirateController pirateController;

    /**
     * Create the panel.
     * 
     * @param pirateController
     */
    public PirateUI(PirateController pirateController) {
	setLayout(null);
	this.pirateController = pirateController;
	JLabel lblNewLabel = new JLabel("Pirate");
	lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
	lblNewLabel.setBounds(6, 6, 90, 16);
	add(lblNewLabel);

	JLabel lblNewLabel_1 = new JLabel("Message échangé");
	lblNewLabel_1.setBounds(47, 34, 122, 16);
	add(lblNewLabel_1);

	textField = new JTextField();
	textField.setBounds(167, 28, 315, 28);
	add(textField);
	textField.setColumns(10);

	JButton btnEnvoyer = new JButton("Envoyer");
	btnEnvoyer.setBounds(270, 62, 117, 29);
	btnEnvoyer.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		pirateController.achemineMessage(textField.getText());
	    }
	});
	add(btnEnvoyer);

    }

    public void afficheMessage(String message) {
	textField.setText(message);
    }

    public void resetMessage() {
	textField.setText(null);

    }

}
