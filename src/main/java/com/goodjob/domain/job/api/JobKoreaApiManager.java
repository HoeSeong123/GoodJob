package com.goodjob.domain.job.api;


import com.goodjob.domain.job.Constants;
import com.goodjob.domain.job.dto.JobResponseDto;
import com.goodjob.domain.job.api.jaxb.DataListTag;
import com.goodjob.domain.job.api.jaxb.Items;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

@Slf4j
public class JobKoreaApiManager extends JobStatistic {

    private String key;


    public void jobKoreaStatistic()  {
        URL url = null;
        int career = 0;
        try {
            url = new URL(Constants.JOBKOREA); // api 발급시 url + key 입력
        } catch (MalformedURLException e) {
            log.error("JobKorea Url 잘못 됐음");
        }
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(DataListTag.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            DataListTag dataListTag = (DataListTag) unmarshaller.unmarshal(url);
            Items items = dataListTag.getItems();

            career = items.getCareer();
            LocalDateTime createDate = LocalDateTime.parse(items.getCreateData());
            LocalDateTime deadLine = LocalDateTime.parse(items.getDeadLine());
            JobResponseDto jobResponseDto = new JobResponseDto(items.getCompany(), items.getSubject(), items.getUrl(), items.getSector(), createDate, deadLine, career);
            setJobDtos(jobResponseDto);
        } catch (JAXBException e) {
            log.error("Jaxb 디펜던시 수정 필요");
        }
    }

}
