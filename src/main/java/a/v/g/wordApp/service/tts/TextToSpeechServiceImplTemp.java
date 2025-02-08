//package a.v.g.wordApp.service.tts;
//
//import a.v.g.wordApp.utils.grpc.AuthTokenInterceptor;
//import io.grpc.ClientInterceptors;
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
//import io.grpc.stub.StreamObserver;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import yandex.cloud.api.ai.tts.v3.SynthesizerGrpc.SynthesizerBlockingStub;
//import yandex.cloud.api.ai.tts.v3.SynthesizerGrpc.SynthesizerStub;
//import yandex.cloud.api.ai.tts.v3.Tts.AudioFormatOptions;
//import yandex.cloud.api.ai.tts.v3.Tts.RawAudio;
//import yandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisRequest;
//import yandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisResponse;
//
//import java.io.FileOutputStream;
//
//import static yandex.cloud.api.ai.tts.v3.SynthesizerGrpc.newBlockingStub;
//
//@Service
//@RequiredArgsConstructor
//public class TextToSpeechServiceImplTemp implements TextToSpeechService {
//
//    private final SynthesizerStub synthesizerStub;
//
//    public void synthesizeText(String text, String outputFilePath) {
//        String token = "your-auth-token"; // Замените на ваш токен
//        String serverAddress = "tts.api.cloud.yandex.net";
//
//        // Создайте базовый канал
//        ManagedChannel baseChannel = ManagedChannelBuilder.forAddress(serverAddress, 443)
//                .useTransportSecurity() // Используем HTTPS
//                .build();
//
//        // Добавьте перехватчик для аутентификации
//        var authChannel = ClientInterceptors.intercept(baseChannel, new AuthTokenInterceptor(token));
//
//        // Создайте клиента
//        SynthesizerBlockingStub client = newBlockingStub(authChannel);
//
//
//        // Создаем запрос
//        UtteranceSynthesisRequest request = UtteranceSynthesisRequest.newBuilder()
//                .setText(text)
//                .setOutputAudioSpec(AudioFormatOptions.newBuilder()
//                        .setRawAudio(RawAudio.newBuilder()
//                                .setAudioEncoding(RawAudio.AudioEncoding.LINEAR16_PCM)
//                                .setSampleRateHertz(22050)
//                                .build())
//                        .build())
//                .build();
//
//        // Потоковый ответ
//        synthesizerStub.utteranceSynthesis(request, new StreamObserver<UtteranceSynthesisResponse>() {
//            FileOutputStream outputStream;
//
//            {
//                try {
//                    outputStream = new FileOutputStream(outputFilePath);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onNext(UtteranceSynthesisResponse response) {
//                try {
//                    outputStream.write(response.getAudioChunk().getData().toByteArray());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                System.err.println("Error: " + t.getMessage());
//            }
//
//            @Override
//            public void onCompleted() {
//                try {
//                    outputStream.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                System.out.println("Synthesis complete. Audio saved to " + outputFilePath);
//            }
//        });
//
//        System.out.println("Response received");
//
////        synthesizerStub.utteranceSynthesis(request, new StreamObserver<UtteranceSynthesisResponse>() {
////            FileOutputStream outputStream;
////
////            {
////                try {
////                    outputStream = new FileOutputStream(outputFilePath);
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////            }
////
////            @Override
////            public void onNext(UtteranceSynthesisResponse response) {
////                try {
////                    outputStream.write(response.getAudioChunk().getData().toByteArray());
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////            }
////
////            @Override
////            public void onError(Throwable t) {
////                System.err.println("Error: " + t.getMessage());
////            }
////
////            @Override
////            public void onCompleted() {
////                try {
////                    outputStream.close();
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////                System.out.println("Synthesis complete. Audio saved to " + outputFilePath);
////            }
////        });
//    }
//}
//
