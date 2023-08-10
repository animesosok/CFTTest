package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    static private BufferedFIleList[] readers;
    static int fileNum;
    public static void main(String[] args) throws IOException {
        int len = args.length;
        boolean asc = true;
        Class type = null;
        String outputFile = null;
        List<String> inputFiles = new ArrayList<>();

        for(var arg : args){
            if (arg.compareTo("-i")==0){
                type = Integer.class;
            }
            else if (arg.compareTo("-s")==0){
                type = String.class;
            }
            else if (arg.compareTo("-d")==0){
                asc = false;
            }
            else if (arg.compareTo("-a") == 0){
                asc = true;
            }
            else if (outputFile == null){
                outputFile = arg;
            }
            else {
                inputFiles.add(arg);
            }

        }
        if (type == null) {
            System.err.println("Not enough args: files type");
            return;
        }
        fileNum = inputFiles.size();
        readers = new BufferedFIleList[fileNum];
        for (int i = 0; i < fileNum; i++){
            readers[i] = new BufferedFIleList(inputFiles.get(i));
        }
        if (outputFile == null) {
            System.err.println("Not enough args: output filename");
            return;
        }
        FileWriter writer = new FileWriter(outputFile);
        if (type.equals(Integer.class)) {
            Comparator<Integer> comparator;
            if (asc) {
                 comparator= new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                };
            }
            else {
                comparator= new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o2.compareTo(o1);
                    }
                };
            }
            Sorter<Integer> sorter = new Sorter<>(comparator);
            for (int i = 0; i < fileNum; i++) {
                String line = readers[i].getNext();
                while (!checkLine(line)) {
                    readers[i].remove();
                    line = readers[i].getNext();
                }
                if (line != null) {
                    try {
                        int a = Integer.parseInt(line);
                        sorter.add(a, i);
                    } catch (Exception e) {
                        throw new RuntimeException("dddddd");
                    }
                }
            }
            Integer next = sorter.pop();
            while (next != null) {
                String wr = readers[next].remove();
                writer.write(wr);
                writer.write("\n");
                String line = readers[next].getNext();
                while (!checkLine(line)) {
                    readers[next].remove();
                    line = readers[next].getNext();
                }
                if (line != null) {
                    int b = Integer.parseInt(line);
                    sorter.add(b, next);
                }
                next = sorter.pop();
            }
            writer.close();
        }
        if (type.equals(String.class)){
            Comparator<String> comparator;
            if (asc) {
                comparator= new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                };
            }
            else {
                comparator= new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o2.compareTo(o1);
                    }
                };
            }
            Sorter<String> sorter = new Sorter<>(comparator);
            for (int i = 0; i < fileNum; i++) {
                String line = readers[i].getNext();
                while (!checkLine(line)) {
                    readers[i].remove();
                    line = readers[i].getNext();
                }
                if (line != null) {
                   sorter.add(line, i);
                }
            }
            Integer next = sorter.pop();
            while (next != null) {
                String wr = readers[next].remove();
                writer.write(wr);
                writer.write("\n");
                String line = readers[next].getNext();
                while (!checkLine(line)) {
                    readers[next].remove();
                    line = readers[next].getNext();
                }
                if (line != null) {
                    sorter.add(line, next);
                }
                next = sorter.pop();
            }
            writer.close();
        }
    }
    private static boolean checkLine(String line){
        if (line != null){
            if (line.contains(" ")){
                System.err.println("Found whitespace, line skipped: " + line);
                return false;
            }
        }
        return true;
    }

}