package com.imooc.admin.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.bo.SaveCategoryBO;

import java.util.List;

public interface CategoryMngService {
    void saveOrUpdateCategory(SaveCategoryBO newCategoryBO);

    List<Category> getCatList();

    boolean queryCatIsExist(String name, String oldCatName);

    void modifyCategory(SaveCategoryBO newCategoryBO);
}