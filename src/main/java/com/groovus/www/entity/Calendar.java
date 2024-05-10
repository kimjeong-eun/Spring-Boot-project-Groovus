package com.groovus.www.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.Mapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name="tb_calendar")
@ToString(exclude = "imageSet")
public class Calendar extends BaseEntity{
    /*
    Calendar Entity 여러 개가 하나의 Project Entity를 가짐
    Calendar Entity 하나는 여러 명의 User Entity를 가짐
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cal_id; // 캘린더 아이디

    @Column(length = 200, nullable = false)
    private String cal_title; // 일정 제목

    @Column(length = 5000, nullable = false)
    private String cal_content; // 일정 내용

    @Column(nullable = false)
    private String cal_cate;

    @Column(nullable = false)
    private String cal_startDate;

    @Column(nullable = false)
    private String cal_endDate;

    @ManyToMany
    @Column(nullable = true)
    private List<CalendarAttach> cal_attach;

    @Column(nullable = true)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<CalendarLink> cal_link_list = new ArrayList<>();

    @Column(nullable = true)
    @ManyToMany
    private List<Member> cal_members;

    @OneToMany(mappedBy = "calendar",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY) //CalendarImage의 calendar 변수
    @Builder.Default
    private Set<CalendarImage> imageSet = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid")
    private Project project;

    @JoinColumn(name = "uid", insertable=false, updatable=false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member create_user_id;

    @JoinColumn(name = "mid")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Member update_user_id; //mid


    public void addImage(String uuid, String fileName) {
        CalendarImage calendarImage = CalendarImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .calendar(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(calendarImage);
    }
    public void setProject(Project project){
        this.project = project;
    }
    public void setUpdate_user_id(Member update_user_id) {
        this.update_user_id = update_user_id;
    }

    public void clearImages() {
        imageSet.forEach(calendarImage -> calendarImage.changeCalendar(null));
        this.imageSet.clear();
    }

    public void change(String cal_title, String cal_content, String cal_cate, String cal_startDate, String cal_endDate) {

        this.cal_title = cal_title;
        this.cal_content = cal_content;
        this.cal_cate = cal_cate;
        this.cal_startDate = cal_startDate;
        this.cal_endDate = cal_endDate;

    }

    // 수정일 및 등록일은 BaseEntity 참고
}
