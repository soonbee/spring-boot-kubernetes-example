package com.example.demo;

import com.example.demo.entity.V1Gateway;
import com.example.demo.entity.V1GatewaySpec;
import com.example.demo.entity.V1Port;
import com.example.demo.entity.V1Server;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.openapi.models.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/kube")
public class IstioController {

	private CustomObjectsApi customApi;

    public IstioController() throws IOException {
		// loading cluster config from resource file
		ClassPathResource resource = new ClassPathResource("kubeconfig.yaml");
		InputStream stream = resource.getInputStream();
		InputStreamReader reader = new InputStreamReader(stream);
		KubeConfig config = KubeConfig.loadKubeConfig(reader);			

		// build api-client from config
		ApiClient client = ClientBuilder.kubeconfig(config).build();

		// set the global default api-client to the in-cluster one from above
		Configuration.setDefaultApiClient(client);

		// the CoreV1Api loads default api-client from global configuration.
		this.customApi = new CustomObjectsApi();
	}

	private V1LabelSelector createIstioDefaultLabelSelector() {
		V1LabelSelector selector = new V1LabelSelector();
		selector.putMatchLabelsItem("istio", "ingressgateway");
		return selector;
	}

	@PutMapping("/gateway/{namespace}/{name}")
	public String createGateway(@PathVariable("name") String name, @PathVariable("namespace") String namespace) {
		try {
			createForwardingGateway(name, namespace);
		} catch (ApiException e) {
			System.err.println("Exception when calling CustomObjectsApi#createGateway");
			System.err.println("Status code: " + e.getCode());
			System.err.println("Reason: " + e.getResponseBody());
			System.err.println("Response headers: " + e.getResponseHeaders());
			e.printStackTrace();
		}
		return "create gateway: " + name + "." + namespace;
	}

	@DeleteMapping("/gateway/{namespace}/{name}")
	public String deleteGateway(@PathVariable("name") String name, @PathVariable("namespace") String namespace) {
		try {
			deleteForwardingGateway(name, namespace);
		} catch (ApiException e) {
			System.err.println("Exception when calling CustomObjectsApi#createGateway");
			System.err.println("Status code: " + e.getCode());
			System.err.println("Reason: " + e.getResponseBody());
			System.err.println("Response headers: " + e.getResponseHeaders());
			e.printStackTrace();
		}
		return "delete gateway: " + name + "." + namespace;
	}

	@PostMapping("/gateway/{namespace}/{name}")
	public String replaceGateway(@PathVariable("name") String name, @PathVariable("namespace") String namespace) {
		try {
			replaceForwardingGateway(name, namespace);
		} catch (ApiException e) {
			System.err.println("Exception when calling CustomObjectsApi#createGateway");
			System.err.println("Status code: " + e.getCode());
			System.err.println("Reason: " + e.getResponseBody());
			System.err.println("Response headers: " + e.getResponseHeaders());
			e.printStackTrace();
		}
		return "create gateway: " + name + "." + namespace;
	}

	private Object createForwardingGateway(String name, String namespace) throws ApiException {
		V1GatewaySpec spec = new V1GatewaySpec();
		spec.setSelector(createIstioDefaultLabelSelector());
		V1Server server = new V1Server();
		server.setPort(new V1Port(80, "http", "HTTP"));
		server.setHosts("*");
		spec.setServers(server);
		return createGateway(name, namespace, spec);
	}

	private Object createGateway(String name, String namespace, V1GatewaySpec spec)  throws ApiException {
		V1Gateway body = new V1Gateway();
		String group = "networking.istio.io";
		String version = "v1alpha3";
		String plural = "gateways";
		body.setApiVersion(String.format("%s/%s", group, version));
		body.setMetadata(new V1ObjectMeta().name(name).namespace(namespace));
		body.setSpec(spec);
		return customApi.createNamespacedCustomObject(group, version, namespace, plural, body, null, null, null);
	}

	private Object deleteForwardingGateway(String name, String namespace) throws ApiException {
		String group = "networking.istio.io";
		String version = "v1alpha3";
		String plural = "gateways";
    	return deleteCustomObject(group, version, namespace, plural, name);
	}

	private Object deleteCustomObject(String group, String version, String namespace, String plural, String name) {
		try {
			return customApi.deleteNamespacedCustomObject(
					group, version, namespace, plural, name, null, null, null, null, null);
		} catch (ApiException e) {
			System.err.println("Exception when calling CustomObjectsApi#deleteNamespacedCustomObject");
			System.err.println("Status code: " + e.getCode());
			System.err.println("Reason: " + e.getResponseBody());
			System.err.println("Response headers: " + e.getResponseHeaders());
			e.printStackTrace();
			return null;
		}
	}

	private Object replaceForwardingGateway(String name, String namespace) throws ApiException {
		V1GatewaySpec spec = new V1GatewaySpec();
		spec.setSelector(createIstioDefaultLabelSelector());
		V1Server server = new V1Server();
		server.setPort(new V1Port(80, "http", "HTTP"));
		server.setHosts("*");
		spec.setServers(server);
		return replaceCustomObject(namespace, name, spec);
	}

	private Object replaceCustomObject(String namespace, String name, V1GatewaySpec spec) throws ApiException {
    	V1Gateway body = new V1Gateway();
		String group = "networking.istio.io";
		String version = "v1alpha3";
		String plural = "gateways";
		body.setApiVersion(String.format("%s/%s", group, version));
		body.setMetadata(new V1ObjectMeta().name(name).namespace(namespace));
		body.setSpec(spec);
    	return customApi.replaceNamespacedCustomObject(group, version, namespace, plural, name, body, null, null);
	}
}