package org.springframework.cloud.netflix.turbine;

import java.util.Map;

import com.netflix.appinfo.AmazonInfo;
import com.netflix.appinfo.DataCenterInfo;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.turbine.discovery.Instance;

/**
 * Fix for the issue with turbine not having context-path when invoking
 * turbine.stream.
 * 
 * @author ffahim
 *
 */
public abstract class ContextRootAwareInstanceDiscovery extends EurekaInstanceDiscovery {
	/**
	 * @see EurekaInstanceDiscovery
	 */
	public ContextRootAwareInstanceDiscovery(final TurbineProperties turbineProperties,
			final EurekaClient eurekaClient) {
		super(turbineProperties, eurekaClient);
	}

	@Override
	Instance marshall(final InstanceInfo instanceInfo) {
		assert instanceInfo != null : "InstanceInfo cannot be null";

		final String hostname = instanceInfo.getHostName();
		final String cluster = getClusterName(instanceInfo);
		final Boolean status = parseInstanceStatus(instanceInfo.getStatus());

		if (hostname == null || cluster == null || status == null) {
			return null;
		}

		final String managementPort = instanceInfo.getMetadata().get("management.port");
		final String port = managementPort == null ? String.valueOf(instanceInfo.getPort()) : managementPort;

		// only change here is to pass the homepage url.
		final Instance instance = getInstance(hostname, port, instanceInfo.getHomePageUrl(), cluster, status);

		final Map<String, String> metadata = instanceInfo.getMetadata();
		boolean securePortEnabled = instanceInfo.isPortEnabled(InstanceInfo.PortType.SECURE);
		final String securePort = String.valueOf(instanceInfo.getSecurePort());

		addMetadata(instance, hostname, port, securePortEnabled, securePort, metadata);

		DataCenterInfo dcInfo = instanceInfo.getDataCenterInfo();
		if (dcInfo != null && dcInfo.getName().equals(DataCenterInfo.Name.Amazon)) {
			AmazonInfo amznInfo = (AmazonInfo) dcInfo;
			instance.getAttributes().putAll(amznInfo.getMetadata());
		}

		return instance;
	}

	/**
	 * Helper that constructs the an {@link Instance} with context-root (i.e.
	 * eureka.instance.home-page-url).
	 */
	protected Instance getInstance(final String hostname, final String port, final String homePageUrl,
			final String cluster, final Boolean status) {
		// context root - assumed starts with / and does not ends with /
		final String contextRoot = homePageUrl == null ? "" : homePageUrl;

		// host with port and context root
		final StringBuilder hostPart = this.isCombineHostPort()
				? new StringBuilder(hostname).append(":").append(port).append(contextRoot)
				: new StringBuilder(hostname).append(contextRoot);

		return new Instance(hostPart.toString(), cluster, status);
	}

	protected Instance getInstance(String hostname, String port, String cluster, Boolean status) {
		String hostPart = this.isCombineHostPort() ? hostname + ":" + port : hostname;
		return new Instance(hostPart, cluster, status);
	}

}
