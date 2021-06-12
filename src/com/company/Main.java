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
                //左递归测试
//                "A → A a | A b | A c | d | e | f",
                //左因子测试
//                "B → a B | a C | a b | e | f | g",
                //样例
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
            //判断是否有左递归
            boolean isLeftRecursion=false;
            for (int j = 1; j < grammar.get(i).size(); j++) {
                if(j==1||grammar.get(i).get(j).equals("|")){
                    if(grammar.get(i).get(j+1).equals(grammar.get(i).get(0))){
                        isLeftRecursion=true;
                        break;
                    }
                }
            }
            //消除左递归操作
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
        HashMap<String,Integer> commonFactor=new HashMap<>();
        for (int i = 0; i < grammar.size(); i++) {
            //寻找公共左因子
            commonFactor.clear();
            int maxTime=1;
            for (int j = 1; j < grammar.get(i).size(); j++) {
                if(j==1||grammar.get(i).get(j).equals("|")){
                    String beginSign=grammar.get(i).get(j+1);
                    if(!Character.isUpperCase(beginSign.charAt(0))){
                        if(commonFactor.containsKey(beginSign)) {
                            int num=commonFactor.get(beginSign)+1;
                            maxTime=num>maxTime?num:maxTime;
                            commonFactor.put(beginSign,num);
                        }
                        else commonFactor.put(beginSign,1);

                    }
                }
            }
            //提取左公因子
            if(maxTime>1) {
                String factor = "";
                for (String s : commonFactor.keySet()) {
                    if (commonFactor.get(s).equals(maxTime)) {
                        factor = s;
                        break;
                    }
                }
                String startSign = grammar.get(i).get(0);
                ArrayList<String> toAdd1 = new ArrayList<>();
                toAdd1.add(startSign);
                toAdd1.add(grammar.get(i).get(1));
                toAdd1.add(factor);
                toAdd1.add(startSign+"'");
                toAdd1.add("|");
                ArrayList<String> toAdd2 = new ArrayList<>();
                toAdd2.add(startSign + "'");
                toAdd2.add(grammar.get(i).get(1));
                for (int j = 0; j < grammar.get(i).size(); j++) {
                    if (j == 1 || grammar.get(i).get(j).equals("|")) {
                        if (!grammar.get(i).get(j + 1).equals(factor)) {
                            int k = j + 1;
                            while (k<grammar.get(i).size()&&!grammar.get(i).get(k).equals("|")){
                                toAdd1.add(grammar.get(i).get(k));
                                k++;
                            }
                            toAdd1.add("|");
                        }else{
                            int k=j+2;
                            while (k<grammar.get(i).size()&& !grammar.get(i).get(k).equals("|")){
                                toAdd2.add(grammar.get(i).get(k));
                                k++;
                            }
                            toAdd2.add("|");
                        }
                    }
                }
                toAdd1.remove(toAdd1.size()-1);
                toAdd2.remove(toAdd2.size()-1);
                grammar.remove(i);
                grammar.add(i,toAdd2);
                grammar.add(i,toAdd1);
                i++;
            }
        }
        System.out.println(grammar);
    }

    public static void grammarChange(){
        //消除左递归
//        eliminateLeftRecursion();
        //提取左公因子
        leftFactoring();
    }
}
