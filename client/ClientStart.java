package client;

public class ClientStart {
    public static void main(String[] args) {
        String ip = "localhost"; // Ŭ���̾�Ʈ ����
        ClientUI b = new ClientUI(ip);
        b.setLocationRelativeTo(null);
        b.setVisible(true);
    }
}