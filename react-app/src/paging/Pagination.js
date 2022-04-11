import React from 'react';
import PagingNextArrow from './PagingNextArrow';
import PagingNumber from './PagingNumber';
import PagingPrevArrow from './PagingPrevArrow';

function Pagination({
    prev,
    next,
    page,
    start,
    end,
    totalPage,
    pageList,
    handleClick,}) { 

    return (
        <>
            <ul className="pagination pagination-sm">
                <PagingPrevArrow start={start} prev={prev} handleClick={handleClick} />
                {
                    pageList?.map((number, idx) => {
                        return <PagingNumber key={idx} page={page} number={number} handleClick={handleClick} />
                    })
                }
                <PagingNextArrow next={next} nextPage={end + 1} end={end} totalPage={totalPage} handleClick={handleClick} />
            </ul>
        </>
    )
}

export default Pagination;