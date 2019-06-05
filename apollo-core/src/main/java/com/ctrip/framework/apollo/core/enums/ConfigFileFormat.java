package com.ctrip.framework.apollo.core.enums;

import com.ctrip.framework.apollo.core.utils.StringUtils;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public enum ConfigFileFormat {
  Properties("properties"), XML("xml"), JSON("json"), YML("yml"), YAML("yaml");

  private String value;

  ConfigFileFormat(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static ConfigFileFormat fromString(String value) {
    if (StringUtils.isEmpty(value)) {
      throw new IllegalArgumentException("value can not be empty");
    }
    if("properties".equals(value.toLowerCase())){
      return Properties;
    } 
	if("xml".equals(value.toLowerCase())){
      return XML;
    } 
	if("json".equals(value.toLowerCase())){
      return JSON;
    } 
	if("yml".equals(value.toLowerCase())){
      return YML;
    } 
	if("yaml".equals(value.toLowerCase())){
      return YAML;
    } 

    throw new IllegalArgumentException(value + " can not map enum");
  }

  public static boolean isValidFormat(String value) {
    try {
      fromString(value);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public static boolean isPropertiesCompatible(ConfigFileFormat format) {
    return format == YAML || format == YML;
  }
}
