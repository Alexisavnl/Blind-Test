package com.example.quizmusic;

public class ChoiceAnswer {

    String cA;
    String cB;
    String cC;
    String cD;
    String correctAnswer;

    public ChoiceAnswer(String cA, String cB, String cC, String cD, String correctAnswer) {
        this.cA = cA;
        this.cB = cB;
        this.cC = cC;
        this.cD = cD;
        this.correctAnswer = correctAnswer;
    }

    public String getcA() {
        return cA;
    }

    public void setcA(String cA) {
        this.cA = cA;
    }

    public String getcB() {
        return cB;
    }

    public void setcB(String cB) {
        this.cB = cB;
    }

    public String getcC() {
        return cC;
    }

    public void setcC(String cC) {
        this.cC = cC;
    }

    public String getcD() {
        return cD;
    }

    public void setcD(String cD) {
        this.cD = cD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
