package com.izdep.app.runner.spell_checker.entity;

import java.io.Serializable;

public class SpellCheckerResult implements Serializable {

    private boolean correct;
    private String result;

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
