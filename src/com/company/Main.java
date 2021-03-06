package com.company;

import java.util.*;

public class Main {

    public static Scanner sin=new Scanner(System.in);
    public static ArrayList<ArrayList<String>> grammar=new ArrayList<>();
    public static HashMap<String,ArrayList<ArrayList<String>>> grammarMap=new HashMap<>();
    public static HashMap<String,ArrayList<String>> firstCollection=new HashMap<>();
    public static HashMap<String,ArrayList<String>> followCollection=new HashMap<>();
    public static HashMap<Integer,ArrayList<String>> selectCollection=new HashMap<>();
    public static ArrayList<ArrayList<String>> selectGrammar=new ArrayList<>();
    public static HashMap<String,HashMap<String,ArrayList<String>>> analyzeTable =new HashMap<>();

    public static void main(String[] args) {
	// GrammaticalAnalysis
        getInput();
        grammarChange();
        initGrammarMap();
        firstFollowSelect();
    // Analyze
        Analyze();
    }

    public static void Analyze(){
        System.out.println("输入待分析队列:(以#结尾)");
        Stack<String> seq=new Stack<>();
        seq.push("$");
        ArrayList<String> input=new ArrayList<>();
        while (sin.hasNext()){
            String getted=sin.next();
            if(!getted.equals("#")){
                input.add(getted);
            }else break;
        }
        System.out.println();
        for (int i = input.size() - 1; i >= 0; i--) {
            seq.push(input.get(i));
        }
        Stack<String> stack=new Stack<>();
        stack.push("$");
        stack.push(grammar.get(0).get(0));
        System.out.println(stack+"---------"+seq);
        while (!seq.empty()&&!stack.empty()){
            if(stack.peek().equals("$")&&stack.peek().equals(seq.peek())){
                stack.pop();
                seq.pop();
                System.out.println("分析成功！");
                break;
            }
            while (stack.peek().equals(seq.peek())){
                System.out.println(stack+"---------"+seq+"---------"+"匹配");
                stack.pop();
                seq.pop();
            }
            if(!stack.empty()) {
                String k = stack.peek();
                if (analyzeTable.containsKey(k) && analyzeTable.get(k).containsKey(seq.peek())) {
                    System.out.println(stack + "---------" + seq + "---------" + analyzeTable.get(k).get(seq.peek()));
                    stack.pop();
                    for (int i = analyzeTable.get(k).get(seq.peek()).size() - 1; i >= 2; i--) {
                        stack.push(analyzeTable.get(k).get(seq.peek()).get(i));
                        if (stack.peek().equals("ε")) stack.pop();
                    }
                }
                else{
                    System.out.println("语法错误！");
                    break;
                }
            }else{
                System.out.println("语法错误！");
                break;
            }

        }
    }

