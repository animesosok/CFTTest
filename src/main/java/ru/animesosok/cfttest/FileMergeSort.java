package ru.animesosok.cfttest;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;

public class FileMergeSort {

    BufferedFIleList[] readers;
    FileWriter writer = null;
    boolean asc;

    int filesNum;
    FileType type;
    public FileMergeSort(List<File> inputFiles, File outputFile, boolean ascending, FileType type){
        filesNum = inputFiles.size();
        readers = new BufferedFIleList[filesNum];
        for (int i = 0; i < filesNum; i++){
            readers[i] = new BufferedFIleList(inputFiles.get(i));
        }
        try {
            writer = new FileWriter(outputFile);
        } catch (IOException e) {
            System.err.println("file write problem " + outputFile.getName());
        }
        this.asc = ascending;
        this.type = type;


    }
    public void sort(){
        if (writer == null){
            return;
        }
        if (type == FileType.INTEGER){
            sortAsInteger();
        }
        if (type == FileType.STRING){
            sortAsString();
        }
        try {
            writer.close();
        } catch (IOException e) {
            System.err.println("Writer close error: " + e);
        }
    }

    private void sortAsInteger(){
        Integer lastWrote = null;
        Comparator<Integer> comparator;
        if (asc) {
            comparator = new Comparator<Integer>() {
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

        for (int i = 0; i < filesNum; i++) {
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
                    System.err.println("Can not parse int, line skipped: " + line);
                }
            }
        }

        Integer next = sorter.pop();

        while (next != null){
            String toWrite = readers[next].remove();
            String line = readers[next].getNext();
            while (!checkLine(line)){
                readers[next].remove();
                line = readers[next].getNext();
            }
            if (line != null) {
                try {
                    Integer el = Integer.parseInt(line);
                    sorter.add(el, next);
                } catch (Exception e) {
                    System.err.println("Can not parse int, line skipped: " + line);
                }
            }
            next = sorter.pop();
            if (lastWrote != null){
                if (comparator.compare(lastWrote, Integer.parseInt(toWrite)) < 0){
                    System.err.println(toWrite + " more/less then " + lastWrote + ", line skipped");
                    continue;
                }
            }
            lastWrote = Integer.parseInt(toWrite);
            write(toWrite);
        }
    }
    private void sortAsString(){
        Comparator<String> comparator;
        String lastWrote = null;
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
        for (int i = 0; i < filesNum; i++) {
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
            String toWrite = readers[next].remove();
            String line = readers[next].getNext();
            while (!checkLine(line)){
                readers[next].remove();
                line = readers[next].getNext();
            }
            if (line != null) {
                sorter.add(line, next);
            }
            next = sorter.pop();
            if (lastWrote != null){
                if (comparator.compare(lastWrote, toWrite) < 0){
                    System.err.println(toWrite + " more/less then " + lastWrote + ", line skipped");
                    continue;
                }
            }
            lastWrote = toWrite;
            write(toWrite);
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

    private void write(String str){
        try {
            writer.write(str);
            writer.write("\n");
        } catch (IOException e) {
            System.err.println("File write error: " + e);
        }
    }

}
