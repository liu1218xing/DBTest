import java.sql.Connection;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.sql.Statement;  
import java.util.HashMap;  
import java.util.Map;  
import javax.sql.DataSource;  
import org.apache.commons.dbcp2.BasicDataSource;  
  
/** 
 * @author  
 * @date  
 * 
 *       dbcp 实用类，提供了dbcp连接，不允许继承； 
 *  
 *       该类需要有个地方来初始化 DS ，通过调用initDS 方法来完成， 可以在通过调用带参数的构造函数完成调用， 
 *       可以在其它类中调用，也可以在本类中加一个static{}来完成； 
 */  
public final class PoolTest {  
    /** 数据源，static */  
    private static DataSource DS;  
  
    /** 从数据源获得一个连接 */  
    public Connection getConn() {  
        Connection con = null;  
        if (DS != null) {  
            try {  
                con = DS.getConnection();  
            } catch (Exception e) {  
                e.printStackTrace(System.err);  
            }  
  
            try {  
                con.setAutoCommit(false);  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
            return con;  
        }  
        return con;  
    }  
  
    /** 默认的构造函数 */  
    public PoolTest() {  
    }  
  
    /** 构造函数，初始化了 DS ，指定 数据库 */  
    public PoolTest(String connectURI) {  
        initDS(connectURI);  
    }  
  
    /** 构造函数，初始化了 DS ，指定 所有参数 */  
    public PoolTest(String connectURI, String username, String pswd,  
            String driverClass, int initialSize, int maxActive, int maxIdle,  
            int maxWait, int minIdle) {  
        initDS(connectURI, username, pswd, driverClass, initialSize, maxActive,  
                maxIdle, maxWait, minIdle);  
    }  
  
    /** 
     * 创建数据源，除了数据库外，都使用硬编码默认参数； 
     *  
     * @param connectURI 
     *            数据库 
     * @return 
     */  
    public static void initDS(String connectURI) {  
        initDS(connectURI, "study_test", "123456", "com.mysql.jdbc.Driver", 5, 100,  
                30, 10000, 1);  
    }  
  
    /** 
     * 指定所有参数连接数据源 
     *  
     * @param connectURI 
     *            数据库 
     * @param username 
     *            用户名 
     * @param pswd 
     *            密码 
     * @param driverClass 
     *            数据库连接驱动名 
     * @param initialSize 
     *            初始连接池连接个数 
     * @param maxtotal 
     *            最大活动连接数 
     * @param maxIdle 
     *            最大连接数 
     * @param maxWaitMillis 
     *            获得连接的最大等待毫秒数 
     * @param minIdle 
     *            最小连接数 
     * @return 
     */  
    public static void initDS(String connectURI, String username, String pswd,  
            String driverClass, int initialSize, int maxtotal, int maxIdle,  
            int maxWaitMillis , int minIdle) {  
        BasicDataSource ds = new BasicDataSource();  
        ds.setDriverClassName(driverClass);  
        ds.setUsername(username);  
        ds.setPassword(pswd);  
        ds.setUrl(connectURI);  
        ds.setInitialSize(initialSize); // 初始的连接数；  
        ds.setMaxTotal(maxtotal);  
        ds.setMaxIdle(maxIdle);  
        ds.setMaxWaitMillis(maxWaitMillis);  
        ds.setMinIdle(minIdle);  
        DS = ds;  
    }  
  
    /** 获得数据源连接状态 */  
    public static Map<String, Integer> getDataSourceStats() throws SQLException {  
        BasicDataSource bds = (BasicDataSource) DS;  
        Map<String, Integer> map = new HashMap<String, Integer>(2);  
        map.put("active_number", bds.getNumActive());  
        map.put("idle_number", bds.getNumIdle());  
        return map;  
    }  
  
    /** 关闭数据源 */  
    protected static void shutdownDataSource() throws SQLException {  
        BasicDataSource bds = (BasicDataSource) DS;  
        bds.close();  
    }  
  
    public static void main(String[] args) {  
    	PoolTest db = new PoolTest("jdbc:mysql://106.2.112.127:3306/cloud_study");  
        Connection conn = null;  
        Statement stmt = null;  
        ResultSet rs = null;  
        try {  
            conn = db.getConn();  
            stmt = conn.createStatement();  
            rs = stmt.executeQuery("select ProductName,inventory from Product ");  
            System.out.println("Results:");  
            int numcols = rs.getMetaData().getColumnCount();  
            while (rs.next()) {  
                for (int i = 1; i <= numcols; i++) {  
                    System.out.print("\t" + rs.getString(i) + "\t");  
                }  
                System.out.println("");  
            }  
            System.out.println(getDataSourceStats());  
        } catch (SQLException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (rs != null)  
                    rs.close();  
                if (stmt != null)  
                    stmt.close();  
                if (conn != null)  
                    conn.close();  
                if (db != null)  
                    shutdownDataSource();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
}  