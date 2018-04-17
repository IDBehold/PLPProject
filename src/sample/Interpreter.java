package sample;

import java.util.ArrayList;
import java.util.List;

public class Interpreter {
    public static List<String> SplitInput(String s)
    {
        List<String> strings = new ArrayList<>();
        int openBracketsCounter = 0;
        int pos = 0;
        int startIndex = 0;
        int endIndex = 0;

        while(pos < s.length())
        {
            if (s.charAt(pos) == '(' && openBracketsCounter == 0)
            {
                startIndex = pos + 1;
                openBracketsCounter++;
            }
            else if (s.charAt(pos) == '(')
            {
                openBracketsCounter++;
            }
            else if (s.charAt(pos) == ')')
            {
                openBracketsCounter--;
            }

            if (openBracketsCounter == 0 && s.charAt(pos) == ')')
            {
                endIndex = pos;
                strings.add(s.substring(startIndex,endIndex));
            }
            pos++;
        }

        return strings;
    }
}
