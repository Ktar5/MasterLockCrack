package me.ktar.lockpick;

import com.sun.deploy.util.StringUtils;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <buildfresh@gmail.com>
 *
 * This file is part of Master-Lockpick.
 *
 * Master-Lockpick can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

public class LockPick extends Application {

    private int valueOne, valueTwo;
    private double valueThree;

    public static void main(String[] args) {
        Application.launch(args);
    }


    @SuppressWarnings("Duplicates")
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Master Combination me.ktar.lockpick.LockPick");
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();

        Label labelOne = new Label("First whole number between 0 and 11 with resistance");
        Label labelTwo = new Label("Second whole number between 0 and 11 with resistance");
        Label labelThree = new Label("Half force to lock, find resistant (can be whole or half)");

        TextField inputOne = new TextField();
        TextField inputTwo = new TextField();
        TextField inputThree = new TextField();

        inputOne.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() == 0) return;
            if (newValue.matches("\\d*") && newValue.length() <= 2) {
                valueOne = Integer.parseInt(newValue);
            } else inputOne.setText(oldValue);
        });
        inputTwo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() == 0) return;
            if (newValue.matches("\\d*") && newValue.length() <= 2) {
                valueTwo = Integer.parseInt(newValue);
            } else inputTwo.setText(oldValue);
        });
        inputThree.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() == 0) return;
            //if (newValue.matches("^[\\d]{1,2}(?:\\.[05]?)?$")){ WORKS
            if (newValue.matches("^[\\d]{1,2}(?:\\.[05]?)?$")) { //ALSO WORKS
                valueThree = Double.parseDouble(newValue);
            } else inputThree.setText(oldValue);
        });

        Button calculate = new Button("Calculate Combo Possibilities");

        TextField outputOne = new TextField(),
                outputTwo = new TextField();
        outputOne.setDisable(true);
        outputTwo.setDisable(true);

        Button comboOne = new Button(), comboTwo = new Button();
        comboOne.setDisable(true);
        comboTwo.setDisable(true);

        HBox hboxCombo = new HBox(20, comboOne, comboTwo);
        VBox vboxOne = new VBox(0, labelOne, inputOne),
                vboxTwo = new VBox(0, labelTwo, inputTwo),
                vboxThree = new VBox(0, labelThree, inputThree),
                vboxInput = new VBox(10, vboxOne, vboxTwo, vboxThree, calculate),
                vboxFour = new VBox(5, outputOne, outputTwo, hboxCombo),
                vboxFinal = new VBox(20, vboxInput, vboxFour);

        vboxFinal.setPadding(new Insets(10, 10, 10, 10));

        //1, 4, 33.5
        calculate.setOnAction(event -> {
            Triplet<Integer, List<Integer>, List<Integer>> triplet = calcCombos(0);
            outputOne.setText(String.valueOf(triplet.getValue0()));
            outputTwo.setText(StringUtils.join(triplet.getValue1().stream().map(String::valueOf).collect(Collectors.toList()), ", "));
            comboOne.setText(String.valueOf(triplet.getValue2().get(0)));
            comboTwo.setText(String.valueOf(triplet.getValue2().get(1)));
            comboOne.setDisable(false);
            comboTwo.setDisable(false);
        });
        comboOne.setOnAction(event -> {
            Triplet<Integer, List<Integer>, List<Integer>> triplet = calcCombos(1);
            outputOne.setText(String.valueOf(triplet.getValue0()));
            outputTwo.setText(StringUtils.join(triplet.getValue1().stream().map(String::valueOf).collect(Collectors.toList()), ", "));
            comboOne.setText(String.valueOf(triplet.getValue2().get(0)));
            comboOne.setDisable(true);
            comboTwo.setDisable(false);
            comboTwo.setText(String.valueOf(triplet.getValue2().get(1)));
        });
        comboTwo.setOnAction(event -> {
            Triplet<Integer, List<Integer>, List<Integer>> triplet = calcCombos(2);
            outputOne.setText(String.valueOf(triplet.getValue0()));
            outputTwo.setText(StringUtils.join(triplet.getValue1().stream().map(String::valueOf).collect(Collectors.toList()), ", "));
            comboOne.setText(String.valueOf(triplet.getValue2().get(0)));
            comboOne.setDisable(false);
            comboTwo.setDisable(true);
            comboTwo.setText(String.valueOf(triplet.getValue2().get(1)));
        });

        Scene scene = new Scene(vboxFinal);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Triplet<Integer, List<Integer>, List<Integer>> calcCombos(int x) {
        int outputOne;
        List<Integer> outputTwo = new ArrayList<>(), outputThree = new ArrayList<>();
        outputOne = (int) ((Math.ceil(valueThree) + 5) % 40);

        int mod = outputOne % 4;
        for (int i = 0; i < 4; i++) {
            if (((10 * i) + valueOne) % 4 == mod) {
                outputThree.add((10 * i) + valueOne);
            }
            if (((10 * i) + valueTwo) % 4 == mod) {
                outputThree.add((10 * i) + valueTwo);
            }
        }

        for (int i = 0; i < 10; i++) {
            int temp = ((mod + 2) % 4) + (4 * i);
            if (x == 0 || ((outputThree.get(x - 1) - 2) % 40 != temp && ((outputThree.get(x - 1) - 2) % 40 != temp))) {
                outputTwo.add(temp);
            }
        }
        return new Triplet<>(outputOne, outputTwo, outputThree);
    }


}