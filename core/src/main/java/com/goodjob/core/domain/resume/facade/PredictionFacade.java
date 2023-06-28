package com.goodjob.core.domain.resume.facade;

import com.goodjob.core.domain.resume.domain.Prediction;
import com.goodjob.core.domain.resume.ports.in.FindPredictionPort;
import com.goodjob.core.domain.resume.usecase.FindPredictionUseCase;
import com.goodjob.core.domain.resume.usecase.SavePredictionUseCase;
import com.goodjob.core.domain.resume.dto.request.PredictionServiceRequest;
import com.goodjob.core.domain.resume.ports.in.SavePredictionPort;
import com.goodjob.core.global.error.ErrorCode;
import com.goodjob.core.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PredictionFacade implements SavePredictionUseCase, FindPredictionUseCase {

    private final SavePredictionPort savePredictionPort;
    private final FindPredictionPort findPredictionPort;

    @Override
    @Transactional
    public void savePrediction(PredictionServiceRequest request) {
        Prediction prediction = request.toEntity();
        savePredictionPort.savePrediction(prediction);
    }

    @Override
    public Prediction getPrediction(Long memberId) {
        return findPredictionPort.findPredictionByMemberId(memberId).orElseThrow(() -> new BusinessException(ErrorCode.PREDICTION_NOT_FOUND));
    }
}