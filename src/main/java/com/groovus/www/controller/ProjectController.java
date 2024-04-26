package com.groovus.www.controller;

import com.groovus.www.dto.RegisterProjectDTO;
import com.groovus.www.dto.RegisterProjectRequestDTO;
import com.groovus.www.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.Banner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Log4j2
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {


    private final ProjectService projectService;

    @PreAuthorize("permitAll()")
    @GetMapping("/list")
    public void  goProjectList(Long mid , Model model){

        model.addAttribute("mid",mid);

        List<RegisterProjectDTO> projectList = projectService.getProjectList(mid);

        model.addAttribute("projectList",projectList);
        log.info("==========================");
        log.info(projectList);
        log.info("==========================");
        //프로젝트 생성 페이지로 이동
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    public String registerProject(RegisterProjectRequestDTO pDTO,String mid, Model model){

        log.info("================등록 정보====================");
        log.info(pDTO);
        log.info("============================================");

        if(projectService.registerProject(pDTO)){

            return "redirect:/project/list?mid="+mid;

        }else {

            return "/main/register";
        }
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{pid}/{projectName}")
    public String projectPage(@PathVariable("pid") String pid , @PathVariable("projectName") String projectName, @RequestParam(value = "type",required = false) String type, RedirectAttributes redirectAttributes){


        redirectAttributes.addFlashAttribute("pid",pid);
        redirectAttributes.addFlashAttribute("projectName",projectName);

        if(type!=null && type.equals("yes")){
            return "redirect:/project/home";

        }else if(type!=null && type.equals("delete")){

            return "redirect:/project/delete";
        }else{
            return "redirect:/project/checkpw";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete")
    public void beforeDeleteProject(){
        //프로젝트 삭제 전 비밀번호 확인



    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/checkpw")
    public void beforeGoProjectHome(){
            //프로젝트 이동전 비밀번호 확인
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping ("/checkpw")
    public String validPw(String projectPw ,String pid , String projectName, RedirectAttributes redirectAttributes){
        //프로젝트 이동전 비밀번호 확인

        if(projectService.validProjectPw(pid,projectPw)){

            redirectAttributes.addFlashAttribute("pid",pid);
            redirectAttributes.addFlashAttribute("projectName",projectName);
            return "redirect:/project/home";

        }else{

            redirectAttributes.addFlashAttribute("msg","비밀번호가 일치하지 않습니다.");
            redirectAttributes.addFlashAttribute("pid",pid);
            redirectAttributes.addFlashAttribute("projectName",projectName);
            return "redirect:/project/checkpw";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping ("/delete")
    public String deleteProject(String projectPw ,String pid , String projectName,String mid, RedirectAttributes redirectAttributes){
        //프로젝트 삭제전 비밀번호확인

        if(projectService.validProjectPw(pid,projectPw)){
            //비밀번호가 동일하다면

            projectService.deleteProject(pid);

            return "redirect:/project/list?mid="+mid;


        }else{
            //비밀번호가 일치하지않는다면

            redirectAttributes.addFlashAttribute("msg","비밀번호가 일치하지 않습니다.");
            redirectAttributes.addFlashAttribute("pid",pid);
            redirectAttributes.addFlashAttribute("projectName",projectName);

            return "redirect:/project/delete";
        }
    }
    
    
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/home")
    public void goProjectHome(){
        //프로젝트 홈으로 이동
    }

}
