import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/*
 * ��ȡ������Ϣ������ѵ���ļ����������ļ������Լ��ļ���λ�á���ǰִ�е��������Ϣ��
 * ָ����ע���еĸ��������ࡣ�Լ���Ԫ���������� �������е���������Ϣ��
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
							line = line.replaceFirst("����", "");					
						}
					}
					read.close();

				}
			} catch (Exception e) {
				System.out.println("��ȡ�ļ����ݲ�������");
				e.printStackTrace();
			}
	    }
	    
	    
	    
	    
	    

}
