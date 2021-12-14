package client2.main;

import com.google.gson.Gson;
import client2.protocol.*;
import client2.view.*;
import processing.core.PApplet;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Window extends PApplet {
    private int bingoEdge;
    private int bingoBlock;
    private int bingoBlockHalf;
    public int win = 0;
    public int lose = 0;

    private float myDiceX;
    private float myDiceY;
    private float oppoDiceX;
    private float oppoDiceY;
    private float diceDiameter;

    private ClientBingoPlate bingoPlate;
    private Button readyButton;
    private Button exitButton;

    public PlayersInfo playersInfo;

    private CountDown countDown;

    private GameResult gameResult;
    private float gameResultX;
    private float gameResultY;

    private Dice myDice;
    private Dice opponentDice;
    private int myDiceNum;
    private int opponentDiceNum;

    private boolean myTurn;

    private int myColor;
    private int mouseValue;

    private boolean countDownFlag;
    private boolean resultMessageFlag;

    //Client
    private Socket socket;
    private ClientNum clientNum;
    private GameState gameState;
    private WinCheck winCheck;

    //queue
    // TODO : Queue �� �ϳ��� ������ ��. => �ϱް�ü�� ����� ��.
    Queue<Protocol> protocolQueue = new ConcurrentLinkedQueue<>();
    
    @Override
    public void settings() {

        connect();


        size(ConstantWindow.WIDTH, ConstantWindow.HEIGHT);

        bingoPlate = new ClientBingoPlate(ConstantWindow.BINGO_LENGTH,
                ConstantWindow.BINGO_EXTERNAL_X_VALUE,
                ConstantWindow.BINGO_EXTERNAL_Y_VALUE);

        readyButton = new Button(ConstantWindow.READY_BUTTON_X,
                ConstantWindow.READY_BUTTON_Y,
                ConstantWindow.BUTTON_DIAMETER,
                ConstantWindow.BUTTON_DIAMETER);
        readyButton.setLabel(ConstantWindow.READY_BUTTON_LABEL);
        readyButton.setColor(Color.LIGHT_GREY.getValue());

        exitButton = new Button(ConstantWindow.EXIT_BUTTON_X,
                ConstantWindow.EXIT_BUTTON_Y,
                ConstantWindow.BUTTON_DIAMETER,
                ConstantWindow.BUTTON_DIAMETER);
        exitButton.setLabel(ConstantWindow.EXIT_BUTTON_LABEL);
        exitButton.setColor(Color.LIGHT_GREY.getValue());

        playersInfo = new PlayersInfo(ConstantWindow.PLAYERS_INFO_X,
                ConstantWindow.PLAYERS_INFO_Y,
                ConstantWindow.PLAYERS_INFO_WIDTH,
                ConstantWindow.PLAYERS_INFO_HEIGHT);

        bingoEdge = getEdge();
        bingoBlock = getBlock();
        bingoBlockHalf = bingoBlock / 2;

        myDiceX = getMyDiceX();
        myDiceY = getMyDiceY();
        oppoDiceX = getOppoDiceX();
        oppoDiceY = getOppoDiceY();
        diceDiameter = getDiecDiameter();

        gameResultX = getGameResultX();
        gameResultY = getGameResultY();

        myTurn = false;
        winCheck = new WinCheck(false);

        myColor = ConstantProtocol.YELLOW_STONE;
        gameState = new GameState(GameState.WAITING);
        System.out.println("gameState: WAITING");
    }

    private int getEdge() {
        return ConstantWindow.BINGO_LENGTH / 30;
    }

    private int getBlock() {
        return (ConstantWindow.BINGO_LENGTH - 2 * bingoEdge) / (ConstantWindow.BINGO_NUM - 1);
    }

    private float getMyDiceX() {
        return (ConstantWindow.BINGO_EXTERNAL_X_VALUE + bingoEdge + 2 * bingoBlock + bingoBlockHalf);
    }

    private float getMyDiceY() {
        return ConstantWindow.BINGO_EXTERNAL_Y_VALUE + bingoEdge + 5 * bingoBlock;
    }

    private float getOppoDiceX() {
        return ConstantWindow.BINGO_EXTERNAL_X_VALUE + bingoEdge + 8 * bingoBlock + bingoBlockHalf;
    }

    private float getOppoDiceY() {
        return ConstantWindow.BINGO_EXTERNAL_Y_VALUE + bingoEdge + 5 * bingoBlock;
    }

    private float getDiecDiameter() {
        return bingoBlock * 3;
    }

    private float getGameResultX() {
        return ConstantWindow.BINGO_EXTERNAL_X_VALUE + ConstantWindow.BINGO_LENGTH / 2;
    }

    private float getGameResultY() {
        return ConstantWindow.BINGO_EXTERNAL_Y_VALUE + ConstantWindow.BINGO_LENGTH / 2;
    }

    @Override
    public void draw() {

        this.background(102, 178, 255, 255);
        readyButton.display(this);
        exitButton.display(this);
        bingoPlate.display(this);
        playersInfo.display(this);

        checkMouseCursor();

        if (!protocolQueue.isEmpty()) {
            Protocol protocol = protocolQueue.poll();
            String data = protocol.getData();
            String type = protocol.getType();

            Gson gson = new Gson();

            switch (type) {
                case ConstantProtocol.GAME_STATE:
                    gameState = gson.fromJson(data, GameState.class);

                    if (gameState.getGameState() == GameState.WAITING) {
                        init();
                    }

                    System.out.println("gameState: " + gameState.getGameState());
                    break;
                case ConstantProtocol.CLIENT_NUM_MINE:
                    clientNum = gson.fromJson(data, ClientNum.class);
                    System.out.println("clientNum: " + clientNum.getClientNum());
                    setPlayersLabel();
                    break;
                case ConstantProtocol.CLIENT_NUM_OTHER:
                    if (clientNum.getClientNum() == ClientNum.ONE) {
                        playersInfo.setOpponentLabel(PlayersInfo.OPPONENT_LABEL);
                    }
                    break;
                case ConstantProtocol.COUNT_DOWN_NUM:
                    CountDownNum countDownNum = gson.fromJson(data, CountDownNum.class);
                    countDown.setCountDownNum(countDownNum);
                    break;
                case ConstantProtocol.DICE:
                    DiceNum diceNum = gson.fromJson(data, DiceNum.class);
                    setDicesNum(diceNum);
                    makeDices();
                    myTurn = diceNum.getMyTurn();
                    setMyColor(myTurn);
                    setStoneViewsColor();
                    break;
                case ConstantProtocol.WIN:
                    winCheck = gson.fromJson(data, WinCheck.class);
                    break;
                case ConstantProtocol.STONE_LOCATION:
                    StoneLocation location = gson.fromJson(data, StoneLocation.class);
                    int row = location.getRow();
                    int col = location.getCol();
                    int stoneColor = location.getColor();
                    
                    bingoPlate.setStack(col, row);
                    bingoPlate.recordStone(bingoPlate.getStack(col), col, stoneColor);
                    myTurn = myColor != stoneColor;

                    System.out.println("stone recorded row: " + row + " col:" + col);

                    break;
                case ConstantProtocol.EXIT:
                    if (clientNum.getClientNum() == ClientNum.TWO) {
                        clientNum.setClientNum(ClientNum.ONE);
                        System.out.println("clientNum: " + clientNum.getClientNum());
                    }
                    initOpponentLabel();
                    break;
            }

        }


        switch (gameState.getGameState()) {
            case GameState.WAITING:
            case GameState.RUNNING:
                break;
            case GameState.SET_ORDER:
                if (!countDownFlag) {
                    countDownFlag = true;
                    countDown = new CountDown(ConstantWindow.BINGO_LENGTH,
                            ConstantWindow.BINGO_EXTERNAL_X_VALUE,
                            ConstantWindow.BINGO_EXTERNAL_Y_VALUE);
                }
                if (countDown != null) {
                    countDown.display(this);
                }

                if (myDice != null) {
                    myDice.display(this);
                }
                if (opponentDice != null) {
                    opponentDice.display(this);
                }
                break;
            case GameState.GAME_OVER:
                sendInitStone();
                if (!resultMessageFlag) {
                    resultMessageFlag = true;
                    String message = winCheck.getWinCheck() ? "WIN" : "LOSE";
                    try {
                    	 OutputStream output = null;
                     	try {
                     		output = new FileOutputStream("./rank.txt");//���� ��� ����Ǵ� ����                			
                     		String str = message;
                 			String result = null;
                 			
                 			if(message.equalsIgnoreCase("win"))
                 			{
                 				win++;
                 			}
                 			else if(message.equalsIgnoreCase("lose"))
                 			{
                 				lose++;
                 			}
                 			
                 			result = win + "�� " + lose + "��";
                 		
                 			output.write(result.getBytes());
                 		} catch (FileNotFoundException e) {
                 			// TODO Auto-generated catch block
                 			e.printStackTrace();
                 		}finally{
                     		output.close();
                     	}
						gameResult = new GameResult(gameResultX, gameResultY, message);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
                if (gameResult != null) {
                    gameResult.display(this);
                }
                break;
        }


    }

    private void setStoneViewsColor() {
        if (myColor == ConstantProtocol.YELLOW_STONE) {
            playersInfo.setMineStoneView(Color.BLACK.getValue());
            playersInfo.setOpponentStoneView(Color.WHITE.getValue());
        } else if (myColor == ConstantProtocol.RED_STONE) {
            playersInfo.setMineStoneView(Color.WHITE.getValue());
            playersInfo.setOpponentStoneView(Color.BLACK.getValue());
        }
    }

    private void init() {
        myTurn = false;
        winCheck = new WinCheck(false);
        countDownFlag = false;
        resultMessageFlag = false;
        bingoPlate.initStones();
        while (!protocolQueue.isEmpty()) {
            protocolQueue.remove();
        }
        if (myDice != null) {
            myDice = makeDiceDisable();
        }
        if (opponentDice != null) {
            opponentDice = makeDiceDisable();
        }
        makeReadyButtonWhite();

        playersInfo.stoneViewsInit();
    }

    private void setDicesNum(DiceNum diceNum) {
        myDiceNum = diceNum.getMyDiceNum();
        opponentDiceNum = diceNum.getOpponentDiceNum();
        System.out.println("myDiceNum: " + myDiceNum);
        System.out.println("oppoDiceNum: " + opponentDiceNum);
    }

    private void makeDices() {
        myDice = new Dice(myDiceX, myDiceY, diceDiameter, myDiceNum);
        myDice.setLabel("mine");
        opponentDice = new Dice(oppoDiceX, oppoDiceY, diceDiameter, opponentDiceNum);
        opponentDice.setLabel("opponent");
    }

    private void setMyColor(boolean myTurn) {
        myColor = myTurn ? ConstantProtocol.YELLOW_STONE : ConstantProtocol.RED_STONE;
    }

    private void checkMouseCursor() {

        switch (gameState.getGameState()) {
            case GameState.WAITING:
                if (readyButton.overRect(mouseX, mouseY) ||
                        exitButton.overRect(mouseX, mouseY)) {
                    cursor(HAND);
                    mouseValue = HAND;
                } else {
                    cursor(ARROW);
                    mouseValue = ARROW;
                }
                break;
            case GameState.SET_ORDER:
            case GameState.GAME_OVER:
                cursor(ARROW);
                mouseValue = ARROW;
                break;
            case GameState.RUNNING:
                if (bingoPlate.overVertex(mouseX, mouseY) && myTurn) {
                    cursor(HAND);
                    mouseValue = HAND;
                } else {
                    cursor(ARROW);
                    mouseValue = ARROW;
                }
                break;
        }

    }

    private void setPlayersLabel() {
        playersInfo.setClientNum(clientNum);
        playersInfo.setMineLabel(PlayersInfo.MINE_LABEL);
        if (clientNum.getClientNum() == ClientNum.TWO) {
            playersInfo.setOpponentLabel(PlayersInfo.OPPONENT_LABEL);
        }
    }

    private void initOpponentLabel() {
        playersInfo.setOpponentLabel(PlayersInfo.NONE_LABEL);
    }
    
    public void exit2() {
        try {
          System.exit(0);
        } catch (SecurityException e) {
          // don't care about applet security exceptions
        }
      }


    @Override
    public void mousePressed() {
        if (mouseButton == LEFT && mouseValue == HAND) {
            if (readyButton.overRect(mouseX, mouseY)) {
                if (readyButton.getColor() == Color.LIGHT_GREY.getValue()) {
                    sendReady();
                    makeReadyButtonBlack();
                } else {
                    sendNotReady();
                    makeReadyButtonWhite();
                }
            } else if (exitButton.overRect(mouseX, mouseY)) {
            	exit();
            	
                
            } else {

                int currentX = mouseX - ConstantWindow.BINGO_EXTERNAL_X_VALUE;
                int currentY = mouseY - ConstantWindow.BINGO_EXTERNAL_Y_VALUE;

                int fixedX = bingoPlate.editPosition(currentX);
                int fixedY = bingoPlate.editPosition(currentY);

                
                int col = bingoPlate.getIndex(fixedX);
                bingoPlate.minusStack(col);
                int row = bingoPlate.getStack(col);
                
                
                sendLocation(row, col);
                myTurn = false;

            }
        }
    }

    private void makeReadyButtonWhite() {
        readyButton = new Button(ConstantWindow.READY_BUTTON_X,
                ConstantWindow.READY_BUTTON_Y,
                ConstantWindow.BUTTON_DIAMETER,
                ConstantWindow.BUTTON_DIAMETER);
        readyButton.setLabel(ConstantWindow.READY_BUTTON_LABEL);
        readyButton.setColor(Color.LIGHT_GREY.getValue());
    }

    private void makeReadyButtonBlack() {
        readyButton = new Button(ConstantWindow.READY_BUTTON_X,
                ConstantWindow.READY_BUTTON_Y,
                ConstantWindow.BUTTON_DIAMETER,
                ConstantWindow.BUTTON_DIAMETER);
        readyButton.setLabel(ConstantWindow.READY_BUTTON_LABEL);
        readyButton.setColor(Color.BLACK.getValue());
    }

    private Dice makeDiceDisable() {
        return new Dice(ConstantWindow.NONE,
                ConstantWindow.NONE,
                ConstantWindow.MIN,
                ConstantWindow.NONE);
    }

    //client
    private void connect() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("127.0.0.1", 5500));
            System.out.println("[���� ���� ����]");
            ReceiveThread receiveThread = new ReceiveThread(socket, this);
            receiveThread.start();

        } catch (IOException e) {
            System.out.println("[���� ���� �ȵ�]");
            exit();
            
        }
    }

    private void sendReady() {
        ReadyData ready = new ReadyData(ReadyData.READY);
        Gson gson = new Gson();

        String data = gson.toJson(ready);
        String type = ConstantProtocol.READY;

        sendToServer(data, type);
    }

    private void sendNotReady() {
        ReadyData notReady = new ReadyData(ReadyData.NOT_READY);
        Gson gson = new Gson();

        String data = gson.toJson(notReady);
        String type = ConstantProtocol.NOT_READY;
        sendToServer(data, type);
    }

    private void sendLocation(int row, int col) {

        StoneLocation location = new StoneLocation(bingoPlate.getStack(col), col, myColor);
        Gson gson = new Gson();

        String data = gson.toJson(location);
        String type = ConstantProtocol.STONE_LOCATION;

        sendToServer(data, type);
        System.out.println("row:" + row);
        System.out.println("col:" + col);

    }

    private void sendInitStone() {

        String data = "";
        String type = ConstantProtocol.INIT_STONE;

        sendToServer(data, type);
    }

    private void sendToServer(String data, String type) {

        try {
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            Gson gson = new Gson();

            Protocol protocol = new Protocol(data, type);
            String json = gson.toJson(protocol);

            int len = json.length();

            dos.writeInt(len);
            os.write(json.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
