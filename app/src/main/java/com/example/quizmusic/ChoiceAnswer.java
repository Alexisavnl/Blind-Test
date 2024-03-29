package com.example.quizmusic;

public class ChoiceAnswer {

    Track cA;
    Track cB;
    Track cC;
    Track cD;
    Track correctAnswer;

    public ChoiceAnswer(Track cA, Track cB, Track cC, Track cD, Track correctAnswer) {
        this.cA = cA;
        this.cB = cB;
        this.cC = cC;
        this.cD = cD;
        this.correctAnswer = correctAnswer;
    }

    public Track getcA() {
        return cA;
    }

    public Track getcB() {
        return cB;
    }

    public Track getcC() {
        return cC;
    }

    public Track getcD() {
        return cD;
    }

    public Track getCorrectAnswer() {
        return correctAnswer;
    }

    @Override
    public String toString() {
        return "ChoiceAnswer{" +
                "cA='" + cA.getTitle() + '\'' +
                ", cB='" + cB.getTitle() + '\'' +
                ", cC='" + cC.getTitle() + '\'' +
                ", cD='" + cD.getTitle() + '\'' +
                ", correctAnswer='" + correctAnswer.getTitle() + '\'' +
                '}';
    }
}
