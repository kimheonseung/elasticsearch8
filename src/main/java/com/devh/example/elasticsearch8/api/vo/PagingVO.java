package com.devh.example.elasticsearch8.api.vo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <pre>
 * Description :
 *     페이징 정보를 갖는 VO
 * ===============================================
 * Member fields :
 *
 * ===============================================
 *
 * Author : HeonSeung Kim
 * Date   : 2021-08-25
 * </pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PagingVO {
    /* 총 데이터 갯수 */
    private long total;
    /* 한 페이지에 보여질 갯수 */
    private int rows;
    /* 현재 페이지 번호 */
    private int page;
    /* 목록 사이즈 */
    private int size;
    /* 총 페이지 번호 */
    private int totalPage;
    /* 시작 페이지 번호, 끝 페이지 번호 */
    private int start, end;
    /* 이전, 다음 */
    private boolean prev, next;
    /* 페이지 번호 목록 */
    private List<Integer> pageList;

    public static PagingVO build(int page, int size, int rows, long total) {

        final int tempEnd   = (int)(Math.ceil(page / (double) size)) * size;
        final int start     = tempEnd - (size - 1);
        final int totalPage = (int) Math.ceil(total / (double) rows);
        final int end       = Math.min(totalPage, tempEnd);

        return PagingVO.builder()
                .page(page)
                .size(size)
                .rows(rows)
                .total(total)
                .totalPage(totalPage)
                .start(start)
                .prev(start > 1)
                .next(totalPage > tempEnd)
                .end(end)
                .pageList(IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList()))
                .build();
    }

    /*
     * 화면에서 시작페이지(start)
     * 화면에서 끝페이지(end)
     * 이전.다음 이동 링크 여부(prev, next)
     * 현재 페이지(page)
     *
     * - 임시 끝번호
     *   tempEnd = (int)(Math.ceil(페이지번호/10.0)) * 10
     *   전체 데이터수가 적다면 10페이지로 끝나면 안됨..
     *   end를 먼저 계산하는 것은 start를 계산하기 쉽기 때문이다.
     * - 시작번호
     *   start = tempEnd - 9
     * - 끝번호
     *   만약 마지막 페이지가 33이면 tempEnd는 40이 된다.
     *   end = totalPage > tempEnd ? tempEnd : totalPage;
     * - 이전
     *   시작번호가 1보다 큰경우
     *   prev = start > 1;
     * - 다음
     *   realEnd가 끝번호(endPage)보다 큰 경우
     *   next = totalPage > tempEnd
     */
}