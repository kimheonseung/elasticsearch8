import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faAngleDoubleLeft, faAngleLeft } from '@fortawesome/free-solid-svg-icons';

function PagingPrevArrow({start, prev, handleClick}) {

    return (
        <>
            {
                start > 1 ?
                    <li className="page-item">
                        <button className="page-link" data-page={''+1} onClick={() => handleClick(1)}>
                            <FontAwesomeIcon icon={faAngleDoubleLeft} />
                        </button>
                    </li>
                :
                    <li className="page-item disabled">
                        <button className="page-link" data-page={''+1} onClick={() => handleClick(1)}>
                            <FontAwesomeIcon icon={faAngleDoubleLeft} />
                        </button>
                    </li>
            }
            {
                prev ?
                    <li className="page-item">
                        <button className="page-link" data-page={''+(start - 1)} onClick={() => handleClick(start - 1)}>
                            <FontAwesomeIcon icon={faAngleLeft} />
                        </button>
                    </li>
                :
                    <li className="page-item disabled">
                        <button className="page-link" data-page={''+(start - 1)} onClick={() => handleClick(start - 1)}>
                            <FontAwesomeIcon icon={faAngleLeft} />
                        </button>
                    </li>
            }
        </>
    )
}

export default PagingPrevArrow;