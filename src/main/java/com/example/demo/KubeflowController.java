package com.example.demo;

import com.example.demo.entity.*;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.openapi.models.V1OwnerReference;
import io.kubernetes.client.openapi.models.V1RoleRef;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/kubef")
public class KubeflowController {

    private CustomObjectsApi customApi;

    public KubeflowController() throws IOException {
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

    @PutMapping("/profile/{name}")
    public String createProfileApi(@PathVariable("name") String name) {
        try {
            createProfile(name);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#createNamespace");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
        return "create profile: " + name;
    }

    @DeleteMapping("/profile/{name}")
    public String deleteProfileApi(@PathVariable("name") String name) {
        try {
            deleteProfile(name);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#createNamespace");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
        return "delete namespace: " + name;
    }

    private Object createProfile(String name) throws ApiException {
        String group = "kubeflow.org";
        String version = "v1";
        String plural = "profiles";
        V1Profile body = new V1Profile();
        body.setApiVersion(String.format("%s/%s", group, version));
        body.setMetadata(new V1ObjectMeta().name(name).finalizers(buildDefaultFinalizers()));
        body.setSpec(buildDefaultProfileSpec());
        return customApi.createClusterCustomObject(group, version, plural, body, null, null, null);
    }

    private Object deleteProfile(String name) throws ApiException {
        String group = "kubeflow.org";
        String version = "v1";
        String plural = "profiles";
        return customApi.deleteClusterCustomObject(group, version, plural, name, null, null, null, null, null);
    }

    private List<String> buildDefaultFinalizers() {
        ArrayList<String> finalizers = new ArrayList<>();
        finalizers.add("profile-finalizer");
        return finalizers;
    }

    private V1ProfileSpec buildDefaultProfileSpec() {
        V1Owner owner = new V1Owner().kind("User").name("anonymous@kubeflow.org");
        return new V1ProfileSpec().owner(owner);
    }
}