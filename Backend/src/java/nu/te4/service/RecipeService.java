/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.service;

import java.sql.SQLException;
import javax.ejb.EJB;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nu.te4.beans.LoginBean;
import nu.te4.beans.RecipeBean;

/**
 *
 * @author Jacob
 */
@Path("/")
public class RecipeService {

    @EJB
    RecipeBean recipeBean;

    @EJB
    LoginBean loginBean;
    //in logging behöver läggas till på alla

    @GET
    @Path("login")
    public Response login(@Context HttpHeaders header) {
        System.out.println(header);
        String basic_auth = header.getHeaderString("Authorization");

        System.out.println(basic_auth);
        boolean result = loginBean.checkCredentials(basic_auth);
        System.out.println(result);
        if (result) {
            return Response.ok().build();
        }
        return Response.ok(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("create/user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(String body) {
        System.out.println(body);
        int statuscode = loginBean.createUser(body);
        return Response.status(statuscode).build();
    }

    //----
    //hämtar all recept
    @GET
    @Path("recipes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecipeTable() throws SQLException {
        JsonArray recipeTable = recipeBean.getRecipes();
        if (recipeTable != null) {
            return Response.ok(recipeTable).build();
        } else {
            return Response.status(418).build();
        }
    }

    //hämtar all ingridencer
    @GET
    @Path("getAllIngrediences")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFoods() throws SQLException {
        JsonArray foods = recipeBean.getAllFoods();
        if (foods != null) {
            return Response.ok(foods).build();
        } else {
            return Response.status(418).build();
        }
    }

    //hämtar all catogerier som finns 
    @GET
    @Path("getCategorys")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategorys() throws SQLException {
        JsonArray categorys = recipeBean.getCategorys();
        if (categorys != null) {
            return Response.ok(categorys).build();
        } else {
            return Response.status(418).build();
        }
    }

    //hämtar recept med en viss category
    @POST
    @Path("recipesByCategory")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecipeByCategory(String category) throws SQLException {
        JsonArray recipes = recipeBean.getRecipesByCategory(category);
        if (recipes != null) {
            return Response.ok(recipes).build();
        }
        return Response.status(418).build();
    }


    //lägger till behöver json 
    @POST
    @Path("addRecipe")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRecipe(String body, @Context HttpHeaders header) {
        String basic_auth = header.getHeaderString("Authorization");
        if (loginBean.checkCredentials(basic_auth)) {
                        System.out.println("?1");

            int statusCode = recipeBean.addRecipe(body);
                        System.out.println("?2");

            return Response.status(statusCode).build();
        } else {
                        System.out.println("?3");

            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    //Hämta ett recept emd visst id 
    @GET
    @Path("recipe/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getRecipeById(@PathParam("id") int id, @Context HttpHeaders header) throws SQLException {
        JsonObject recipe = recipeBean.getRecipesById(id);
        if (recipe != null) {
            return Response.ok(recipe).build();
        }
        return Response.status(418).build();
    }

    @PUT
    @Path("recipe/{id}/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRecipe(@PathParam("id") int id, String body, @Context HttpHeaders header) {
        String basic_auth = header.getHeaderString("Authorization");
        if (loginBean.checkCredentials(basic_auth)) {
            System.out.println("ok");
            int statuscode = recipeBean.updateRecipe(id, body);
            return Response.status(statuscode).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    //delete kräver id av receptet i databasen 
    @DELETE
    @Path("delete/recipe/{id}")
    public Response deleteRecipe(@PathParam("id") int id, @Context HttpHeaders header) {
        String basic_auth = header.getHeaderString("Authorization");
        if (loginBean.checkCredentials(basic_auth)) {
            int statusCode = recipeBean.deleteRecipe(id);
            return Response.status(statusCode).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    }

    //update
}