    public static void getInput(){
        //从代码读入
        String[] input={
                //左递归测试
//                "A → A a | A b | A c | d | e | f",
                //左因子测试
//                "B → a B | a C | a b | e | f | g",
                //样例
                "E → E + T | T",
                "T → T * F | F",
                "F → ( E ) | i",
                //样例
//                "L -> T",
//                "T -> F + F",
//                "F -> b L a | i | ε",
                //样例
//                "S -> D b B",
//                "D -> d | ε",
//                "B -> a | B b a | ε",
                //样例
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
//        sout(1);
    }

    public static void sout(int key){
        switch (key){
            case 1:{
                System.out.println("输入语法如下:");
                for (ArrayList<String> strings : grammar) {
                    for (String s : strings) {
                        System.out.print(s+" ");
                    }
                    System.out.println();
                }
                System.out.println();
                break;
            }
            case 2:{
                System.out.println("消除左递归之后:");
                for (ArrayList<String> strings : grammar) {
                    for (String s : strings) {
                        System.out.print(s+" ");
                    }
                    System.out.println();
                }
                System.out.println();
                break;
            }
            case 3:{
                System.out.println("提取左公因子之后:");
                for (ArrayList<String> strings : grammar) {
                    for (String s : strings) {
                        System.out.print(s+" ");
                    }
                    System.out.println();
                }
                System.out.println();
                break;
            }
            case 4:{
                System.out.println("First集和:");
                for (ArrayList<String> strings : grammar) {
                    System.out.println(strings.get(0)+"=" + firstCollection.get(strings.get(0)));
                }
                System.out.println();
                break;
            }
            case 5:{
                System.out.println("Follow集和:");
                for (ArrayList<String> strings : grammar) {
                    System.out.println(strings.get(0)+"="+followCollection.get(strings.get(0)));
                }
                System.out.println();
                break;
            }
            case 6:{
                System.out.println("Select集和:");
                for (int i = 0; i < selectGrammar.size(); i++) {
                    System.out.println(i+ " : "+selectCollection.get(i));
                }
                System.out.println();
                break;
            }
            case 7:{
                System.out.println("单独语法：");
                for (ArrayList<String> strings : selectGrammar) {
                    System.out.println(strings);
                }
                System.out.println();
                break;
            }
            case 8:{
                System.out.println("分析表：");
                for (ArrayList<String> strings : grammar) {
                    String key1=strings.get(0);
                    System.out.print(key1+":   ");
                    for (String key2 : analyzeTable.get(key1).keySet()) {
                        System.out.print(key2+ "=" + analyzeTable.get(key1).get(key2)+"   ");
                    }
                    System.out.println();
                }
                break;
            }
            default:
                System.out.println("计科1807-董昌通-2018040511");
        }
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
        sout(2);
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
                            boolean haveFollow=false;
                            while (k<grammar.get(i).size()&& !grammar.get(i).get(k).equals("|")){
                                toAdd2.add(grammar.get(i).get(k));
                                haveFollow=true;
                                k++;
                            }
                            if(!haveFollow){
                                toAdd2.add("ε");
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
        sout(3);
    }
    public static void grammarChange(){
        //消除左递归
        eliminateLeftRecursion();
        //提取左公因子
        leftFactoring();
    }

    public static void initGrammarMap(){
        for (int i = 0; i < grammar.size(); i++) {
            ArrayList<ArrayList<String>> toAdd=new ArrayList<>();
            for (int j = 2; j < grammar.get(i).size(); j++) {
                ArrayList<String> signs=new ArrayList<>();
                while (j<grammar.get(i).size()&&!grammar.get(i).get(j).equals("|")){
                    signs.add(grammar.get(i).get(j));
                    j++;
                }
                toAdd.add(signs);
            }
            grammarMap.put(grammar.get(i).get(0),toAdd);
        }
//        System.out.println("将数组保存的数据转换为HashMap保存：\n"+grammarMap+"\n");
    }

    public static HashSet<String> keySet=new HashSet<>();
    public static ArrayList<String> getFirstCollection(String key){
        if(keySet.contains(key)) return firstCollection.get(key);
        else keySet.add(key);
        if (!firstCollection.containsKey(key)) {
            ArrayList<String> toAdd = new ArrayList<>();
            firstCollection.put(key, toAdd);
        }
        ArrayList<ArrayList<String>> thisGrammar = grammarMap.get(key);
        for (ArrayList<String> signs : thisGrammar) {
            String thisSign=signs.get(0);
            if(Character.isUpperCase(signs.get(0).charAt(0))){
                int k=0;
                ArrayList<String> returned=new ArrayList<>();
                while (k<signs.size()&&Character.isUpperCase(signs.get(k).charAt(0))){
                    ArrayList<String> nextSignsFirst=getFirstCollection(signs.get(k));
                    if(!returned.containsAll(nextSignsFirst)){
                        returned.addAll(nextSignsFirst);
                    }
                    if(returned.contains("ε")){
                        returned.remove("ε");
                        k++;
                    }
                    else break;
                }
                if(!Character.isUpperCase(signs.get(k).charAt(0))){
                    returned.remove("ε");
                    returned.add(signs.get(k));
                }else if(k!=signs.size()){
                    returned.remove("ε");
                }
                if(!firstCollection.get(key).containsAll(returned))
                    firstCollection.get(key).addAll(returned);
            }else {
                if (!firstCollection.get(key).contains(thisSign))
                    firstCollection.get(key).add(thisSign);
            }
        }
        return firstCollection.get(key);
    }

    public static boolean containsEpsilon(String key){
        for (ArrayList<String> strings : grammarMap.get(key)) {
            if(strings.contains("ε")) return true;
        }
        return false;
    }
    public static void getFollowCollection(){
        if(followCollection.size()==0) {
            for (int i = 0; i < grammar.size(); i++) {
                ArrayList<String> init = new ArrayList<>();
                if (i == 0) init.add("$");
                followCollection.put(grammar.get(i).get(0), init);
            }
        }
        for (String key : grammarMap.keySet()) {
            for (ArrayList<String> signs : grammarMap.get(key)) {
                for (int i = 0; i < signs.size(); i++) {
                    if(Character.isUpperCase(signs.get(i).charAt(0))){
                        if(i==signs.size()-1){
                            followCollection.get(signs.get(i)).addAll(followCollection.get(key));
                        }else{
                            int k=i+1;
                            boolean isEpsilon=true;
                            while (k<signs.size()){
                                if(!Character.isUpperCase(signs.get(k).charAt(0))){
                                    followCollection.get(signs.get(i)).add(signs.get(k));
                                    isEpsilon=false;
                                    break;
                                }else if (!containsEpsilon(signs.get(k))){
                                    followCollection.get(signs.get(i)).addAll(firstCollection.get(signs.get(k)));
                                    followCollection.get(signs.get(i)).remove("ε");
                                    isEpsilon=false;
                                    break;
                                }else {
                                    followCollection.get(signs.get(i)).addAll(firstCollection.get(signs.get(k)));
                                    followCollection.get(signs.get(i)).remove("ε");
                                }
                                k++;
                            }
                            if (isEpsilon){
                                followCollection.get(signs.get(i)).addAll(followCollection.get(key));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void getSelectCollection(){
        for (ArrayList<String> gram : grammar) {
            for (ArrayList<String> strings : grammarMap.get(gram.get(0))) {
                ArrayList<String> toAdd=new ArrayList<>();
                toAdd.add(gram.get(0));
                toAdd.add("->");
                toAdd.addAll(strings);
                selectGrammar.add(toAdd);
            }
        }
        sout(7);
        for (int i = 0; i < selectGrammar.size(); i++) {
            ArrayList<String> toAdd=new ArrayList<>();
            int k=2;
            boolean isEpsilon=true;
            while (k<selectGrammar.get(i).size()){
                String head=selectGrammar.get(i).get(k);
                k++;
                if(Character.isUpperCase(head.charAt(0))){
                    toAdd.addAll(firstCollection.get(head));
                    if(toAdd.contains("ε")){
                        toAdd.remove("ε");
                    }else {
                        isEpsilon=false;
                        break;
                    }
                }else if(head.equals("ε")){
                    continue;
                }
                else{
                    toAdd.add(head);
                    isEpsilon=false;
                    break;
                }
            }
            if(isEpsilon) toAdd.addAll(followCollection.get(selectGrammar.get(i).get(0)));
            selectCollection.put(i,toAdd);
        }
    }

    public static void getAnalyzeTable() {
        for (String key : grammarMap.keySet()) {
            HashMap<String,ArrayList<String>> toAdd=new HashMap<>();
            analyzeTable.put(key,toAdd);
        }
        for (int i = 0; i < selectGrammar.size(); i++) {
            String key1=selectGrammar.get(i).get(0);
            for (String s : selectCollection.get(i)) {
                if(analyzeTable.containsKey(key1)){
                    analyzeTable.get(key1).put(s,selectGrammar.get(i));
                }else {
                    HashMap<String,ArrayList<String>> toAdd=new HashMap<>();
                    toAdd.put(s,selectGrammar.get(i));
                    analyzeTable.put(key1,toAdd);
                }
            }
        }
    }

    public static void firstFollowSelect(){
        //计算first集和
        int k=5;
        while (k-->0) {
            keySet.clear();
            for (String key : grammarMap.keySet())
                getFirstCollection(key);
        }
        sout(4);
        //计算follow集和
        k=5;
        while (k-->0) {
            getFollowCollection();
        }
        for (String key : followCollection.keySet()) {
            followCollection.put(key,new ArrayList<>(new HashSet<>(followCollection.get(key))));
        }
        sout(5);
        //计算select集和
        getSelectCollection();
        sout(6);
        //得到分析表
        getAnalyzeTable();
        sout(8);
    }

}
