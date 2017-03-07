import cn.edu.hfut.dmic.contentextractor.ContentExtractor;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import models.Author;
import models.Paper;
import models.Pub;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexhao on 2017/2/28.
 */
public class Catcher extends BreadthCrawler {

    public String prefix = "http://dl.acm.org/";
    public String suffix = "&preflayout=flat";
    ApplicationContext ctx = new FileSystemXmlApplicationContext("src/spring-config.xml");
    Dao dao = (Dao) ctx.getBean("Dao");

    public Catcher(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
              this.addSeed("http://dl.acm.org/citation.cfm?id=1410076&preflayout=flat");
              this.addSeed("http://dl.acm.org/citation.cfm?id=1060320&preflayout=flat");
            //  this.addSeed("http://dl.acm.org/citation.cfm?id=2537060&preflayout=flat");
    }


    @Override
    public void visit(Page page, CrawlDatums crawlDatums) {

        // references
        String references = page.select("div[class='flatbody']").get(2).select("a[href^='citation']").toString();
        //citations
        String citations = page.select("div[class='flatbody']").get(3).select("a[href^='citation']").toString();
        //提取link中从citation到id部分的正则表达式
        String regEx = "citation.*id=\\d*(?=&)";
        Pattern pattern = Pattern.compile(regEx);
        Matcher ref_matcher = pattern.matcher(references);
        Matcher cit_matcher = pattern.matcher(citations);
        //提取id的正则表达式
        String id_regEx = "(?<=id=)\\d*(?=&)";
        //创建dao层对象

        String thispageid = getThisPageId(page,id_regEx);
        //判断当前page是否已存在
        int id = dao.getPaperById(thispageid);
        if(id ==0)//不存在
        {
            getContent(page,thispageid);
            getLinks(thispageid,crawlDatums,ref_matcher,id_regEx,0);
            getLinks(thispageid,crawlDatums,cit_matcher,id_regEx,1);

       }else{
            //若存在 就不操作
        }

    }

    /**
     * 提取ref和cit的链接
     * @param thispageid
     * @param crawlDatums visit的crawlDatums
     * @param link_matcher ref或cit的matcher
     * @param id_regEx 提取id的regex
     * @param type 类型 ref或cit
     */
    public void getLinks(String thispageid,CrawlDatums crawlDatums,Matcher link_matcher,String id_regEx,int type){


        if(type==0) {
            System.out.println("references are:");
        }else{
            System.out.println("citations are:");
        }

        while(link_matcher.find()){

            String originstr = link_matcher.group();
            String finalstr = prefix+originstr+suffix;

            Pattern id_pattern = Pattern.compile(id_regEx);
            Matcher id_matcher = id_matcher = id_pattern.matcher(finalstr);
            String ref_id = "";

            if(id_matcher.find()){
                ref_id = id_matcher.group();
            }

            if(type==0)
            {
                dao.insertRefPaper(thispageid,ref_id);
            }else{
                dao.insertCitPaper(thispageid,ref_id);
            }
            System.out.println(finalstr + " and id is:" + ref_id);
            crawlDatums.add(finalstr,ref_id);
        }
    }

    /**
     * 获取本页面具体的抽取内容
     * @param page
     * @param thispageid
     */
    public void getContent(Page page,String thispageid){
        //内容提取
        String paper_title = page.select("div[class='large-text']>h1[class='mediumb-text']").text();
        String paper_abstact = page.select("div[class='flatbody']>div[style='display:inline']").text();
        String paper_pub = page.select("td[style='padding-left:10px;']").text();
        String pub_year = page.select("td[align='left'][nowrap='nowrap']").text().substring(0,4);
        Elements author_name = page.select("td[valign='top'][nowrap='nowrap']>a[title='Author Profile Page']");
        Elements author_info = page.select("td[valign='bottom']");
        String author_ids = page.select("table[class='medium-text']").get(2).select("a[title='Author Profile Page']").toString();
        String authorid_regex = "(?<=\\?id=)\\d*(?=&)";
        Pattern pattern = Pattern.compile(authorid_regex);
        Matcher matcher_authorid = pattern.matcher(author_ids);
        List<String> ids = new ArrayList<String>();
            while(matcher_authorid.find()){
                ids.add(matcher_authorid.group());
            }

        int paperpub = dao.getPubByName(paper_pub);
            if(paperpub==0) {
                Pub pub = new Pub();
                pub.setNAME(paper_pub);
                pub.setYEAR(pub_year);
                paperpub = dao.insertPub(pub);
            }
                Paper paper = new Paper();
                paper.setID(thispageid);
                paper.setABSTRACT(paper_abstact);
                paper.setPUB(paperpub);
                paper.setTITLE(paper_title);
                dao.insertPaper(paper);

                for (int i = 0; i < author_name.size(); i++) {
                    Author author = new Author();
                    String authorid = ids.get(i);
                    author.setID(authorid);
                    author.setNAME(author_name.get(i).text());
                    author.setINFO(author_info.get(i).text());
                    dao.insertAuthor(author);
                    try {
                        dao.insertAuthorPaper(authorid,thispageid);
                    }catch (Exception e){
                        System.out.println(e);
                    }

                }

                System.out.println("paper title is:"+paper_title);
                System.out.println("paper abstact is:"+paper_abstact);
                System.out.println("paper pub is:"+paper_pub);
                System.out.println("pub year is:"+pub_year);
                System.out.println("author names and infos and ids are:");
                for (int i=0;i<author_name.size();i++) {
                    System.out.println(author_name.get(i).text() + author_info.get(i).text()+ids.get(i));
                }

    }

    /**
     * 获取本页面的url id
     * @param page
     */
    public String getThisPageId(Page page,String id_regEx){
        String url = page.url();
        Pattern pattern = Pattern.compile(id_regEx);
        Matcher matcher = pattern.matcher(url);
        if(matcher.find()){
            System.out.println("url is:"+url);
            System.out.println("paper id is:"+matcher.group());
        }
        return matcher.group();
    }


    public static void main(String[] args) throws Exception {
        Catcher catcher  = new Catcher("catcher",true);
        //线程数设置
        catcher.setThreads(2);
        //设置断点爬虫
       // catcher.setResumable(true);
        //爬虫深度设置
        catcher.start(1);

    }

}
