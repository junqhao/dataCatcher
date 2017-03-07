import models.Author;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.util.List;

/**
 * Created by alexhao on 2017/3/6.
 */
public class dbtest {


    public static void main(String[] args)  {
        ApplicationContext ctx = new FileSystemXmlApplicationContext("src/spring-config.xml");
//        JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
//
//        String sql = "insert into author (ID,`NAME`,INFO) values (?,?,?)";
//        String sql1 = "select * from author";
//
//        Author author = new Author();
//        author.setID(4);
//        author.setNAME("2发生的");
//        author.setINFO("sd");
//        Object o = author;
//        RowMapper<Author> mapper = new BeanPropertyRowMapper<>(Author.class);
//  //      jdbcTemplate.update(sql, new Object[]{author.getID(),author.getNAME(),author.getINFO()});
//        List<Author> authors = jdbcTemplate.query(sql1,mapper);
//        for(Author aa:authors){
//            System.out.println(aa.getINFO());
//        }
            Dao dao = (Dao) ctx.getBean("Dao");
            int i = dao.getAuthorById("3");
            System.out.println(i);
            // dao.getAll();
    }

}
