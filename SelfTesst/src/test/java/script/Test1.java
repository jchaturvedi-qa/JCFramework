package script;

import org.testng.annotations.Test;
import org.testng.internal.thread.IThreadFactory;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by jchaturvedi on 25-07-2017.
 */
public class Test1 {
//=============================to reveser a String
    String reverse = "";
    public String reverseString(String s){
        if (s.length()==1)
        {
            return s;
        }
        else {
            reverse = reverse+ s.charAt(s.length()-1)+reverseString(s.substring(0,s.length()-1));
            return reverse;
        }
    }



    @Test(description = "Program to revers a string using recursion ")
    public void printName(){
        System.out.print("in Main =========="+reverseString("Jitendra kuar chaturvedi"));


    }
    //=============================================================


    //===================To read a file , create a map as per occurance of the words and prepare list as per occurance value===
    public Map<String,Integer> getWordCount(String filName){

        FileInputStream  fis = null;
        DataInputStream dis = null ;
        BufferedReader br = null;

        Map<String , Integer > wordMap = new HashMap<String , Integer>();
        try{
            fis = new FileInputStream(filName);
            dis = new DataInputStream(fis);
            br = new BufferedReader(new InputStreamReader(dis));
            String line = null;
            while ((line = br.readLine())!=null)
            {
                StringTokenizer st = new StringTokenizer(line," ");
                 while(st.hasMoreTokens()){
                     String temp = st.nextToken().toLowerCase();
                     if(wordMap.containsKey(temp)){
                         wordMap.put(temp,wordMap.get(temp)+1);
                        }
                        else
                     {
                         wordMap.put(temp,1);
                     }
                 }
            }

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return wordMap;
    }

    public List<Entry<String,Integer>> sortByValue(Map<String,Integer> wordMap){
        Set<Entry<String,Integer>> set= wordMap.entrySet();
        List<Entry<String,Integer>> list = new ArrayList<Entry<String, Integer>>(set);
       /* Collections.sort(list, new Comparator<Entry<String, Integer>>() {
            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });*/

       // Collections.sort(list, (Entry<String ,Integer> e1, Entry<String ,Integer> e2)->e2.getValue().compareTo(e1.getValue()));
        list.sort( (Entry<String ,Integer> e1, Entry<String ,Integer> e2)->e2.getValue().compareTo(e1.getValue()));
        return list;

    }
    @Test(description = "count occurance of the words , based on reverse counting order")
    public void pringDuplicateWordFromFie(){
        String filename = "D:\\test.txt";
        System.out.println("Workd cound in the given file is "+ getWordCount(filename));
         Map<String,Integer> mg = getWordCount(filename);
         List<Entry<String,Integer>> ls = sortByValue(mg);


         /* Iterator itr= ls.iterator();
        while(itr.hasNext()){
            System.out.println("value is "+itr.next());

        }*/



      /*  for(Map.Entry<String,Integer> entry : mg.entrySet()){
            System.out.println(entry.getKey()+ "==>"+ entry.getValue());
        }*/

        for(Map.Entry<String,Integer> entry : ls){
            System.out.println(entry.getKey()+ "==>"+ entry.getValue());
        }

    }

}
