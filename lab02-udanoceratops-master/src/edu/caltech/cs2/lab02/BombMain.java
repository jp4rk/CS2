package edu.caltech.cs2.lab02;

public class BombMain {
    public static void main(String[] args) {
        Bomb b = new Bomb();
        b.phase0("22961293");
        b.phase1("hdc");
        String pass = "1374866960";
        for (int i = 0; i < 5000; i++){
            pass = " " + pass;
        }
        b.phase2(pass);
    }
}