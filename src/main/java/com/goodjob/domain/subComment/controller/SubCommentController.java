package com.goodjob.domain.subComment.controller;

import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.comment.service.CommentService;
import com.goodjob.domain.subComment.dto.request.SubCommentRequestDto;
import com.goodjob.domain.subComment.entity.SubComment;
import com.goodjob.domain.subComment.service.SubCommentService;
import com.goodjob.global.base.rq.Rq;
import com.goodjob.global.base.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subComment")
public class SubCommentController {
    private final Rq rq;
    private final SubCommentService subCommentService;
    private final CommentService commentService;

    @PostMapping("/create/{id}")
    public String createSubComment(Model model, @PathVariable("id") Long id, @Valid SubCommentRequestDto subCommentRequestDto, BindingResult bindingResult) {
        RsData<SubComment> subCommentRsData = subCommentService.createSubComment(rq.getMember(), id, subCommentRequestDto);

        if(subCommentRsData.isFail()) {
            return rq.historyBack(subCommentRsData);
        }

        return rq.redirectWithMsg("/article/detail/%s".formatted(subCommentRsData.getData().getComment().getArticle().getId()), subCommentRsData);
    }

    @PostMapping("/update/{id}")
    public String updateSubComment(@Valid SubCommentRequestDto subCommentRequestDto, BindingResult bindingResult,
                                   @PathVariable("id") Long id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "article/detailArticle";
        }

        RsData<SubComment> subCommentRsData = subCommentService.updateComment(rq.getMember(), id, subCommentRequestDto);

        if(subCommentRsData.isFail()) {
            return rq.historyBack(subCommentRsData);
        }

        return rq.redirectWithMsg("/article/detail/%s".formatted(subCommentRsData.getData().getComment().getArticle().getId()), subCommentRsData);
    }

    @GetMapping("/delete/{id}")
    public String deleteSubComment(Principal principal, @PathVariable("id") Long id) {
        RsData<SubComment> subCommentRsData = subCommentService.deleteSubComment(rq.getMember(), id);

        return rq.redirectWithMsg("/article/detail/%s".formatted(subCommentRsData.getData().getComment().getArticle().getId()), subCommentRsData);
    }
}