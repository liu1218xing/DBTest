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
 *       dbcp ʵ���࣬�ṩ��dbcp���ӣ�������̳У� 
 *  
 *       ������Ҫ�и��ط�����ʼ�� DS ��ͨ������initDS ��������ɣ� ������ͨ�����ô������Ĺ��캯����ɵ��ã� 
 *       �������������е��ã�Ҳ�����ڱ����м�һ��static{}����ɣ� 
 */  
public final class PoolTest {  
    /** ����Դ��static */  
    private static DataSource DS;  
  
    /** ������Դ���һ������ */  
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
  
    /** Ĭ�ϵĹ��캯�� */  
    public PoolTest() {  
    }  
  
    /** ���캯������ʼ���� DS ��ָ�� ���ݿ� */  
    public PoolTest(String connectURI) {  
        initDS(connectURI);  
    }  
  
    /** ���캯������ʼ���� DS ��ָ�� ���в��� */  
    public PoolTest(String connectURI, String username, String pswd,  
            String driverClass, int initialSize, int maxActive, int maxIdle,  
            int maxWait, int minIdle) {  
        initDS(connectURI, username, pswd, driverClass, initialSize, maxActive,  
                maxIdle, maxWait, minIdle);  
    }  
  
    /** 
     * ��������Դ���������ݿ��⣬��ʹ��Ӳ����Ĭ�ϲ����� 
     *  
     * @param connectURI 
     *            ���ݿ� 
     * @return 
     */  
    public static void initDS(String connectURI) {  
        initDS(connectURI, "study_test", "123456", "com.mysql.jdbc.Driver", 5, 100,  
                30, 10000, 1);  
    }  
  
    /** 
     * ָ�����в�����������Դ 
     *  
     * @param connectURI 
     *            ���ݿ� 
     * @param username 
     *            �û��� 
     * @param pswd 
     *            ���� 
     * @param driverClass 
     *            ���ݿ����������� 
     * @param initialSize 
     *            ��ʼ���ӳ����Ӹ��� 
     * @param maxtotal 
     *            ��������� 
     * @param maxIdle 
     *            ��������� 
     * @param maxWaitMillis 
     *            ������ӵ����ȴ������� 
     * @param minIdle 
     *            ��С������ 
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
        ds.setInitialSize(initialSize); // ��ʼ����������  
        ds.setMaxTotal(maxtotal);  
        ds.setMaxIdle(maxIdle);  
        ds.setMaxWaitMillis(maxWaitMillis);  
        ds.setMinIdle(minIdle);  
        DS = ds;  
    }  
  
    /** �������Դ����״̬ */  
    public static Map<String, Integer> getDataSourceStats() throws SQLException {  
        BasicDataSource bds = (BasicDataSource) DS;  
        Map<String, Integer> map = new HashMap<String, Integer>(2);  
        map.put("active_number", bds.getNumActive());  
        map.put("idle_number", bds.getNumIdle());  
        return map;  
    }  
  
    /** �ر�����Դ */  
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