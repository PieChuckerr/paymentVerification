package in.cdac.container;
import in.cdac.bean.SBIPaymentResponseBean;
import in.cdac.util.SBIOnlinePaymentResponse;

import java.awt.Color;
import java.awt.Container;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;

import Encryption.AESEncrypt;

import javax.swing.JTextPane;
import javax.swing.JTextArea;

public class PaymentVerify extends javax.swing.JFrame {

		public static String classPath = "";
		private javax.swing.JLabel paymentIdLable;
		private javax.swing.JLabel amountLabel;
		private javax.swing.JButton sendButton;

		Color color = new Color(30,98,19);
		private JTextField paymentId;
		private JTextField feeAmount;
		private JComboBox comboBox;
		private JScrollPane currencyPane;
		private JList currencyList;
		private JScrollPane merchantPane;
		private JList merchantList;
		private JLabel lblMerchantId;

		private String encdata;
		private JLabel responseLabel;
		private JLabel statusLabel;
		
		
		public PaymentVerify() {
			setResizable(false);
			setSize(200, 300);
			initComponents();
		}

		
		private void initComponents() {
				DefaultListModel<String> merchantListElements = new DefaultListModel<>();
				merchantListElements.addElement("GATE");
				merchantListElements.addElement("JAM");
				
				DefaultListModel<String> currencyListElements = new DefaultListModel<>();
				currencyListElements.addElement("Rupees");
				currencyListElements.addElement("Dollars");
				
				//XXX: Generated using some WindowBuilder plugin
				paymentIdLable = new javax.swing.JLabel();
				amountLabel = new javax.swing.JLabel();
				sendButton = new javax.swing.JButton();
				setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
				
				setTitle("Payment Verification Utility for GATE/JAM");

				paymentIdLable.setText("Payment ID");
				amountLabel.setText("Amount");
				sendButton.setText("Check Payment Status");

				sendButton.addActionListener( new java.awt.event.ActionListener() {
								public void actionPerformed( java.awt.event.ActionEvent evt ) {
								sendButtonActionPerformed(evt);
								}
							});
				
				paymentId = new JTextField();
				paymentId.setColumns(10);
				
				feeAmount = new JTextField();
				feeAmount.setColumns(10);
				
				currencyPane = new JScrollPane();
				currencyPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				
				merchantPane = new JScrollPane();
				merchantPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				
				JLabel lblCurrencyType = new JLabel();
				lblCurrencyType.setText("Currency Type");
				
				lblMerchantId = new JLabel();
				lblMerchantId.setText("Exam");
				
				responseLabel = new JLabel();
				responseLabel.setForeground(Color.BLUE);

				Container container = getContentPane();
				container.setSize(200, 300);
				
				statusLabel = new JLabel();
				statusLabel.setForeground(Color.BLUE);
				javax.swing.GroupLayout layout = new javax.swing.GroupLayout(container);
				layout.setHorizontalGroup(
					layout.createParallelGroup(Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(layout.createParallelGroup(Alignment.TRAILING)
								.addGroup(layout.createSequentialGroup()
									.addGroup(layout.createParallelGroup(Alignment.TRAILING)
										.addComponent(amountLabel)
										.addComponent(paymentIdLable))
									.addGap(43))
								.addGroup(layout.createSequentialGroup()
									.addGroup(layout.createParallelGroup(Alignment.TRAILING)
										.addComponent(lblMerchantId, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblCurrencyType, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE))
									.addGap(32)))
							.addGroup(layout.createParallelGroup(Alignment.TRAILING)
								.addComponent(merchantPane, 0, 0, Short.MAX_VALUE)
								.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
									.addComponent(paymentId, 246, 246, Short.MAX_VALUE)
									.addComponent(currencyPane, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
									.addComponent(feeAmount)))
							.addGap(1131))
						.addGroup(layout.createSequentialGroup()
							.addGap(165)
							.addComponent(sendButton)
							.addContainerGap(1172, Short.MAX_VALUE))
						.addGroup(layout.createSequentialGroup()
							.addGap(35)
							.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addComponent(statusLabel, GroupLayout.PREFERRED_SIZE, 730, GroupLayout.PREFERRED_SIZE)
								.addComponent(responseLabel, GroupLayout.PREFERRED_SIZE, 730, GroupLayout.PREFERRED_SIZE))
							.addContainerGap(765, Short.MAX_VALUE))
				);
				layout.setVerticalGroup(
					layout.createParallelGroup(Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
							.addContainerGap()
							.addGroup(layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(paymentId, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
								.addComponent(paymentIdLable))
							.addGap(22)
							.addGroup(layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(feeAmount, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
								.addComponent(amountLabel))
							.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
									.addGap(21)
									.addComponent(currencyPane, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
								.addGroup(layout.createSequentialGroup()
									.addGap(31)
									.addComponent(lblCurrencyType)))
							.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
									.addGap(18)
									.addComponent(merchantPane, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
								.addGroup(layout.createSequentialGroup()
									.addGap(29)
									.addComponent(lblMerchantId)))
							.addGap(37)
							.addComponent(sendButton)
							.addGap(18)
							.addComponent(responseLabel, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(statusLabel, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(66, Short.MAX_VALUE))
				);
				
				
				
				this.merchantList = new JList(merchantListElements);
				merchantPane.setViewportView(merchantList);
				
				this.currencyList = new JList(currencyListElements);
				currencyPane.setViewportView(this.currencyList);
				getContentPane().setLayout( layout );
				pack();
				setSize(800, 400);
		}

	

	private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {

		try {
		
			this.responseLabel.setText("");
			this.responseLabel.setForeground(Color.BLUE);
			
			this.statusLabel.setText("");
			this.statusLabel.setForeground(Color.BLUE);
			
			String feeAmount = null, merchantCode = "IIT_ROORKEE", paymentId = null, currencyType = "INR";
			feeAmount = this.feeAmount.getText();
			paymentId = this.paymentId.getText();
			String currency = (String) this.currencyList.getSelectedValue();
			String exam = (String) this.merchantList.getSelectedValue();
			
			if(currency!=null)
				currencyType = currency.equals("Rupees") ? "INR" : "USD";
			
			if(exam!=null)
				merchantCode = exam.equals("GATE") ? "IIT_ROORKEE" : "IIT_DELHI";
			SBIOnlinePaymentResponse sbiPaymentResponse = new SBIOnlinePaymentResponse();
			SBIPaymentResponseBean response = sbiPaymentResponse.paymentDoubleVerification(feeAmount, paymentId, merchantCode, currencyType, "https://merchant.onlinesbi.com/thirdparties/doubleverification.htm");
			System.out.println(response.toString());	
			this.responseLabel.setText("Payment ID: "+response.getPayment_Id()+" | Currency: "+response.getCurrencyType()+" | Amount: "+response.getAmount()+" | SBI Reference number: "+response.getSbi_ref_no());
			this.statusLabel.setText("Status: "+response.getStatus()+" | Status Description: "+response.getStatus_desc());
			if(response.getStatus().equalsIgnoreCase("success")){
				this.responseLabel.setForeground(this.color);
				this.statusLabel.setForeground(this.color);
			}
			else if(response.getStatus_desc().equalsIgnoreCase("Cancel Transaction")) {
				this.responseLabel.setForeground(Color.BLUE);
				this.statusLabel.setForeground(Color.BLUE);
			}
			else{
				this.responseLabel.setForeground(Color.RED);
				this.statusLabel.setForeground(Color.RED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

		public static void main(String args[]) {
			String classPath = PaymentVerify.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		
			
				java.awt.EventQueue.invokeLater(new Runnable() {
								public void run() {
								new PaymentVerify().setVisible(true);
				}});
		}
}