package com.ctrip.framework.foundation.internals;

import com.ctrip.framework.foundation.internals.provider.DefaultNetworkProvider;
import com.ctrip.framework.foundation.internals.provider.DefaultServerProvider;
import com.ctrip.framework.foundation.spi.ProviderManager;
import com.ctrip.framework.foundation.spi.provider.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class ServyouProviderManager implements ProviderManager {
    private static final Logger logger = LoggerFactory.getLogger(ServyouProviderManager.class);
    private Map<Class<? extends Provider>, Provider> providers = new LinkedHashMap<Class<? extends Provider>, Provider>();

    public ServyouProviderManager() {
        Provider applicationProvider = new ServyouApplicationProvider();
        applicationProvider.initialize();
        providers.put(applicationProvider.getType(), applicationProvider);

        // Load network parameters
        Provider networkProvider = new DefaultNetworkProvider();
        networkProvider.initialize();
        providers.put(networkProvider.getType(), networkProvider);

        // Load environment (fat, fws, uat, prod ...) and dc, from /opt/settings/server.properties, JVM property and/or OS
        // environment variables.
        Provider serverProvider = new DefaultServerProvider();
        serverProvider.initialize();
        providers.put(serverProvider.getType(), serverProvider);
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T extends Provider> T provider(Class<T> clazz) {
        Provider provider = providers.get(clazz);

        if (provider != null) {
            return (T) provider;
        } else {
            logger.error("No provider [{}] found in ServyouProviderManager, please make sure it is registered in ServyouProviderManager ",
                    clazz.getName());
            return (T) NullProviderManager.provider;
        }
    }

    @Override
    public String getProperty(String name, String defaultValue) {
        for (Provider provider : providers.values()) {
            String value = provider.getProperty(name, null);

            if (value != null) {
                return value;
            }
        }

        return defaultValue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(512);
        if (null != providers) {
            for (Map.Entry<Class<? extends Provider>, Provider> entry : providers.entrySet()) {
                sb.append(entry.getValue()).append("\n");
            }
        }
        sb.append("(ServyouProviderManager)").append("\n");
        return sb.toString();
    }

}
