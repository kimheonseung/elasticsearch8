import React from 'react';

function PagingNumber({page, number, handleClick}) {

    return (
        <>
            {
                page === number ?
                    <li className="page-item active">
                        <button className="page-link" data-page={''+number} onClick={() => handleClick(number)}>
                            {number}
                        </button>
                    </li>
                :
                    <li className="page-item">
                        <button className="page-link" data-page={''+number} onClick={() => handleClick(number)}>
                            {number}
                        </button>
                    </li>
            }
        </>
    )
}

export default PagingNumber;