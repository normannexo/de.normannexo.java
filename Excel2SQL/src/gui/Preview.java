package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;

public class Preview extends JFrame {

    private JPanel contentPane;
    private JTextArea taPreview;

    
    /**
     * Create the frame.
     */
    public Preview() {
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	setBounds(100, 100, 450, 300);
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	contentPane.setLayout(new BorderLayout(0, 0));
	setContentPane(contentPane);
	
	JScrollPane scrollPane = new JScrollPane();
	contentPane.add(scrollPane, BorderLayout.CENTER);
	
	taPreview = new JTextArea();
	taPreview.setWrapStyleWord(false);
	
	scrollPane.setViewportView(taPreview);
    }
    
    public void setText(String s) {
	taPreview.setText(s);
    }

}
