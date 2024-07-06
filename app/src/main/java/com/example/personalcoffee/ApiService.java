package com.example.personalcoffee;

import com.example.personalcoffee.Model.Login;
import com.example.personalcoffee.Model.Recipe;
import com.example.personalcoffee.Model.Register;

import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    //유저
    @POST("/user/register")
    Call<ResponseBody> userRegister(@Body Register register);

    @POST("/user/login")
    Call<ResponseBody> userLogin(@Body Login login);

    @POST("/user/logout")
    Call<ResponseBody> userLogout();

    @GET("/user/getId")
    Call<ResponseBody> getId();

    @GET("/user/nfc/{nfcId}")
    Call<ResponseBody> addNfc(@Path("nfcId") String nfcId);

    //레시피
    @GET("/recipe/list")
    Call<ResponseBody> RecipeList();

    @POST("/recipe")
    Call<ResponseBody> createRecipe(@Body Recipe recipe);

    @DELETE("/recipe/{recipeId}")
    Call<ResponseBody> deleteRecipe(@Path("recipeId") String recipeId);

    @PUT("/recipe/{recipeId}")
    Call<ResponseBody> updateRecipe(@Path("recipeId") String recipeId, @Body Recipe recipe);

    @PATCH("/recipe/like/{recipeId}/{loveId}")
    Call<ResponseBody> loveRecipe(@Path("recipeId") String recipeId, @Path("loveId") String loveId);

    //추천레시피
    @GET("/recipe/recommend")
    Call<ResponseBody> getRecommendRecipe();

    //친구
    @GET("/friend/search")
    Call<ResponseBody> searchFriend(@Query("search") String search);

    @GET("/friend/list/request")
    Call<ResponseBody> requestFriendList();

    @GET("/friend/list")
    Call<ResponseBody> friendList();

    @POST("/friend/add/{friendNickname}")
    Call<ResponseBody> addFriend(@Path("friendNickname") String friendNickname);


    @GET("/friend/{nickname}/recipe")
    Call<ResponseBody> friendRecipe(@Path("nickname") String nickname);

    @POST("/friend/recipe/{recipeId}")
    Call<ResponseBody> addMyRecipe(@Path(("recipeId")) String recipeId);

    @DELETE("/friend/{friendNickname}")
    Call<ResponseBody> deleteFriend(@Path("friendNickname") String friendNickname);

    @POST("/friend/request/{friendNickname}")
    Call<ResponseBody> requestFriend(@Path("friendNickname") String friendNickname);

}
