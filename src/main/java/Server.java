import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.SelfSignedCertificate;

import java.time.LocalDateTime;

public class Server extends AbstractVerticle {
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        SelfSignedCertificate certificate = SelfSignedCertificate.create();
        vertx.createHttpServer(new HttpServerOptions()
        .setSsl(true)
                .setKeyCertOptions(certificate.keyCertOptions())
                .setTrustOptions(certificate.trustOptions())
        ).requestHandler(req->{req.response().end("Deployed");
           req.exceptionHandler(error->{
               error.printStackTrace();
           });
          req.response().close();
        }).listen(8443,handler->{
            if(handler.succeeded()){
                System.out.println("Server running "+ LocalDateTime.now());
                startFuture.tryComplete();
            }
            else startFuture.fail(handler.cause());
        });
    }
}
