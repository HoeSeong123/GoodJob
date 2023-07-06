package com.goodjob.core.domain.article.repository;


import com.goodjob.core.domain.article.entity.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepositoryCustom {
    List<Article> findQslBySortCode(int id, int sortCode, String category, String kw);


    Optional<Article> findQslById(Long id);
}
