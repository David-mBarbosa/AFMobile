package com.example.afmobile;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleBooksService {
    @GET("volumes")
    Call<BookResponse> buscarLivros(@Query("q") String query);
}

// Classes de mapeamento do JSON da API (Simplificado)
class BookResponse {
    public java.util.List<BookItem> items;
}

class BookItem {
    public VolumeInfo volumeInfo;
}

class VolumeInfo {
    public String title;
    public java.util.List<String> authors;
    public String publishedDate;
    public String publisher;
}