package mang.tools.ftp;
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  

import org.apache.commons.net.ftp.FTPClient;  
import org.apache.commons.net.ftp.FTPClientConfig;  
import org.apache.commons.net.ftp.FTPFile;  
import org.apache.commons.net.ftp.FTPReply;  
import org.apache.log4j.Logger;
  
public class FTPSend1 {  
	private static Logger logger= Logger.getLogger(FTPSend1.class);
	private Boolean isNeedChangeDirectory=true;
    /** 
     * 获得连接-FTP方式 
     * @param hostName FTP服务器地址 
     * @param port FTP服务器端口 
     * @param userName FTP登录用户名 
     * @param passWord FTP登录密码 
     * @return FTPClient 
     */  
    public FTPClient getConnectionFTP(String hostName, int port, String userName, String passWord) {  
        //创建FTPClient对象  
        FTPClient ftp = new FTPClient();  
        try {  
            //连接FTP服务器  
            ftp.connect(hostName, port);  
            //下面三行代码必须要，而且不能改变编码格式，否则不能正确下载中文文件  
            ftp.setControlEncoding("GBK");  
            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);  
            conf.setServerLanguageCode("zh");  
            //登录ftp  
            ftp.login(userName, passWord);  
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {  
                ftp.disconnect();  
                System.out.println("连接服务器失败");  
                ftp=null;
            }  
            System.out.println("登陆服务器成功");  
        } catch (IOException e) {  
            e.printStackTrace();  
            ftp=null;
        }  finally{
        	return ftp;          	
        }
    }  
    
    /** 
     * 上传文件-FTP方式 
     * @param ftp FTPClient对象 
     * @param path FTP服务器上传地址 
     * @param fileName 本地文件路径 
     * @param inputStream 输入流 
     * @param message 调用该方法时传入一些信息 用于写日志
     * @return boolean 
     */  
    public boolean uploadFile(FTPClient ftp, String path, String fileName, InputStream inputStream,String message) {  
        boolean success = false;  
        try {  
        	//这四句代码 2014-12-9测试时 我在ftp传文件的同时 人家海关删除数据时我这里就卡列了。后来把这句删除代码也好用 所以就注释了
//            ftp.changeWorkingDirectory(path);//转移到指定FTP服务器目录  
//            FTPFile[] fs = ftp.listFiles();//得到目录的相应文件列表  
//            fileName = FTPSend1.changeName(fileName, fs);  
//            fileName = new String(fileName.getBytes("GBK"),"ISO-8859-1");  
            
            logger.info("正在上传:"+message+fileName);
            path = new String(path.getBytes("GBK"), "ISO-8859-1");  
            //转到指定上传目录  (如果不加这句会把文件直接存储到ftp根目录 与你连接的地址有关)
          //第一次切换目录后 下一次就不再切换了 add by manig at 2014-12-9 16:07:33
            if(isNeedChangeDirectory){
            	ftp.changeWorkingDirectory(path);  
            	isNeedChangeDirectory=false; 
            }
            //将上传文件存储到指定目录  
//            ftp.setFileType(FTP.BINARY_FILE_TYPE);  
            //如果缺省该句 传输txt正常 但图片和其他格式的文件传输出现乱码  
            ftp.storeFile(fileName, inputStream);  
            //关闭输入流  
            inputStream.close();  
            //退出ftp  
//            ftp.logout();  
            //表示上传成功  
            success = true;  
            logger.info("上传成功:"+message+fileName);
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return success;  
    }  
      
    
    
    
    
    
    
    
    /** 
     * 上传文件-FTP方式 
     * 2014-12-9 17:42:09 我自己封装的方法
     * @param ftp FTPClient对象 
     * @param path FTP服务器上传地址 
     * @param file 本地文件
     * @param message 调用该方法时传入一些信息 用于写日志
     * @return boolean 
     */  
    public boolean uploadFile(FTPClient ftp, String path, File file,String message) {  
        boolean success = false;  
        try {  
        	//这四句代码 2014-12-9测试时 我在ftp传文件的同时 人家海关删除数据时我这里就卡列了。后来把这句删除代码也好用 所以就注释了
//            ftp.changeWorkingDirectory(path);//转移到指定FTP服务器目录  
//            FTPFile[] fs = ftp.listFiles();//得到目录的相应文件列表  
//            fileName = FTPSend1.changeName(fileName, fs);  
//            fileName = new String(fileName.getBytes("GBK"),"ISO-8859-1");  
            String fileName = file.getName();
            logger.info("正在上传:"+message+fileName);
            
            InputStream inputStream = new FileInputStream(file);  
            path = new String(path.getBytes("GBK"), "ISO-8859-1");  
            //转到指定上传目录  (如果不加这句会把文件直接存储到ftp根目录 与你连接的地址有关)
          //第一次切换目录后 下一次就不再切换了 add by manig at 2014-12-9 16:07:33
            if(isNeedChangeDirectory){
//            	ftp.changeWorkingDirectory(path);  
            	if (!ftp.changeWorkingDirectory(path)) {// 如果不能进入dir下，说明此目录不存在！  则新建该目录 
            		  ftp.makeDirectory(path);
            		  ftp.changeWorkingDirectory(path); //再次切换到该目录 如果目录不存在 则上一步没有切换到该目录上来 所以这里要再切换一次
                }  
            	isNeedChangeDirectory=false; 
            }
            //将上传文件存储到指定目录  
//            ftp.setFileType(FTP.BINARY_FILE_TYPE);  
            //如果缺省该句 传输txt正常 但图片和其他格式的文件传输出现乱码  
            ftp.storeFile(fileName, inputStream);  
            //关闭输入流  
            inputStream.close();  
            //退出ftp  
//            ftp.logout();  
            //表示上传成功  
            success = true;  
            logger.info("上传成功:"+message+fileName);
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return success;  
    }  
    /** 
     * 关闭连接-FTP方式 
     * @param ftp FTPClient对象 
     * @return boolean 
     */  
    public boolean closeFTP(FTPClient ftp) {  
        if (ftp.isConnected()) {  
            try {  
                ftp.disconnect();  
                isNeedChangeDirectory=true;
//                System.out.println("ftp已经关闭");  
                logger.info("FTP已经关闭");
                return true;  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return false;  
    }  
      
   
  
    /** 
     * 删除文件-FTP方式 
     * @param ftp FTPClient对象 
     * @param path FTP服务器上传地址 
     * @param fileName FTP服务器上要删除的文件名 
     * @return  boolean
     */  
    public boolean deleteFile(FTPClient ftp, String path, String fileName) {  
        boolean success = false;  
        try {  
            ftp.changeWorkingDirectory(path);//转移到指定FTP服务器目录  
            fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");  
            path = new String(path.getBytes("GBK"), "ISO-8859-1");  
            ftp.deleteFile(fileName);  
            ftp.logout();  
            success = true;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return success;  
    }  
  
    /** 
     * 上传文件-FTP方式 
     * @param ftp FTPClient对象 
     * @param path FTP服务器上传地址 
     * @param fileName 本地文件路径 
     * @param localPath 本里存储路径 
     * @return boolean 
     */  
    public boolean downFile(FTPClient ftp, String path, String fileName, String localPath) {  
        boolean success = false;  
        try {  
            ftp.changeWorkingDirectory(path);//转移到FTP服务器目录  
            FTPFile[] fs = ftp.listFiles(); //得到目录的相应文件列表  
            for (FTPFile ff : fs) {  
                if (ff.getName().equals(fileName)) {  
                    File localFile = new File(localPath + "\\" + ff.getName());  
                    OutputStream outputStream = new FileOutputStream(localFile);  
                    //将文件保存到输出流outputStream中  
                    ftp.retrieveFile(new String(ff.getName().getBytes("GBK"), "ISO-8859-1"), outputStream);  
                    outputStream.flush();  
                    outputStream.close();  
                    System.out.println("下载成功");  
                }  
            }  
            ftp.logout();  
            success = true;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return success;  
    }  
      
    /** 
     * 判断是否有重名文件 
     * @param fileName  文件名
     * @param fs fs
     * @return  boolean
     */  
    public static boolean isFileExist(String fileName, FTPFile[] fs) {  
        for (int i = 0; i < fs.length; i++) {  
            FTPFile ff = fs[i];  
            if (ff.getName().equals(fileName)) {  
                return true; //如果存在返回 正确信号  
            }  
        }  
        return false; //如果不存在返回错误信号  
    }  
  
    /** 
     * 根据重名判断的结果 生成新的文件的名称 
     * @param fileName  文件名
     * @param fs  ftpfile
     * @return 新的文件名称
     */  
    public static String changeName(String fileName, FTPFile[] fs) {  
        int n = 0;  
//      fileName = fileName.append(fileName);  
        while (isFileExist(fileName.toString(), fs)) {  
            n++;  
            String a = "[" + n + "]";  
            int b = fileName.lastIndexOf(".");//最后一出现小数点的位置  
            int c = fileName.lastIndexOf("[");//最后一次"["出现的位置  
            if (c < 0) {  
                c = b;  
            }  
            StringBuffer name = new StringBuffer(fileName.substring(0, c));//文件的名字  
            StringBuffer suffix = new StringBuffer(fileName.substring(b + 1));//后缀的名称  
            fileName = name.append(a) + "." + suffix;  
        }  
        return fileName.toString();  
    }  
  
    /** 
     *  
     * @param args 测试参数
     *  
     * @throws FileNotFoundException 
     *  
     * 测试程序 
     *  
     */  
    public static void main(String[] args) throws FileNotFoundException {  
  
//        String path = "c:/users/mang/ApplicationData/ftp/";  
//        String path = "c:/users/mang/ApplicationData/ftp/";  
        String path = "";
//        File f1 = new File("c:\\upload\\CENIN-InBulkGate_001_20141204201145.xml");  
        File f1 = new File("c:\\sanqi_imp_yh3.log");  
        String filename = f1.getName();  
        System.out.println(filename);  
        //InputStream input = new FileInputStream(f1);  
        //ftpTest a = new ftpTest();  
        //a.uploadFile("172.25.5.193", 21, "shiyanming", "123", path, filename, input);  
        /* 
         * String path ="D:\\ftpindex\\"; File f2 = new 
         * File("D:\\ftpindex\\old.txt"); String filename2= f2.getName(); 
         * System.out.println(filename2); ftpTest a = new 
         * ftpTest(); a.downFile("172.25.5.193", 21, "shi", "123", path, 
         * filename2, "C:\\"); 
         */  
        FTPSend1 a = new FTPSend1();  
        InputStream input = new FileInputStream(f1);  
//      a.uploadFile("218.108.250.205", 21, "hwyhftp", "!#hwyhftp", path, filename, input);  
        //a.deleteFile("218.108.250.205", 21, "hwyhftp", "!#hwyhftp", path, filename);  
//      a.downFile("218.108.250.205", 21, "hwyhftp", "!#hwyhftp", path, "欢[2].txt");  
//        FTPClient ftp = a.getConnectionFTP("127.0.0.1", 21, "maning", "1");  
        FTPClient ftp = a.getConnectionFTP("192.168.1.200", 21, "maning", "1");  
//      a.deleteFile(ftp, path, "a[2].txt");  
        a.uploadFile(ftp, path, filename, input,"");  
        a.closeFTP(ftp);  
    }  
}  