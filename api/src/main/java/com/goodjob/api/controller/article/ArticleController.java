package com.goodjob.api.controller.article;


import com.goodjob.core.domain.article.dto.request.ArticleRequestDto;
import com.goodjob.core.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.core.domain.article.entity.Article;
import com.goodjob.core.domain.article.service.ArticleService;
import com.goodjob.core.domain.comment.dto.request.CommentRequestDto;
import com.goodjob.core.domain.file.dto.request.FileRequest;
import com.goodjob.core.domain.file.entity.File;
import com.goodjob.core.domain.file.service.FileService;
import com.goodjob.core.domain.s3.service.S3Service;
import com.goodjob.core.domain.subComment.dto.request.SubCommentRequestDto;
import com.goodjob.core.global.base.rsData.RsData;
import com.goodjob.core.global.rq.Rq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
@Slf4j
public class ArticleController {
    private final Rq rq;
    private final ArticleService articleService;
    private final S3Service s3Service;
    private final FileService fileService;

    @GetMapping("/list/{id}")
    public String list(Model model, @PathVariable("id") int id, @RequestParam(value="page", defaultValue="0") int page, ToListForm toListForm) {
        Page<ArticleResponseDto> paging = articleService.findByCategory(page, id, toListForm.sortCode, toListForm.category, toListForm.query);
        model.addAttribute("paging", paging);

        return "article/list";
    }

    @GetMapping("/detail/{id}")
    public String detailArticle (Model model, @PathVariable("id") Long id, CommentRequestDto commentRequestDto, SubCommentRequestDto subCommentRequestDto) {
        RsData<Article> articleRsData = articleService.getArticle(id);
        if(articleRsData.isFail()) {
            return rq.historyBack(articleRsData);
        }
        ArticleResponseDto articleResponseDto = articleService.increaseViewCount(articleRsData.getData());
        Map<String, File> fileMap = fileService.getFileMap(articleResponseDto.getId());
        articleResponseDto.getExtra().put("fileMap", fileMap);

        model.addAttribute("article", articleResponseDto);
        return "article/detailArticle";

    }

    @GetMapping("/create")
    public String createArticle(ArticleRequestDto articleRequestDto ) {
        return "article/createForm";

    }

    @PostMapping("/create")
    public String createArticle(@Valid ArticleRequestDto articleRequestDto, BindingResult bindingResult, MultipartRequest multipartRequest, FileRequest fileRequest) throws IOException {
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

        RsData<Article> articleRsData = articleService.createArticle(rq.getMember(), articleRequestDto);
        if(articleRsData.isFail()) {
            return rq.historyBack(articleRsData);
        }
        Article article = articleRsData.getData();
        s3Service.uploadFile(article, fileMap);


        return "redirect:/article/detail/%s".formatted(article.getId());
    }


    @GetMapping("/update/{id}")
    public String updateArticle(Model model, @PathVariable("id") Long id) {
        RsData<ArticleResponseDto> articleResponseDtoRsData = articleService.getArticleResponseDto(id);

        if(articleResponseDtoRsData.isFail()) {
            return rq.historyBack(articleResponseDtoRsData);
        }

        ArticleResponseDto articleResponseDto = articleResponseDtoRsData.getData();
        Map<String, File> fileMap = fileService.getFileMap(articleResponseDto.getId());
        articleResponseDto.getExtra().put("fileMap", fileMap);

        model.addAttribute("article", articleResponseDto);

        return "article/modifyForm";
    }

    @PostMapping("/update/{id}")
    public String updateArticle(@Valid ArticleRequestDto articleRequestDto, BindingResult bindingResult,
                                @PathVariable("id") Long id, MultipartRequest multipartRequest,
                                FileRequest fileRequest, @RequestParam Map<String, String> params) throws IOException {

        RsData<Article> articleRsData = articleService.updateArticle(rq.getMember(), id, articleRequestDto);

        if(articleRsData.isFail()) {
            return rq.historyBack(articleRsData);
        }

        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

        Article article = articleRsData.getData();
        for(String value : params.keySet()) {
            log.info(value);
        }
        s3Service.deleteFiles(article, params);
        s3Service.uploadFile(article, fileMap);

        return rq.redirectWithMsg("/article/detail/%s".formatted(id), articleRsData);
    }

    @GetMapping("/delete/{id}")
    public String deleteArticle(@PathVariable("id") Long id) {
        RsData<Article> articleRsData = articleService.deleteArticle(rq.getMember(), id);

        if(articleRsData.isFail()) {
            return rq.historyBack(articleRsData);
        }


        return rq.redirectWithMsg("/article/list/0", articleRsData);
    }

    @Setter
    public static class ToListForm {
        private int sortCode = 1;
        private String category = "제목";
        private String query = "";

    }

    @GetMapping("/show/list")
    public String showArticles(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<ArticleResponseDto> paging = articleService.findByCategory(page, 0, 1, "글쓴이", rq.getMember().getNickname());
        model.addAttribute("paging", paging);

        return "member/myArticles";
    }
}
