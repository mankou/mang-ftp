package mang.tools.ftp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;


public class FTPToolTest {
	private static final Logger log = LoggerFactory.getLogger(FTPToolTest.class);
	
	private String userName="maning";
	private String password="1";
	private String host="127.0.0.1";
	private int port=21;
	FTPTool ftptool=new FTP4jTool(host,userName,password);
	
	@Before
	public void testLogin(){
		if(ftptool.login()){
			log.info("login ok");
		}
	
	}
	
	@Test
	public void testList(){
		List<String> fileName=ftptool.listNames();
		log.info(fileName.toString());
	}
	
	/**
	 * 测试使用通配符是否好用 
	 * 如下会输出 ftp的out目录下的xml文件  注只有out目录下的xml文件会显示 如果是out目录的子目录的xml文件不会显示
	 * */
	@Test
	public void testListFilter(){
		FTPClient ftpClient=(FTPClient) ftptool.getClient();
		try {
			FTPFile[] files= ftpClient.list("out/*.xml");
			for(FTPFile file:files){
				System.out.println(file.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testDownloadDir(){  //失败
		//测试能不能下载目录 测试结果是不能 只能下载文件
		File localPath=new File("c:/test");
		FTPClient ftpClient=(FTPClient) ftptool.getClient();
		try {
			ftpClient.download("out", localPath);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	@Test
	public void testDownloadDir2(){ // OK
		// 注意是将远程目录下的文件下载到 本地目录 不包含远程目录名
		ftptool.downloadDir("out", "c:/test");
	}
	
	@Test
	public void testRename(){
		//测试结果1 源路径和目标路径可以是目录
		//测试结果1 源路径和目标路径可以是文件
		//测试结果2 目标路径或者目标文件的父目录必须存在
		FTPClient ftpClient=(FTPClient) ftptool.getClient();
		try {
			ftpClient.rename("out/testInside", "ok/testInside");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testCreateDirectory(){ //OK
		// 可以级联创建目录
		// 如果目录存在 则创建时报错
		boolean result=ftptool.createDirectory("test/test/test");
		System.out.println(result);
	}
	
	
	@Test
	public void testExistDirectory(){ //OK
		boolean isExist=ftptool.existDirectory("test/test");
		System.out.println("目录是否存在:"+isExist);
	}
	
	
	@Test
	public void testTransferListener(){
		//测试 transferListener
		FTPClient client=(FTPClient) ftptool.getClient();
		String remoteFileName="testFTP/info/test.xml";
		String localFile="c:/test/test.xml";
		try {
			client.download(remoteFileName, new File(localFile), new SimpleFTPDataTransferListener());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@After
	public void logOut(){
		ftptool.logout();
	}
}
