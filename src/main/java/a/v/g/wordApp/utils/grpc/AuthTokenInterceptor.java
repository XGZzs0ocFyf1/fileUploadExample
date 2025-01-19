package a.v.g.wordApp.utils.grpc;

import io.grpc.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthTokenInterceptor implements ClientInterceptor {
    private final String authToken;



    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {

        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
                next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                headers.put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), "Bearer " + authToken);
                super.start(responseListener, headers);
            }
        };
    }
}
