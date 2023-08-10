package ru.animesosok.cfttest;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    static private BufferedFIleList[] readers;
    static private int fileNum;
    static private FileWriter writer;
    public static void main(String[] args) throws IOException {
        boolean asc = true;
        FileType type = null;
        File outputFile = null;
        List<File> inputFiles = new ArrayList<>();

        for(var arg : args){
            if (arg.compareTo("-i")==0){
                type = FileType.INTEGER;
            }
            else if (arg.compareTo("-s")==0){
                type = FileType.STRING;
            }
            else if (arg.compareTo("-d")==0){
                asc = false;
            }
            else if (arg.compareTo("-a") == 0){
                asc = true;
            }
            else if (outputFile == null){
                try {
                    outputFile = new File(arg);
                }
                catch (Exception e){

                }
            }
            else {
                try {
                    inputFiles.add(new File(arg));
                }
                catch (Exception e) {

                }
            }

        }
        if (type == null) {
            System.err.println("Not enough args: files type");
            return;
        }
        if (outputFile == null) {
            System.err.println("Not enough args: output file");
            return;
        }
        if (inputFiles.isEmpty()){
            System.err.println("Not enough args: input file");
            return;
        }

        FileMergeSort mergeSort = new FileMergeSort(inputFiles, outputFile, asc, type);
        mergeSort.sort();
    }


}