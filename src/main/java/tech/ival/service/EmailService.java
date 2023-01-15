package tech.ival.service;

import io.quarkus.mailer.MailTemplate;
import io.quarkus.qute.Location;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

@ApplicationScoped
public class EmailService {

    @Inject
    @Location("emailTemplate/hello")
    MailTemplate hello;

    public Uni<Void> sendEmail() throws IOException {
        JsonObject content = new JsonObject();
        content.put("fullName","Ival");
        content.put("email","ivalivalrakha@gmail.com");
        content.put("address","garut");

        return hello.to(content.getString("email"))
                .subject("Hello from qute")
                .data("name",content.getString("fullName"))
                .send();
    }
}
