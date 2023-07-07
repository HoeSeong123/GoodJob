package com.goodjob.resume.facade;

import com.goodjob.resume.config.error.exception.BusinessException;
import com.goodjob.resume.domain.Contents;
import com.goodjob.resume.domain.Prediction;
import com.goodjob.resume.domain.ServiceType;
import com.goodjob.resume.domain.Titles;
import com.goodjob.resume.dto.request.PredictionServiceRequest;
import com.goodjob.resume.dto.response.ResponsePredictionDto;
import com.goodjob.resume.application.outs.FindPredictionPort;
import com.goodjob.resume.application.outs.SavePredictionPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PredictionFacadeTest {

    @Mock
    private SavePredictionPort savePredictionPort;

    @Mock
    private FindPredictionPort findPredictionPort;

    @InjectMocks
    private PredictionFacade predictionFacade;

    @DisplayName("예상 조언이 생성되면, 성공적으로 저장된다.")
    @Test
    void savePredictionTest() {
        // Given
        PredictionServiceRequest request = PredictionServiceRequest.builder()
                .serviceType(ServiceType.EXPECTED_ADVICE)
                .titles(Titles.of(List.of("대주제와 소주제가 매끄럽게 이어지지 않습니다.", "프로젝트 경험을 더 자세히 작성해주세요.")))
                .contents(Contents.of(List.of("모델 조언1", "모델 조언2")))
                .memberId(1L).build();

        // When
        doNothing().when(savePredictionPort).savePrediction(any(Prediction.class));
        predictionFacade.savePrediction(request);

        // Then
        verify(savePredictionPort, times(1)).savePrediction(ArgumentMatchers.any());
    }

    @DisplayName("존재하는 memberId로 Prediction을 조회하면 성공한다,")
    @Test
    void getPredictionSuccessTest() {
        // Given
        Long memberId = 1L;
        Prediction expectedPrediction = Prediction.builder().id(1L)
                .serviceType(ServiceType.EXPECTED_ADVICE).titles(Titles.of(List.of("대주제와 소주제가 매끄럽게 이어지지 않습니다.", "프로젝트 경험을 더 자세히 작성해주세요."))).contents(Contents.of(List.of("모델 조언1", "모델 조언2"))).memberId(1L).build();
        when(findPredictionPort.findPredictionByMemberId(memberId)).thenReturn(Optional.of(expectedPrediction));

        // When
        ResponsePredictionDto result = predictionFacade.getPredictionByMemberId(memberId);

        // Then
        assertEquals(expectedPrediction.getContents(), result.getContents());
    }
    @DisplayName("주어진 memberId에 해당하는 Prediction이 없으면 예외가 발생한다.")
    @Test
    void testGetPrediction_notFound() {
        // Given
        Long memberId = 1L; // 적절한 memberId 값
        when(findPredictionPort.findPredictionByMemberId(memberId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BusinessException.class, () -> predictionFacade.getPredictionByMemberId(memberId));
    }
}