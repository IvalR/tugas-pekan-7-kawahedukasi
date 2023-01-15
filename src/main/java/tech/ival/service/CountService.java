package tech.ival.service;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.scheduler.Scheduled;
import io.vertx.core.json.JsonObject;
import tech.ival.models.KebunModel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class CountService {


    @Transactional
    int itung(){
        KebunModel keMod = KebunModel.find("ORDER BY createdAt DESC").firstResult();
        int tambahTomat = keMod.total + 500;
        int z = tambahTomat;
        return z;
    }

    @Scheduled(cron="0 57 13 ? * SUN *")
    @Transactional
    public void count(){

        JsonObject req = new JsonObject();
        req.put("komoditas","tomat");
        req.put("total", itung());
        KebunModel kebunModel = new KebunModel();
        kebunModel.komoditas = req.getString("komoditas");
        kebunModel.total = Integer.valueOf(req.getString("total"));
        kebunModel.persist();
//        System.out.println(itung());

    }

    @Transactional
    public JsonObject resetTotal(){
        JsonObject req = new JsonObject();
        req.put("komoditas","tomat");
        req.put("total", 0);
        return req;
    }


}
