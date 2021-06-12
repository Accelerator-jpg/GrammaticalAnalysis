package com.company;

import java.util.*;

public class Main {

    public static Scanner sin=new Scanner(System.in);
    public static ArrayList<ArrayList<String>> grammar=new ArrayList<>();
    public static void main(String[] args) {
	// GrammaticalAnalysis
        getInput();
        grammarChange();
    }
    public static void getInput(){
        //从代码读入
        String[] input={
                "A → A a | A b | A c | d | e | f",

//                "E → E + T | T",
//                "T → T * F | F",
//                "F → ( E ) | i",

        };
        for (String s : input) {
            String[] arr = s.split(" ");
            ArrayList<String> toAdd = new ArrayList<>();
            Collections.addAll(toAdd, arr);
            grammar.add(toAdd);
        }
        //从键盘读入
//        while (sin.hasNextLine()){
//            String thisLine=sin.nextLine();
//            if(thisLine.equals("#")) break;
//            String[] arr=thisLine.split(" ");
//            ArrayList<String> toAdd=new ArrayList<>();
//            Collections.addAll(toAdd,arr);
//            grammar.add(toAdd);
//        }
        System.out.println(grammar);
    }
    public static void eliminateLeftRecursion(){
        for (int i = 0; i < grammar.size(); i++) {
            boolean isLeftRecursion=false;
            for (int j = 1; j < grammar.get(i).size(); j++) {
                if(j==1||grammar.get(i).get(j).equals("|")){
                    if(grammar.get(i).get(j+1).equals(grammar.get(i).get(0))){
                        isLeftRecursion=true;
                        break;
                    }
                }
            }
            if(isLeftRecursion){
                String startSign=grammar.get(i).get(0);
                ArrayList<String> toAdd1=new ArrayList<>();
                toAdd1.add(startSign);
                toAdd1.add(grammar.get(i).get(1));
                ArrayList<String> toAdd2=new ArrayList<>();
                toAdd2.add(startSign+"'");
                toAdd2.add(grammar.get(i).get(1));
                for (int j = 1; j < grammar.get(i).size(); j++) {
                    if(j==1||grammar.get(i).get(j).equals("|")){
                        if(grammar.get(i).get(j+1).equals(grammar.get(i).get(0))){
                            int k=j+2;
                            while (k<grammar.get(i).size()&&!grammar.get(i).get(k).equals("|")){
                                toAdd2.add(grammar.get(i).get(k));
                                k++;
                            }
                            toAdd2.add(startSign+"'");
                            toAdd2.add("|");
                        }else {
                            int k=j+1;
                            while (k<grammar.get(i).size()&&!grammar.get(i).get(k).equals("|")){
                                toAdd1.add(grammar.get(i).get(k));
                                k++;
                            }
                            toAdd1.add(startSign+"'");
                            toAdd1.add("|");
                        }
                    }
                }
                toAdd1.remove(toAdd1.size()-1);
                toAdd2.add("ε");
                grammar.remove(i);
                grammar.add(i,toAdd2);
                grammar.add(i,toAdd1);//先加2再加1可以让2在1的后面
                i++;
            }
        }
        System.out.println(grammar);
    }

    public static void leftFactoring() {

    }

    public static void grammarChange(){
        //消除左递归
        eliminateLeftRecursion();
        //提取左公因子
        leftFactoring();
    }
}
