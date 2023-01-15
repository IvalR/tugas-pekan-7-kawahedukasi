package tech.ival.controller;

import io.quarkus.panache.common.Sort;
import io.vertx.core.json.JsonObject;
import tech.ival.models.Character;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.util.List;

@Path("character")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CharacterController {

    @GET
    public List<Character> getCharacter(){
        return Character.listAll(Sort.ascending("id"));
    }

    @POST
    @Transactional
    public List<Character> addCharacter(Character character){
    character.persist();

    return Character.listAll(Sort.ascending("id"));
    }

    @PUT
    @Path("{id}")
    @Transactional
    public List<Character> updateCharacter(@PathParam("id") Long id,Character character){

        Character oldCharacter = Character.findById(id);
        if(oldCharacter == null) {
            throw new NotFoundException();
        }

        oldCharacter.nickname = character.nickname;
        oldCharacter.role = character.role;
        oldCharacter.deff = character.deff;
        oldCharacter.physicalDmg = character.physicalDmg;
        oldCharacter.magicalDmg = character.magicalDmg;

        return Character.listAll();
    }

    @PATCH
    @Transactional
    public JsonObject editCharacter(@QueryParam("id") Long id, @QueryParam("newDeff") Integer newDeff,
                                    @QueryParam("newRole") String newRole) {
        Character.update("deff = ?1, role = ?2 where id = ?3", newDeff, newRole, id);
        return new JsonObject();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public List<Character> deleteCharacter(@PathParam("id") Long id){
        Character.deleteById(id);
        return Character.listAll();
    }
}
