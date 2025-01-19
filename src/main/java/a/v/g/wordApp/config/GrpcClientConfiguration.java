package a.v.g.wordApp.config;

import io.grpc.CallCredentials;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import yandex.cloud.api.ai.tts.v3.SynthesizerGrpc;
import yandex.cloud.api.ai.tts.v3.SynthesizerGrpc.*;

import java.util.concurrent.Executor;

@Configuration
public class GrpcClientConfiguration {

    @Value("${tts.api.cloud.yandex.token}")
    private String ttsJwtToken;

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress("tts.api.cloud.yandex.net", 443)
                .useTransportSecurity() // Используем TLS

                .build();
    }


    private static final Metadata.Key<String> AUTHORIZATION_HEADER =
            Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);

    private String getJwtToken() {
        // Реализуйте метод для получения вашего токена
        return "Bearer " + ttsJwtToken;
    }

    @Bean
    public SynthesizerStub synthesizerStub(ManagedChannel managedChannel) {
        String jwtToken = getJwtToken();

        // Реализуем CallCredentials
        CallCredentials callCredentials = new CallCredentials() {
            @Override
            public void applyRequestMetadata(
                    RequestInfo requestInfo, Executor appExecutor, MetadataApplier applier) {
                Metadata headers = new Metadata();
                headers.put(AUTHORIZATION_HEADER, jwtToken);
                headers.put( Metadata.Key.of("x-folder-id", Metadata.ASCII_STRING_MARSHALLER), "b1gcnhmcmm2ufn6m4030");
                applier.apply(headers);
            }

        };

        return SynthesizerGrpc
                .newStub(managedChannel)
                .withCallCredentials(callCredentials);
    }

}
