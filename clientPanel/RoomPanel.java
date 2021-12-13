package clientPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import client.ClientUI;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RoomPanel extends JPanel { //�� �г�
   public JButton btCreateRoom;
   public JButton btLogOut;
   public JButton bthow;
   public List<JButton> btList;
   public JList userList;
  // public PnInfoPanel pnInfo;
   public JTextField textField;
   public JButton btExit;
   public JButton btQuickStart;
   private Graphics screenGraphic;
   private Image panelImage;
   private Image selectedImage ;
   public JTextArea messageArea;
   public JButton btcount;
 //  private Image backgroundImage = new ImageIcon(getClass().getResource("imge/roomImage.jpg")).getImage();
   private JLabel lbInfoPicture;

   @SuppressWarnings({ "unchecked", "serial" })
public RoomPanel(ClientUI c) {
      // setBackground(new Color(51, 0, 0));
       setSize(800, 800);
       setLayout(null);

       btList = new ArrayList<>();    // ���� ��ư�� List�� ���� 
    /*   // ������ �г� ���� ==========================================
       pnInfo = c.pnInfo;
       add(pnInfo);
       pnInfo.setBounds(34, 62, 200, 139);
      */ // ==========================================================    

       // �������� �� �̹��� =====================================
       //lbInfoPicture = new JLabel("");
     //  URL url3 = getClass().getResource("imge/mypsa4.png");
      // lbInfoPicture.setBounds(74, 23, 50, 50);
      // lbInfoPicture.setIcon(new ImageIcon(url3));
       //pnInfo.add(lbInfoPicture);
       // ===========================================================

       // ��������� ��� ����Ʈ ======================================
       JScrollPane scrollPane = new JScrollPane();
       scrollPane.setBounds(34, 62, 200, 200);
       add(scrollPane); 

       userList = new JList<Object>();
           userList.setCellRenderer(new DefaultListCellRenderer() {
               @Override
               public int getHorizontalAlignment() {
                   // TODO Auto-generated method stub
                   return CENTER;
               }
           });        
       scrollPane.setViewportView(userList);
       // ===========================================================

       textField = new JTextField(); //�ؽ�Ʈ�� �Է��ϴ� â 
       textField.setBounds(34, 720, 200, 30);
       add(textField);

       messageArea = new JTextArea();
       messageArea.setSize(200, 365);
       //add(messageArea);
       scrollPane = new JScrollPane(messageArea);
           scrollPane.setBounds(34, 350, 200, 365);
           add(scrollPane);
       // �游��� ===================================================
       btCreateRoom = new JButton("�� �����");
       //URL url2 = getClass().getResource("imge/CreateRoom.jpg");
      // btCreateRoom.setBorder(new LineBorder(new Color(0, 0, 0)));
     //  btCreateRoom.setBorderPainted(false);
     //  btCreateRoom.setFocusPainted(false);
       btCreateRoom.setBackground(Color.ORANGE);
     //  btCreateRoom.setIcon(new ImageIcon(url2));
       btCreateRoom.setBounds(246, 679, 155, 71);
       add(btCreateRoom);
       // ===========================================================

       // �α׾ƿ� ===================================================
       btLogOut = new JButton("L O G O U T");
      // btLogOut.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 18));
       btLogOut.setBackground(Color.ORANGE);
       btLogOut.setBounds(623, 718, 149, 33);
       btLogOut.setFocusPainted(false);
       add(btLogOut);
       // ===========================================================
       btcount = new JButton("���� ����");
       btcount.addActionListener(new ActionListener() {
       	public void actionPerformed(ActionEvent e) {
       	}
       });
       // bthow.setBorder(new LineBorder(new Color(0, 0, 0)));
        btcount.setBackground(Color.ORANGE);
        btcount.setBounds(411, 679, 200, 71);
      //  btQuickStart.setIcon(new ImageIcon(url1));
        add(btcount);
       // �������� ===================================================
       btExit = new JButton("E X I T");
       btExit.setBackground(Color.ORANGE);
      // btExit.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 18));
       btExit.setBounds(623, 683, 149, 32);
       btExit.setFocusPainted(false);
       add(btExit);
       // ============================================================

       // �������� ====================================================
      // URL url1 = getClass().getResource("imge/QuickStart.jpg");
       bthow = new JButton("\uCC44\uD305 \uB3C4\uC6C0\uB9D0");
       //bthow.setBorder(new LineBorder(new Color(0, 0, 0)));
       bthow.setBackground(Color.ORANGE);
       bthow.setBounds(34, 272, 200, 71);
     //  btQuickStart.setIcon(new ImageIcon(url1));
       add(bthow);
       // ============================================================

       // ���� ���� ���� ����Ʈ ====================================
       JPanel panel = new JPanel();
       panel.setBackground(Color.WHITE);
       panel.setBounds(246, 62, 526, 607);
       add(panel);
       panel.setPreferredSize(new Dimension(488, 464));
       panel.setLayout(new GridLayout(0, 2, 0, 0));
       for(int i=1;i<=8;i++) {
           JButton bt = new JButton("");
           bt.setHorizontalAlignment(SwingConstants.CENTER);
           bt.setBackground(Color.WHITE);
           bt.setEnabled(false);
           panel.add(bt);
           btList.add(bt);
       }
       // =============================================================

   }

}