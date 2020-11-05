package com.example.demo;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1Status;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/kube")
public class KubernetesController {

	private CoreV1Api api = new CoreV1Api();

    public KubernetesController() throws IOException {
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
		this.api = new CoreV1Api();
	}

	@GetMapping("/pods/{namespace}")
	public String getNamespacedPods(@PathVariable("namespace") String namespace) {
		try {
			V1PodList result = this.api.listNamespacedPod(namespace, null, null, null, null, null, null, null, null, null);
			System.out.println(result);
		  } catch (ApiException e) {
			System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
			System.err.println("Status code: " + e.getCode());
			System.err.println("Reason: " + e.getResponseBody());
			System.err.println("Response headers: " + e.getResponseHeaders());
			e.printStackTrace();
		  }
		return "get pods of namespace " + namespace;
	}

	@GetMapping("/namespace")
	public String getNamespace() {
		try {
			V1NamespaceList result = this.api.listNamespace(null, null, null, null, null, null, null, null, null);
			System.out.println(result);
		  } catch (ApiException e) {
			System.err.println("Exception when calling CoreV1Api#listNamespace");
			System.err.println("Status code: " + e.getCode());
			System.err.println("Reason: " + e.getResponseBody());
			System.err.println("Response headers: " + e.getResponseHeaders());
			e.printStackTrace();
		  }
		return "get namespaces";
	}

	@PutMapping("/namespace/{name}")
	public String createNamespace(@PathVariable("name") String name) {
		V1Namespace body = new V1Namespace();
		V1ObjectMeta meta = new V1ObjectMeta();
		meta.setName(name);
		body.setMetadata(meta);

		try {
			V1Namespace result = this.api.createNamespace(body, null, null, null);
			System.out.println(result);
		} catch (ApiException e) {
			System.err.println("Exception when calling CoreV1Api#createNamespace");
			System.err.println("Status code: " + e.getCode());
			System.err.println("Reason: " + e.getResponseBody());
			System.err.println("Response headers: " + e.getResponseHeaders());
			e.printStackTrace();
		}
		return "create namespace: " + name;
	}

	@DeleteMapping("/namespace/{name}")
	public String deleteNamespace(@PathVariable("name") String name) {
		try {
			V1Status result = this.api.deleteNamespace(name, null, null, null, null, null, null);
			System.out.println(result);
		} catch (ApiException e) {
			System.err.println("Exception when calling CoreV1Api#createNamespace");
			System.err.println("Status code: " + e.getCode());
			System.err.println("Reason: " + e.getResponseBody());
			System.err.println("Response headers: " + e.getResponseHeaders());
			e.printStackTrace();
		}
		return "delete namespace: " + name;
	}
}