package tech.ival.controller;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MailTemplate;
import io.quarkus.mailer.Mailer;
import io.quarkus.panache.common.Sort;
import io.quarkus.qute.Location;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import tech.ival.models.KebunModel;
import tech.ival.service.CountService;
import tech.ival.service.ExcelService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Path("/garden")
public class KebunController {

    @Inject
    @Location("hello")
    MailTemplate hello;

    @Inject
    Mailer mailer;
    @Inject
    ExcelService excelService;

    @Inject
    CountService countService;

    @GET
    public Response getGardens(){
        List<KebunModel> gardens = KebunModel.listAll(Sort.ascending("id"));
        return Response.ok(gardens).build();
    }

    @POST
    @Transactional
    public Response input(KebunModel gardens){
        if (gardens.id != null){
            throw new WebApplicationException("Id was invalidly se on request", 422);
        }

        gardens.persist();
        return Response.ok(gardens).status(201).build();
    }



    @GET
    @Path("/generate")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public JsonObject generate() throws IOException {
        return excelService.generateExcel();
    }

    @GET
    @Path("/imperative")
    @Blocking
    public void sendASimpleEmail() {
        mailer
                .send(Mail.withText("maikelkepin13@gmail.com",
                "A simple email from quarkus", "This is my body"));
    }

    @GET
    @Path("/sendFile")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Scheduled(cron = "0 15 10 L * ?")
    @Blocking
    public Uni<Void> sendEmailUsingReactiveMailer() throws IOException {
        JsonObject result = excelService.generateExcel();
        return hello.to("maikelkepin13@gmail.com")
                .subject("Hello from qute")
                .data("name","Ival")
                .addInlineAttachment(result.getString("fileName")
                        ,new File(result.getString("fileName"))
                        ,"application/xlsx"
                        ,"<my-document@quarkus.io>")
                .send();
    }


}
