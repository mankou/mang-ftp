package mang.tools.ftp.sn;

import org.springframework.stereotype.Component;

import mang.util.common.DateUtil;

@Component
public class SimpleDateSn implements SnBuild {
	public String getSn(String prefix) {
		String sn = prefix+DateUtil.getCurrentDateString("yyyyMMddHHmmss");
		return sn;
	}
}
