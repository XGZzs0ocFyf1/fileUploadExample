package a.v.g.wordApp.config;

import io.grpc.CallCredentials;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import yandex.cloud.api.ai.tts.v3.SynthesizerGrpc;
import yandex.cloud.api.ai.tts.v3.SynthesizerGrpc.*;

import java.util.concurrent.Executor;

@Configuration
public class GrpcClientConfiguration {

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress("tts.api.cloud.yandex.net", 443)
                .useTransportSecurity() // Используем TLS

                .build();
    }

//    @Bean
//    public SynthesizerStub synthesizerStub(ManagedChannel managedChannel) {
//        return SynthesizerGrpc
//                .newStub(managedChannel)
//    }

    private static final Metadata.Key<String> AUTHORIZATION_HEADER =
            Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);

    private String getJwtToken() {
        // Реализуйте метод для получения вашего токена
        return "Bearer t1.9euelZrLmc2LnY7Nk5ySl47Ji5iKz-3rnpWayJKYlZDMk5WOycuSjcmVz8nl8_dqA3pD-e9naVYj_t3z9yoyd0P572dpViP-zef1656Vmoqdi5zPlZzLm5nIkJzLyM3J7_zF656Vmoqdi5zPlZzLm5nIkJzLyM3J.zUZlLfIzgxEMvGMYRSXLJMCWHNENEJ-shFRn4B3waXLyV6-RIOaGs8dhgqr4Vpdr0IkXNJGKKQvwfRdvijI-CQ";
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
