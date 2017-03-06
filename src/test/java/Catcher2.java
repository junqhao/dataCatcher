import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexhao on 2017/2/28.
 */
public class Catcher2 extends BreadthCrawler {

    public String prefix = "http://dl.acm.org/";
    public String suffix = "&preflayout=flat";


    public Catcher2(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
              this.addSeed("http://dl.acm.org/citation.cfm?id=1410076&preflayout=flat");
            //  this.addSeed("http://dl.acm.org/citation.cfm?id=2537060&preflayout=flat");
    }


    @Override
    public void visit(Page page, CrawlDatums crawlDatums) {

        //references
        String references = page.select("div[class='flatbody']").get(2).select("a[href^='citation']").toString();
        System.out.println(references);
        //citations
        String citations = page.select("div[class='flatbody']").get(3).select("a[href^='citation']").toString();
        //提取link中从citation到id部分的正则表达式
        String regEx = "citation.*id=\\d*(?=&)";
        Pattern pattern = Pattern.compile(regEx);
        Matcher ref_matcher = pattern.matcher(references);
        Matcher cit_matcher = pattern.matcher(citations);
        //提取id的正则表达式
        String id_regEx = "(?<=id=)\\d*(?=&)";
        Pattern id_pattern = Pattern.compile(id_regEx);
        Matcher id_matcher = null;

        System.out.println("references are : ");
        while(ref_matcher.find()){

                  String originstr = ref_matcher.group();
               // String newstr = originstr.replace("amp;","");
                String finalstr = prefix+originstr+suffix;

                id_matcher = id_pattern.matcher(finalstr);
                String ref_id = "";
                if(id_matcher.find()){
                    ref_id = id_matcher.group();
                }
                     System.out.println(finalstr+" and id is:"+ref_id);
                     crawlDatums.add(finalstr);
            }

        System.out.println("citations are : ");
        while(cit_matcher.find()){

            String originstr = cit_matcher.group();
            // String newstr = originstr.replace("amp;","");
            String finalstr = prefix+originstr+suffix;

            id_matcher = id_pattern.matcher(finalstr);
            String ref_id = "";
            if(id_matcher.find()){
                ref_id = id_matcher.group();
            }
            System.out.println(finalstr+" and id is:"+ref_id);
            crawlDatums.add(finalstr);
        }

    }

    public static void main(String[] args) throws Exception {
        Catcher2 catcher  = new Catcher2("catcher",true);
        catcher.setThreads(2);
        catcher.start(1);

    }
}
