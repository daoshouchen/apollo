package com.ctrip.framework.foundation.internals;

import com.ctrip.framework.foundation.internals.io.BOMInputStream;
import com.ctrip.framework.foundation.internals.provider.DefaultApplicationProvider;
import com.ctrip.framework.foundation.spi.provider.ApplicationProvider;
import com.ctrip.framework.foundation.spi.provider.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

public class ServyouApplicationProvider implements ApplicationProvider {


    private static final Logger logger = LoggerFactory.getLogger(DefaultApplicationProvider.class);
    private Properties m_appProperties = new Properties();

    private String m_appId;

    @Override
    public void initialize() {
        try {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
            if (in == null) {
                in = DefaultApplicationProvider.class.getResourceAsStream("application.properties");
            }

            initialize(in);
        } catch (Throwable ex) {
            logger.error("Initialize DefaultApplicationProvider failed.", ex);
        }
    }

    @Override
    public void initialize(InputStream in) {
        try {
            if (in != null) {
                try {
                    m_appProperties.load(new InputStreamReader(new BOMInputStream(in), Charset.forName("UTF-8")));
                } finally {
                    in.close();
                }
            }

            initAppId();
        } catch (Throwable ex) {
            logger.error("Initialize DefaultApplicationProvider failed.", ex);
        }
    }

    @Override
    public String getAppId() {
        return m_appId;
    }

    @Override
    public boolean isAppIdSet() {
        return !Utils.isBlank(m_appId);
    }

    @Override
    public String getProperty(String name, String defaultValue) {
        if ("spring.application.name".equals(name)) {
            String val = getAppId();
            return val == null ? defaultValue : val;
        } else {
            String val = m_appProperties.getProperty(name, defaultValue);
            return val == null ? defaultValue : val;
        }
    }

    @Override
    public Class<? extends Provider> getType() {
        return ApplicationProvider.class;
    }

    private void initAppId() {

        // 1. Get app.id from System Property
        m_appId = System.getProperty("spring.application.name");
        if (!Utils.isBlank(m_appId)) {
            m_appId = m_appId.trim();
            logger.info("App ID is set to {} by app.id property from System Property", m_appId);
            return;
        }

        //2. Try to get app id from OS environment variable
        m_appId = System.getenv("SPRING.APPLICATION.NAME");
        if (!Utils.isBlank(m_appId)) {
            m_appId = m_appId.trim();
            logger.info("App ID is set to {} by APP_ID property from OS environment variable", m_appId);
            return;
        }

        // 3. Try to get app id from app.properties.
        m_appId = m_appProperties.getProperty("spring.application.name");
        if (!Utils.isBlank(m_appId)) {
            m_appId = m_appId.trim();
            logger.info("App ID is set to {} by app.id property from {}", m_appId, "application.properties");
            return;
        }

        m_appId = null;
        logger.warn("app.id is not available from System Property and {}. It is set to null", "application.properties");
    }

    @Override
    public String toString() {
        return "appId [" + getAppId() + "] properties: " + m_appProperties + " (DefaultApplicationProvider)";
    }
}
