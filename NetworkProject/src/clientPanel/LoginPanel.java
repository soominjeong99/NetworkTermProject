package clientPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;


public class LoginPanel extends JPanel { //�α��� �г�
    public JTextField tfid;
    public JButton btlogin;
    public JButton btsign;
    public JPasswordField tfpw;
    public JLabel logo;
    private Graphics screenGraphic;
    private Image panelImage;
    private Image selectedImage ;
 //   private Image backgroundImage = new ImageIcon(getClass().getResource("imge/123.gif")).getImage();

    public LoginPanel() {
        init();
    }

    private void init() {
        setSize(400, 355);
        setLayout(null);

        // ��� �ΰ� �̹��� ===========================================================
        /*logo = new JLabel("");
        logo.setBounds(0, 0, 496, 300);
        URL url;
           url = getClass().getResource("C:/java_ee/logo.png");
        logo.setIcon(new ImageIcon(url));
        add(logo);*/
        // ===========================================================================

      /*  //Background ���������
        JPanel back = null;
        try {
           back = new Back("C:/java_ee/omok/join.jpg");
         
           this.add(back);
           back.setBounds(0, 0, 350, 400);
           }catch (IOException e1) {
              e1.printStackTrace();
           } catch (NullPointerException ne){
              ne.printStackTrace();
           }*/

        JLabel title = new JLabel("4 - BingGo");
        title.setForeground(Color.BLACK);
        title.setFont(new Font("���ʷҹ���", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(92, 77, 226, 58);
        add(title);
        // ID �Է� �ʵ� ===============================================================
        tfid = new JTextField();
        tfid.setBounds(152, 142, 116, 24);
        add(tfid);
        tfid.setColumns(10);
        // ===========================================================================

        // PW �Է� �ʵ� ===============================================================
        tfpw = new JPasswordField();
        tfpw.setBounds(152, 187, 116, 24);
        add(tfpw);
        // ===========================================================================

        // �� ======================================================================
        JLabel lblNewLabel_1 = new JLabel("���̵� : ");
        lblNewLabel_1.setForeground(Color.BLACK);
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
        lblNewLabel_1.setFont(new Font("���ʷҹ���", Font.BOLD, 15));
        lblNewLabel_1.setBounds(72, 144, 62, 18);
        add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("�н����� : ");
        lblNewLabel_2.setForeground(Color.BLACK);
        lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
        lblNewLabel_2.setFont(new Font("���ʷҹ���", Font.BOLD, 15));
        lblNewLabel_2.setBounds(42, 189, 91, 18);
        add(lblNewLabel_2);
        // ===========================================================================        

        // �α��� ��ư ================================================================
        btlogin = new JButton("�α���");
        btlogin.setBackground(Color.ORANGE);
        //URL url1 = getClass().getResource("imge/login.png");
        btlogin.setBounds(92, 254, 100, 33);
        //btlogin.setIcon(new ImageIcon(url1));
       // btlogin.setBorderPainted(false);
        //btlogin.setFocusPainted(false);
       // btlogin.setBackground(new Color(0, 0, 0, 0));
        add(btlogin);
        // ===========================================================================

        // ȸ������ ��ư ==============================================================
        btsign = new JButton("ȸ������");
        btsign.setBackground(Color.ORANGE);
       // URL url2 = getClass().getResource("imge/signup.png");
        btsign.setBounds(212, 254, 100, 33);
        //btsign.setIcon(new ImageIcon(url2));
        //btsign.setBorderPainted(false);
        //btsign.setFocusPainted(false);
      //  btsign.setBackground(new Color(255, 255, 0, 0));
        add(btsign);
        // ===========================================================================

        JLabel team = new JLabel("team 10");
        team.setForeground(Color.BLACK);
        team.setFont(new Font("���ʷҹ���", Font.BOLD, 15));
        team.setHorizontalAlignment(SwingConstants.CENTER);
        team.setBounds(92, 297, 226, 58);
        add(team);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon(LoginPanel.class.getResource("/image/Logo.png")));
        lblNewLabel.setBounds(72, 10, 261, 91);
        add(lblNewLabel);
    }
}