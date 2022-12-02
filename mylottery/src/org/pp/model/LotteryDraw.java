package org.pp.model;

/**
 * ************自强不息************
 *
 * @author 鹏鹏
 * @date 2022/12/1 13:07
 * ************厚德载物************
 **/
public class LotteryDraw {
    private String lotteryDrawNum;
    private String lotteryDrawResult;
    private String lotteryDrawTime;
    private String lotteryGameName;
    private String lotteryGameNum;

    public LotteryDraw() {
    }

    public LotteryDraw(String lotteryDrawNum, String lotteryDrawResult, String lotteryDrawTime) {
        this.lotteryDrawNum = lotteryDrawNum;
        this.lotteryDrawResult = lotteryDrawResult;
        this.lotteryDrawTime = lotteryDrawTime;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb/*.append(lotteryDrawTime).append(" ")*/.append(lotteryDrawNum).append(" ").append(lotteryDrawResult);
        return sb.toString();
    }

    public String getLotteryDrawNum() {
        return lotteryDrawNum;
    }

    public void setLotteryDrawNum(String lotteryDrawNum) {
        this.lotteryDrawNum = lotteryDrawNum;
    }

    public String getLotteryDrawResult() {
        return lotteryDrawResult;
    }

    public void setLotteryDrawResult(String lotteryDrawResult) {
        this.lotteryDrawResult = lotteryDrawResult;
    }

    public String getLotteryDrawTime() {
        return lotteryDrawTime;
    }

    public void setLotteryDrawTime(String lotteryDrawTime) {
        this.lotteryDrawTime = lotteryDrawTime;
    }

    public String getLotteryGameName() {
        return lotteryGameName;
    }

    public void setLotteryGameName(String lotteryGameName) {
        this.lotteryGameName = lotteryGameName;
    }

    public String getLotteryGameNum() {
        return lotteryGameNum;
    }

    public void setLotteryGameNum(String lotteryGameNum) {
        this.lotteryGameNum = lotteryGameNum;
    }
}
