import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SelfSignedCertificate;
import io.vertx.ext.web.Router;

import java.awt.*;
import java.time.LocalDateTime;

public class Server extends AbstractVerticle {
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        SelfSignedCertificate certificate = SelfSignedCertificate.create();
        Router router = Router.router(vertx);
        router.get().handler(h->{
            h.request().exceptionHandler(ex->{
                System.out.println("ate get exception");
            });
            h.request().response().end(new JsonObject().put("response","responded").encode());
            h.request().response().close();
        });
        router.post("/post/request").handler(h->{
            h.request().exceptionHandler(ex->{
                System.out.println("ate post exception");
            });
           h.request().response().end(new JsonObject().put("response","responded").encode());
           h.request().response().close();
        });
        vertx.createHttpServer(new HttpServerOptions()
        .setSsl(true)
                .setKeyCertOptions(certificate.keyCertOptions())
                .setTrustOptions(certificate.trustOptions())
        ).requestHandler(router::accept).exceptionHandler(error->{
            System.out.println("Error...lol");})
                .listen(8443,handler->{
            if(handler.succeeded()){
                System.out.println("Server running "+ LocalDateTime.now());
                startFuture.tryComplete();
            }
            else startFuture.fail(handler.cause());
        });
    }
}
