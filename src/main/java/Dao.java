import com.mysql.jdbc.ConnectionFeatureNotAvailableException;
import models.Author;
import models.Paper;
import models.Pub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import sun.org.mozilla.javascript.internal.ast.TryStatement;
import sun.rmi.server.UnicastServerRef;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by alexhao on 2017/3/7.
 * 数据库访问的详细操作实现
 */
public class Dao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * 根据author id查询anthor是否已经存在
     * @param id
     * @return 1 存在 0 不存在
     */
    public int getAuthorById(String id){
        String sql = "select id from author where id=?";
        int newid = Integer.parseInt(id);
        Object[] args = new Object[]{newid};
        int userid = 0;
        userid = (int)jdbcTemplate.queryForObject(sql,args,Integer.class);
        if(userid!=0){
            return 1;
        }else
            return 0;
    }

    /**
     * 根据paper id查询paper是否已经存在
     * @param id
     * @return 1 存在 0 不存在
     */
    public int getPaperById(String id){
        String sql = "select id from paper where id=?";
        int newid = Integer.parseInt(id);
        Object[] args = new Object[]{newid};
        int paperid = 0;
        paperid = (int)jdbcTemplate.queryForObject(sql,args,Integer.class);
        if(paperid!=0){
            return 1;
        }else
            return 0;
    }

    /**
     * 根据pub name查询pub是否已经存在
     * @param name
     * @return 1 存在 0 不存在
     */
    public int getPubByName(String name){
        String sql = "select `name` from pub where `name`=?";
        Object[] args = new Object[]{name};
        String pubname = null;
        pubname = (String)jdbcTemplate.queryForObject(sql,args,String.class);
        if(pubname!=null){
            return 1;
        }else
            return 0;
    }

    /**
     * 插入author 成功返回1 失败返回0
     * @param author
     * @return
     */
    public int insertAuthor(Author author){
        String sql = "insert into author (id,`name`,info) values (?,?,?)";
        Object[] args = new Object[]{author.getID(),author.getNAME(),author.getINFO()};
        try {
                jdbcTemplate.update(sql,args);
                return 1;
        }catch (Exception e){
                return 0;
        }
    }

    /**
     * 插入pub 成功返回自增主键值 失败返回0
     * @param pub
     * @return
     */
    public int insertPub(final Pub pub){
        final String sql = "insert into pub (`name`,`year`) values (?,?)";
        Object[] args = new Object[]{pub.getNAME(),pub.getYEAR()};
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement presta = connection.prepareStatement(sql);
                    presta.setString(1,pub.getNAME());
                    presta.setString(2,pub.getYEAR());
                    return presta;
                }
            });
            return keyHolder.getKey().intValue();
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * 插入paper 成功返回1 失败返回0
     * @param paper
     * @return
     */
    public int insertPaper(Paper paper){
        String sql = "insert into paper (id,title,abstract,pub) values (?,?,?,?)";
        Object[] args = new Object[]{paper.getID(),paper.getTITLE(),paper.getABSTRACT(),paper.getPUB()};
        try {
            jdbcTemplate.update(sql,args);
            return 1;
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * 插入ref_paper关联表
     * @param sourceid
     * @param refid
     * @param citid
     * @return 成功返回1 失败返回0
     */
    public int insertRefPaper(int sourceid,int refid,int citid){
        String sql = "insert into ref_paper (source_paper,ref_paper,cit_paper) values (?,?,?)";
        Object[] args = new Object[]{sourceid,refid,citid};
        try {
            jdbcTemplate.update(sql,args);
            return 1;
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * 插入ref_author_paper关联表
     * @param authorid
     * @param paperid
     * @return
     */
    public int insertAuthorPaper(int authorid,int paperid){
        String sql = "insert into ref_author_paper (author_id,paper_id) values (?,?)";
        Object[] args = new Object[]{authorid,paperid};
        try {
            jdbcTemplate.update(sql,args);
            return 1;
        }catch (Exception e){
            return 0;
        }
    }

}//dao
