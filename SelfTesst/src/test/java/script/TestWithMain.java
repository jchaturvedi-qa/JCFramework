package script;

import org.testng.annotations.Test;

/**
 * Created by jchaturvedi on 25-07-2017.
 */
public class TestWithMain {

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

    public static void main(String args []){
        TestWithMain t = new TestWithMain();
        System.out.print("in Main =========="+t.reverseString("Jitendra kuar chaturvedi"));


    }
}
