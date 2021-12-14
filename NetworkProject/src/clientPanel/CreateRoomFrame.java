package clientPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import client.ClientUI;
import handler.BtCreateRoomFinishHandler;
import java.awt.Color;


public class CreateRoomFrame extends JFrame{ //�� ���� �г�
    public JTextField tfTitle;
    public JButton btCreate;
    public JPasswordField tfPw;
    public ClientUI ui;
    public JRadioButton rbSecret;
    public JRadioButton rbTwoUser;
    public JRadioButton rbOneUser;

    public CreateRoomFrame(ClientUI c) {
        setTitle("\uBC29 \uB9CC\uB4E4\uAE30");
        ui = c;
        setSize(400,300);
        setLocation(400,400);
        getContentPane().setLayout(null);
        c.setLocationRelativeTo(null);


        // ������ �ʵ� ================================================
        tfTitle = new JTextField();
        tfTitle.setBounds(182, 79, 116, 21);
        getContentPane().add(tfTitle);
        tfTitle.setColumns(10);
        // ===========================================================        

        // ��й�ȣ �Է� �ʵ� =========================================
        tfPw = new JPasswordField();
        tfPw.setEnabled(false);
        tfPw.setEditable(false);
        tfPw.setBounds(183, 118, 115, 21);
        getContentPane().add(tfPw);
        // ===========================================================

        // �游��� �Ϸ� ��ư =========================================
        btCreate = new JButton("\uBC29 \uC0DD\uC131");
        btCreate.setBackground(Color.ORANGE);
        btCreate.setBounds(140, 197, 97, 23);
        getContentPane().add(btCreate);
        // ===========================================================



        // ��й�ȣ �� ���� ���� ��ư ================================
        rbSecret = new JRadioButton("\uBE44\uBC00\uBC29");
        rbSecret.setBounds(159, 157, 121, 23);
        getContentPane().add(rbSecret);
        rbSecret.setFocusable(false);
        // ===========================================================

        // 2�ο� ���� ���� ��ư =============================

        rbTwoUser = new JRadioButton("2\uC778\uC6A9");
        rbTwoUser.setBounds(182, 40, 67, 23);
        rbTwoUser.setSelected(true);
        rbTwoUser.setFocusable(false);
        getContentPane().add(rbTwoUser);

        ButtonGroup b = new ButtonGroup();
        b.add(rbTwoUser);
        // ===========================================================

        // ��Ÿ �� ======================================================
        JLabel label = new JLabel("\uCC38\uC5EC \uC778\uC6D0 : ");
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setBounds(92, 44, 67, 15);
        getContentPane().add(label);

        JLabel lblNewLabel = new JLabel("\uBC29 \uC774\uB984 : ");
        lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        lblNewLabel.setBounds(102, 82, 57, 15);
        getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("\uBE44\uBC00\uBC88\uD638 : ");
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
        lblNewLabel_1.setBounds(92, 121, 67, 15);
        getContentPane().add(lblNewLabel_1);
        // ================================================================

        // 1�ο� or 2�ο� / ��й�ȣ �� ���� ��ư�� ������ �� ���� ==========
        rbTwoUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                tfTitle.setEditable(true);
                tfPw.setEditable(false);
                rbSecret.setEnabled(true);
                String [] titles = new String[] { "����÷����սô�.", "�ųʰ���","�ϴ� ��������.","���ǵ� �Ѱ���"};
                tfTitle.setText(titles[(int)(Math.random()*titles.length)]);
            }
        });

        rbSecret.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if(rbSecret.isSelected()) {
                    tfPw.setEditable(true);
                    tfPw.setEnabled(true);
                } else {
                    tfPw.setEditable(false);
                    tfPw.setEnabled(false);
                }
            }
        });
        // =================================================================

        // �游��� �Ϸ� ��ư ���� �� ȣ���� ������ ==================================
        ActionListener bcrfh = new BtCreateRoomFinishHandler(this);
        btCreate.addActionListener(bcrfh);
        // ========================================================================

        // ��й�ȣ �ʵ� or ���� �Է� �ʵ忡�� ���� ���� �� �游��� ��ư �ڵ� Ŭ�� ====
      /*  tfTitle.addKeyListener(new KeyListener() {
            
            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                if(e.getKeyCode() == 10) {
                    btCreate.doClick();
                }
            }
        });
        
        tfPw.addKeyListener(new KeyListener() {
            
            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                if(e.getKeyCode() == 10) {
                    btCreate.doClick();
                }
            }
            
        });*/
        // ==========================================================================
    }


}