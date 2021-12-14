package client2.view;


import client2.protocol.ConstantProtocol;
import processing.core.PApplet;


public class ClientBingoPlate implements Displayable {

    private static final int NUM = 15;

    private final int length;
    private final int externalXValue;
    private final int externalYValue;

    private final int stoneDiameter;
    private final int edge;
    private final int block;
    private final int rangeSize;
    

    private Stone[][] stones = new Stone[NUM][NUM];
    public int[] stack = new int[NUM];

    public ClientBingoPlate(int length, int externalXValue, int externalYValue) {
        this.length = length;
        this.externalXValue = externalXValue;
        this.externalYValue = externalYValue;

        stoneDiameter = getStoneDiameter();
        edge = getEdge();
        block = getBlock();
        rangeSize = getRangeSize();
        
        
        
        stack = new int[] {NUM, NUM, NUM, NUM, NUM, NUM, NUM, NUM, NUM, NUM, NUM, NUM, NUM, NUM, NUM};
        initStones();
    }

    private int getStoneDiameter() {
        return length / 20;
    }

    private int getEdge() {
        return length / 30;
    }

    private int getBlock() {
        return (length - 2 * edge) / (NUM - 1);
    }

    private int getRangeSize() {
        return block / 4;
    }


    @Override
    public void display(PApplet p) {
        drawFrame(p);
        drawCoordinate(p);
        drawStones(p);
    }

    private void drawFrame(PApplet p) {
        p.noStroke();
        p.fill(102, 178, 255, 255);
        p.rect(externalXValue, externalYValue, length, length);

        p.fill(Color.WHITE.getValue());
        p.stroke(Color.BLACK.getValue());
        p.strokeWeight(2);
        p.rect(externalXValue + edge , externalYValue + edge,  (NUM - 1) * block, (NUM - 1) * block);
    }

    private void drawCoordinate(PApplet p) {
        p.strokeWeight(1);
        for (int i = 0; i < NUM; i++) {
            p.line(externalXValue + edge, externalYValue + edge + i * block,
                    externalXValue + edge + (NUM - 1) * block, externalYValue + edge + i * block);
            p.line(externalXValue + edge + i * block, externalYValue + edge,
                    externalXValue + edge + i * block, externalYValue + edge + (NUM - 1) * block);
        }
    }

    
    
    private void drawStones(PApplet p) {
        for (int i = 0; i < NUM; i++) {
            for (int j = 0; j < NUM; j++) {
                if (stones[i][j].getValue() == Stone.YELLOW_STONE) { //Yellow
                    p.fill(255, 255, 0, 255);
                    p.ellipse(edge + (j + 1/2) * block + externalXValue, edge + (i + 1/2) * block + externalYValue,
                            stoneDiameter, stoneDiameter);
                } else if (stones[i][j].getValue() == Stone.RED_STONE) { //Red
                    p.fill(255, 0, 0, 255);
                    p.ellipse(edge + (j + 1/2) * block + externalXValue, edge + (i + 1/2) * block + externalYValue,
                            stoneDiameter, stoneDiameter);
                }
            }
        }
    }


    public int editPosition(int mousePos) {

        int quotient = (mousePos - edge) / block;
        int remainder = (mousePos - edge) % block;

        if (remainder >= block - rangeSize) {
            return (quotient + 1) * block;
        } else if (remainder <= rangeSize) {
            return (quotient) * block;
        }
        return 0;
    }

    public int getIndex(int fixPos) {
        return fixPos / block;
    }


    public boolean overVertex(int mouseX, int mouseY) {

        int currentX = mouseX - externalXValue;
        int currentY = mouseY - externalYValue;

        if (checkRange(currentX, currentY)) {
            return checkHasStone(currentX, currentY);
        }
        return false;
    }

    private boolean checkRange(int currentX, int currentY) {
        for (int i = 0; i < NUM; i++) {
            for (int j = 0; j < NUM; j++) {
                if (((edge - rangeSize + block * i) < currentX) &&
                        ((edge + rangeSize + block * i) > currentX) &&
                        ((edge - rangeSize + block * j) < currentY) &&
                        ((edge + rangeSize + block * j) > currentY)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkHasStone(int currentX, int currentY) {
        return stones[getIndex(editPosition(currentY))][getIndex(editPosition(currentX))].getValue() == 0;
    }


    public void recordStone(int row, int col, int stoneFlag) {
        int value = stoneFlag == ConstantProtocol.YELLOW_STONE ? Stone.YELLOW_STONE : Stone.RED_STONE;
        stones[stack[col]][col].setValue(value);
        System.out.println("recordStone" + stack[col] + " " + col);
        
    }
    
    public void minusStack(int col) {
    	stack[col]--;
    }
    
    public int getStack(int col) {
    	return stack[col];
    }
    
    public void setStack(int col, int value) {
    	stack[col] = value;
    }
    
    public void initStones() {
        for (int i = 0; i < NUM; i++) {
            for (int j = 0; j < NUM; j++) {
                stones[i][j] = new Stone(Stone.NONE_STONE);
            }
        }
        stack = new int[] {NUM, NUM, NUM, NUM, NUM, NUM, NUM, NUM, NUM, NUM, NUM, NUM, NUM, NUM, NUM};
    }

}
