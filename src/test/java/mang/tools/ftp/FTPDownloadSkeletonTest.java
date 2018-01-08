package mang.tools.ftp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import mang.tools.ftp.download.DownloadFinish;
import mang.tools.ftp.download.FTPDownloadSkeleton;
import mang.tools.ftp.download.after.RemoveFtpFileAfter;
import mang.tools.ftp.download.listener.StaDownloadCountListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( { "/applicationContext-mang-ftp.xml" })
public class FTPDownloadSkeletonTest {
	private static final Logger log = LoggerFactory.getLogger(FTPDownloadSkeletonTest.class);
	
	@Autowired
	@Qualifier("ftpDownloadSkeleton")
	private FTPDownloadSkeleton downloadSkeleton;
	
	@Autowired
	@Qualifier("removeFtpFileAfter")
	private RemoveFtpFileAfter removeFtpFileAfter;
	
	@Autowired
	@Qualifier("localBakeFileByProSn")
	private DownloadFinish localBakeFileByProSn;
	
	
	@Autowired
	@Qualifier("cleanLocalDownloadXml")
	private DownloadFinish cleanLocalDownloadXml;
	
	@Test
	public void testDownloadSkeleton(){
		log.info("hello 开始");
		
		StaDownloadCountListener countListener=new StaDownloadCountListener();
		downloadSkeleton.getDownloadProcessor().addDownloadListener(countListener); //添加一个监听 用于记录下载了多少个文件
		downloadSkeleton.addDownloadAfter(removeFtpFileAfter);
		downloadSkeleton.addDownloadFinsh(localBakeFileByProSn);
		downloadSkeleton.addDownloadFinsh(cleanLocalDownloadXml);
		downloadSkeleton.run();
		
		int count=countListener.getDownloadCount();
		log.info("download {} files",count);
	}
}
