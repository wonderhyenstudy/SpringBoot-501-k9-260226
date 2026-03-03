package com.busanit501.springboot0226.repository.search;

import com.busanit501.springboot0226.domain.Board;
import com.busanit501.springboot0226.domain.QBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch{

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {
        // 자바 문법으로, 검색 및 필터에 필요한 문장을 작성.(SQL 대신에, 자바 코드로 작성.), 빌더 패턴을 이용해서요.

        QBoard board  = QBoard.board; // Q 도메인 객체를 이용

        JPQLQuery<Board> query = from(board); // select .. from board

        query.where(board.title.contains("t")); // where title like..

        // 추가2, 제목, 작성자 검색 조건 추가,
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.or(board.title.contains("t"));
        booleanBuilder.or(board.content.contains("t"));

        // query , 조건을 적용하기.
        query.where(booleanBuilder);
        // 유효성 체크, bno 0보다 초과하는 조건,
        query.where(board.bno.gt(0L));

        // 추가1, 페이징 처리
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch(); // DB 서버로 호출해서, 데이터를 받아오기.
        long count = query.fetchCount(); // 조회된 데이터 갯수 확인.

        return null;
    }
}
