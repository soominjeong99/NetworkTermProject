package clientPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class SignupPanel extends JPanel{ //ȸ������ �г�
    public JTextField tfname;
    public JPasswordField tfpw;
    public JPasswordField tfrpw;
    public JTextField tfId;
    public JRadioButton rbAgree;
    public JRadioButton rbdisAgree;
    public JButton btCreate;
    public JButton btCancel;
    private Graphics screenGraphic;
    private Image panelImage;
    private Image selectedImage ;
    public JTextField tfemail;
    public JTextField tfsns;
  //  private Image backgroundImage = new ImageIcon(getClass().getResource("imge/123.gif")).getImage();

    public SignupPanel() {
        setSize(450, 700);
        setLayout(null);

        // ID �Է� ==================================================================
        JLabel lblNewLabel = new JLabel("ȸ �� �� ��");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("���ʷҹ���", Font.BOLD, 30));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(109, 63, 226, 58);
        add(lblNewLabel);
        tfId = new JTextField();
        tfId.setBounds(219, 163, 116, 24);
        add(tfId);
        tfId.setColumns(10);
        // =============================================================================

        // Usert name �Է� �ʵ�
        tfname = new JTextField();
        tfname.setBounds(219, 223, 116, 24);
        add(tfname);
        tfname.setColumns(10);
        // =============================================================================

        // ��й�ȣ�Է�â 2�� =========================================================== 
        tfpw = new JPasswordField();
        tfpw.setBounds(219, 280, 116, 24);
        add(tfpw);

        tfrpw = new JPasswordField();
        tfrpw.setBounds(219, 336, 116, 24);
        add(tfrpw);
        // =============================================================================

        // ȸ������ �Ϸ� ��ư=============================================================
        btCreate = new JButton("����");
        btCreate.setBackground(Color.ORANGE);
        btCreate.setBounds(87, 600, 105, 27);
        add(btCreate);
        // =============================================================================

        // ȸ������ ��� ��ư=============================================================
        btCancel = new JButton("���");
        btCancel.setBackground(Color.ORANGE);
        btCancel.setBounds(249, 600, 105, 27);
        add(btCancel);
        // =============================================================================

        // ȸ����� ���� or ���� ��ư ==================================================
        ButtonGroup bg = new ButtonGroup();
        rbAgree = new JRadioButton("����");
        rbAgree.setOpaque(false);
        rbAgree.setForeground(Color.BLACK);
        rbAgree.setBounds(138, 544, 71, 27);
        add(rbAgree);

        rbdisAgree = new JRadioButton("����");
        rbdisAgree.setOpaque(false);
        rbdisAgree.setForeground(Color.BLACK);
        rbdisAgree.setBounds(230, 544, 139, 27);
        add(rbdisAgree);

        bg.add(rbAgree);
        bg.add(rbdisAgree);
        // =============================================================================    

        // ��Ÿ �� ====================================================================
        JLabel rrag = new JLabel("\uC544\uC774\uB514(\uB2C9\uB124\uC784) : ");
        rrag.setForeground(Color.BLACK);
        rrag.setHorizontalAlignment(SwingConstants.RIGHT);
        rrag.setFont(new Font("���ʷҹ���", Font.BOLD, 15));
        rrag.setBounds(72, 169, 116, 18);
        add(rrag);

        JLabel label = new JLabel("�̸� : ");
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setFont(new Font("���ʷҹ���", Font.BOLD, 15));
        label.setBounds(125, 229, 62, 18);
        add(label);

        JLabel label_1 = new JLabel("��й�ȣ : ");
        label_1.setForeground(Color.BLACK);
        label_1.setHorizontalAlignment(SwingConstants.RIGHT);
        label_1.setFont(new Font("���ʷҹ���", Font.BOLD, 15));
        label_1.setBounds(87, 286, 105, 18);
        add(label_1);

        JLabel label_2 = new JLabel("��й�ȣ Ȯ�� : ");
        label_2.setForeground(Color.BLACK);
        label_2.setHorizontalAlignment(SwingConstants.RIGHT);
        label_2.setFont(new Font("���ʷҹ���", Font.BOLD, 15));
        label_2.setBounds(48, 342, 139, 18);
        add(label_2);

        JLabel label_3 = new JLabel("\uC774\uBA54\uC77C:");
        label_3.setHorizontalAlignment(SwingConstants.RIGHT);
        label_3.setForeground(Color.BLACK);
        label_3.setFont(new Font("���ʷҹ���", Font.BOLD, 15));
        label_3.setBounds(48, 399, 139, 18);
        add(label_3);

        JLabel label_4 = new JLabel("SNS :");
        label_4.setHorizontalAlignment(SwingConstants.RIGHT);
        label_4.setForeground(Color.BLACK);
        label_4.setFont(new Font("���ʷҹ���", Font.BOLD, 15));
        label_4.setBounds(48, 456, 139, 18);
        add(label_4);

        //�̸����� �� ��ư: tfemail
        tfemail = new JTextField();
        tfemail.setBounds(219, 399, 116, 21);
        add(tfemail);
        tfemail.setColumns(10);

        //sns�� �� ��ư: tfsns
        tfsns = new JTextField();
        tfsns.setBounds(219, 458, 116, 21);
        add(tfsns);
        tfsns.setColumns(10);

        JLabel agreement = new JLabel("\uD574\uB2F9 \uAC8C\uC784\uC758 \uAC1C\uC778\uC815\uBCF4 \uCDE8\uAE09 \uBC29\uCE68\uC5D0 \uB3D9\uC758\uD569\uB2C8\uB2E4");
        agreement.setForeground(Color.RED);
        agreement.setBounds(94, 511, 260, 24);
        add(agreement);

        JLabel logo_1 = new JLabel("");
        logo_1.setIcon(new ImageIcon(SignupPanel.class.getResource("/image/Yell.png")));
        logo_1.setBounds(72, 69, 71, 52);
        add(logo_1);

        JLabel logo_2 = new JLabel("");
        logo_2.setIcon(new ImageIcon(SignupPanel.class.getResource("/image/Red.png")));
        logo_2.setBounds(304, 69, 65, 52);
        add(logo_2);
        // =============================================================================
    }
}