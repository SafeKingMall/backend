package com.safeking.shop.domain.notice.domain.repository;

import com.safeking.shop.domain.notice.domain.entity.Notice;
import com.safeking.shop.domain.notice.domain.sevice.servicedto.notice.NoticeRownumDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public interface NoticeRepository extends JpaRepository<Notice,Long> {

    Page<Notice> findByTitleContainingAndContentsContaining(Pageable pageable, String title, String contents);

    Page<Notice> findByTitleContaining(Pageable pageable, String title);


    Page<Notice> findByCreateDateBetween(Pageable pageable, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = "SELECT @num\\:=@num+1 AS rownum, a.notice_id as id, a.title, a.contents, a.member_id as memberId" +
            ", a.create_date as createDate, a.last_modified_date as lastModifiedDate\n" +
            "   FROM (SELECT @num\\:=0) AS n, notice a\n", nativeQuery = true)
    ArrayList<NoticeRownumDto> findRownumById(Pageable pageable);

    @Query(value = "SELECT @num\\:=@num+1 AS rownum, a.notice_id as id, a.title, a.contents, a.member_id as memberId" +
            ", a.create_date as createDate, a.last_modified_date as lastModifiedDate\n" +
            "   FROM (SELECT @num\\:=0) AS n, notice a\n"+
            "   WHERE a.title like CONCAT('%',:title,'%')\n"
            , nativeQuery = true)
    ArrayList<NoticeRownumDto> findRownumByIdAndTitleContaining(@Param("title") String title, Pageable pageable);

    @Query(value = "SELECT @num\\:=@num+1 AS rownum, a.notice_id as id, a.title, a.contents, a.member_id as memberId" +
            ", a.create_date as createDate, a.last_modified_date as lastModifiedDate\n" +
            "   FROM (SELECT @num\\:=0) AS n, notice a\n"+
            "   WHERE a.create_date like CONCAT(:createDate,'%')\n"
            , nativeQuery = true)
    ArrayList<NoticeRownumDto> findRownumByIdAndCreateDateBetween(@Param("createDate") String createDate, Pageable pageable);
}
