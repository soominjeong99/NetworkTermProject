package client2.view;

import client2.protocol.CountDownNum;
import processing.core.PApplet;

public class CountDown implements Displayable {


    private static final int NONE = 0;

    private final int bingoPlateLength;
    private final int bingoPlateExternalXValue;
    private final int bingoPlateExternalYValue;
    private final int textSize;

    private CountDownNum countDownNum;

    public CountDown(int bingoPlateLength, int bingoPlateExternalXValue, int bingoPlateExternalYValue) {
        this.bingoPlateLength = bingoPlateLength;
        this.bingoPlateExternalXValue = bingoPlateExternalXValue;
        this.bingoPlateExternalYValue = bingoPlateExternalYValue;
        textSize = getTextSize();
        countDownNum = new CountDownNum(0);
    }

    private int getTextSize() {
        return bingoPlateLength / 12;
    }

    public void display(PApplet p) {
        if (countDownNum.getCountDown() == 0) {
            p.text("", NONE, NONE);

        } else {
            p.fill(Color.BLACK.getValue());
            p.textSize(textSize);
            p.text(countDownNum.getCountDown(), bingoPlateExternalXValue + bingoPlateLength / 2, bingoPlateExternalYValue + bingoPlateLength / 2);
        }
    }

    public void setCountDownNum(CountDownNum countDownNum) {
        this.countDownNum = countDownNum;
    }
}
