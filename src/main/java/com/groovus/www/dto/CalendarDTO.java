package com.groovus.www.dto;

import com.groovus.www.entity.CalendarAttach;
import com.groovus.www.entity.Member;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalendarDTO {

    //tb_calendar
    private Long cal_id;

    @NotEmpty
    private String cal_title;

    @NotEmpty
    private String cal_content;

    @NotEmpty
    private String cal_cate;

    @NotEmpty
    private String cal_startDate;

    @NotEmpty
    private String cal_endDate;

    private List<CalendarAttach> cal_attach;

    private String cal_link;

    private List<Member> cal_member;

    private String create_user_id;

    private String update_user_id;

}
