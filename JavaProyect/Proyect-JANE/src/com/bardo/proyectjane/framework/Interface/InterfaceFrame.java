package com.bardo.proyectjane.framework.Interface;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class InterfaceFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField queueText;

	public InterfaceFrame() {
		initComponents();
	}

	private void initComponents() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("JANE");

		queueText = new javax.swing.JTextField();
		queueText.setText("Hay 0 elementos en la cola");

		getContentPane().add(queueText);

		pack();
	}

	public void editText(String str) {
		queueText.setText(str);
	}

}
