package com.groovus.www.controller;

import com.groovus.www.dto.StatusHistoryDTO;
import com.groovus.www.dto.TaskDTO;
import com.groovus.www.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class TaskRestController {

    private final TaskService taskService;

    @PostMapping("/task/countall")
    public ResponseEntity<Long> getTaskCount(String pid){

        Long count = taskService.getTaskCount(Long.parseLong(pid));
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PostMapping ("/task/view")
    public ResponseEntity<TaskDTO> readTask(String tid){

        log.info("=============================");
        log.info(tid);
        log.info("=============================");

        TaskDTO taskDTO = taskService.readTask(tid);

        return new ResponseEntity<>(taskDTO,HttpStatus.OK);
    }

    @PostMapping("/task/changestatus")
    public ResponseEntity<List<StatusHistoryDTO>> changeStatus(String tid , String status , String uid ,String prevStatus){

        //날짜 구하기
        LocalDate now = LocalDate.now();
        //날짜 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        //포맷 적용
        String formatedNow = now.format(formatter);

        String result  = taskService.changeTaskStatus(tid,status,uid,prevStatus,formatedNow);

        if(result.equals("success")){

            List<StatusHistoryDTO> statusHistoryDTOList = taskService.getHistory(tid);

            log.info("========================상태 리스트=====================");
            log.info(statusHistoryDTOList);
            log.info("===================================================");


            if(statusHistoryDTOList != null){

                return new ResponseEntity<>(statusHistoryDTOList,HttpStatus.OK);

            }else{
                return null;
            }
        }else{
            return null;
        }
    }


    @PostMapping("/task/history")
    public ResponseEntity<List<StatusHistoryDTO>> getHistory(String tid){

        List<StatusHistoryDTO> list = taskService.getHistory(tid);

        return new ResponseEntity<>(list,HttpStatus.OK);
    }


}
