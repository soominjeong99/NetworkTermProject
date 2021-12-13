package client;

public class ClientStart {
    public static void main(String[] args) {
        String ip = "localhost"; // 클라이언트 시작
        ClientUI b = new ClientUI(ip);
        b.setLocationRelativeTo(null);
        b.setVisible(true);
    }
}