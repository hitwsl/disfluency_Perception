import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/*
 * 读取配置信息，包括训练文件，开发集文件，测试集文件的位置。当前执行的任务等信息。
 * 指定标注序列的个数，种类。以及二元特征的种类 （即序列的特征等信息）
 * */
public class config {
	 public static String PackageName ;
	    

	    public config(String fileInPath)
	    {
	      //  PackageName  = name;
	    	try {
				File f = new File(fileInPath);
				if (f.isFile() && f.exists()) {
					InputStreamReader read = new InputStreamReader(
							new FileInputStream(f), "gbk");
					BufferedReader reader = new BufferedReader(read);
					String line;
					while ((line = reader.readLine()) != null) {
						if (line.length() > 1) {						
							line = line.replaceFirst("　　", "");					
						}
					}
					read.close();

				}
			} catch (Exception e) {
				System.out.println("读取文件内容操作出错");
				e.printStackTrace();
			}
	    }
	    
	    
	    
	    
	    

}
