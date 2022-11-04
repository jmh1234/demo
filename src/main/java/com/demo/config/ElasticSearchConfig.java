package com.demo.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/**
 * Created with IntelliJ IDEA.
 * ElasticSearchConfig
 *
 * @author Ji MingHao
 * @since 2022-09-13 14:22
 */
@Configuration
public class ElasticSearchConfig {

    @Bean
    public CredentialsProvider credentialsProvider() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "mumzP+j7eXnVB3vtwknN"));
        return credentialsProvider;
    }

    @Bean
    public SSLContext sslContext() {
        final Path path = Paths.get("/Users/jimh/elasticsearch/pwd/certs/http_ca.crt");
        SSLContext sslContext = null;
        try {
            final CertificateFactory factory = CertificateFactory.getInstance("X.509");
            try (InputStream inputStream = Files.newInputStream(path)) {
                final Certificate certificate = factory.generateCertificate(inputStream);
                final KeyStore trustStore = KeyStore.getInstance("pkcs12");
                trustStore.load(null, null);
                trustStore.setCertificateEntry("http_ca", certificate);
                final SSLContextBuilder sslContextBuilder = SSLContexts.custom().loadTrustMaterial(trustStore, null);
                sslContext = sslContextBuilder.build();
            }
        } catch (CertificateException | IOException | KeyStoreException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return sslContext;
    }

    @Bean
    public RestClient restClient(CredentialsProvider credentialsProvider, SSLContext sslContext) {
        return RestClient.builder(
                new HttpHost("127.0.0.1", 9200, "https"))
                .setHttpClientConfigCallback(httpAsyncClientBuilder ->
                        httpAsyncClientBuilder.setSSLContext(sslContext)
                                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                                .setDefaultCredentialsProvider(credentialsProvider)
                ).build();
    }
}
