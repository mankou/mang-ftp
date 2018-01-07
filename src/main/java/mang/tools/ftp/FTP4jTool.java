package mang.tools.ftp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPFile;

public class FTP4jTool implements FTPTool {
	private static final Logger log = LoggerFactory.getLogger(FTP4jTool.class);

	private FTPClient client = null;
	private String host = "127.0.0.1";
	private int port = 21;
	private String userName;
	private String password;
	private String charset = "GBK";

	public FTP4jTool(String host, String userName, String password) {
		this.host = host;
		this.userName = userName;
		this.password = password;
	}

	public FTP4jTool(String host, int port, String userName, String password) {
		this.port = port;
		this.host = host;
		this.userName = userName;
		this.password = password;
	}

	/**
	 * 登录
	 */
	public boolean login() {
		if (client == null) {
			client = new FTPClient();
		}
		try {
			client.connect(host, port);
			client.login(userName, password);

			if (charset != null) {
				client.setCharset(charset);
			}
			String currentDirectory=client.currentDirectory();
			log.info("current directory:{}",currentDirectory);
			// 打印地址信息
			log.info("ftp登录成功 {}", client);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 登出
	 */
	public void logout() {
		if (client != null) {
			try {
				client.disconnect(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.info("logout successfully");
	}

	@Override
	public List<String> listNames() {
		List<String> resultList = this.listNames("");
		return resultList;
	}

	public List<String> listNames(String fileSpec) {
		List<String> resultList = new ArrayList<String>();
		if (!isLogin()) {
			return null;
		}
		try {
			FTPFile[] list = client.list(fileSpec);
			// 使用通配浏览文件
			// FTPFile[] list = client.list("*.txt");
			// 显示文件或文件夹的修改时间 你不能获得 . 或 .. 的修改日期，否则Permission denied
			for (FTPFile f : list) {
				//2017-12-02发现有不同的FTP服务器上  如果 out/*.xml 则有的取出来是 out/a.xml 有的是a.xml 我希望文件名是a.xml 所以这里处理下
				 File file=new File(f.getName());
				 String fileName=file.getName();
				 System.out.println("========================="+fileName);
				if (!".".equals(fileName)&&!"..".equals(fileName)) {
					resultList.add(fileName);
				}
			}
		} catch (Exception e) {
			log.error("获取{}下的文件失败", fileSpec, e);
		}

		return resultList;
	}
	
	
	/**
	 * 获取某一目录下的文件名
	 * */
	public List<String> listPathFileName(String dirPath,String filter){
		List<String> result=new ArrayList<String>();
		String pwd=null;
		try {
			pwd=client.currentDirectory();
			client.changeDirectory(dirPath);
			
			FTPFile[] files;
			if(filter==null||"".equals(filter)){
				files=client.list();	
			}else{
				files=client.list(filter);				
			}
			
			for(FTPFile file:files){
				String fileName=file.getName();
				result.add(fileName);
			}
		} catch (Exception e) {
			
		}finally {
			if(pwd!=null){
				try{
					client.changeDirectory(pwd);					
				}catch(Exception e){
					
				}
			}
		}
		return result;
	}
	
	@Override
	public Object list(String fileSpec) {
		List<FTPFile> resultList=new ArrayList<FTPFile>();
		try {
			FTPFile[] list = client.list(fileSpec);
			for(FTPFile f:list){
				if (!f.getName().equals(".") && !f.getName().equals("..")) {
					resultList.add(f);
				}
			}
			return list;
		} catch (Exception e) {
			log.error("获取{}下的文件失败", fileSpec, e);
		} 
		return resultList.toArray();
	}
	

	@Override
	public void downloadFile(String remoteFileName, String localFilePath) {

		// TODO 处理windows路径问题
		String fileName = remoteFileName.substring(remoteFileName.lastIndexOf("/") + 1);

		// localFilePath 如果以/结尾 当成目录处理 则远程文件下载到该目录下
		// localFilePath 如果不以/结尾 当成文件处理 下载到本地的文件名以你指定的为准

		File localTargetFile = null;
		try {
			if (localFilePath.endsWith("/")) {
				// 如果目录不存在 则新建
				File localFold = new File(localFilePath);
				if (!localFold.exists()) {
					localFold.mkdirs();
				}
				String localTargetFilePath = localFilePath + "/" + fileName;
				localTargetFile = new File(localTargetFilePath);

			} else {
				localTargetFile = new File(localFilePath);
				File localParentFold = localTargetFile.getParentFile();
				if (!localParentFold.exists()) {
					localParentFold.mkdirs();
				}

			}
			client.download(remoteFileName, localTargetFile);
		} catch (Exception e) {
			log.error("下载文件异常 ", e);
		}

	}

	@Override
	public void downloadDir(String remoteDirPath, String localDirPath) {
		File localDir = new File(localDirPath);
		if (!localDir.exists()) {
			localDir.mkdirs();
		}
		try {

			FTPFile[] files = client.list(remoteDirPath);

			if (files == null) {
				return;
			}

			for (FTPFile file : files) {
				String fileName = file.getName();
				if (".".equals(fileName) || "..".equals(fileName)) {
					continue;
				}

				if (file.getType() == FTPFile.TYPE_DIRECTORY) { // 递归下载子目录
					downloadDir(remoteDirPath + "/" + fileName, localDirPath + "/" + fileName);
				} else if (file.getType() == FTPFile.TYPE_FILE) { // 下载文件
					String remoteFilePath = remoteDirPath + "/" + fileName;
					String localFilePath = localDirPath + "/" + fileName;
					File localFile = new File(localFilePath);
					client.download(remoteFilePath, localFile);
					log.info("download {} to {} ok...", new Object[] { remoteFilePath, localFilePath });
				}
			}

		} catch (Exception e) {
			log.error("下载文件异常", e);
		}
	}
	
	@Override
	public void downloadRecursive(String remoteDirPath, String fileSpec, String localDirpath,FTPDownListener downListener) {
		FTPFile [] fileArray=(FTPFile[]) this.list(remoteDirPath);
		for(FTPFile ftpFile:fileArray){
			String fileName=ftpFile.getName();
			String remoteFileName=remoteDirPath+"/"+fileName;
			
			if(ftpFile.getType()==FTPFile.TYPE_DIRECTORY){
				downloadRecursive(remoteFileName, fileSpec, localDirpath,downListener);
			}else if(ftpFile.getType()==FTPFile.TYPE_FILE){
				if(fileSpec==null||"".equals(fileSpec)||fileName.indexOf(fileSpec)>-1){
					this.downloadFile(remoteFileName, localDirpath);
					if(downListener!=null){
						downListener.afterDownload(remoteFileName,localDirpath);
					}
				}
			}
		}
		
	}

	@Override
	public void rename(String oldPath, String newPath) {
		// 因rename时必须保证目标的父级目录存在
		String parentPath = this.getFileParentPath(newPath);

		if (!existDirectory(parentPath)) {
			createDirectory(parentPath);
		}

		try {
			client.rename(oldPath, newPath);
		} catch (Exception e) {
			log.error("rename error", e);
		}

	}

	public boolean createFileParentDirectory(String filePath) {
		boolean result = true;
		String parentPath = getFileParentPath(filePath);
		try {
			client.createDirectory(parentPath);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	@Override
	public boolean createDirectory(String directoryName) {
		boolean result = true;
		try {
			client.createDirectory(directoryName);
		} catch (Exception e) {
			result = false;
			log.error("createDirectory error", e);
		}
		return result;

	}

	@Override
	public boolean existDirectory(String directoryName) {
		boolean result = true;
		try {
			String pwd = client.currentDirectory();
			client.changeDirectory(directoryName);
			client.changeDirectory(pwd);
		} catch (Exception e) {
			result = false;
			// 因为经常通常该方法判断目录是否存在 所以我不想把错误打印出来
			log.debug("existDirectory error", e);
		}
		return result;
	}

	private String getFileParentPath(String filePath) {
		File file = new File(filePath);
		String parentPath = file.getParent();
		return parentPath;
	}

	/**
	 * 判断路径是不是目录的简易方法
	 */
	private boolean isDirectory(String path) {
		if (path.endsWith("/")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void deleteDirectory(String directoryName,FTPDeleteListener deleteListener) {
		/**
		 * 因client.deleteDirectory 只能删除空目录 所以需要先删除目录下的文件
		 */
		try {
			FTPFile[] ftpFiles = client.list(directoryName);
			for (FTPFile ftpFile : ftpFiles) {
				String fileName = ftpFile.getName();
				String deletePath = directoryName + "/" + fileName;
				try {
					//递归删除子目录
					if (ftpFile.getType() == FTPFile.TYPE_DIRECTORY) {
						deleteDirectory(deletePath,deleteListener);
					} else {
						client.deleteFile(deletePath);
						if(deleteListener!=null){
							deleteListener.afterDeleteFile(deletePath);
						}
					}
				} catch (Exception e) {
					log.error("删除文件出错{}",deletePath,e);
				}
			}
			client.deleteDirectory(directoryName);
		} catch (Exception e) {
			log.error("删除目录出错{}",directoryName,e);
		}

	}
	
	
	@Override
	public void deleteFile(String fileName) {
		try {
			client.deleteFile(fileName);
		} catch (Exception e) {
			log.error("删除文件出错{}",fileName,e);
		}
		
	}

	private boolean isLogin() {
		if (client == null) {
			return false;
		}
		return true;
	}

	public FTPClient getClient() {
		return client;
	}

	public void setClient(FTPClient client) {
		this.client = client;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	@Override
	public void setPassive(boolean passive) {
		this.client.setPassive(passive);
	}

	@Override
	public boolean isPassive() {
		return this.client.isPassive();
	}

}
