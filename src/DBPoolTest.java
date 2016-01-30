	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;
	import org.apache.commons.dbcp2.BasicDataSource;  
	public class DBPoolTest {
		public static BasicDataSource ds =null;
	    static final String JDBC_DRIVER ="com.mysql.jdbc.Driver";
	    static String DB_URL ="jdbc:mysql://106.2.112.127:3306/cloud_study";
	    static final String USER="study_test";
	    static final String PASSWORD ="123456";
	    public static void dbPoolInit(){
	    	ds = new BasicDataSource();
	    	ds.setUrl(DB_URL);
	    	ds.setDriverClassName(JDBC_DRIVER);
	    	ds.setUsername(USER);
	    	ds.setPassword(PASSWORD);
	    }
	    public void dbPoolTest(){
	    	Connection conn =null;
	        Statement stmt =null;
	        ResultSet rs =null;
	        try{
	        	conn =ds.getConnection();
	        	System.out.println("conn");
	        	stmt = conn.createStatement();
	        	rs = stmt.executeQuery("select ProductName,inventory from Product" );
	        	while(rs.next()){
	                System.out.print("ProductName:"+rs.getString("ProductName"));
	                System.out.print(" ");
	                System.out.print("inventory:"+rs.getString("inventory"));
	                System.out.println();
	            }
	        }catch(SQLException e){
	        	e.printStackTrace();
	        }finally{
	        	
					try {
						if(conn!=null) conn.close();
						if(stmt!=null)
		                    stmt.close();
		                if(rs!=null)
		                    rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
//						e.printStackTrace();
					}
//                
	        }
	        
	    }
	    public static void DbObdc() throws ClassNotFoundException{
	        Connection conn =null;
	        Statement stmt =null;
	        PreparedStatement pstmt =null;
	        ResultSet rs =null;
	        //1.装载数据库
	        Class.forName(JDBC_DRIVER);
	        try {
	            //2.建立数据库连接
	            conn =DriverManager.getConnection(DB_URL, USER, PASSWORD);
	            DB_URL = DB_URL +"useCursorFetch=true";
	            //3.执行SQL数据库
	            pstmt = conn.prepareStatement("select ProductName,inventory from Product");
	            pstmt.setFetchSize(1);
	            rs = pstmt.executeQuery();
	            //4.获取执行结果
	            while(rs.next()){
	                System.out.print("ProductName:"+rs.getString("ProductName"));
	                System.out.print(" ");
	                System.out.print("inventory:"+rs.getString("inventory"));
	                System.out.println();
	            }
	            
	        } catch (SQLException e) {
	            //异常处理
	            e.printStackTrace();
	        } finally{
	            try {
	                //5.清理环境
	                if(conn!=null)
	                conn.close();
	                if(stmt!=null)
	                    stmt.close();
	                if(rs!=null)
	                    rs.close();
	            } catch (SQLException e) {
	            }
	        }    
	    }
	    public static void main(String[] args) throws ClassNotFoundException {
	    	dbPoolInit();
	     	new DBPoolTest().dbPoolTest();
	    }
	}