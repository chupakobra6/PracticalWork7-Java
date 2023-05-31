package com.example.practicalwork7;

import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    private String savedSolution;
    private String savedResult;

    public void setData(String solution, String result) {
        savedSolution = solution;
        savedResult = result;
    }

    public String getSavedSolution() {
        return savedSolution;
    }

    public String getSavedResult() {
        return savedResult;
    }
}
