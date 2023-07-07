package com.goodjob.resume.application.outs;

import com.goodjob.resume.domain.Prediction;

import java.util.List;
import java.util.Optional;

public interface FindPredictionPort {

    Optional<Prediction> findPredictionById(Long id);

    Optional<Prediction> findPredictionByMemberId(Long memberId);

    List<Prediction> findPredictionsByMemberId(Long memberId);

}
