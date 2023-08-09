package org.example;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class BufferedFIleList {

    private final int QUEUE_SIZE = 100;
    private FileReader reader;
    private Scanner scanner;
    private Queue<String> queue = new LinkedList<>();

    public BufferedFIleList(String filename){
        try {
            reader = new FileReader(filename);
            scanner = new Scanner(reader);
            fillQueue();
        } catch (Exception e) {
            System.err.println("Can not find file: " + filename);
        }
    }
    private void fillQueue(){
        if (scanner == null){
            return;
        }
        while ( QUEUE_SIZE != queue.size() && scanner.hasNext()){
            String s = scanner.nextLine();
            queue.add(s);
        }
    }

    public String getNext(){
        if (queue.isEmpty()){
            fillQueue();
        }
        return queue.peek();
    }
    public String remove(){
        return queue.poll();
    }
}
