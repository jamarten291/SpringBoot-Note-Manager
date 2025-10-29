package com.jackson.demonotes2.model;

public class NoteStats {
    private int noteAmount;
    private double noteContentAvg;
    private int noteLongestTitle;

    public NoteStats(int noteAmount, double noteContentAvg, int noteLongestTitle) {
        this.noteAmount = noteAmount;
        this.noteContentAvg = noteContentAvg;
        this.noteLongestTitle = noteLongestTitle;
    }

    public int getNoteAmount() {
        return noteAmount;
    }

    public double getNoteContentAvg() {
        return noteContentAvg;
    }

    public int getNoteLongestTitle() {
        return noteLongestTitle;
    }

    @Override
    public String toString() {
        return "NoteStats{" +
                "noteAmount=" + noteAmount +
                ", noteContentAvg=" + noteContentAvg +
                ", noteLongestTitle=" + noteLongestTitle +
                '}';
    }
}
