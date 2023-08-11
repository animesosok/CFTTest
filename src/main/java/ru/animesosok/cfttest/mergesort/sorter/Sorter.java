package ru.animesosok.cfttest.mergesort.sorter;

import java.util.*;

public class Sorter<T> {
    List<T> elemList = new ArrayList<>();
    List<Integer> indexList = new ArrayList<>();
    Comparator<T> comparator;
    public Sorter(Comparator<T> comparator){
        this.comparator = comparator;
    }
    public void add(T el, int index){
        int size = elemList.size();
        int i;
        for (i = 0; i < size; i++) {
            if (comparator.compare(elemList.get(i), el) > 0) {
                elemList.add(i, el);
                indexList.add(i, index);
                break;
            }
        }
        if (i >= size){
            elemList.add(el);
            indexList.add(index);
        }

    }
    public Integer pop(){
        if (elemList.isEmpty()){
            return null;
        }
        elemList.remove(0);
        int index = indexList.get(0);
        indexList.remove(0);
        return index;
    }
}
