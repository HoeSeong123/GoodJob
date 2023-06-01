package com.goodjob.domain.job.controller;

import com.goodjob.domain.job.dto.JobRequestForm;
import com.goodjob.domain.job.entity.JobStatistic;
import com.goodjob.domain.job.service.JobStatisticService;
import com.goodjob.global.base.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jobstatistic")
public class JobStatisticController {
    private final JobStatisticService jobStatisticService;
    private final Rq rq;

    @GetMapping("/search")
    public String SearchFilter(JobRequestForm jobRequestForm) {
        return "jobstatistic/jobRequest";
    }

    @GetMapping("/search/all")
    public String SearchList(@RequestParam(value = "sector") String sector, @RequestParam(value = "career") String career
                            , @RequestParam(value = "page", defaultValue = "0") int page, Model model) {

        Page<JobStatistic> paging = jobStatisticService.getList(sector , career, page);
        model.addAttribute("paging", paging);
        return "jobstatistic/list";
    }
//        RsData<List<JobStatistic>> list = jobStatisticService.getList(jobRequestForm.getSector(), jobRequestForm.getCareer(), );

//    @GetMapping("/list")
//    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page, String keyword){
//
//    }
}


//    @GetMapping("/list")
//    public String list(Model model, @RequestParam(value = "page", defaultValue = "0")
//    int page, String keyword) {
//
//        Page<Rule> paging;
//        if (keyword != null) {
//            paging = ruleService.getList(keyword, page);
//        } else {
//            paging = ruleService.getList(page);
//        }
//
//        model.addAttribute("paging", paging);
//        return "rule/ruleList";
//    }