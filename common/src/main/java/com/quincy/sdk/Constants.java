package com.quincy.sdk;

import org.bouncycastle.openpgp.operator.KeyFingerPrintCalculator;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;

public class Constants {
	public final static int VCODE_LENGTH = 4;//验证码长度
	public final static String MV_ATTR_NAME_STATUS = "status";
	public final static String MV_ATTR_NAME_MSG = "msg";
	public final static String KEY_LOCALE = "locale";
	public final static String CLIENT_TYPE_P = "p";
	public final static String CLIENT_TYPE_M = "m";
	public final static String CLIENT_TYPE_J = "j";
	public final static String CLIENT_APP = "app_client";
	public final static String ENV_PRO = "pro";
	public final static String ENV_DEV = "dev";
	public final static String CLIENT_TOKEN = "JSESSIONID_DUCATI";
	public final static String GLOBAL_CACHE_KEY_PREFIX = "ducati";
	public final static String ATTR_SESSION = "dddsession";//改了会影响页面模板，要同时改
	public final static String ATTR_VCODE = "vcode";
	public final static String ATTR_DENIED_PERMISSION = "denied_permission";
	public final static String BEAN_NAME_PROPERTIES = "propertiesFactory";
//	public final static KeyFingerPrintCalculator KEY_FINGER_PRINT_CALCULATOR = new JcaKeyFingerprintCalculator();
	public final static KeyFingerPrintCalculator KEY_FINGER_PRINT_CALCULATOR = new BcKeyFingerprintCalculator();
}
