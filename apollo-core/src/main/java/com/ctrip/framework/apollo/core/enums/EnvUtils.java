package com.ctrip.framework.apollo.core.enums;

import com.ctrip.framework.apollo.core.utils.StringUtils;

public final class EnvUtils {
  
  public static Env transformEnv(String envName) {
    if (StringUtils.isBlank(envName)) {
      return Env.UNKNOWN;
    }
    if("LPT".equals(envName.trim().toUpperCase())){
      return Env.LPT;
    }
    if("FAT".equals(envName.trim().toUpperCase())||"FWS".equals(envName.trim().toUpperCase())){
      return Env.FAT;
    }
    if("UAT".equals(envName.trim().toUpperCase())){
      return Env.UAT;
    }
    if("PRO".equals(envName.trim().toUpperCase()) ||"PROD".equals(envName.trim().toUpperCase()) ) {
      return Env.PRO;
    }
    if("DEV".equals(envName.trim().toUpperCase())){
        return Env.DEV;
    }
    if("LOCAL".equals(envName.trim().toUpperCase())){
      return Env.LOCAL;
    }
    if("TOOLS".equals(envName.trim().toUpperCase())){
      return Env.TOOLS;
    }
    return Env.UNKNOWN;
  }
}
