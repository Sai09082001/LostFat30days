package com.merryblue.femalefitness.data.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.merryblue.femalefitness.data.model.TipsArticle;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface TipsArticleDao {

    @Query("SELECT * FROM `tips`")
    Flowable<List<TipsArticle>> getAll();

}
