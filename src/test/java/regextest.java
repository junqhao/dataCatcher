import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexhao on 2017/3/6.
 */
public class regextest {
    public static void main(String[] args) {
        String finalstr = "http://dl.acm.org/citation.cfm?id=1410076&preflayout=flat";
        String test = "<a href=\"author_page.cfm?id=81324487620&amp;coll=DL&amp;dl=ACM&amp;trk=0&amp;cfid=735581093&amp;cftoken=87338330\" title=\"models.Author Profile Page\" target=\"_self\">Yaw Anokwa</a>";
        String regEx = "(?<=\\?id=)\\d*(?=&)";
        String regEx1 = "(?<=id=)\\d*(?=&)";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(test);
        while(matcher.find()){
        System.out.println(matcher.group());
        }
    }
}
