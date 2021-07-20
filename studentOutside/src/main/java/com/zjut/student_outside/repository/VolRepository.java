package com.zjut.student_outside.repository;

import com.zjut.student_outside.model.Student;
import com.zjut.student_outside.model.VolActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface VolRepository extends JpaRepository<VolActivity, Long>
{
    @Query(value = "select ifnull(sum(v.manhour*t.num),0) from vol_activity v join (select vol_id, count(vol_id) as num from t_stu_vol where stu_id" +
            " in (select id from student where banji=?1) group by vol_id) t on v.id=t.vol_id", nativeQuery = true)
    Integer sumManhourForBanji(String clazzName);

    @Query(value = "select ifnull(sum(v.manhour*t.num),0) from vol_activity v join (select vol_id, count(vol_id) as num from t_stu_vol where stu_id" +
            " in " +
            "(select id from student where grade=?1) group by vol_id) t on v.id=t.vol_id", nativeQuery = true)
    Integer sumManhourForGrade(String gradeName);

    @Query(value = "select sum(manhour) as sum, time from vol_activity where id in (select vol_id from t_stu_vol where stu_id=?1) group by time;",
            nativeQuery = true)
    List<Map<String, Object>> sumManhourByTime(Long id);

    Page<VolActivity> findAllByStudents(Student stu, Pageable pageable);

    @Query(value = "select ifnull(sum(v.manhour*t.num),0) from vol_activity v join (select vol_id, count(vol_id) as num from t_stu_vol where stu_id" +
            " in (select id from student where banji=?1) group by vol_id) t on v.id=t.vol_id where v.time=?2", nativeQuery = true)
    Integer sumManhourForBanjiByTime(String clazzName, String time);
}
