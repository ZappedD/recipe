/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.beans;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author Jacob
 */
@Stateless
public class RecipeBean {

    public JsonArray getRecipes() throws SQLException {
        try {
            //retunerar alla recept
            Connection connection = ConnectionFactory.getConnection();
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM recipes,categorys,users WHERE recipes.category_id = categorys.id AND recipes.user_id = users.id";
            ResultSet data = stmt.executeQuery(sql);
            JsonArray jsonRecipes = getRecipesFromSqlData(data);
            connection.close();
            return jsonRecipes;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public JsonObject getRecipesById(int id) throws SQLException {
        try {
            // retunerar alla recept med en viss id 
            Connection connection = ConnectionFactory.getConnection();
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT * FROM recipes,categorys,users WHERE recipes.category_id = categorys.id AND users.id = recipes.user_id AND recipes.id = " + id);
            ResultSet data = stmt.executeQuery(sql);
            JsonArray recipe = getRecipesFromSqlData(data);
            JsonObject recipeObj = recipe.getJsonObject(0);
            System.out.println(recipeObj);
            connection.close();
            return recipeObj;
        } catch (Exception e) {
            System.out.println("Exeption : " + e);
            return null;
        }
    }

    public JsonArray getRecipesByCategory(String body) throws SQLException {
        try {
            // retunerar alla recept med en viss kategori 
            Connection connection = ConnectionFactory.getConnection();
            JsonReader jsonReader = Json.createReader(new StringReader(body));
            JsonObject categoryTofind = jsonReader.readObject();
            jsonReader.close();
            //hämtar kategorin från json objected man får
            String categoryName = categoryTofind.getString("category").toString();
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM recipes,categorys,users WHERE recipes.category_id = categorys.id AND categorys.category = '" + categoryName + "'";
            ResultSet data = stmt.executeQuery(sql);
            JsonArray recipes = getRecipesFromSqlData(data);
            connection.close();
            return recipes;
        } catch (Exception e) {
            System.out.println("Exeption : " + e);
            return null;
        }
    }

    private JsonArray getRecipesFromSqlData(ResultSet data) throws SQLException {
        //koverterar sql datan från recept data basen till json
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        while (data.next()) {
            JsonObject recipe = Json.createObjectBuilder()
                    .add("name", data.getString("name"))
                    .add("preparing_time", data.getString("preparing_time"))
                    .add("description", data.getString("description"))
                    .add("instruktions", data.getString("instruktions"))
                    .add("serving_quantity", Integer.toString(data.getInt("serving_quantity")))
                    .add("id", Integer.toString(data.getInt("id")))
                    .add("category", data.getString("category"))
                    .add("user", data.getString("username"))
                    .add("ingredience", getIngredienceOfRecipe(data.getInt("id")))
                    .add("time_added", data.getTimestamp("time_added").toString())
                    .add("imageUrl", data.getString("imageUrl"))
                    .build();
            jsonArrayBuilder.add(recipe);
        }
        return jsonArrayBuilder.build();
    }

    private JsonArray getIngredienceOfRecipe(int id) throws SQLException {
        try {
            Connection connection = ConnectionFactory.getConnection();
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT  * FROM ingrediences, foods WHERE foods.id = ingrediences.food_id AND ingrediences.recipe_id =%d", id);
            ResultSet data = stmt.executeQuery(sql);

            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                JsonObject recipe = Json.createObjectBuilder()
                        .add("amountOfUnit", data.getString("amount-of-unit"))
                        .add("unit", data.getString("unit"))
                        .add("type", data.getString("name"))
                        .add("id", data.getString("id"))
                        .build();
                jsonArrayBuilder.add(recipe);
            }

            connection.close();
            return jsonArrayBuilder.build();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public JsonArray getCategorys() throws SQLException {
        try {
            Connection connection = ConnectionFactory.getConnection();
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM categorys";
            ResultSet data = stmt.executeQuery(sql);
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                JsonObject categorys = Json.createObjectBuilder()
                        .add("id", data.getInt("id"))
                        .add("category", data.getString("category"))
                        .build();
                jsonArrayBuilder.add(categorys);
            }
            connection.close();
            return jsonArrayBuilder.build();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    private Integer getRecipeId(String body) throws SQLException {

        JsonObject recipe = bodyToJsonObj(body);

        //hämta data för att kunna hitta id
        String recieName = recipe.getString("name");
        String userName = recipe.getString("user");
        int user_id = getUserId(userName);

        //försöker finna id för ett recept
        try {
            Connection connection = ConnectionFactory.getConnection();
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT * FROM recipes WHERE recipes.time_added = (SELECT MAX(recipes.time_added) "
                    + "FROM recipes WHERE recipes.user_id = '%d' AND recipes.name = '%s')", user_id, recieName);
            ResultSet data = stmt.executeQuery(sql);
            data.next();
            int recipeId = data.getInt("id");
            connection.close();
            return recipeId;
        } catch (Exception e) {

            System.out.println("Eror: " + e);
            return 500;
        }

    }

    private int getCategoryId(String categoryToF) throws SQLException {
        try {
            Connection connection = ConnectionFactory.getConnection();
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT * FROM categorys WHERE category = '%s'", categoryToF);
            ResultSet data = stmt.executeQuery(sql);
            data.next();
            int categoryID = data.getInt("id");
            connection.close();
            return categoryID;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return -1;
        }

    }

    private Integer getUserId(String userName) throws SQLException {
        try {
            Connection connection = ConnectionFactory.getConnection();
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT * FROM users WHERE username = '%s'", userName.toString());
            ResultSet data = stmt.executeQuery(sql);
            data.next();
            int userID = Integer.parseInt(data.getString("id"));
            if (userID < 0) {
                return -1;
            }
            connection.close();
            return userID;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return -1;
        }

    }

    public JsonArray getAllFoods() throws SQLException {
        //Hämtar all ingrediencer som finns på data basen
        try {
            Connection connection = ConnectionFactory.getConnection();
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM foods";
            ResultSet data = stmt.executeQuery(sql);
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                JsonObject foods = Json.createObjectBuilder()
                        .add("id", data.getInt("id"))
                        .add("name", data.getString("name"))
                        .add("unit", data.getString("unit"))
                        .build();
                jsonArrayBuilder.add(foods);
            }
            connection.close();
            return jsonArrayBuilder.build();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public Integer addRecipe(String body) {
        //läsa json
        try {
            JsonObject recipeToAdd = bodyToJsonObj(body);
            //kotrolera om den är tom eller inte 

            //hämta data från jsonobj 
            String name = recipeToAdd.getString("name");
            String preparing_time = recipeToAdd.getString("preparing_time");
            String description = recipeToAdd.getString("description");
            String instruktions = recipeToAdd.getString("instruktions");
            String imageUrl = recipeToAdd.getString("imageUrl");

            int serving_quantity = Integer.parseInt(recipeToAdd.getString("serving_quantity"));
            String userName = recipeToAdd.getString("user");

            int user_id = -1;
            user_id = getUserId(userName);

            if (user_id == -1) {
                //användaren finns inte
                System.out.println("User not FOUND");
                return 404;
            }

            String category = recipeToAdd.getString("category");
            int category_id = getCategoryId(category);
            if (category_id == -1) {
                // om det är skillt från -1 finns den i databasen annar så finns den inte
                // då lägger den till den och ger id på den tillagda
                category_id = addCategoryAGetId(category);
            }
            // lägga upp 
            Connection connection = ConnectionFactory.getConnection();
            Statement stmt = connection.createStatement();
            String sql = String.format("INSERT INTO recipes VALUES('%s', '%s', '%s', '%s', '%d', '%d', '%d','%s', null, null) ",
                    name, preparing_time, description, instruktions, serving_quantity, user_id, category_id, imageUrl);
            stmt.executeUpdate(sql);
            connection.close();
            // kontrolera om det finns några
            JsonArray ingredinces = recipeToAdd.getJsonArray("ingredience");
            if (ingredinces != null) {
                addRecipeIngrediences(body);
            }
        } catch (Exception e) {
            System.out.println("Error Recipe: " + e);
            return 500;
        }

        return 201;
    }

    private Integer addCategoryAGetId(String categoryToAdd) {
        try {
            Connection connection = ConnectionFactory.getConnection();
            Statement stmt = connection.createStatement();
            String sql = String.format("INSERT INTO categorys VALUES('%s', null)", categoryToAdd);
            stmt.executeUpdate(sql);
            connection.close();
            return getRecipeId(categoryToAdd);
        } catch (Exception e) {
            System.out.println("Exeption add category: " + e);
            return 418;
        }
    }

    private Integer addRecipeIngrediences(String body) throws SQLException {
        int recipeID = getRecipeId(body);

        //finner ingrideinserna hur json 
        JsonObject recipe = bodyToJsonObj(body);

        JsonArray ingredincesArray = recipe.getJsonArray("ingredience");
        JsonObject ingredience;
        String currentIngredience = "";
        String currentUnit = "";
        int currentAmountOfUnit = 0;
        int idOfIngrediences = -1;
        for (int i = 0; i < ingredincesArray.size(); i++) {
            ingredience = (JsonObject) ingredincesArray.get(i);
            currentIngredience = ingredience.getString("type");
            currentUnit = ingredience.getString("unit");

            // kotrilloerar om det redan finns i foods
            idOfIngrediences = checkIfFoodExist(currentUnit, currentIngredience);
            if (idOfIngrediences != -1) {

                //Lägger inte till en ny ingridence i listan utan tar den befintliga
                currentAmountOfUnit = Integer.parseInt(ingredience.getString("amountOfUnit"));
                try {
                    Connection connection = ConnectionFactory.getConnection();
                    Statement stmt = connection.createStatement();
                    String sql = String.format("INSERT INTO ingrediences VALUES(NULL, %d, %d, %d)", currentAmountOfUnit, idOfIngrediences, recipeID);
                    stmt.executeUpdate(sql);
                    connection.close();
                } catch (Exception e) {
                    System.out.println("Exeption in Adding ing: " + e);
                    return 418;
                }
            } else {
                try {
                    // läger till maten som en ny ingridience
                    Connection connection = ConnectionFactory.getConnection();
                    Statement stmt = connection.createStatement();
                    String sqlNewFood = String.format("INSERT INTO foods VALUES('%s', '%s', null)", currentIngredience, currentUnit);
                    // lägger till maten 
                    stmt.executeUpdate(sqlNewFood);

                    //Hämta idét på den tilllagda
                    String getIdOfNewFood = String.format("SELECT id FROM foods WHERE name = '%s' AND unit = '%s'", currentIngredience, currentUnit);
                    ResultSet data = stmt.executeQuery(getIdOfNewFood);
                    data.next();
                    int idOfNewFood = data.getInt("id");

                    // lägger till till receptet   
                    String sqlNewIngredience = String.format("INSERT INTO ingrediences VALUES(NULL, '%d', '%d', '%d') ", currentAmountOfUnit, idOfNewFood, recipeID);
                    stmt.executeUpdate(sqlNewIngredience);
                    connection.close();
                } catch (Exception e) {
                    System.out.println("Error . " + e);
                    return 418;
                }

            }
        }

        return 200;
    }

    public Integer updateRecipe(int id, String body) {
        try {
            JsonReader jsonReader = Json.createReader(new StringReader(body));
            JsonObject recipe = jsonReader.readObject();
            jsonReader.close();

            //hämta data
            String name = recipe.getString("name");
            String preparing_time = recipe.getString("preparing_time");
            String description = recipe.getString("description");
            String instruktions = recipe.getString("instruktions");
            int serving_quantity = Integer.parseInt(recipe.getString("serving_quantity"));
            String userName = recipe.getString("user");
            int user_id = getUserId(userName);
            String category = recipe.getString("category");
            int category_id = getCategoryId(category);
            String imageUrl = recipe.getString("imageUrl");
            //kolla datan 

            Connection connection = ConnectionFactory.getConnection();
            Statement stmt = connection.createStatement();
            String sql = String.format("UPDATE recipes SET name = '%s',"
                    + "preparing_time = '%s', description  = '%s', instruktions = '%s',"
                    + "serving_quantity = '%d', user_id = '%d', category_id ='%d' , imageUrl = '%s' WHERE id = '%d'",
                    name, preparing_time, description, instruktions, serving_quantity, user_id, category_id, imageUrl, id);

            stmt.executeUpdate(sql);
            connection.close();

        } catch (Exception e) {
            System.out.println("Error: " + e);
            return 500;
        }

        return 201;
    }

    public int deleteRecipe(int id) {
        try {
            Connection connection = ConnectionFactory.getConnection();
            Statement stmt = connection.createStatement();
            String sql = String.format("DELETE FROM recipes WHERE id=%d", id);
            stmt.executeUpdate(sql);
            connection.close();
            deleteRecipeIngrediences(id);
            return 204;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return 400;
        }
    }

    public int deleteRecipeIngrediences(int id) {
        try {
            Connection connection = ConnectionFactory.getConnection();
            Statement stmt = connection.createStatement();
            String sql = String.format("DELETE FROM ingrediences WHERE recipe_id=%d", id);
            stmt.executeUpdate(sql);
            connection.close();
            return 204;
        } catch (Exception e) {
            System.out.println("Error: removing Ingrediences : " + e.getMessage());
            return 400;
        }
    }

    public Integer checkIfFoodExist(String unit, String ingridience) throws SQLException {
        try {
            //retunerar -1 om inte hittar något och id på maten om den finns 
            Connection connection = ConnectionFactory.getConnection();
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT * FROM foods WHERE name = '%s' AND unit = '%s' ", ingridience, unit);
            ResultSet data = stmt.executeQuery(sql);
            if (data.next()) {
                int idToReturn = data.getInt("id");
                return idToReturn;
            } else {
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return -1;
        }
    }

    private JsonObject bodyToJsonObj(String body) {
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject recipe = jsonReader.readObject();
        jsonReader.close();
        return recipe;
    }

}
