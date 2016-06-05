package frontend;

import executer.Controller;

public class ParamsWindow extends javax.swing.JFrame {

	private static final long serialVersionUID = 2690169326367456723L;

	/**
	 * Creates new form Interface
	 */
	public ParamsWindow() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanel1 = new javax.swing.JPanel();
		filePathField = new javax.swing.JTextField();
		jLabel1 = new javax.swing.JLabel();
		btnRunScheduler = new javax.swing.JButton();
		jLabel2 = new javax.swing.JLabel();
		strategySelect = new javax.swing.JComboBox<>();
		jLabel3 = new javax.swing.JLabel();
		intervalSelect = new javax.swing.JComboBox<>();
		
		intervalSelect.disable();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Protocolo 2PL");

		jPanel1.setName(""); // NOI18N

		filePathField.setText("/home/vinimdocarmo/workspace/2PL-protocol/transaction-files");

		filePathField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				txtCaminhoArquivoActionPerformed(evt);
			}
		});

		jLabel1.setText("Informe a pasta para importação das transações:");

		btnRunScheduler.setText("Run Scheduler");

		btnRunScheduler.addActionListener(new java.awt.event.ActionListener() {
			//TODO: só rodar o scheduler nesse listener
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnRunSchedulerActionPerformed(evt);
			}
		});

		jLabel2.setText("Estratégia de prevenção de deadlocks:");

		strategySelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Wait Die", "Wound Wait" }));
		strategySelect.setName(""); // NOI18N
		strategySelect.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				strategySelectActionPeformed(evt);
			}
		});

		jLabel3.setText("Intervalo entre execuções (em segundos):");

		intervalSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel1Layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(
										jLabel1)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										jPanel1Layout.createSequentialGroup()
												.addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(intervalSelect, javax.swing.GroupLayout.PREFERRED_SIZE,
														61, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(btnRunScheduler))
								.addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(filePathField).addComponent(strategySelect, 0,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel1)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(filePathField, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel2)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(strategySelect, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel3).addComponent(intervalSelect,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(btnRunScheduler))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void btnRunSchedulerActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnImportarActionPerformed
		String path = this.filePathField.getText();
		int strategy = this.strategySelect.getSelectedIndex();
		
		Controller.init(path, strategy);
	}// GEN-LAST:event_btnImportarActionPerformed

	private void txtCaminhoArquivoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtCaminhoArquivoActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtCaminhoArquivoActionPerformed

	private void strategySelectActionPeformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_comboEstrategiaActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_comboEstrategiaActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnRunScheduler;
	private javax.swing.JComboBox<String> strategySelect;
	private javax.swing.JComboBox<String> intervalSelect;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JTextField filePathField;
	// End of variables declaration//GEN-END:variables
}
