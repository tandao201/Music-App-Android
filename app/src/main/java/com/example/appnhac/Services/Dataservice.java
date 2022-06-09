package com.example.appnhac.Services;

import com.example.appnhac.Models.Album;
import com.example.appnhac.Models.BaiHat;
import com.example.appnhac.Models.ChuDe;
import com.example.appnhac.Models.ChuDeTheLoaiToDay;
import com.example.appnhac.Models.Comment;
import com.example.appnhac.Models.Playlist;
import com.example.appnhac.Models.Quangcao;
import com.example.appnhac.Models.TheLoai;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Dataservice {

    // Only simple read can use GET

    @GET("quangcao.php")
    Call<List<Quangcao>> getQuangCao();

    @GET("playlistToday.php")
    Call<List<Playlist>> getPlaylistToday();

    @GET("chuDeTheLoaiToDay.php")
    Call<ChuDeTheLoaiToDay> getChuDeTheLoaiToDay();

    @GET("albumToday.php")
    Call<List<Album>> getAlbumToDay();

    @FormUrlEncoded
    @POST("DsBaiHat.php")
    Call<List<BaiHat>> getDsBaiHatTheoQc(@Field("idQuangcao") String idQuangcao);

    @FormUrlEncoded
    @POST("DsBaiHat.php")
    Call<List<BaiHat>> getDSBaiHatTheoPlaylist(@Field("idPlaylist") String idPlaylist);

    @GET("DsPlaylist.php")
    Call<List<Playlist>> getAllPlaylists();

    @FormUrlEncoded
    @POST("DsBaiHat.php")
    Call<List<BaiHat>> getDsBaiHatTheoTheLoai(@Field("idTheloai") String idTheloai);

    @GET("tatCaChuDe.php")
    Call<List<ChuDe>> getAllChuDe();

    @FormUrlEncoded
    @POST("theLoaiTheoChuDe.php")
    Call<List<TheLoai>> getAllTheLoaiTheoChuDe(@Field("idchude") String idchude);

    @GET("tatCaAlbum.php")
    Call<List<Album>> getAllAlbum();

    @FormUrlEncoded
    @POST("DsBaiHat.php")
    Call<List<BaiHat>> getDsBaiHatTheoAlbum(@Field("idAlbum") String idAlbum);

    @FormUrlEncoded
    @POST("searchBaiHat.php")
    Call<List<BaiHat>>getBaiHatTheoSearch(@Field("tukhoa") String tukhoa);

    @FormUrlEncoded
    @POST("userthichbaihat.php")
    Call<String> upDateBaiHatYeuThich(@Field("luotthich") String luotthich,
                                      @Field("idbaihat") String idbaihat,
                                      @Field("user") String user);

    @FormUrlEncoded
    @POST("baiHatYeuThichTheoTaiKhoan.php")
    Call<List<BaiHat>> baiHatYeuThichTheoUser(@Field("user") String user);

    @FormUrlEncoded
    @POST("getAllCommentForSong.php")
    Call<List<Comment>> getAllCommentBySongId(@Field("idbaihat") String idbaihat);

    @FormUrlEncoded
    @POST("upLoadComment.php")
    Call<String> upLoadComment(@Field("username") String username,
                                   @Field("comment") String comment,
                                   @Field("idbaihat") String idbaihat,
                                   @Field("avatar") String avatar);
}
